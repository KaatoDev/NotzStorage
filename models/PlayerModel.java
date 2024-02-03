package notzStorage.models;

import notzStorage.utils.sub.DM;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static notzStorage.Main.getEconomy;
import static notzStorage.utils.managers.StorageManager.calc;
import static notzStorage.utils.sub.MsgU.c;
import static notzStorage.utils.sub.MsgU.send;


public class PlayerModel extends DM implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final Player p;
    private Integer[] storage = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    private Integer[] storageMin = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    private HashMap<BoosterModel, Integer> boosters;
    private int storageTime = 0;
    private boolean notify = true;

    public PlayerModel(Player player) throws SQLException, IOException {
        p = player;
        id = setup();
    }

    public PlayerModel(int id, Player p, Integer[] storage, boolean notify) throws SQLException {
        this.id = id;
        this.p = p;
        this.storage = storage;
        this.notify = notify;

        boosters = getPlayerBoosters(id);
    }

    public int getId() {
        return id;
    }

    public Player getP() {
        return p;
    }

    public Integer[] getStorage() {
        return storage;
    }

    public Integer[] getStorageMin() {
        return storageMin;
    }

    public boolean isNotify() {
        return notify;
    }

    public void enableNotify() {
        this.notify = true;
        try {
            setNotify(id, true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disableNotify() {
        this.notify = false;
        try {
            setNotify(id, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean resetStorageMin() {
        storageTime++;

        if (storageTime == 10) {
            storageMin = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            storageTime = 0;
            return true;
        }

        return false;
    }

    public HashMap<BoosterModel, Integer> getBoosters() {
        return boosters;
    }

    public void reduceSecond() {
        if (!boosters.isEmpty()) {

            Arrays.stream(boosters.keySet().toArray(new BoosterModel[0])).forEach(b -> {
                int a = boosters.get(b) - 1;
                if (a > 0)
                    boosters.replace(b, a);

                else {
                    send(p, "&eO seu &f&lBooster " + b.getDisplay() + "&e acabou!");

                    boosters.remove(b);
                    try {
                        removeBooster(b);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

   /* public boolean reduceMinute() {
        boosters.keySet().forEach(b -> {
            if (boosters.get(b) > 60) {

                int a = boosters.get(b)-60;
                boosters.replace(b, a);
            }
        });

        return boosters.values().stream().anyMatch(t -> t < 61);
    }*/

    public void getFromInventory() {
        Arrays.stream(p.getInventory().getContents()).forEach(it -> {
            if (it != null && it.getAmount() > 0) {
                switch (it.getType()) {
                    case CARROT_ITEM:
                        storage[0] += it.getAmount();
                        p.getInventory().remove(Material.CARROT_ITEM);
                        break;
                    case CACTUS:
                        storage[1] += it.getAmount();
                        p.getInventory().remove(Material.CACTUS);
                        break;
                    case MELON:
                        storage[2] += it.getAmount();
                        p.getInventory().remove(Material.MELON);
                        break;
                    case MELON_BLOCK:
                        storage[3] += it.getAmount();
                        p.getInventory().remove(Material.MELON_BLOCK);
                        break;
                    case NETHER_STALK:
                        storage[4] += it.getAmount();
                        p.getInventory().remove(Material.NETHER_STALK);
                        break;
                    case POTATO_ITEM:
                        storage[5] += it.getAmount();
                        p.getInventory().remove(Material.POTATO_ITEM);
                        break;
                    case PUMPKIN:
                        storage[6] += it.getAmount();
                        p.getInventory().remove(Material.PUMPKIN);
                        break;
                    case SUGAR_CANE:
                        storage[7] += it.getAmount();
                        p.getInventory().remove(Material.SUGAR_CANE);
                        break;
                    case WHEAT:
                        storage[8] += it.getAmount();
                        p.getInventory().remove(Material.WHEAT);
                }
            }
        });
    }

    public void sellStorage() {

        int[] i = new int[]{0};

        if (notify)
            p.sendMessage("");

        Arrays.stream(storage).forEach(item -> {
            if (item >= 1) {
                double res = calc(item, i[0]);
                depositFunds(res);

                if (notify)
                    send(p, "&eVocê vendeu &a" + item + "&r " + item(i[0]) + "&e por &2$&a" + String.format("%.2f", res) + "&e.");

                if (!boosters.isEmpty()) {
                    if (boosters.size() > 1) {

                        if (notify)
                            p.sendMessage(c("  &9>&e E ganhou, de " + boosters.size() + " Boosters, um adicional de:"));

                        boosters.keySet().forEach(b -> {
                            double bo = res * b.getPercentage() / 100;
                            depositFunds(bo);

                            if (notify)
                                p.sendMessage(c("&2+ &2$&a" + String.format("%.2f", bo) + "&e do &f&lBooster " + b.getDisplay() + "&e."));
                        });
                    } else {
                        BoosterModel b = boosters.keySet().stream().findFirst().get();
                        double bo = res * b.getPercentage() / 100;
                        depositFunds(bo);

                        if (notify)
                            p.sendMessage(c("  &9>&e e ganhou um adicional de: &2$&a" + String.format("%.2f", bo) + "&e do &f&lBooster " + b.getDisplay() + "&e."));
                    }
                }
            }


            storage[i[0]] = 0;
            i[0]++;
        });
        if (notify)
            p.sendMessage("");
    }

    public void sellStorage(int id) {
        int item = storage[id];

            if (item >= 1) {
                double res = calc(item, id);
                depositFunds(res);

                p.sendMessage("");
                send(p, "&eVocê vendeu &a" + item + "&r " + item(id) + "&e por &2$&a" + String.format("%.2f", res) + "&e.");

                if (!boosters.isEmpty()) {
                    if (boosters.size() > 1) {
                        p.sendMessage(c("  &9>&e E ganhou, de " + boosters.size() + " Boosters, um adicional de:"));
                        boosters.keySet().forEach(b -> {
                            double bo = res * b.getPercentage() / 100;
                            depositFunds(bo);

                            p.sendMessage(c("&2   + &2$&a" + String.format("%.2f", bo) + "&e do &f&lBooster " + b.getDisplay() + "&e."));
                        });
                    } else {
                        BoosterModel b = boosters.keySet().stream().findFirst().get();
                        double bo = res * b.getPercentage() / 100;
                        depositFunds(bo);

                        p.sendMessage(c("  &9>&e e ganhou um adicional de: &2$&a" + String.format("%.2f", bo) + "&e do &f&lBooster " + b.getDisplay() + "&e."));
                    }
                }
                p.sendMessage("");
                storage[id] = 0;
            }
    }

    public void addBooster(BoosterModel booster, int time) throws SQLException {
        if (boosters.containsKey(booster)) {
            int t = time + boosters.get(booster);
            updatePlayerBooster(id, booster, t);
            boosters.replace(booster, t);

        } else {
            boosters.put(booster, time);
            addPlayerBooster(id, booster, time);
        }
    }

    public void removeBooster(BoosterModel booster) throws SQLException {
        remPlayerBooster(id, booster);
    }

    public void save() {
        if (!boosters.isEmpty()){
            boosters.keySet().forEach(boost -> {
                try {
                    savePlayerBoostTime(id, boost, boosters.get(boost));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private String item(int id) {
        switch (id) {
            case 0:
                return c("&f&ocenouras");
            case 1:
                return c("&f&ocactos");
            case 2:
                return c("&f&omelancias");
            case 3:
                return c("&f&oblocos de melancia");
            case 4:
                return c("&f&ofungos do nether");
            case 5:
                return c("&f&obatatas");
            case 6:
                return c("&f&oabóboras");
            case 7:
                return c("&f&ocanas-de-açúcar");
            case 8:
                return c("&f&otrigos");
            default:
                return c("&cerror404");
        }
    }

    public void depositFunds(double amount) {
        getEconomy().depositPlayer(p, amount);
    }

    public void withdrawFunds(double amount) {
        getEconomy().withdrawPlayer(p, amount);
    }

    public double getFunds() {
        return getEconomy().getBalance(p);
    }

    public void addCarrot() {
        int quantity = new Random().nextInt(7)+2;

        storage[0] += quantity;
        storageMin[0] += quantity;
    }
    public void addCactus() {
        int quantity = 1;

        storage[1] += quantity;
        storageMin[1] += quantity;
    }
    public void addMelon() {
        int quantity = new Random().nextInt(7)+3;

        storage[2] += quantity;
        storageMin[2] += quantity;
    }
    public void addMelonBlock() {
        int quantity = 1;

        storage[3] += quantity;
        storageMin[3] += quantity;
    }
    public void addNetherWarts() {
        int quantity = new Random().nextInt(8)+2;

        storage[4] += quantity;
        storageMin[4] += quantity;
    }
    public void addPotato() {
        int quantity = new Random().nextInt(7)+2;

        storage[5] += quantity;
        storageMin[5] += quantity;
    }
    public void addPumpkim() {
        int quantity = 1;

        storage[6] += quantity;
        storageMin[6] += quantity;
    }
    public void addSugarCane() {
        int quantity = 1;

        storage[7] += quantity;
        storageMin[7] += quantity;
    }
    public void addWheat() {
        int quantity = new Random().nextInt(4)+1;

        storage[8] += quantity;
        storageMin[8] += quantity;
    }
    public void remCarrot(int quantity) {
        storage[0] -= quantity;
    }
    public void remCactus(int quantity) {
        storage[1] -= quantity;
    }
    public void remMelon(int quantity) {
        storage[2] -= quantity;
    }
    public void remMelonBlock(int quantity) {
        storage[3] -= quantity;
    }
    public void remNetherWarts(int quantity) {
        storage[4] -= quantity;
    }
    public void remPotato(int quantity) {
        storage[5] -= quantity;
    }
    public void remPumpkim(int quantity) {
        storage[6] -= quantity;
    }
    public void remSugarCane(int quantity) {
        storage[7] -= quantity;
    }
    public void remWheat(int quantity) {
        storage[8] -= quantity;
    }

    private int setup() {
        try {
            addPlayerModel(p, storage);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            return findPlayerModelId(p);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerModel)) return false;
        PlayerModel that = (PlayerModel) o;
        return getId() == that.getId() && Objects.equals(getP(), that.getP());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getP());
    }
}
// inv[0] = carrot
// inv[1] = cactus
// inv[2] = melon
// inv[3] = melon_block
// inv[4] = nether_warts
// inv[5] = potato
// inv[6] = pumpkim
// inv[7] = sugar_cane
// inv[8] = wheat