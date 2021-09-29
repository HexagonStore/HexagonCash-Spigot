package hexagonstore.cash.commands;

import hexagonstore.cash.CashSpigot;
import hexagonstore.cash.api.CashAPI;

import hexagonstore.cash.utils.EC_Config;
import hexagonstore.cash.utils.NumberFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashCommand implements CommandExecutor {

    private CashAPI cashAPI = CashSpigot.getPlugin().cashAPI;
    private EC_Config config =  CashSpigot.getPlugin().config;
    
    private String get(String path) {
        return config.getString("Messages." + path).replace("&", "§");
    }
    
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lb, String[] a) {
        if (a.length == 0) {
            if (!(s instanceof Player)) {
                s.sendMessage(get("no_console"));
                return true;
            }

            Player player = (Player) s;
            double cash = cashAPI.getCashAndCreateAccount(player.getName(), false);
            s.sendMessage(get("cash").replace("{cash}", NumberFormatter.formatNumber(cash)));
            return true;
        }

        if (a[0].equalsIgnoreCase("help") || a[0].equalsIgnoreCase("ajuda")) {
            showHelp(s);
        } else if (a[0].equalsIgnoreCase("set") || a[0].equalsIgnoreCase("setar")) {
            if (s.hasPermission(config.getString("cmd permission.admin"))) {
                if (a.length < 3) {
                    s.sendMessage(get("no_args.set"));
                    return true;
                }

                String nick = a[1].toLowerCase();
                if (!cashAPI.accounts.containsKey(nick.toLowerCase())) {
                    s.sendMessage(get("no_exists"));
                    return true;
                }
                try {
                    double amount = NumberFormatter.parseString(a[2]);
                    if (amount < 1) {
                        s.sendMessage(get("invalid_number"));

                        return true;
                    }
                    cashAPI.setCash(nick, amount);
                    s.sendMessage(get("setted").replace("{cash}", NumberFormatter.formatNumber(amount)).replace("{player}", nick));
                } catch (Exception e) {
                    s.sendMessage(get("invalid_number"));
                }
            } else {
                s.sendMessage(get("no_permission"));
            }
        } else if (a[0].equalsIgnoreCase("add") || a[0].equalsIgnoreCase("give") || a[0].equalsIgnoreCase("adicionar")) {
            if (s.hasPermission(config.getString("cmd permission.admin"))) {
                if (a.length < 3) {
                    s.sendMessage(get("no_args.add"));
                    return true;
                }

                String nick = a[1].toLowerCase();
                if (!cashAPI.accounts.containsKey(nick.toLowerCase())) {
                    s.sendMessage(get("no_exists"));
                    return true;
                }
                try {
                    double amount = NumberFormatter.parseString(a[2]);
                    if (amount < 1) {
                        s.sendMessage(get("invalid_number"));
                        return true;
                    }
                    cashAPI.setCash(nick, cashAPI.getCash(nick));
                    s.sendMessage(get("added").replace("{cash}", NumberFormatter.formatNumber(amount)).replace("{player}", nick));
                } catch (Exception e) {
                    s.sendMessage(get("invalid_number"));
                }
            } else {
                s.sendMessage(get("no_permission"));
            }
        } else if (a[0].equalsIgnoreCase("remove") || a[0].equalsIgnoreCase("remover") || a[0].equalsIgnoreCase("withdraw")) {
            if (s.hasPermission(config.getString("cmd permission.admin"))) {
                if (a.length < 3) {
                    s.sendMessage(get("no_args.remove"));
                    return true;
                }

                String nick = a[1].toLowerCase();
                if (!cashAPI.accounts.containsKey(nick.toLowerCase())) {
                    s.sendMessage(get("no_exists"));
                    return true;
                }
                try {
                    double amount = NumberFormatter.parseString(a[2]);
                    if (amount < 1) {
                        s.sendMessage(get("invalid_number"));
                        return true;
                    }
                    cashAPI.setCash(nick, cashAPI.getCash(nick) - amount);
                    s.sendMessage(get("removed").replace("{cash}", NumberFormatter.formatNumber(amount)).replace("{player}", nick));
                } catch (Exception e) {
                    s.sendMessage(get("invalid_number"));
                }
            } else {
                s.sendMessage(get("no_permission"));
            }
        } else if (a[0].equalsIgnoreCase("ver") || a[0].equalsIgnoreCase("view") || a[0].equalsIgnoreCase("vizualizar")) {
            if (a.length < 2) {
                s.sendMessage(get("no_args.ver"));
                return true;
            }

            String nick = a[1].toLowerCase();
            if (!cashAPI.accounts.containsKey(nick.toLowerCase())) {
                s.sendMessage(get("no_exists"));
                return true;
            }
            s.sendMessage(get("cash_target").replace("{cash}", NumberFormatter.formatNumber(cashAPI.getCash(nick))).replace("{player}", nick));
        } else {
            showHelp(s);
        }
        return true;
    }

    private void showHelp(CommandSender s) {
        String helpType = "normal";
        if(s.hasPermission(config.getString("cmd permission.admin"))) {
            helpType = "admin";
        }else if(!s.hasPermission(config.getString("cmd permission.normal"))) {
            s.sendMessage(get("no_permission"));
            return;
        }
        config.getStringList("Messages.help." + helpType).forEach(line -> {
            s.sendMessage(line.replace("&", "§"));
        });
    }
}
