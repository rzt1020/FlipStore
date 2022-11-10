package cn.myrealm.flipstore.guis;

import cn.myrealm.flipstore.FlipStore;
import cn.myrealm.flipstore.managers.LanguageManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: FlipStore
 * @description: Abstract class for sign gui, This code borrows a lot from @R3h4b's code in <a href="https://www.spigotmc.org/threads/signgui-api-1-8-1-18-2-easy-to-use.551416/">...</a>.
 * @author: rzt1020
 * @create: 2022/11/08
 **/
public abstract class GUISign extends GUI{
    // vars
    protected List<String> text; // size 4, used to store four lines of text for sign
    private PacketAdapter packetListener; // sign done, listener
    private LeaveListener listener; // player quit listener
    private Sign sign; // block sign
    /**
     * @Description: Constructor
     * @Param: [owner]
     * @return:
     * @Author: rzt1020
     * @Date: 2022/11/8
    **/
    public GUISign(Player owner) {
        super(owner);
    }

    
    /**
     * @Description: open sign GUI for owner
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/10
    **/
    @Override
    public void openGUI() {
        listener = new LeaveListener();
        int x_start = owner.getLocation().getBlockX();

        int y_start = 255;

        int z_start = owner.getLocation().getBlockZ();

        Material material = Material.getMaterial("WALL_SIGN");
        if (material == null)
            material = Material.OAK_WALL_SIGN;
        while (!owner.getWorld().getBlockAt(x_start, y_start, z_start).getType().equals(Material.AIR) &&
                !owner.getWorld().getBlockAt(x_start, y_start, z_start).getType().equals(material)) {
            y_start--;
            if (y_start == 1)
                return;
        }
        owner.getWorld().getBlockAt(x_start, y_start, z_start).setType(material);

        sign = (Sign)owner.getWorld().getBlockAt(x_start, y_start, z_start).getState();
        int i = 0;
        for(String line : text){
            sign.setLine(i, line);
            i++;
        }
        if (Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.18")) {
            sign.setColor(DyeColor.GREEN);
        }
        LanguageManager.instance.sendMessage(owner,sign.getBlockData().toString());

        sign.update(false, false);

        PacketContainer openSign = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);

        BlockPosition position = new BlockPosition(x_start, y_start, z_start);

        openSign.getBlockPositionModifier().write(0, position);
        Bukkit.getScheduler().runTaskLater(FlipStore.instance, () -> {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(owner, openSign);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 3L);
        Bukkit.getScheduler().runTaskLater(FlipStore.instance, ()-> sign.getBlock().setType(Material.AIR),4L);
        Bukkit.getPluginManager().registerEvents(listener, FlipStore.instance);
        registerSignUpdateListener();
    }

    private class LeaveListener implements Listener {
        /**
         * @Description: call when player quit
         * @Param: [e]
         * @return: void
         * @Author: rzt1020
         * @Date: 2022/11/10
        **/
        @EventHandler
        public void onLeave(PlayerQuitEvent e) {
            if (e.getPlayer().equals(owner)) {
                ProtocolLibrary.getProtocolManager().removePacketListener(packetListener);
                HandlerList.unregisterAll(this);
            }
        }
    }
    
    /**
     * @Description: register listeners when sign is opened, and unregister listeners after done sign
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/10
    **/
    private void registerSignUpdateListener() {
        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        packetListener = new PacketAdapter(FlipStore.instance, PacketType.Play.Client.UPDATE_SIGN) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPlayer().equals(owner)) {
                    text = Stream.of(0,1,2,3).map(line -> getLine(event, line)).collect(Collectors.toList());
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        manager.removePacketListener(this);
                        HandlerList.unregisterAll(listener);
                        doneHandle();
                    });
                }
            }
        };
        manager.addPacketListener(this.packetListener);
    }

    /**
     * @Description: special processing 1.8 version of line
     * @Param: [event, line]
     * @return: java.lang.String
     * @Author: rzt1020
     * @Date: 2022/11/10
    **/
    private String getLine(PacketEvent event, int line){
        return Bukkit.getVersion().contains("1.8") ?
                ((WrappedChatComponent[])event.getPacket().getChatComponentArrays().read(0))[line].getJson().replaceAll("\"", "") :
                ((String[])event.getPacket().getStringArrays().read(0))[line];
    }

    /**
     * @Description: handle after done
     * @Param: []
     * @return: void
     * @Author: rzt1020
     * @Date: 2022/11/10
    **/
    protected abstract void doneHandle();
}
