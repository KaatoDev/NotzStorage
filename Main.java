package notzStorage;

import net.milkbowl.vault.economy.Economy;
import notzStorage.commands.BoosterC;
import notzStorage.commands.SellC;
import notzStorage.commands.StorageC;
import notzStorage.events.*;
import notzStorage.files.ConfigFile;
import notzStorage.utils.managers.BoosterManager;
import notzStorage.utils.managers.PlayerManager;
import notzStorage.utils.managers.StorageManager;
import notzStorage.utils.managers.VirtualPlotManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static notzStorage.utils.managers.GeralManager.startBackup;
import static notzStorage.utils.managers.GeralManager.startPlugin;
import static notzStorage.utils.sub.MsgU.c;
import static notzStorage.utils.sub.MsgU.send;

public class Main extends JavaPlugin {
    private static Economy econ = null;
    public static Main plugin;
    public static String prefix, header;
    public static ConfigFile cf, sqf, msgf;
    public static boolean started;
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PreLoadGrowEv(), this);
        plugin = this;
        cf = new ConfigFile(this, "config");
        sqf = new ConfigFile(this, "notzStorage");
        msgf = new ConfigFile(this, "messages");
        prefix = c(msgf.getConfig().getString("prefix"));
        header = "\n" + c("&f-=-=-=-&b= " + prefix + "&b=&f-=-=-=-");

        new BukkitRunnable(){public void run() {
            startPlugin();
            letters();
        }}.runTaskLater(this, 2*20);

        new BukkitRunnable(){public void run() {
            BoosterManager.load();
            StorageManager.load();
            PlayerManager.load();
            VirtualPlotManager.load();

            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer("Gago3242")))
                send(Bukkit.getPlayer("Gago3242"), "&aplugin iniciado.");
            Bukkit.getConsoleSender().sendMessage(c(prefix + "&aplugin iniciado"));

            started = true;
        }}.runTaskLater(this, 4*20);

        regCommands();
        regEvents();
        regTab();

        Bukkit.getScheduler().runTaskTimer(this, StorageManager.getInstance(), 20*60, 20*60);
        Bukkit.getScheduler().runTaskTimer(this, BoosterManager.getInstance(), 100, 20);

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (getServer().getPluginManager().getPlugin("Vault") != null && rsp != null)
            econ = rsp.getProvider();
        else {
            Bukkit.getConsoleSender().sendMessage(c("&4ECONOMIA NÃO SETADA KRL"));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        VirtualPlotManager.save();
        BoosterManager.save();
        PlayerManager.save();
        startBackup();
    }
    
    private void regEvents() {
        Bukkit.getPluginManager().registerEvents(new GrowEv(), this);
        Bukkit.getPluginManager().registerEvents(new BoosterEv(), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeaveEv(), this);
        Bukkit.getPluginManager().registerEvents(new GuiEv(), this);
    }

    private void regCommands() {
        getCommand("nbooster").setExecutor(new BoosterC());
        getCommand("nstorage").setExecutor(new StorageC());
        getCommand("sell").setExecutor(new SellC());
    }
    private void regTab() {
        //getCommand("ncrates").setTabCompleter(new NCratesC());
    }
    public static Economy getEconomy() {
        return econ;
    }

    private void letters() {
        Bukkit.getConsoleSender().sendMessage(prefix + c("&2Inicializado com sucesso.") +
                        c("\n&f┳┓    &6┏┓           "
                        + "\n&f┃┃┏┓╋┓&6┗┓╋┏┓┏┓┏┓┏┓┏┓"
                        + "\n&f┛┗┗┛┗┗&6┗┛┗┗┛┛ ┗┻┗┫┗ "
                        + "\n&f      &6          ┛  "));
    }
}
