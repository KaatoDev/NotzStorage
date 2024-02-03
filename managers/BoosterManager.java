package notzStorage.utils.managers;

import notzStorage.models.BoosterModel;
import notzStorage.models.PlayerModel;
import notzStorage.utils.sub.DM;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static notzStorage.utils.sub.MsgU.send;

public class BoosterManager extends DM implements Runnable {
    private static HashMap<String, BoosterModel> boosters = new HashMap<>();
    private static List<PlayerModel> playerBoost = new ArrayList<>();
    private static final BoosterManager instance = new BoosterManager();

    public static HashMap<String, BoosterModel> getBoosters() {
        return boosters;
    }

    public static void addPlayerBoost(PlayerModel pm) {
        if (!playerBoost.contains(pm) && !pm.getBoosters().isEmpty())
            playerBoost.add(pm);
    }

    public static void checkBoosters(PlayerModel pm) {
        if (pm.getBoosters().isEmpty())
            playerBoost.remove(pm);
    }

    public static BoosterModel getBoosterByID(int id) {
        return boosters.values().stream().filter(b -> b.getId() == id).findFirst().get();
    }

    public static boolean addBooster(String name, String display, int percentage) {
        if (boosters.containsKey(name))
            return false;
        else {
            boosters.put(name, new BoosterModel(name, display, percentage));
            return true;
        }
    }

    public static void removeBooster(BoosterModel boost) throws SQLException {
        boosters.remove(boost.getName());
        remBooster(boost);
    }

    public static BoosterModel getBoost(String name) {
        for (String b : boosters.keySet())
            if (b.equals(name))
                return boosters.get(b);
        return null;
    }

    public static void giveBooster(Player p, String booster, int time) {
        p.getInventory().addItem(boosters.get(booster).getBooster(time));

        String aa = "&eVocê recebeu o &f&lBooster " + boosters.get(booster).getDisplay() + "&e com duração de &a";


        if (time/60 == 1)
            aa += time/60 + "&2 minuto &ee &a";
        else if (time/60 > 1) {
            int tt = time/60;
            aa += tt + "&2 minutos &ee &a";
        }

        if (time%60 == 1)
            aa += time%60 + "&2 segundo&e.";
        else aa += time%60 + "&2 segundos&e.";

        send(p, aa);
    }

    public static void load() {
        try {
            boosters = loadBoosters();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save() {
        playerBoost.forEach(PlayerModel::save);
    }

    public static BoosterManager getInstance() {
        return instance;
    }

    @Override
    public void run() {
        if (!playerBoost.isEmpty()) {
            Arrays.stream(playerBoost.toArray(new PlayerModel[0])).forEach(pm -> {
                pm.reduceSecond();
                checkBoosters(pm);
            });
        }
    }
}