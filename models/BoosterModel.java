package notzStorage.models;

import notzStorage.utils.sub.DM;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static notzStorage.utils.sub.MsgU.c;

public class BoosterModel extends DM implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;
    private String display;
    private int percentage;

    public BoosterModel(String boostName, String display, int percentage) {
        name = boostName;
        this.display = display;
        this.percentage = percentage;

        id = setup();
    }

    public BoosterModel(int id, String boostName, String boostDisplayName, int percentage) {
        this.id = id;
        name = boostName;
        display = boostDisplayName;
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public ItemStack getBooster(int time) {
        return buildItem(time);
    }

    public void setPercentage(int percentage) throws SQLException {
        this.percentage = percentage;
        setBoosterPercentage(id, percentage);
    }

    public void setDisplay(String display) throws SQLException {
        this.display = display;
        setBoosterDisplay(id, display);
    }

    private ItemStack buildItem() {
        ItemStack newItem = new ItemStack(Material.EXP_BOTTLE);

        ItemMeta metaKey = newItem.getItemMeta();
        metaKey.setDisplayName(c("&f&lBooster " + display));

        List<String> lore = new ArrayList<>();

        lore.add(c("&7&oBooster de: &5&o" + percentage));
        lore.add(c("&7&ocom duração de:"));
        metaKey.setLore(lore);

        metaKey.addEnchant(Enchantment.LUCK, id, true);
        metaKey.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        newItem.setItemMeta(metaKey);

        return newItem;
    }

    private ItemStack buildItem(int time) {
        ItemStack newItem = new ItemStack(Material.EXP_BOTTLE);

        ItemMeta metaKey = newItem.getItemMeta();
        metaKey.setDisplayName(c("&f&lBooster " + display));

        List<String> lore = new ArrayList<>();

        lore.add(c("&7&oBooster de: &5&o" + percentage + "%"));
        lore.add(c("&7&ocom duração de:"));

        if (time > 60) {
            int m, s;

            m = time / 60;
            s = time % 60;

            if (s == 0)
                lore.add(c("&5&o" + m + " minutos&7."));
            else lore.add(c("&5&o" + m + " minutos &7&oe &5&o" + s + " segundos&7."));
        } else lore.add(c("&4&o" + time + " segundos&7."));

        metaKey.setLore(lore);

        metaKey.addEnchant(Enchantment.LUCK, id, true);
        metaKey.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        newItem.setItemMeta(metaKey);

        return newItem;
    }

    public int setup() {
        try {
            addBoosterDB(name, display, percentage);
            return findBoosterID(name);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
