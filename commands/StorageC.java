package notzStorage.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Arrays;

import static notzStorage.Main.header;
import static notzStorage.Main.started;
import static notzStorage.utils.managers.GeralManager.*;
import static notzStorage.utils.managers.PlayerManager.getPlayers;
import static notzStorage.utils.managers.StorageManager.*;
import static notzStorage.utils.managers.VirtualPlotManager.*;
import static notzStorage.utils.sub.MsgU.c;
import static notzStorage.utils.sub.MsgU.send;

public class StorageC implements CommandExecutor {
    private static Player p;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!started)
            return false;

        if (!(sender instanceof Player))
            return false;

        p = (Player) sender;

        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "updatestock":
                        updateStockNow();
                        break;
                    case "backup":
                        startBackup();
                        send(p, "&eBackup da database do plugin sendo realizada!");
                        break;
                    case "getprices":
                        send(p, "&eLista de prices: " + Arrays.toString(getPrices()));
                        break;
                    case "reset":
                        resetPlugin();
                        startPlugin();
                        send(p, "&eDatabase resetada com sucesso!");
                        break;
                    case "setcactus":
                        send(p, "&eUtilize &f/&enstorage setcactus &f<&ePlayer&f> <&eId da VP&f> <&e32&f/&e64&f>");
                        break;
                    case "help":
                    default:
                        help();
                }
                break;
            case 2:
                switch (args[0]) {
                    case "addvp":
                        if (Bukkit.getPlayerExact(args[1]) != null) {
                            if (addVirtualPlot(getPlayers().get(Bukkit.getPlayerExact(args[1])))) {
                                send(p, "&eO player &f" + args[1] + "&e recebeu uma nova Plot Virtual.");
                                send(Bukkit.getPlayerExact(args[1]), "&aVocê recebeu uma nova &fPlot Virtual&e!");

                            } else send(p, "&cNão foi possível atribuir uma Plot Virtual ao player &f" + args[1] + "&e.");

                        } else help();
                        break;

                    case "list":
                        if (Bukkit.getPlayerExact(args[1]) != null) {
                            if (!getPlayerVirtualPlots(Bukkit.getPlayerExact(args[1])).isEmpty())
                                send(p, "&eVPs de &f" + args[1] + "&e: " + Arrays.toString(getPlayerVirtualPlots(Bukkit.getPlayerExact(args[1])).toArray()).replace("[", "[&f").replace("]", "&e].").replace(",", "&e,&f"));

                            else send(p, "&cNão há VPs para o player &f" + args[1] + "&c.");

                        } else send(p, "&cEste player não existe ou está offline.");
                        break;

                    case "setcactus":
                        send(p, "&eUtilize &f/&enstorage setcactus &f<&ePlayer&f> <&eId da VP&f> <&e32&f/&e64&f>");
                        break;

                    default:
                        help();
                }
                break;
            case 3:
                switch (args[0].toLowerCase()){
                    case "removevp":
                        if (Bukkit.getPlayerExact(args[1]) != null && getVirtualPlots().containsValue(getPlayers().get(Bukkit.getPlayerExact(args[1])))) {

                            String vps = args[1] + args[2];

                            if (getVirtualPlots().keySet().stream().anyMatch(vp -> vp.getUuid().equals(vps)))
                                if (remVirtualPlot(getVirtualPlots().keySet().stream().filter(vp -> vp.getUuid().equals(vps)).findFirst().get())) {
                                    send(p, "&eA &fPlot Virtual " + vps + "&e foi excluída com sucesso.");
                                    send(Bukkit.getPlayerExact(args[1]), "&eSua &fPlot Virtual " + vps + "&e foi excluída.");
                                } else send(p, "&cNão foi possível excluir a &fPlot Virtual " + vps + "&e.");
                        } else help();
                        break;
                    case "setcactus":
                        send(p, "&eUtilize &f/&enstorage setcactus &f<&ePlayer&f> <&eId da VP&f> <&e32&f/&e64&f>");
                        break;
                    case "setprice":
                        try {
                            switch (args[1]) {
                                case "carrot":
                                    setPrice(0, Double.parseDouble(args[2]));
                                    send(p, "&ePreço da &fcenoura &ealterado para &2$&a" + args[2] + "&e.");
                                    break;
                                case "cactus":
                                    setPrice(1, Double.parseDouble(args[2]));
                                    send(p, "&ePreço do &fcacto &ealterado para &2$&a" + args[2] + "&e.");
                                    break;
                                case "melon":
                                    setPrice(2, Double.parseDouble(args[2]));
                                    send(p, "&ePreço da &fmelancia &ealterado para &2$&a" + args[2] + "&e.");
                                    break;
                                case "melon_block":
                                    setPrice(3, Double.parseDouble(args[2]));
                                    send(p, "&ePreço do &fbloco de melancia &ealterado para &2$&a" + args[2] + "&e.");
                                    break;
                                case "nether_warts":
                                    setPrice(4, Double.parseDouble(args[2]));
                                    send(p, "&ePreço do &ffungo &ealterado para &2$&a" + args[2] + "&e.");
                                    break;
                                case "potato":
                                    setPrice(5, Double.parseDouble(args[2]));
                                    send(p, "&ePreço da &fbatata &ealterado para &2$&a" + args[2] + "&e.");
                                    break;
                                case "pumpkim":
                                    setPrice(6, Double.parseDouble(args[2]));
                                    send(p, "&ePreço da &fabóbora &ealterado para &2$&a" + args[2] + "&e.");
                                    break;
                                case "sugar_cane":
                                    setPrice(7, Double.parseDouble(args[2]));
                                    send(p, "&ePreço da &fcana-de-açúcar &ealterado para &2$&a" + args[2] + "&e.");
                                    break;
                                case "wheat":
                                    setPrice(8, Double.parseDouble(args[2]));
                                    send(p, "&ePreço do &ftrigo &ealterado para &2$&a" + args[2] + "&e.");
                                    break;
                                case "cactusvp":
                                    if (Integer.parseInt(args[2]) > getPrices()[1]) {
                                        setCactusPriceVP(Integer.parseInt(args[2]));
                                        send(p, "&ePreço do &fcacto VirtualPlot &ealterado para &2$&a" + args[2] + "&e.");
                                    } else send(p, "&cO preço do cacto da VP precisa maior que o preço de venda &f(&2$&a" + getPrices()[1] + "&6c&f)&c.");
                                    break;
                                default:
                                    help();
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (NumberFormatException e) {
                            send(p, "&cUtilize . (ponto) para casa decimal.");
                        }
                        break;
                    default:
                        help();
                }
                break;

            case 4:
                System.out.println(Arrays.toString(args));
                if (args[0].equalsIgnoreCase("setcactus")) {
                    if (Bukkit.getPlayer(args[1]) == null)
                        send(p, "&cEste player não existe ou está offline.");

                    else if (getPlayerVirtualPlots(Bukkit.getPlayer(args[1])).isEmpty())
                        send(p, "&cEste player não possui &fVirtual Plots&c.");

                    else if (getPlayerVirtualPlots(Bukkit.getPlayer(args[1])).stream().noneMatch(vp -> vp.getUuid().equalsIgnoreCase(args[1] + args[2])))
                        send(p, "&cO Player não tem uma &f&lVirtual Plot " + args[2] + "&c.");

                    else {
                        switch (args[3]) {
                            case "32":
                                if (setCactus(getPlayerVirtualPlots(Bukkit.getPlayer(args[1])).stream().filter(vp -> vp.getUuid().equalsIgnoreCase(args[1] + args[2])).findFirst().get(), 32000))
                                    send(p, "&aFoi setado 32K cactos para a &f&lVirtual Plot &f" + args[1]+args[2] + "&a.");

                                else send(p, "&cNão foi possível setar 32K cactos para a &f&lVirtual Plot &f" + args[1]+args[2] + "&c.");
                                break;
                            case "64":
                                if (setCactus(getPlayerVirtualPlots(Bukkit.getPlayer(args[1])).stream().filter(vp -> vp.getUuid().equalsIgnoreCase(args[1] + args[2])).findFirst().get(), 64000))
                                    send(p, "&aFoi setado 32K cactos para a &f&lVirtual Plot &f" + args[1]+args[2] + "&a.");

                                else send(p, "&cNão foi possível setar 32K cactos para a &f&lVirtual Plot &f" + args[1]+args[2] + "&c.");
                                break;
                            default:
                                send(p, "&eUtilize &f/&enstorage setcactus &f<&ePlayer&f> <&eId da VP&f> <&e32&f/&e64&f>");
                        }
                    }

                } else help();
                break;

            default:
                help();
        }

        return true;
    }


    private void help() {
        p.sendMessage(header);
        p.sendMessage(c("&f/&enstorage &7| &f/&envp"));
        p.sendMessage(c("&f/&enstorage backup &7- Faz backup da database."));
        p.sendMessage(c("&f/&enstorage addvp &f<&ePlayer&f> &7- Adiciona uma Virtual Plot à um player."));
        p.sendMessage(c("&f/&enstorage removevp &f<&ePlayer&f> <&ePlot id&f> &7- Exclui uma Virtual Plot."));
        p.sendMessage(c("&f/&enstorage setcactus &f<&ePlayer&f> <&eId da VP&f> <&e32&f/&e64&f> &7- Adiciona 32K ou 64K à Virtual Plot de um player."));
        p.sendMessage(c("&f/&enstorage setprice &f<&eitem&f> <&eprice&f> &7- Seta o valor de um item de farm."));
        p.sendMessage(c("&f/&enstorage setprice cactusVP &f<&eprice&f> &7- Seta o valor do item cacto da Virtual Plot."));
        p.sendMessage(c("&f/&enstorage updatestock &7- Atualiza a bolsa de valores."));
        p.sendMessage("");
    }
}
