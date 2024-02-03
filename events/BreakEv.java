package notzStorage.events;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static notzStorage.utils.managers.PlayerManager.getPlayers;
import static notzStorage.utils.sub.MsgU.send;

public class BreakEv implements Listener {
    PlotAPI api = new PlotAPI();

    @EventHandler
    public void farmBreak(BlockBreakEvent e) {
        if (breakSeed(e.getBlock().getState())) {
            e.setCancelled(true);
            if (e.getPlayer().isSneaking())
                returnSeed(e.getPlayer(), e.getBlock().getState());
            else
                send(e.getPlayer(), "&eClique agachado para poder quebrar a plantação.");
            return;
        }

        if (isFinalCropBlock(e.getBlock().getState())) {
            e.setCancelled(true);

            Block block = e.getBlock();
            Plot plot = api.getPlot(block.getLocation());
            Player p = e.getPlayer();

            if (plot.getOwners().stream().findFirst().get().equals(p.getUniqueId())) {
                block.setType(Material.AIR);

                switch (block.getType()) {
                    case PUMPKIN:
                        getPlayers().get(p).addPumpkim();
                        break;
                    case MELON_BLOCK:
                        if (p.getItemInHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH))
                            getPlayers().get(p).addMelonBlock();
                        else getPlayers().get(p).addMelon();
                        break;
                    case CARROT:
                        getPlayers().get(p).addMelonBlock();
                        break;
                    case POTATO:
                    case CROPS:
                    case NETHER_WARTS:
                }

            } else send(p, "&cVocê só pode quebrar a plantação da sua plot!");
        }
    }

    private boolean isFinalCropBlock(BlockState block) {
        switch (block.getType()) {
            case CARROT:
            case POTATO:
            case CROPS:
                return block.getRawData() == 7;
            case MELON_BLOCK:
            case PUMPKIN:
                return true;
            case NETHER_WARTS:
                return block.getRawData() == 3;
            default:
                return false;
        }
    }

    private boolean isFinalCrop(BlockState block) {
        switch (block.getType()) {
            case CARROT:
            case POTATO:
            case CROPS:
                return block.getRawData() == 7;
            case NETHER_WARTS:
                return block.getRawData() == 3;
            default:
                return false;
        }
    }

    private boolean breakSeed(BlockState block) {
        switch (block.getType()) {
            case SUGAR_CANE_BLOCK:
            case PUMPKIN_STEM:
            case MELON_STEM:
            case CACTUS:
            case CARROT:
            case POTATO:
            case CROPS:
            case NETHER_WARTS:
                return true;
            default:
                return false;
        }
    }

    private void returnSeed(Player p, BlockState block) {
        switch (block.getType()) {
            case SUGAR_CANE_BLOCK:
                p.getInventory().addItem(new ItemStack(Material.SUGAR_CANE));
                break;
            case CACTUS:
                p.getInventory().addItem(new ItemStack(Material.CACTUS));
                break;
            case PUMPKIN_STEM:
                p.getInventory().addItem(new ItemStack(Material.PUMPKIN_SEEDS));
                break;
            case MELON_STEM:
                p.getInventory().addItem(new ItemStack(Material.MELON_SEEDS));
                break;
            case CARROT:
                p.getInventory().addItem(new ItemStack(Material.CARROT_ITEM));
                break;
            case POTATO:
                p.getInventory().addItem(new ItemStack(Material.POTATO));
                break;
            case CROPS:
                p.getInventory().addItem(new ItemStack(Material.SEEDS));
                break;
            case NETHER_WARTS:
                p.getInventory().addItem(new ItemStack(Material.NETHER_WARTS));
        }
        block.setType(Material.AIR);
    }
}
