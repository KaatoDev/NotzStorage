package notzStorage.utils.managers;

import notzStorage.models.PlayerModel;
import notzStorage.models.VirtualPlotModel;
import notzStorage.utils.sub.DM;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static notzStorage.utils.managers.PlayerManager.getPlayers;

public class VirtualPlotManager extends DM {
    private static HashMap<VirtualPlotModel, PlayerModel> virtualPlots = new HashMap<>();
    public static HashMap<VirtualPlotModel, PlayerModel> getVirtualPlots() {
        return virtualPlots;
    }
    private static double cactusPrice;

    public static List<VirtualPlotModel> getPlayerVirtualPlots(Player player) {
        if (virtualPlots.containsValue(getPlayers().get(player)))
            return virtualPlots.keySet().stream().filter(vp -> virtualPlots.get(vp).getP().getName().equals(player.getName())).collect(Collectors.toList());
        else return Collections.emptyList();
    }

    public static VirtualPlotModel getVirtualPlot(Player player, int id) {
        return virtualPlots.keySet().stream().filter(vp -> vp.getUuid().equals(player.getName()+id)).findFirst().get();
    }

    public static boolean addVirtualPlot(PlayerModel pm) {
        try {
            virtualPlots.put(new VirtualPlotModel(pm), pm);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean remVirtualPlot(VirtualPlotModel vp) {
        try {
            virtualPlots.remove(vp);
            removeVirtualPlot(vp);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static double getCactusPriceVP() {
        return cactusPrice;
    }

    public static void setCactusPriceVP(double price) {
        try {
            setCactusPrice(price);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean buyCactus(VirtualPlotModel vp, int cactus) {
        if (vp.getPm().getFunds() > cactus * cactusPrice && vp.addCactus(cactus))
            vp.getPm().withdrawFunds(cactus * cactusPrice);

        else return false;
        return true;
    }

    public static boolean sellCactus(VirtualPlotModel vp, int cactus) {
        if (vp.getCactus() >= cactus && vp.remCactus(cactus)) {
            vp.getPm().depositFunds(cactus * cactusPrice * 0.9);
            return true;
        } else {
            return false;
        }
    }

    public static boolean setCactus(VirtualPlotModel vp, int cactus) {
        if (vp.getCactus() == 0 && vp.addCactus(cactus))
            return true;

        else if (vp.getCactus() != cactus && vp.getCactus() <= 64000) {
            if (vp.getCactus() < cactus && (vp.addCactus(cactus - vp.getCactus())))
                return true;

            else return vp.remCactus(vp.getCactus() - cactus);
        } else return false;
    }

    public static void save() {
        virtualPlots.keySet().forEach(VirtualPlotModel::save);
    }

    public static void load() {
        try {
            if (!Bukkit.getOnlinePlayers().isEmpty())
                virtualPlots = loadVirtualPlots();
            cactusPrice = getCactusPrice();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
