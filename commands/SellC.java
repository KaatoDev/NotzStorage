package notzStorage.commands;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.RegionWrapper;
import notzStorage.gui.StorageGUI;
import notzStorage.models.BoosterModel;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static notzStorage.Main.header;
import static notzStorage.Main.started;
import static notzStorage.utils.managers.PlayerManager.getPlayers;
import static notzStorage.utils.managers.StorageManager.getPrices;
import static notzStorage.utils.managers.StorageManager.getStock;
import static notzStorage.utils.managers.VirtualPlotManager.getPlayerVirtualPlots;
import static notzStorage.utils.sub.MsgU.c;
import static notzStorage.utils.sub.MsgU.send;

public class SellC implements TabExecutor {
    private Player p;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!started)
            return false;

        if (!(sender instanceof Player))
            return false;

        p = (Player) sender;

        switch (label) {
            case "virtualplot":
            case "plotvirtual":
            case "vp":
                if (args.length == 0)
                    p.openInventory(new StorageGUI(p).menuVP());
                else send(p, "&eUtilize apenas &f/&estorage.");
                return true;

            case "bolsa":
            case "stock":
                if (args.length == 0)
                    send(p, "&eA &2&lbolsa de valores &eatualmente está em &f" + getStock() + "&2%&e.");
                else send(p, "&eUtilize apenas &f/&ebolsa.");
                return true;

            case "boost":
            case "boosts":
            case "booster":
            case "boosters":
                if (args.length == 0)
                    viewBoosters();
                else send(p, "&eUtilize apenas &f/&eboosters.");
                return true;

            case "storage":
            case "armazem":
            case "drop":
            case "drops":
                if (args.length == 0 && getPlayers().containsKey(p))
                    p.openInventory(new StorageGUI(p).menuFarm());
                else send(p, "&eUtilize apenas &f/&edrops.");
                return true;

            case "farm":
            case "farms":
                if (args.length == 0 && getPlayers().containsKey(p))
                    viewFarms();
                else send(p, "&eUtilize apenas &f/&efarms.");
                return true;
            case "prices":
                if (args.length == 0 && getPlayers().containsKey(p)) {
                    p.sendMessage("");
                    send(p, "&f&lCenoura&f: &2$&a" + getPrices()[0] + "&f.");
                    send(p, "&f&lCacto&f: &2$&a" + getPrices()[1] + "&f.");
                    send(p, "&f&lMelancia&f: &2$&a" + getPrices()[2] + "&f.");
                    send(p, "&f&lBlocos de melancia&f: &2$&a" + getPrices()[3] + "&f.");
                    send(p, "&f&lFungos do nether&f: &2$&a" + getPrices()[4] + "&f.");
                    send(p, "&f&lBatata&f: &2$&a" + getPrices()[5] + "&f.");
                    send(p, "&f&lAbóbora&f: &2$&a" + getPrices()[6] + "&f.");
                    send(p, "&f&lCanas-de-açúcar&f: &2$&a" + getPrices()[7] + "&f.");
                    send(p, "&f&lTrigo&f: &2$&a" + getPrices()[8] + "&f.");
                    send(p, "&f&lCacto VP&f: &2$&a" + getPrices()[9] + "&f.");
                    p.sendMessage("");
                    return true;
                } else send(p, "&eUtilize apenas &f/&eprices.");
        }

        if (args.length == 0 && getPlayers().containsKey(p)) {
            if (label.equals("sellall")) {
                getPlayers().get(p).getFromInventory();
                getPlayers().get(p).sellStorage();
            } else getPlayers().get(p).sellStorage();
        } else send(p, "&eUtilize apenas &f/&esell.");

        return true;
    }

    private void viewBoosters() {
        if (getPlayers().get(p).getBoosters().isEmpty())
            send(p, "&cVocê não possui Boosters ativos!");
        else {
            p.sendMessage(header);

            getPlayers().get(p).getBoosters().keySet().forEach(b -> {

                int mm, ss, time = getPlayers().get(p).getBoosters().get(b);
                String res;

                if (time > 60) {
                    mm = time / 60;
                    ss = time % 60;
                    res = mm + " minuto(s) &fe &e" + ss + " segundo(s)&f.";

                } else if (time % 60 == 0) {
                    mm = time / 60;
                    res = mm + " minuto(s) &f.";
                } else {
                    ss = time;
                    res = ss + " segundo(s)&f.";
                }

                p.sendMessage(c("&f&lBooster" + b.getDisplay() + " &f- &a" + b.getPercentage() + "&2% &f- &eTempo restante: &e" + res));
            });
            p.sendMessage(c("&7(Total bônus de " + getPlayers().get(p).getBoosters().keySet().stream().mapToInt(BoosterModel::getPercentage).sum() + "%)"));
            p.sendMessage("");
        }
    }

    private void viewFarms() {
        /* PlotAPI papi = new PlotAPI();

        if (!papi.getPlayerPlots(p).isEmpty()) {

            p.sendMessage(header);
            p.sendMessage(c("&e Total de plantação em cada &fplot &esua:"));

            for (Plot plot : papi.getPlayerPlots(p)) {
                List<String> st = new ArrayList<>();
                System.out.println("333333333");
                int[] it = {0};

                Arrays.stream(getAllBlocksInPlot(plot)).forEach(s -> {
                    switch (it[0]) {
                        case 0:
                            st.add("&a" + s + " &fcenouras&6");
                            break;
                        case 1:
                            st.add("&a" + s + " &fcactos&6");
                            break;
                        case 2:
                            st.add("&a" + s + " &fmelancias&6");
                            break;
                        case 3:
                            st.add("&a" + s + " &ffungos&6");
                            break;
                        case 4:
                            st.add("&a" + s + " &fbatatas&6");
                            break;
                        case 5:
                            st.add("&a" + s + " &fabóboras&6");
                            break;
                        case 6:
                            st.add("&a" + s + " &fcanas-de-açúcar&6");
                            break;
                        case 7:
                            st.add("&a" + s + " &ftrigos&6");
                    }

                    it[0]++;
                });

                if (!st.isEmpty())
                    p.sendMessage(c("&f  " + plot.getId() + "&e: &6" + st.toString().replace(",", ", ").replace("]", "&6].")));
                else p.sendMessage(c("&f  " + plot.getId() + "&e: &cNão há plantações."));
            }

            p.sendMessage("");
        }*/


        if (!getPlayerVirtualPlots(p).isEmpty()) {
            p.sendMessage(header);
            p.sendMessage(c("&e Total de plantação em cada &fVirtual Plot &esua:"));

            getPlayerVirtualPlots(p).forEach(vp -> {
                p.sendMessage("&6  [&fVP " + vp.getUuid() + "&e: &a" + vp.getCactus() + "&2 cactos&e&6]");
            });
        } send(p, "&cVocê não possui &fVirtual Plots&c.");
    }

    private Integer[] getAllBlocksInPlot(Plot plot) {
        PlotArea pa = plot.getArea();
        RegionWrapper rg = pa.getRegion();
        Integer[] count = {0, 0, 0, 0, 0, 0, 0, 0};
        World w = Bukkit.getWorld(plot.getWorldName());

        for (int x = rg.minX; x <= rg.maxX; x++)
            for (int y = rg.minY; y <= rg.maxY; y++)
                for (int z = rg.minY; z <= rg.maxY; z++) {
                    switch (w.getBlockAt(x, y, z).getType()) {
                        case CARROT:
                            count[0]++;
                            break;
                        case CACTUS:
                            count[1]++;
                            break;
                        case MELON_STEM:
                            count[2]++;
                            break;
                        case NETHER_WARTS:
                            count[3]++;
                            break;
                        case POTATO:
                            count[4]++;
                            break;
                        case PUMPKIN_STEM:
                            count[5]++;
                            break;
                        case SUGAR_CANE_BLOCK:
                            count[6]++;
                            break;
                        case CROPS:
                            count[7]++;
                    }
                }

        return count;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
