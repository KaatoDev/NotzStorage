package notzStorage.events;

import notzStorage.models.BoosterModel;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

import static notzStorage.Main.started;
import static notzStorage.utils.managers.BoosterManager.addPlayerBoost;
import static notzStorage.utils.managers.BoosterManager.getBoosterByID;
import static notzStorage.utils.managers.PlayerManager.getPlayers;
import static notzStorage.utils.sub.MsgU.c;
import static notzStorage.utils.sub.MsgU.send;

public class BoosterEv implements Listener {

    @EventHandler
    public void boosterActivate(PlayerInteractEvent e) {
        if (!started)
            return;

        if (e.getItem() != null && e.getItem().getType().equals(Material.EXP_BOTTLE)
                && e.getItem().getItemMeta().hasEnchant(Enchantment.LUCK)) {
            e.setCancelled(true);

            if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                send(e.getPlayer(), "&eClique com o botão direito e no ar para ativar o Booster!");
                return;
            }

            Player p = e.getPlayer();
            ItemStack item = e.getItem().clone();
            item.setAmount(1);
            BoosterModel boost = getBoosterByID(item.getItemMeta().getEnchantLevel(Enchantment.LUCK));

            String time = item.getItemMeta().getLore().get(2);
            int seconds = 1;

            if (time.contains("segundo") && time.contains("minuto")) {
                String mm = time.split(c(" &7&oe "))[0].split(" ")[0];
                String ss = time.split(c(" &7&oe "))[1].split(" ")[0];

                if (ss.length() == 5)
                    seconds = Integer.parseInt(ss.substring(4, 5));
                else seconds = Integer.parseInt(ss.substring(4, 6));

                seconds += Integer.parseInt(mm.substring(4)) * 60;

                String aa = "&eVocê ativou o &f&lBooster " + boost.getDisplay() + "&e com duração de &a" + seconds/60 + "&2 minuto(s) &ee &a" + seconds%60 + "&2 segundos &e.";

                send(p, aa);
            } else if (time.contains("segundo")) {
                String s1 = time.split(" ")[0];

                if (s1.length() == 5)
                    seconds = Integer.parseInt(s1.substring(4, 5));
                else seconds = Integer.parseInt(s1.substring(4, 6));
                String aa = "&eVocê ativou o &f&lBooster " + boost.getDisplay() + "&e com duração de &a" + seconds + "&2 segundos&e.";

                send(p, aa);
            }  else if (time.contains("minuto")) {
                String m1 = time.split(" ")[0];

                if (m1.length() == 5)
                    seconds = Integer.parseInt(m1.substring(4, 5)) * 60;
                else seconds = Integer.parseInt(m1.substring(4, 6)) * 60;

                String aa = "&eVocê ativou o &f&lBooster " + boost.getDisplay() + "&e com duração de &a" + seconds/60 + "&2 minuto(s)&e.";

                send(p, aa);
            } else {
                send(p, "&cEste &f&lBooster " + boost.getDisplay() + "&c está inválido! Chame um staff superior.");
            }

            try {
                getPlayers().get(p).addBooster(boost, seconds);

                addPlayerBoost(getPlayers().get(p));

                p.getInventory().removeItem(item);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}