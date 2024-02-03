package notzStorage.gui;

import notzStorage.models.VirtualPlotModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;

import static notzStorage.utils.managers.PlayerManager.getPlayers;
import static notzStorage.utils.managers.StorageManager.calc;
import static notzStorage.utils.managers.VirtualPlotManager.*;
import static notzStorage.utils.sub.MsgU.*;

public class StorageGUI {
    public HashMap<String, ItemStack> items = new HashMap<>();
    private VirtualPlotModel[] vps;
    private Player p;
    public static boolean shopping;
    public static int cactus = 0;

    public StorageGUI(Player player) {
        p = player;

        vps = getVirtualPlots().keySet().stream().filter(v -> v.getPm().getP().equals(p)).sorted(Comparator.comparingInt(VirtualPlotModel::getId)).toArray(VirtualPlotModel[]::new);

        items.put("head", buildItem(getHead(p), s("head", p), Collections.emptyList(), false));
        items.put("sair", buildItem(Material.BARRIER, s("sair"), l("sair"), false));
        items.put("voltar", buildItem(Material.ARROW, s("voltar"), l("voltar"), false));
        items.put("save", buildItem(Material.SLIME_BALL, s("save"), l("save"), false));

        items.put("plots", buildItem(Material.QUARTZ_BLOCK, s("plots"), l("plots"), false));
        items.put("drops", buildItem(Material.INK_SACK, s("drops"), l("drops"), false));
        items.put("cactus", buildItem(Material.CACTUS, s("cactus"), l("cactus"), false));
        items.put("speed", buildItem(Material.FEATHER, s("speed"), l("speed"), false));
        items.put("capacity", buildItem(Material.ENDER_CHEST, s("capacity"), l("capacity"), false));
        items.put("sell", buildItem(Material.DOUBLE_PLANT, s("sell"), l("sell"), false));
        items.put("settings", buildItem(Material.REDSTONE_COMPARATOR, s("settings"), l("settings"), false));
        items.put("sellall", buildItem(Material.EYE_OF_ENDER, s("sellall"), l("sellall"), false));
        items.put("sellone", buildItem(Material.ENDER_PEARL, s("sellone"), l("sellone"), false));
        items.put("confirm", buildItem(g(5), s("confirm"), l("confirm"), false));
        items.put("cancel", buildItem(g(14), s("cancel"), l("cancel"), false));
        items.put("confirm2", buildItem(g(5), s("confirm2"), l("confirm2"), false));
        items.put("cancel2", buildItem(g(14), s("cancel2"), l("cancel2"), false));
        items.put("storagefarm", buildItem(Material.CACTUS, s("storagefarm"), l("storagefarm"), false));
        items.put("settingsfarm", buildItem(Material.REDSTONE_BLOCK, s("settingsfarm"), l("settingsfarm"), false));
        items.put("sellfarm", buildItem(Material.DOUBLE_PLANT, s("sellfarm"), l("sellfarm"), false));
        items.put("autofarm", buildItem(Material.HOPPER_MINECART, s("autofarm"), l("autofarm"), false));
        items.put("notifyon", buildItem(Material.NOTE_BLOCK, s("notifyon"), l("notifyon"), true));
        items.put("notifyoff", buildItem(Material.NOTE_BLOCK, s("notifyoff"), l("notifyoff"), false));

        items.put("cactus-1", buildItem(d(9), s("cactus-1"), l("cactus-1"), false));
        items.put("cactus-10", buildItem(d(13), s("cactus-10"), l("cactus-10"), false));
        items.put("cactus-32", buildItem(d(5), s("cactus-32"), l("cactus-32"), false));
        items.put("cactus-64", buildItem(d(8), s("cactus-64"), l("cactus-64"), false));
        items.put("cactus+1", buildItem(d(5), s("cactus+1"), l("cactus+1"), false));
        items.put("cactus+10", buildItem(d(13), s("cactus+10"), l("cactus+10"), false));
        items.put("cactus+32", buildItem(d(9), s("cactus+32"), l("cactus+32"), false));
        items.put("cactus+64", buildItem(d(10), s("cactus+64"), l("cactus+64"), false));
        items.put("cactusdisplay", buildItem(Material.PAPER, s("cactusdisplay"), l("cactusdisplay"), true));


    }

    // --------- Farms INÍCIO
    public Inventory menuFarm() {
        Inventory inv = Bukkit.createInventory(p, 27, c("&6&lFarm &eMenu: &7[" + p.getName() + "]"));

        inv.setContents(buildMold(0, "10-16", inv.getContents()));
        inv.setContents(buildMold(11, "frame", inv.getContents()));

        inv.setItem(11, items.get("storagefarm"));
        inv.setItem(15, items.get("settingsfarm"));
        inv.setItem(22, items.get("sair"));

        return inv;
    }

    public Inventory storageFarm() {
        Inventory inv = Bukkit.createInventory(p, 54, c("&6&lFarm &eStorage: &7[" + p.getName() + "]"));

        inv.setContents(buildMold(0, "all", inv.getContents()));
        inv.setContents(buildMold(11, "9-17 36-44", inv.getContents()));
        inv.setContents(buildMold(13, "19 21", inv.getContents()));
        inv.setContents(buildMold(5, "23 25", inv.getContents()));

        Integer[] storage = getPlayers().get(p).getStorage().clone();
        inv.setItem(20, buildItem(Material.CACTUS, "&2&lCactos", Arrays.asList("&fVocê possui:", "&a" + storage[1] + "&f cactos.", "&fPreço: &2$&a" + String.format("%.2f", calc(storage[1], 1))), false));
        inv.setItem(24, buildItem(Material.SUGAR_CANE, "&a&lCanas-de-açúcar", Arrays.asList("&fVocê possui:", "&a" + storage[7] + "&f canas-de-açúcar.", "&fPreço: &2$&a" + String.format("%.2f", calc(storage[7], 7))), false));
        inv.setItem(28, buildItem(Material.CARROT_ITEM, "&6&lCenouras", Arrays.asList("&fVocê possui:", "&a" + storage[0] + "&f cenouras.", "&fPreço: &2$&a" + String.format("%.2f", calc(storage[0], 0))), false));
        inv.setItem(29, buildItem(Material.MELON, "&a&lMelancias", Arrays.asList("&fVocê possui:", "&a" + storage[2] + "&f melancias.", "&fPreço: &2$&a" + String.format("%.2f", calc(storage[2], 2))), false));
        inv.setItem(30, buildItem(Material.MELON_BLOCK, "&2&lBlocos de melancia", Arrays.asList("&fVocê possui:", "&a" + storage[3] + "&f blocos de melancia.", "&fPreço: &2$&a" + String.format("%.2f", calc(storage[3], 3))), false));
        inv.setItem(31, buildItem(Material.NETHER_STALK, "&c&lFungos", Arrays.asList("&fVocê possui:", "&a" + storage[4] + "&f fungos.", "&fPreço: &2$&a" + String.format("%.2f", calc(storage[4], 4))), false));
        inv.setItem(32, buildItem(Material.POTATO_ITEM, "&e&lBatatas", Arrays.asList("&fVocê possui:", "&a" + storage[5] + "&f batatas.", "&fPreço: &2$&a" + String.format("%.2f", calc(storage[5], 5))), false));
        inv.setItem(33, buildItem(Material.PUMPKIN, "&6&lAbóboras", Arrays.asList("&fVocê possui:", "&a" + storage[6] + "&f abóboras.", "&fPreço: &2$&a" + String.format("%.2f", calc(storage[6], 6))), false));
        inv.setItem(34, buildItem(Material.WHEAT, "&e&lTrigos", Arrays.asList("&fVocê possui:", "&a" + storage[8] + "&f trigos.", "&fPreço: &2$&a" + String.format("%.2f", calc(storage[8], 8))), false));

        inv.setItem(0, items.get("voltar"));
        inv.setItem(3, items.get("sellfarm"));
        inv.setItem(5, items.get("plots"));
        inv.setItem(8, items.get("head"));
        inv.setItem(53, items.get("sair"));

        return inv;
    }

    public Inventory settingsFarm() {
        Inventory inv = Bukkit.createInventory(p, 27, c("&6&lFarm &eSettigs: &7[" + p.getName() + "]"));

        inv.setContents(buildMold(0, "all", inv.getContents()));

        inv.setItem(0, items.get("voltar"));
        inv.setItem(8, items.get("head"));
        inv.setItem(11, items.get("autofarm")); // em breve
        inv.setItem(22, items.get("sair"));

        inv.setContents(buildMold(3, "5 7 23 25", inv.getContents()));
        if (getPlayers().get(p).isNotify()) {
            inv.setContents(buildMold(5, "6 14 16 24", inv.getContents()));
            inv.setItem(15, items.get("notifyon"));

        } else {
            inv.setContents(buildMold(14, "6 14 16 24", inv.getContents()));
            inv.setItem(15, items.get("notifyoff"));

        }


        return inv;
    }
    // --------- Farms FIM

// ---------------------------------------------------------------

    // --------- VirtualPlots INÍCIO
    public Inventory menuVP() {
        Inventory inv = Bukkit.createInventory(p, 27, c("&6&lVirtualPlots &eMenu &7[" + p.getName() + "]"));

        inv.setContents(buildMold(0, "all", inv.getContents()));

        inv.setItem(11, items.get("plots"));
        inv.setItem(15, items.get("drops"));
        inv.setItem(22, items.get("sair"));

        return inv;
    }

    public Inventory dropsVP() {
        Inventory inv = Bukkit.createInventory(p, 27, c("&6&lVirtualPlots &eDrops &7[" + p.getName() + "]"));

        inv.setContents(buildMold(0, "all", inv.getContents()));

        if (getPlayerVirtualPlots(p).size() == 1) {
            VirtualPlotModel vp = getPlayerVirtualPlots(p).get(0);
            inv.setItem(15, buildItem(Material.GOLD_INGOT, "&ePreços dos drops", Collections.singletonList("&f" + vp.getUuid() + ": &2$&a" + vp.getPrice()), false));
        } else {
            List<VirtualPlotModel> vps = getPlayerVirtualPlots(p);
            List<String> vplots = new ArrayList<>();
            vplots.add("&fTodas: &2$&a" + vps.stream().map(VirtualPlotModel::getPrice).count());

            vplots.addAll(vps.stream().map(vp -> "&f" + vp.getUuid() + ": &2$&a" + vp.getPrice()).collect(Collectors.toList()));

            inv.setItem(15, buildItem(Material.GOLD_INGOT, "&ePreços dos drops", vplots, false));
        }

        inv.setItem(0, items.get("voltar"));
        inv.setItem(11, items.get("sellall"));
        inv.setItem(15, items.get("sellone"));
        inv.setItem(22, items.get("sair"));

        return inv;
    }

    public Inventory virtualPlots() {
        Inventory inv = Bukkit.createInventory(p, 36, c("&6&lVirtualPlots &7[" + p.getName() + "]"));

        inv.setContents(buildMold(0, "0-9 17 18 26-35", inv.getContents()));

        int start = 10;

        for (VirtualPlotModel vp : vps) {
            if (start == 17 || start == 18)
                start++;

            inv.setItem(start++, buildVP(vp));
        }

        inv.setItem(0, items.get("voltar"));
        inv.setItem(8, items.get("head"));
        inv.setItem(31, items.get("sair"));

        return inv;
    }

    public Inventory storageVP(int id) {
        Inventory inv = Bukkit.createInventory(p, 54, c("&6&lVirtualPlot &eStorage: &7[&f" + vps[id].getUuid() + "&7]"));

        inv.setContents(buildMold(0, "frame", inv.getContents()));

        if (vps[id].getStorage() > 0) {
            int sto = vps[id].getStorage();

            for (int i = 10; i <= 43; i++) {
                if (i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36)
                    i++;

                if (sto > 100000) {
                    sto -= 100000;
                    inv.setItem(i, buildItem(Material.CACTUS, "&2Inv de cactos", Arrays.asList("&fEste pack possui um total", "&fde 100000 cactos acumulados!"), true));

                } else if (sto > 0) {
                    inv.setItem(i, buildItem(Material.CACTUS, "&2Pack de cactos", Arrays.asList("&fEste pack possui um total", "&fde " + sto + " cactos acumulados!"), false));
                    sto = 0;
                }
            }
        }

        inv.setItem(0, items.get("voltar"));
        inv.setItem(3, items.get("sell"));
        inv.setItem(5, buildItem(Material.CACTUS, "&e&lVP Storage:", Arrays.asList("&fEsta VP contém:", "&a" + vps[id].getStorage() + "&f cactos.", "&ePreço: &2$&a"), true));
        inv.setItem(8, buildVP(vps[id]));
        inv.setItem(48, items.get("settings"));
        inv.setItem(50, items.get("sair"));

        return inv;
    }

    public Inventory settingsVP(int id) {
        Inventory inv = Bukkit.createInventory(p, 27, c("&6&lVirtualPlot &eSettings: &7[&f" + vps[id].getUuid() + "&7]"));

        inv.setContents(buildMold(0, "all", inv.getContents()));

        inv.setItem(0, items.get("voltar"));
        inv.setItem(8, buildVP(vps[id]));
        inv.setItem(11, items.get("cactus"));
        inv.setItem(13, items.get("speed")); // em breve
        inv.setItem(15, items.get("capacity")); // em breve
        inv.setItem(22, items.get("sair"));

        return inv;
    }

    public Inventory storageCactusVP(int id) {
        Inventory inv = Bukkit.createInventory(p, 54, c("&6&lVirtualPlot &eCactus: &7[&f" + vps[id].getUuid() + "&7]"));

        inv.setContents(buildMold(0, "all", inv.getContents()));
        inv.setContents(buildMold(11, "9-17 36-44", inv.getContents()));

        inv.setItem(4, buildItem(Material.CACTUS, "&eQuantidade de cactos", Arrays.asList("&fTotal: &a" + vps[id].getCactus(), "&eLimite: &264.000"), false));

        inv.setItem(8, buildVP(vps[id]));
        inv.setItem(20, items.get("cactus-64"));
        inv.setItem(24, items.get("cactus+64"));
        inv.setItem(28, items.get("cactus-1"));
        inv.setItem(29, items.get("cactus-10"));
        inv.setItem(30, items.get("cactus-32"));
        inv.setItem(32, items.get("cactus+1"));
        inv.setItem(33, items.get("cactus+10"));
        inv.setItem(34, items.get("cactus+32"));

        inv.setItem(0, items.get("voltar"));
        inv.setItem(13, items.get("cactusdisplay"));
        inv.setItem(47, items.get("save"));
        inv.setItem(51, items.get("sair"));

        shopping = true;
        return inv;
    }

    public Inventory storageCactusSaveVP(int id, int cac) {
        Inventory inv = Bukkit.createInventory(p, 27, c("&6&lVirtualPlot &eConfirm: &7[&f" + vps[id].getUuid() + "&7]"));

        inv.setContents(buildMold(0, "all", inv.getContents()));

        double price;
        inv.setItem(8, buildVP(vps[id]));
        if (cac > 0) {
            price = (cac - (vps[id].getCactus())) * getCactusPriceVP();
            inv.setItem(11, items.get("confirm"));
            inv.setItem(13, buildItem(Material.CACTUS, "&eQuantidade de cactus", Arrays.asList("&fAntiga: &a" + vps[id].getCactus(), "&fNova: " + (cac+vps[id].getCactus()), "&fPreço: &2$&a" + price), false));
            inv.setItem(15, items.get("cancel"));
        } else {
            price = (vps[id].getCactus() - cac) * getCactusPriceVP();
            inv.setItem(11, items.get("confirm2"));
            inv.setItem(13, buildItem(Material.CACTUS, "&eQuantidade de cactus", Arrays.asList("&fAntiga: &a" + vps[id].getCactus(), "&fNova: " + (vps[id].getCactus()+cac), "&fDevolução &c(90%): &2$&a" + (price * 0.9)), false));
            inv.setItem(15, items.get("cancel2"));
        }
        inv.setItem(22, items.get("sair"));

        shopping = false;
        return inv;
    }
// --------- VirtualPlots FIM

    @SuppressWarnings("deprecation")
    public ItemStack g(int b) {
        ItemStack a = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) b);
        ItemMeta s = a.getItemMeta();
        s.setDisplayName(" ");
        s.setLore(Collections.emptyList());
        a.setItemMeta(s);
        return a;
    }
    @SuppressWarnings("deprecation")
    private ItemStack d(int b) {
        ItemStack a = new ItemStack(351, 1, (short) 0, (byte) b);
        ItemMeta s = a.getItemMeta();
        s.setDisplayName(" ");
        s.setLore(Collections.emptyList());
        a.setItemMeta(s);
        return a;
    }

    private ItemStack buildItem(Material material, String name, List<String> lore, boolean enchantment) {
        return buildItem(new ItemStack(material), name, lore, enchantment);
    }
    private ItemStack buildItem(ItemStack item, String name, List<String> lore, boolean ench) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(c(name + "&r"));
        if (lore.size() != 1)
            lore.replaceAll(s -> c(s + "&r"));
        else lore = Collections.singletonList(c(lore.get(0) + "&r"));
        meta.setLore(lore);
        if (ench) {
            meta.addEnchant(Enchantment.LUCK, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack[] buildMold(int data, String slots, ItemStack[] it) {
        if (slots.equals("all"))
            for (int i=0; i<it.length; i++)
                it[i] = g(data);
        else if (slots.equals("frame"))
            switch (it.length/9) {
                case 3:
                    it = buildMold(data, "0-9 17-26", it);
                    break;
                case 4:
                    it = buildMold(data, "0-9 17-18 26-35", it);
                    break;
                case 5:
                    it = buildMold(data, "0-9 17-18 26-27 35-44", it);
                    break;
                case 6:
                    it = buildMold(data, "0-9 17-18 26-27 35-36 44-53", it);
                    break;
                default:
                    it = buildMold(data, "all", it);
            }
        else for (String sl : slots.split(" ")) {
                if (sl.contains("-")) {
                    int a1 = Integer.parseInt(sl.split("-")[0]);
                    int a2 = Integer.parseInt(sl.split("-")[1]);
                    for (int i=a1; i<=a2; i++) {
                        if (i < it.length)
                            it[i] = g(data);
                    }
                } else {
                    int ss = Integer.parseInt(sl);
                    if (ss < it.length)
                        it[ss] = g(data);
                }
            }
        return it;
    }

    private ItemStack getHead(Player player) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM);
        head.setDurability((short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(player.getName());
        head.setItemMeta(meta);
        return head;
    }

    private ItemStack buildVP(VirtualPlotModel vp) {
        ItemStack vpItem = new ItemStack(Material.SAND);
        ItemMeta meta = vpItem.getItemMeta();

        meta.setDisplayName(c("&f&l" + vp.getUuid().charAt(vp.getUuid().length() - 1)));
        meta.setLore(Arrays.asList(c("&fVirtualPlot de"), c("&f&o" + p.getName())));
        vpItem.setItemMeta(meta);

        return vpItem;
    }

    public HashMap<String, ItemStack> getItems() {
        return items;
    }
}
