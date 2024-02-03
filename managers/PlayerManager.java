package notzStorage.utils.managers;

import notzStorage.models.PlayerModel;
import notzStorage.models.VirtualPlotModel;
import notzStorage.utils.sub.DM;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static notzStorage.utils.managers.BoosterManager.addPlayerBoost;
import static notzStorage.utils.managers.VirtualPlotManager.getPlayerVirtualPlots;
import static notzStorage.utils.sub.MsgU.c;
import static notzStorage.utils.sub.MsgU.send;

public class PlayerManager extends DM {
    private static HashMap<Player, PlayerModel> players = new HashMap<>();

    public static HashMap<Player, PlayerModel> getPlayers() {
        return players;
    }

    public static void addPlayer(Player player) {
        try {
            if (existsPlayerModel(player.getUniqueId())) {
                players.put(player, findPlayerModel(player));
                addPlayerBoost(players.get(player));

            } else players.put(player, new PlayerModel(player));
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void leavePlayer(Player player) {
        try {
            setPlayerStorage(players.get(player));
            getPlayerVirtualPlots(player).forEach(VirtualPlotModel::save);
            players.get(player).save();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        players.remove(player);
    }

    public static void removePlayer(Player player) {
        try {
            remPlayerModel(players.get(player));
            players.remove(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerModel findOffDBPlayer(UUID uuid) {
        try {
            return findOfflinePlayerModel(uuid);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateStorages() {
        players.values().forEach(pm -> {
            try {
                DM.setPlayerStorage(pm);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void storageMin() {
        players.values().forEach(pm -> {
            if (!pm.isNotify())
                return;

            Integer[] stor = pm.getStorageMin().clone();

            if (Arrays.stream(stor).anyMatch(it -> it > 0)) {

                if (pm.resetStorageMin()) {

                    int[] i = {0};
                    pm.getP().sendMessage("");
                    send(pm.getP(), "&eNos últimos 10 minutos você farmou:");

                    String[] items = new String[9], it = new String[]{""};

                    if (pm.getP().hasPermission("notzstorage.autofarm")) {
                        Arrays.stream(stor).forEach(t -> {
                            String aa = null;
                            if (t > 0)
                                switch (i[0]) {
                                    case 0:
                                        aa = " &6[&fCenouras: &a" + t + "&6]&r";
                                        break;
                                    case 1:
                                        aa = " &6[&fCactos: &a" + t + "&6]&r";
                                        break;
                                    case 2:
                                        aa = " &6[&fMelancias: &a" + t + "&6]&r";
                                        break;
                                    case 3:
                                        aa = " &6[&fBlocos de melancia: &a" + t + "&6]&r";
                                        break;
                                    case 4:
                                        aa = " &6[&fFungos: &a" + t + "&6]&r";
                                        break;
                                    case 5:
                                        aa = " &6[&fBatatas: &a" + t + "&6]&r";
                                        break;
                                    case 6:
                                        aa = " &6[&fAbóboras: &a" + t + "&6]&r";
                                        break;
                                    case 7:
                                        aa = " &6[&fCanas-de-açúcar: &a" + t + "&6]&r";
                                        break;
                                    case 8:
                                        aa = " &6[&fTrigos: &a" + t + "&6]&r";
                                }

                            items[i[0]] = aa;
                            i[0]++;
                        });

                        i[0] = 0;

                        Bukkit.getConsoleSender().sendMessage(c("&aNos últimos 10 minutos de:"));
                        Bukkit.getConsoleSender().sendMessage(c("&2" + pm.getP().getName() + "&f: " + Arrays.toString(items)));


                        Arrays.stream(items).forEach(aa -> {
                            if (aa != null) {
                                it[0] += aa;
                                i[0]++;
                            }
                            if (i[0] == 3) {
                                pm.getP().sendMessage(c(it[0].replace("&6] &6[", "&6] &7- &6[")));
                                i[0] = 0;
                                it[0] = "";
                            }
                        });

                        if (i[0] > 0) {
                            if (it[0].contains("&6] &6["))
                                pm.getP().sendMessage(c(it[0].replace("&6] &6[", "&6] &7- &6[")));
                            else pm.getP().sendMessage(c(it[0]));

                            i[0] = 0;
                            it[0] = "";
                        }

                    } else {
                        Arrays.stream(stor).forEach(t -> {
                            String aa = null;
                            if (t > 0)
                                switch (i[0]) {
                                    case 1:
                                        aa = " &6[&fCactos: &a" + t + "&6]";
                                        break;
                                    case 7:
                                        aa = " &6[&fCanas-de-açúcar: &a" + t + "&6]&r";
                                }

                            items[i[0]] = aa;
                            i[0]++;
                        });

                        Arrays.stream(items).forEach(aa -> {
                            if (aa != null)
                                it[0] += aa;
                        });

                        if (it[0].contains("&6] &6["))
                            pm.getP().sendMessage(c(it[0].replace("&6] &6[", "&6] &7- &6[")));
                        else pm.getP().sendMessage(c(it[0]));
                    }
                }
            }
        });
    }

    public static void load() {
        Bukkit.getOnlinePlayers().forEach(PlayerManager::addPlayer);
    }

    public static void save() {
        updateStorages();

    }
}
