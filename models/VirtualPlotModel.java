package notzStorage.models;

import notzStorage.utils.sub.DM;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import static notzStorage.utils.managers.StorageManager.calc;
import static notzStorage.utils.sub.MsgU.c;
import static notzStorage.utils.sub.MsgU.send;

public class VirtualPlotModel extends DM implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String uuid;
    private final PlayerModel pm;
    private final float count = 0.06f;
    private int cactus, storage, speedLevel, capacityLevel; // limites: cactus[64k], storage[2kk800]
    private float cache = 0;

    public VirtualPlotModel(PlayerModel playerM) throws SQLException {
        pm = playerM;

        storage = 0;
        speedLevel = 0;
        capacityLevel = 0;

        uuid = playerM.getP().getName() + (existsVirtualPlot(pm.getId())+1);

        id = setup();
    }

    public VirtualPlotModel(int id, PlayerModel pm, String uuid, int cactus, int storage, float cache) {
        this.id = id;
        this.pm = pm;
        this.uuid = uuid;
        this.cactus = cactus;
        this.storage = storage;
        this.cache = cache;

        speedLevel = 0;
        capacityLevel = 0;
    }

    public double getPrice() {
        return calc(storage, 1);
    }

    public void sellAll() {
        double res = calc(storage, 1);
        if (res > 0) {
            pm.depositFunds(res);

            pm.getP().sendMessage("");
            send(pm.getP(), "&eVocê vendeu &a" + storage + "&r cactus &epor &2$&a" + String.format("%.2f", res) + "&e.");
            storage = 0;
            if (!pm.getBoosters().isEmpty()) {
                if (pm.getBoosters().size() > 1) {
                    pm.getP().sendMessage(c("  &9>&e e ganhou, de " + pm.getBoosters().size() + " Boosters, um adicional de:"));
                    pm.getBoosters().keySet().forEach(b -> {
                        double bo = res * b.getPercentage() / 100;
                        pm.depositFunds(bo);

                        pm.getP().sendMessage(c("&2+ &2$&a" + String.format("%.2f", bo) + "&e do &f&lBooster " + b.getDisplay() + "&e."));
                    });
                } else {
                    BoosterModel b = pm.getBoosters().keySet().stream().findFirst().get();
                    double bo = res * b.getPercentage() / 100;
                    pm.depositFunds(bo);

                    pm.getP().sendMessage(c("  &9>&e e ganhou um adicional de: &2$&a" + String.format("%.2f", bo) + "&e do &f&lBooster " + b.getDisplay() + "&e."));
                }
            }
        }
    }

    public String getUuid() {
        return uuid;
    }

    public PlayerModel getPm() {
        return pm;
    }

    public float getCount() {
        return count;
    }

    public int getStorage() {
        return storage;
    }

    public int getId() {
        return id;
    }

    public int getCactus() {
        return cactus;
    }

    public boolean addCactus(int quantity) {
        if (cactus+quantity <= 64000)
            cactus += quantity;

        else return false;
        return true;
    }

    public boolean remCactus(int quantity) {
        if (cactus-quantity >= 0)
            cactus -= quantity;

        else return false;
        return true;
    }

    public int withdraw() {
        int st = storage;
        storage = 0;
        return st;
    }

    public int getTimeToStartSell() {
        if (cactus == 0 || (getGrowMinute() == 0 && cache == 0))
            return -1;

        if (getGrowMinute() > 0)
            return 0;

        float rs = (float) ((1 - cache) / 0.06);

        return (int) rs;
    }

    public int getGrowMinute() {
        double i = cactus * count;

        int d = (int) i, e = (int) (i + cache);

        if (i - d != 0)
            cache = (float) (i - d);

        return e;
    }

    public void farming() {
        int farm = getGrowMinute();
        storage += farm;

        if (pm.isNotify())
            send(pm.getP(), "&eSua &fPlot Virtual N." + uuid.charAt(uuid.length() - 1) + "&e farmou &a" + farm + " cactos&e.");
    }

    public void save() {
        try {
            setStorageCacheVirtualPlot(storage, cache, uuid);
            setCactusVirtualPlot(cactus, uuid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int setup() {
        try {
            addVirtualPlotDB(pm, uuid);
            return findVirtualPlotID(uuid);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}