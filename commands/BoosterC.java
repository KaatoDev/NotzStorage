package notzStorage.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static notzStorage.Main.*;
import static notzStorage.utils.managers.BoosterManager.*;
import static notzStorage.utils.sub.MsgU.c;
import static notzStorage.utils.sub.MsgU.send;

public class BoosterC implements TabExecutor {
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
                switch (args[0].toLowerCase()) {
                    case "create":
                    case "help":
                    case "remove":
                        help();
                        break;
                    case "list":
                        if (getBoosters().isEmpty())
                            send(p, "&cNão há Boosters.");
                        else send(p, Arrays.toString(getBoosters().keySet().toArray()));
                        break;
                    default:
                        if (getBoosters().containsKey(args[0].toLowerCase()))
                            helpBooster();
                        else send(p, "&cEste Booster não existe.");
                }
                break;
            case 2:
                switch (args[0].toLowerCase()) {
                    case "create":
                        send(p, "&eDê &f/&enbooster create &f<&enome&f> <&edisplay&f> <&e%&f>");
                        break;
                    case "remove":
                        if (getBoosters().containsKey(args[1].toLowerCase())) {
                            try {
                                removeBooster(getBoosters().get(args[1]));
                                send(p, "&eO &f&lBooster " + args[1] + "&e foi excluído.");
                            } catch (SQLException e) {
                                send(p, "&cNão foi possível excluir o &f&lBooster " + args[1] + "&e.");
                                throw new RuntimeException(e);
                            }
                        } else send(p, "&cEste Booster não existe.");
                        break;
                    default:
                        switch (args[1]) {
                            case "get":
                            case "setdisplay":
                            case "setpercentage":
                                if (getBoosters().containsKey(args[0].toLowerCase()))
                                    helpBooster();
                                else send(p, "&cNão existe um Booster com esse nome");
                                break;
                            default:
                                help();
                        }
                }
                break;
            case 3:
                if (args[1].equalsIgnoreCase("create"))
                    send(p, "&eDê &f/&enbooster create &f<&enome&f> <&edisplay&f> <&e%&f>");

                else switch (args[1]) {
                    case "get":
                        String bo = args[0].toLowerCase();

                        if (!getBoosters().containsKey(bo))
                            send(p, "&cNão há nenhum Booster com este nome.");

                        else giveBooster(p, bo, Integer.parseInt(args[2]));

                        break;
                    case "setdisplay":
                        String bob = args[1].toLowerCase();

                        if (!getBoosters().containsKey(bob))
                            send(p, "&cNão existe um Booster com esse nome");

                        else try {
                            getBoosters().get(bob).setDisplay(args[2]);
                            send(p, "&eDisplay do &f&lBooster " + bob + "&e alterada para " + args[2]);

                        } catch (SQLException e) {
                            send(p, "&cNão foi possível alterar o display do Booster.");
                            e.printStackTrace();
                        }

                        break;
                    case "setpercentage":
                        if (!getBoosters().containsKey(args[0].toLowerCase()))
                            send(p, "&cNão existe um Booster com esse nome");

                        try {
                            getBoosters().get(args[0].toLowerCase()).setPercentage(Integer.parseInt(args[2]));
                            send(p, "&aPorcentagem do &f&lBooster " + getBoosters().get(args[0].toLowerCase()).getDisplay() + "&a alterada com sucesso para &f" + args[2] + "%&e.");
                        } catch (SQLException e) {
                            send(p, "&cNão foi possível alterar a porcentagem do Booster.");
                            e.printStackTrace();
                        }

                        break;
                    default:
                        help();
                }

                break;
            case 4:
                if (args[0].equals("create"))
                    try {
                        if (addBooster(args[1].toLowerCase(), args[2], Integer.parseInt(args[3])))
                            send(p, "&f&lBooster " + args[2] + "&a criado com sucesso!");
                        else send(p, "&cO &f&lBooster " + args[1].toLowerCase() + "&c já existe.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        p.sendMessage(c("&cEscreve direito o seu poha."));
                    }

                else switch (args[1]) {
                    case "get":
                        if (Bukkit.getPlayer(args[3]) == null || !Bukkit.getPlayer(args[3]).isOnline())
                            send(p, "&cEste Player não existe ou está offline.");
                        else {
                            String bo = args[0].toLowerCase();
                            Player pp = Bukkit.getPlayer(args[3]);

                            if (!getBoosters().containsKey(bo))
                                send(p, "&cNão existe um Booster com esse nome");

                            giveBooster(pp, bo, Integer.parseInt(args[2]));

                            send(p, "&eVocê deu um &f&lBooster " + getBoosters().get(bo).getDisplay() + "&e ao &fplayer " + args[3] + "&e.");
                        }
                        break;
                    case "setdisplay":
                    case "setpercentage":
                        if (getBoosters().containsKey(args[0].toLowerCase()))
                            helpBooster();
                        else send(p, "&cNão existe um Booster com esse nome");
                        break;
                    default:
                        help();
                }
                break;
            default:
                help();
        }
        return true;
    }

    private void help() {
        p.sendMessage(header);
        p.sendMessage(c("&f/&enbooster &f<&ebooster&f> &7- Entra na edição de Booster."));
        p.sendMessage(c("&f/&enbooster create &f<&enome&f> <&edisplay&f> <&e%&f> &7- Cria um novo Booster."));
        p.sendMessage(c("&f/&enbooster list &7- Lista os Boosters existentes."));
        p.sendMessage(c("&f/&enbooster remove &f<&ebooster&f> &7- Exclui um Booster."));
        p.sendMessage("");
    }

    private void helpBooster() {
        p.sendMessage(header);
        p.sendMessage(c("&eUtilize: &f/&enbooster &f<&ebooster&f> &a+"));
        p.sendMessage(c("&a+ &eget &f<&etempo em segundos&f> (&eplayer&f) &7- Pegue ou dê o Booster."));
        p.sendMessage(c("&a+ &esetdisplay &f<&enovo display&f> &7- Altera o display do Booster."));
        p.sendMessage(c("&a+ &esetpercentage &f<&enova %&f> &7- Altera a porcentagem do Booster."));
        p.sendMessage("");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!started)
            return null;

        return null;
    }
}
