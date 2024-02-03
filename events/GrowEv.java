package notzStorage.events;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import notzStorage.models.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;

import static notzStorage.Main.started;
import static notzStorage.utils.managers.PlayerManager.getPlayers;

public class GrowEv implements Listener {
    PlotAPI api = new PlotAPI();

    @EventHandler
    public void farm(BlockGrowEvent e) {
        if (isNormalSeed(e.getNewState()))
            return;

        if (pass3(e.getNewState()))
            e.setCancelled(true);

        if (!started)
            return;

        if (pass1(e.getNewState())) {

            BlockState block = e.getNewState();
            Plot plot = api.getPlot(block.getLocation());

            if (plot != null && plot.hasOwner()/* && (plot.isOnline() || !plot.isOnline())*/) {
                Player p;
                PlayerModel pm = null;
                boolean farm = false;

                if (Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).anyMatch(uuid -> uuid.equals(plot.getOwners().stream().findFirst().get()))) {
                    p = Bukkit.getPlayer(plot.getOwners().stream().findFirst().get());
                    pm = getPlayers().get(p);
                    farm = p.hasPermission("notzstorage.autofarm");
                } else if (!plot.isOnline()) {
                    return;
                    //pm = findOffDBPlayer(plot.getOwners().stream().findFirst().get());
                }

                if (pm == null)
                    return;

                if (farm) {
                    e.setCancelled(true);
                    switch (block.getType()) {
                        case CARROT:
                            pm.addCarrot();
                            break;
                        case CACTUS:
                            pm.addCactus();
                            break;
                        case MELON:
                            pm.addMelon();
                            break;
                        case MELON_BLOCK:
                            pm.addMelonBlock();
                            break;
                        case NETHER_WARTS:
                            pm.addNetherWarts();
                            break;
                        case POTATO:
                            pm.addPotato();
                            break;
                        case PUMPKIN:
                            pm.addPumpkim();
                            break;
                        case SUGAR_CANE_BLOCK:
                            pm.addSugarCane();
                            break;
                        case CROPS:
                            pm.addWheat();
                            break;
                    }
                } else {
                    switch (block.getType()) {
                        case CACTUS:
                            pm.addCactus();
                            break;
                        case SUGAR_CANE_BLOCK:
                            pm.addSugarCane();
                    }
                }
            } else e.setCancelled(true);
        }

    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e) {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase("plotworld")) {
            int id = e.getBlock().getTypeId();
            if (id == 8 || id == 9) {
                e.setCancelled(true);
            }
        }
    }

    /*@EventHandler
    public void breakFarm(BlockBreakEvent e) {
        if (!started)
            return;

        if (pass2(e.getBlock().getState())) {

            Block block = e.getBlock();
            Plot plot = api.getPlot(block.getLocation());

            if (plot != null && plot.hasOwner() && plot.isOnline() && Bukkit.getPlayer(plot.getOwners().stream().findFirst().get()).isOnline()) {
                Player p = Bukkit.getPlayer(plot.getOwners().stream().findFirst().get());
                PlayerModel pm = getPlayers().get(p);

                switch (block.getType()) {
                    case CARROT:
                        pm.addCarrot(1);
                        break;
                    case MELON:
                        pm.addMelon(1);
                        break;
                    case MELON_BLOCK:
                        pm.addMelonBlock(1);
                        break;
                    case NETHER_WARTS:
                        pm.addNetherWarts(1);
                        break;
                    case POTATO:
                        pm.addPotato(1);
                        break;
                    case PUMPKIN:
                        pm.addPumpkim(1);
                        break;
                    case WHEAT:
                        pm.addWheat(1);
                }
            } else e.setCancelled(true);
        }
    }*/

    private boolean pass1(BlockState block) {
        switch (block.getType()) {
            case CARROT:
            case CACTUS:
            case MELON:
            case MELON_BLOCK:
            case NETHER_WARTS:
            case POTATO:
            case PUMPKIN:
            case SUGAR_CANE_BLOCK:
            case CROPS:
                return true;
            default:
                return false;
        }
    }

    private boolean pass3(BlockState block) {
        switch (block.getType()) {
            case CACTUS:
            case SUGAR_CANE_BLOCK:
                return true;
            default:
                return false;
        }
    }

    private boolean isNormalSeed(BlockState block) {
        switch (block.getType()) {
            case CARROT:
            case POTATO:
            case CROPS:
                return block.getRawData() < 7;
            case MELON_STEM:
            case PUMPKIN_STEM:
                return true;
            case NETHER_WARTS:
                return block.getRawData() < 3;
            default:
                return false;
        }
    }
}
