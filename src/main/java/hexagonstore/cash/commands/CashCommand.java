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

        if (a[0].equalsIgnoreCase("help")) {
            showHelp(s);
        }else if (a[0].equalsIgnoreCase("set")) {
            if (s.hasPermission("cash.admin")) {
                if (a.length < 3) {
                    s.sendMessage("§cUtilize: /cash set <jogador> <quantia>.");
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
                    s.sendMessage("§aVocê setou o saldo de: §f"+ NumberFormatter.formatNumber(amount) + " §apara o jogador: §f" + nick);
                } catch (Exception e) {
                    s.sendMessage("§cDigite um número válido.");
                }
            } else {
                s.sendMessage("§cVocê não tem permissão suficiente.");
            }
        } else if (a[0].equalsIgnoreCase("add")) {
            if (s.hasPermission("cash.admin")) {
                if (a.length < 3) {
                    s.sendMessage("§cUtilize: /cash add <jogador> <quantia>.");
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
                    s.sendMessage("§aVocê adicionou ao saldo de: §f" + nick + " §aa quantia de: §f"+ NumberFormatter.formatNumber(amount));
                } catch (Exception e) {
                    s.sendMessage("§cDigite um número válido.");
                }
            } else {
                s.sendMessage("§cVocê não tem permissão suficiente.");
            }
        } else if (a[0].equalsIgnoreCase("remove")) {
            if (s.hasPermission("cash.admin")) {
                if (a.length < 3) {
                    s.sendMessage("§cUtilize: /cash remove <jogador> <quantia>.");
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
                    s.sendMessage("§aVocê removeu do saldo de: §f" + nick + " §aa quantia de: §f"+ NumberFormatter.formatNumber(amount));
                } catch (Exception e) {
                    s.sendMessage("§cDigite um número válido.");
                }
            } else {
                s.sendMessage("§cVocê não tem permissão suficiente!");
            }
        } else if (a[0].equalsIgnoreCase("ver")) {
            if (s.hasPermission("cash.admin")) {
                if (a.length < 2) {
                    s.sendMessage("§cUtilize: /cash ver <jogador>.");
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
            s.sendMessage("§a/cash set §8- §7Sete o saldo de cash de algum jogador.");
            s.sendMessage("§a/cash remove §8- §7Remova uma quantia de cash do saldo de algum jogador.");
            s.sendMessage("§a/cash add §8- §7Adicione uma quantia de cash no saldo de algum jogador.");
            s.sendMessage("§a/cash ver §8- §7Veja o saldo de cash de algum jogador.");
        }
    }
}
