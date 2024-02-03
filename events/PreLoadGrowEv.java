package notzStorage.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;

import static notzStorage.Main.started;

public class PreLoadGrowEv implements Listener {
    @EventHandler
    public void blockGrowEvent(BlockGrowEvent e) {
        if (!started) {
            switch (e.getNewState().getType()) {
                case NETHER_WARTS:
                    if (e.getNewState().getRawData() == 4)
                        return;
                case CARROT:
                case POTATO:
                case CROPS:
                    if (e.getNewState().getRawData() == 7)
                        return;
                case MELON_STEM:
                case PUMPKIN_STEM:
                    e.setCancelled(true);
            }
        } else e.getHandlers().unregister(this);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e) {
        if (!started) {
            Bukkit.getPlayer("Gago3242").sendMessage("bbbbbb");
            if (e.getBlock().getWorld().getName().equalsIgnoreCase("plotworld")) {
                int id = e.getBlock().getTypeId();
                if (id == 8 || id == 9) {
                    e.setCancelled(true);
                }
            }
        } else e.getHandlers().unregister(this);
    }
}

