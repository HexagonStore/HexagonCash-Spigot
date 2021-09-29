package hexagonstore.cash.commands;

import hexagonstore.cash.CashPlugin;
import hexagonstore.cash.api.CashAPI;

import hexagonstore.cash.utils.NumberFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashCommand implements CommandExecutor {

    private CashAPI cashAPI = CashPlugin.getPlugin().cashAPI;

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lb, String[] a) {
        if (a.length == 0) {
            if (!(s instanceof Player)) {
                s.sendMessage("§cComando desativado via Console.");
                return false;
            }

            Player player = (Player) s;
            double cash = cashAPI.getCashAndCreateAccount(player.getName(), false);
            s.sendMessage("§aSeu saldo de cash: §f" + NumberFormatter.formatNumber(cash) + ".");
            return true;
        }

        if (a[0].equalsIgnoreCase("help") || a[0].equalsIgnoreCase("ajuda")) {
            showHelp(s);
        }else if (a[0].equalsIgnoreCase("set") || a[0].equalsIgnoreCase("setar")) {
            if (s.hasPermission("cash.admin")) {
                if (a.length < 3) {
                    s.sendMessage("§cUtilize: /cash set <player> <quantia>.");
                    return true;
                }

                String nick = a[1].toLowerCase();
                if (!cashAPI.accounts.containsKey(nick.toLowerCase())) {
                    s.sendMessage("§cConta inexistente.");
                    return true;
                }
                try {
                    double amount = NumberFormatter.parseString(a[2]);
                    if(amount < 1) {
                        s.sendMessage("§cDigite um número válido.");
                        return true;
                    }
                    cashAPI.setCash(nick, amount);
                    s.sendMessage("§aVocê setou o saldo de: §f" + nick + " §apara: §f" + NumberFormatter.formatNumber(amount));
                } catch (Exception e) {
                    s.sendMessage("§cDigite um número válido.");
                }
            } else {
                s.sendMessage("§cVocê não tem permissão suficiente.");
            }
        } else if (a[0].equalsIgnoreCase("add") || a[0].equalsIgnoreCase("give") || a[0].equalsIgnoreCase("adicionar")) {
            if (s.hasPermission("cash.admin")) {
                if (a.length < 3) {
                    s.sendMessage("§cUtilize: /cash add <player> <quantia>.");
                    return true;
                }

                String nick = a[1].toLowerCase();
                if (!cashAPI.accounts.containsKey(nick.toLowerCase())) {
                    s.sendMessage("§cConta inexistente.");
                    return true;
                }
                try {
                    double amount = NumberFormatter.parseString(a[2]);
                    if(amount < 1) {
                        s.sendMessage("§cDigite um número válido.");
                        return true;
                    }
                    cashAPI.setCash(nick, cashAPI.getCash(nick));
                    s.sendMessage("§aVocê adicionou ao saldo de: §f" + nick + " §aa quantia de: §f" + NumberFormatter.formatNumber(amount));
                } catch (Exception e) {
                    s.sendMessage("§cDigite um número válido.");
                }
            } else {
                s.sendMessage("§cVocê não tem permissão suficiente.");
            }
        } else if (a[0].equalsIgnoreCase("remove") || a[0].equalsIgnoreCase("remover") || a[0].equalsIgnoreCase("withdraw")) {
            if (s.hasPermission("cash.admin")) {
                if (a.length < 3) {
                    s.sendMessage("§cUtilize: /cash remove <player> <quantia>.");
                    return true;
                }

                String nick = a[1].toLowerCase();
                if (!cashAPI.accounts.containsKey(nick.toLowerCase())) {
                    s.sendMessage("§cConta inexistente.");
                    return true;
                }
                try {
                    double amount = NumberFormatter.parseString(a[2]);
                    if(amount < 1) {
                        s.sendMessage("§cDigite um número válido.");
                        return true;
                    }
                    cashAPI.setCash(nick, cashAPI.getCash(nick) - amount);
                    s.sendMessage("§aVocê removeu do saldo de: §f" + nick + " §aa quantia de: §f" + NumberFormatter.formatNumber(amount));
                } catch (Exception e) {
                    s.sendMessage("§cDigite um número válido.");
                }
            } else {
                s.sendMessage("§cVocê não tem permissão suficiente!");
            }
        } else if (a[0].equalsIgnoreCase("ver") || a[0].equalsIgnoreCase("view") || a[0].equalsIgnoreCase("vizualizar")) {
            if (s.hasPermission("cash.admin")) {
                if (a.length < 2) {
                    s.sendMessage("§cUtilize: /cash ver <player>.");
                    return true;
                }

                String nick = a[1].toLowerCase();
                if (!cashAPI.accounts.containsKey(nick.toLowerCase())) {
                    s.sendMessage("§cConta inexistente.");
                    return true;
                }
                s.sendMessage("§aO saldo de: §f" + nick + "§aé: §f" + cashAPI.getCash(nick) + ".");
            } else {
                s.sendMessage("§cVocê não tem permissão suficiente.");
            }
        }else {
            showHelp(s);
        }
        return true;
    }

    private void showHelp(CommandSender s) {
        s.sendMessage(" ");
        s.sendMessage("     §e§lAJUDA     ");
        s.sendMessage(" ");
        s.sendMessage(" §a/cash §8- §7Veja seu saldo de cash.");
        s.sendMessage(" §a/cash help §8- §7Veja essa mensagem.");
        if (s.hasPermission("cash.admin")) {
            s.sendMessage(" §a/cash set <player> <quantia> §8- §7Sete o saldo de cash de algum jogador.");
            s.sendMessage(" §a/cash remove <player> <quantia> §8- §7Remova uma quantia de cash do saldo de algum jogador.");
            s.sendMessage(" §a/cash add <player> <quantia> §8- §7Adicione uma quantia de cash no saldo de algum jogador.");
            s.sendMessage(" §a/cash ver <player> §8- §7Veja o saldo de cash de algum jogador.");
        }
        s.sendMessage(" ");
    }
}
