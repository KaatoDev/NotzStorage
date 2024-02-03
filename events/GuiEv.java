package notzStorage.events;

import notzStorage.gui.StorageGUI;
import notzStorage.models.VirtualPlotModel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static notzStorage.Main.started;
import static notzStorage.gui.StorageGUI.cactus;
import static notzStorage.gui.StorageGUI.shopping;
import static notzStorage.utils.managers.PlayerManager.getPlayers;
import static notzStorage.utils.managers.VirtualPlotManager.*;
import static notzStorage.utils.sub.MsgU.*;

public class GuiEv implements Listener {
    private HashMap<UUID, Long> contador = new HashMap<>();
    private int cont;

    private boolean pass(InventoryView view) {
        return view.getType().equals(InventoryType.CHEST)
                && (view.getTitle().contains("VirtualPlots")
                || view.getTitle().contains("VirtualPlot")
                || view.getTitle().contains("Farm"));
    }

    private boolean spam(Player player) {
        if (cont++ > 5) {
            player.kickPlayer("CALMA AE KRL");
            cont = 0;
        }
        return contador.containsKey(player.getUniqueId()) && System.currentTimeMillis() - contador.get(player.getUniqueId()) < 500;
    }
    @EventHandler
    public void openGui(InventoryClickEvent e) {
        if (!started)
            return;

        if (e.getCurrentItem() == null || !pass(e.getView()))
            return;

        Player p = (Player) e.getWhoClicked();

        if (spam((Player) e.getWhoClicked())) {
            send(p, msg("spamGUI"));
            e.setCancelled(true);
            return;
        }

        if (!((Player) e.getWhoClicked()).isOnline()) {
            e.setCancelled(true);
            return;
        }

        StorageGUI stGUI = new StorageGUI(p);

        HashMap<String, ItemStack> items = stGUI.getItems();
        String title = e.getView().getTitle();
        ItemStack item = e.getCurrentItem();

        contador.put(p.getUniqueId(), System.currentTimeMillis());
        cont = 0;

        HashMap<String, String> menus = new HashMap<>();
        menus.put("menu", c("&6&lVirtualPlots &eMenu &7[" + p.getName() + "]"));
        menus.put("drops", c("&6&lVirtualPlots &eDrops &7[" + p.getName() + "]"));
        menus.put("virtualplots", c("&6&lVirtualPlots &7[" + p.getName() + "]"));
        menus.put("storage", c("&6&lVirtualPlot &eStorage:"));
        menus.put("settings", c("&6&lVirtualPlot &eSettings:"));
        menus.put("storagecactus", c("&6&lVirtualPlot &eCactus:"));
        menus.put("storagecactussave", c("&6&lVirtualPlot &eConfirm:"));
        menus.put("menufarm", c("&6&lFarm &eMenu: &7[" + p.getName() + "]"));
        menus.put("storagefarm", c("&6&lFarm &eStorage: &7[" + p.getName() + "]"));
        menus.put("settingsfarm", c("&6&lFarm &eSettigs: &7[" + p.getName() + "]"));

        // MENU - start
        if (title.equals(menus.get("menu"))) {
            e.setCancelled(true);

            if (items.containsValue(item)) {

                if (e.getClick().isLeftClick()) {

                    if (item.equals(items.get("plots")))
                        p.openInventory(stGUI.virtualPlots());

                    else if (item.equals(items.get("drops")))
                        p.openInventory(stGUI.dropsVP());
                }

                if (item.equals(items.get("sair")))
                    p.closeInventory();

            }
            return;
        }
        // MENU - end

        // DROPS - start
        if (title.equals(menus.get("drops"))) {
            e.setCancelled(true);

            if (items.containsValue(item)) {

                if (e.getClick().isLeftClick()) {
                    if (item.equals(items.get("sellall"))) {

                        if (!getPlayerVirtualPlots(p).isEmpty()) {
                            p.sendMessage("");
                            getPlayerVirtualPlots(p).forEach(VirtualPlotModel::sellAll);
                            p.sendMessage("");
                            p.openInventory(stGUI.dropsVP());

                        } else send(p, "&cVocê não tem o que vender.");

                    } else if (item.equals(items.get("sellone")))
                        p.openInventory(stGUI.virtualPlots());
                }

                if (item.equals(items.get("voltar")))
                    p.openInventory(stGUI.menuVP());
                else if (item.equals(items.get("sair")))
                    p.closeInventory();

            }
            return;
        }
        // DROPS - end

        // VIRTUALPLOTS - start
        if (title.equals(menus.get("virtualplots"))) {
            e.setCancelled(true);


            if (e.getClick().isLeftClick() && item.getType().equals(Material.SAND)) {
                int id = Integer.parseInt(item.getItemMeta().getDisplayName().substring(4, 5));
                p.openInventory(stGUI.storageVP(id - 1));
            }

            else if (items.containsValue(item)) {

                if (item.equals(items.get("voltar")))
                    p.openInventory(stGUI.menuVP());

                else if (item.equals(items.get("sair")))
                    p.closeInventory();
            }
            return;
        }
        // VIRTUALPLOTS - end

        // STORAGE - start
        if (title.contains(menus.get("storage"))) {
            e.setCancelled(true);

            int id = Integer.parseInt(e.getInventory().getItem(8).getItemMeta().getDisplayName().substring(4, 5))-1;
            int idd = id + 1;

            if (!title.equals(c("&6&lVirtualPlot &eStorage: &7[&f" + p.getName()+(id+1) + "&7]")))
                return;

            if (items.containsValue(item)) {
                if (e.getClick().isLeftClick())

                    if (item.equals(items.get("sell"))) {
                        p.sendMessage("");
                        getVirtualPlot(p, idd).sellAll();
                        p.sendMessage("");
                        p.openInventory(stGUI.storageVP(id));

                    } else if (item.equals(items.get("settings")))
                        p.openInventory(stGUI.settingsVP(id));

                if (item.equals(items.get("voltar")))
                    p.openInventory(stGUI.virtualPlots());

                else if (item.equals(items.get("sair")))
                    p.closeInventory();
            }
            return;
        }
        // STORAGE - end

        // SETTINGS - start
        if (title.contains(menus.get("settings"))) {
            e.setCancelled(true);

            int id = Integer.parseInt(e.getInventory().getItem(8).getItemMeta().getDisplayName().substring(4, 5))-1;

            if (!title.equals(c("&6&lVirtualPlot &eSettings: &7[&f" + p.getName()+(id+1) + "&7]")))
                return;

            if (items.containsValue(item)) {
                if (e.getClick().isLeftClick())
                    if (item.equals(items.get("cactus")))
                        p.openInventory(stGUI.storageCactusVP(id));

//                    else if (item.equals(items.get("speed")))
//                    else if (item.equals(items.get("capacity")))


                if (item.equals(items.get("voltar")))
                    p.openInventory(stGUI.storageVP(id));

                else if (item.equals(items.get("sair")))
                    p.closeInventory();
            }
            return;
        }
        // SETTINGS - end

        // STORAGECACTUS - start
        if (title.contains(menus.get("storagecactus"))) {
            e.setCancelled(true);

            int id = Integer.parseInt(e.getInventory().getItem(8).getItemMeta().getDisplayName().substring(4, 5))-1;

            int idd = id + 1;

            if (!title.equals(c("&6&lVirtualPlot &eCactus: &7[&f" + p.getName()+(id+1) + "&7]")))
                return;

            if (!shopping)
                cactus = 0;

            int cac = getVirtualPlot(p, idd).getCactus();

            if (items.containsValue(item)) {
                if (e.getClick().isLeftClick()) {

                    if (item.equals(items.get("cactus-1"))) {
                        if (cac > 0 && cactus+cac-1 >= 0)
                            cactus--;

                    } else if (item.equals(items.get("cactus-10"))) {
                        if (cac > 9 && cactus+cac-10 >= 0) {
                            cactus -= 10;
                        } else cactus = cac*-1;

                    } else if (item.equals(items.get("cactus-32"))) {
                        if (cac > 31 && cactus+cac-32 >= 0) {
                            cactus -= 32;
                        } else cactus = -cac;

                    } else if (item.equals(items.get("cactus-64"))) {
                        if (cac > 63 && cactus+cac-64 >= 0) {
                            cactus -= 64;
                        } else cactus = -cac;

                    } else if (item.equals(items.get("cactus+1"))) {
                        if (cactus < 64000)
                            cactus++;

                    } else if (item.equals(items.get("cactus+10"))) {
                        if (cac < 63991) {
                            if (cactus > 63990)
                                cactus = 64000;
                            else cactus += 10;
                        } else cactus = 64000-cac;

                    } else if (item.equals(items.get("cactus+32"))) {
                        if (cac < 63969) {
                            if (cactus > 63968)
                                cactus = 64000;
                            else cactus += 32;
                        } else cactus = 64000-cac;

                    } else if (item.equals(items.get("cactus+64"))) {
                        if (cac < 63937) {
                            if (cactus > 63936)
                                cactus = 64000;
                            else cactus += 64;
                        } else cactus = 64000-cac;

                    } else if (item.equals(items.get("save")) && cactus != 0)
                        p.openInventory(stGUI.storageCactusSaveVP(id, cactus));

                    ItemMeta meta = e.getInventory().getItem(13).getItemMeta();

//                    int qtt = cactus - getVirtualPlot(p, idd).getCactus();

                    if (cactus > 0) {
                        meta.setLore(Arrays.asList(c("&aCompra&f: " + cactus + "&7 cactos."), c("&ePreço: &2$&c-" + cactus * getCactusPriceVP())));
                        e.getInventory().getContents()[13].setAmount(64);

                    } else if (cactus < 0) {
                        meta.setLore(Arrays.asList(c("&cVenda&f: " + cactus + "&7 cactos."), c("&eReembolso: &2$&a+" + cactus * getCactusPriceVP() * 0.9 * -1)));
                        e.getInventory().getContents()[13].setAmount(-64);

                    } else meta.setLore(Collections.singletonList(c("&eAdicione ou remova cactos.")));

                    e.getInventory().getItem(13).setItemMeta(meta);
                }


                if (item.equals(items.get("voltar")))
                    p.openInventory(stGUI.storageVP(id));

                else if (item.equals(items.get("sair")))
                    p.closeInventory();
            }
            return;
        }
        // STORAGECACTUS - end

        // STORAGECACTUSSAVE - start
        if (title.contains(menus.get("storagecactussave"))) {
            e.setCancelled(true);

            int id = Integer.parseInt(e.getInventory().getItem(8).getItemMeta().getDisplayName().substring(4, 5))-1;
            int idd = id+1;
            int cac = cactus;
            cactus=0;

            if (!title.equals(c("&6&lVirtualPlot &eConfirm: &7[&f" + p.getName()+(idd) + "&7]")))
                return;

            if (items.containsValue(item)) {
                if (e.getClick().isLeftClick()) {
                    if (item.equals(items.get("confirm"))) {

                        if (cac > 0) {
                            if (buyCactus(getVirtualPlot(p, idd), cac)) {
                                send(p, "&eVocê comprou &a" + cac + "&e para a sua &fPlot Virtual " + idd);
                                send(p, "&epor &2$&a" + cac * getCactusPriceVP() + " {money} &e.");

                            } else send(p, "&cVocê não tem dinheiro suficiente para isto!");

                        } else send(p, "&cErro, contate um staff superior.");

                        p.openInventory(stGUI.storageCactusVP(id));

                    } else if (item.equals(items.get("confirm2"))) {

                        if (cac < 0) {
                            if (sellCactus(getVirtualPlot(p, idd), cac)) {
                                send(p, "&eVocê vendeu &a" + cac + "&e da sua &fPlot Virtual " + idd);
                                send(p, "&epor &2$&a" + cac * getCactusPriceVP() * 0.9 + " {money} &e.");

                            } else send(p, "&cNão foi possível vender os cactos!");

                        } else send(p, "&cErro, contate um staff superior.");

                        p.openInventory(stGUI.storageCactusVP(id));

                    } else if (item.equals(items.get("cancel")))
                        p.openInventory(stGUI.storageCactusVP(id));
                }

                if (item.equals(items.get("voltar")))
                    p.openInventory(stGUI.storageVP(id));

                else if (item.equals(items.get("sair")))
                    p.closeInventory();
            }
            return;
        }
        // STORAGECACTUSSAVE - end

        // MENUFARM - start
        if (title.equals(menus.get("menufarm"))) {
            e.setCancelled(true);

            if (items.containsValue(item)) {
                if (e.getClick().isLeftClick()) {

                    if (item.equals(items.get("storagefarm")))
                        p.openInventory(stGUI.storageFarm());

                    else if (item.equals(items.get("settingsfarm")))
                        p.openInventory(stGUI.settingsFarm());
                }

                if (item.equals(items.get("sair")))
                    p.closeInventory();
            }
            return;
        }
        // MENUFARM - end

        // STORAGEFARM - start
        if (title.equals(menus.get("storagefarm"))) {
            e.setCancelled(true);

            if (items.containsValue(item)) {
                if (e.getClick().isLeftClick()) {

                    if (item.equals(items.get("sellfarm"))) {
                        getPlayers().get(p).sellStorage();
                        p.openInventory(stGUI.storageFarm());

                    } else if (item.equals(items.get("plots")))
                        p.openInventory(stGUI.virtualPlots());
                }

                if (item.equals(items.get("voltar")))
                    p.openInventory(stGUI.menuFarm());

                else if (item.equals(items.get("sair")))
                    p.closeInventory();
            } else {

                switch (e.getSlot()) {
                    case 20:
                        if (item.getType().equals(Material.CACTUS))
                            getPlayers().get(p).sellStorage(1);
                        break;
                    case 24:
                        if (item.getType().equals(Material.SUGAR_CANE))
                            getPlayers().get(p).sellStorage(7);
                        break;
                    case 28:
                        if (item.getType().equals(Material.CARROT_ITEM))
                            getPlayers().get(p).sellStorage(0);
                        break;
                    case 29:
                        if (item.getType().equals(Material.MELON))
                            getPlayers().get(p).sellStorage(2);
                        break;
                    case 30:
                        if (item.getType().equals(Material.MELON_BLOCK))
                            getPlayers().get(p).sellStorage(3);
                        break;
                    case 31:
                        if (item.getType().equals(Material.NETHER_STALK))
                            getPlayers().get(p).sellStorage(4);
                        break;
                    case 32:
                        if (item.getType().equals(Material.POTATO_ITEM))
                            getPlayers().get(p).sellStorage(5);
                        break;
                    case 33:
                        if (item.getType().equals(Material.PUMPKIN))
                            getPlayers().get(p).sellStorage(6);
                        break;
                    case 34:
                        if (item.getType().equals(Material.WHEAT))
                            getPlayers().get(p).sellStorage(8);
                }
                p.openInventory(stGUI.storageFarm());
            }
            return;
        }
        // STORAGEFARM - end

        // SETTINGSFARM - start
        if (title.equals(menus.get("settingsfarm"))) {
            e.setCancelled(true);

            if (items.containsValue(item)) {
                if (e.getClick().isLeftClick()) {

                    if (item.equals(items.get("notifyon"))) {
                        getPlayers().get(p).disableNotify();
                        p.openInventory(stGUI.settingsFarm());

                    } else if (item.equals(items.get("notifyoff"))) {
                        getPlayers().get(p).enableNotify();
                        p.openInventory(stGUI.settingsFarm());
                    }

//                  else if (item.equals(items.get("autofarm")))
                }

                if (item.equals(items.get("voltar")))
                    p.openInventory(stGUI.menuFarm());

                else if (item.equals(items.get("sair")))
                    p.closeInventory();
            }
        }
        // SETTINGSFARM - end
    }
}