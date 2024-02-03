package notzStorage.utils.managers;

import notzStorage.models.VirtualPlotModel;
import notzStorage.utils.sub.DM;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.Random;

import static notzStorage.Main.prefix;
import static notzStorage.utils.managers.PlayerManager.storageMin;
import static notzStorage.utils.managers.PlayerManager.updateStorages;
import static notzStorage.utils.managers.VirtualPlotManager.getVirtualPlots;
import static notzStorage.utils.sub.MsgU.c;

public class StorageManager extends DM implements Runnable {
    private static Double[] prices = new Double[10];
    private static int stock, stockTime, stockclock;
    private static final StorageManager instance = new StorageManager();

    public static Double[] getPrices() {
        return prices;
    }

    public static int getStock() {
        return stock;
    }

    public static void setPrice(int id, double value) throws SQLException {
        prices[id] = value;
        setPriceItem(id, value);
    }


    public static void updateStock() {
        try {
            saveStock(stock, stockTime, stockclock);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (stockTime-stockclock > 0) {
            stockclock++;
        } else updateStockNow();
    }

    public static void updateStockNow() {
        Random r = new Random();

        for (int i = 0; i < 6; i++) {
            int a = r.nextInt(200);
            stock += a;
            Bukkit.getConsoleSender().sendMessage(c("&a[log]&f " + i + ": " + a + "%"));
        }
        stock /= 6;
        stockTime = r.nextInt(r.nextInt(r.nextInt(r.nextInt(380)+60)+30)+15);
        stockclock = 0;

        int h, m;
        Bukkit.getConsoleSender().sendMessage("stockTime: " + stockTime);

        if (stockTime > 60) {
            h = stockTime / 60;
            m = stockTime % 60;

            Bukkit.getConsoleSender().sendMessage("-------------");
            Bukkit.getConsoleSender().sendMessage(String.valueOf(stock));
            Bukkit.getConsoleSender().sendMessage(String.valueOf(h));
            Bukkit.getConsoleSender().sendMessage(String.valueOf(m));
            Bukkit.broadcastMessage(c(prefix + "&eA porcentagem da &2&oBolsa de Valores &emudou para &2&l" + stock + "&2% &ee durará &a" + h + " hora(s) &ee &a" + m + " minutos&e."));

        } else Bukkit.broadcastMessage(c(prefix + "&eA porcentagem da &2&oBolsa de Valores &emudou para &2&l" + stock + "&2% &ee durará &a" + stockTime + " minutos&e."));
    }

    public static double calc(int quantity, int id) {
        double price = prices[id];
        double result = 0;

        if (quantity > 1000) {
            double temp=0;
            for (int i = 0; i < 1000; i++) {
                temp += price * (1 - 0.001 * i);
            }
            int med= quantity/1000;
            result = temp*med;
            temp=0;
            for (int i = 0; i < quantity-med*1000; i++) {
                temp += price * (1 - 0.001 * i);
            }
            result += temp;

        } else for (int i = 0; i < quantity; i++) {
            result += price * (1 - 0.001 * i);
        }

        return result * getStock()/100;
    }

    public static void load() {
        try {
            prices = loadPrices();

            Integer[] st = loadStock();
            stock = st[0];
            stockTime = st[1];
            stockclock = st[2];
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static StorageManager getInstance() {
        return instance;
    }

    @Override
    public void run() {
        if (!getVirtualPlots().isEmpty())
            getVirtualPlots().keySet().forEach(VirtualPlotModel::farming);

        updateStorages();
        updateStock();

        storageMin();
    }
}