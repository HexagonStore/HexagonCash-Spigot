package hexagonstore.cash.papi;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import hexagonstore.cash.CashSpigot;
public class CashExpansion extends PlaceholderExpansion {
    
    private final String VERSION = "1.0";

    /**
     * Defines the name of the expansion that is also used in the
     * placeholder itself.
     * 
     * @return {@code hexagoncash} as String
     */
    @Override
    public String getIdentifier() {
        return "hexagoncash";
    }

    /**
     * The author of the expansion.
     * 
     * @return {@code Tiago Dinis} as String
     */
    @Override
    public String getAuthor() {
        return "Tiago Dinis";
    }

    /**
     * Returns the version of the expansion as String.
     *
     * @return The VERSION String
     */
    @Override
    public String getVersion() {
        return VERSION;
    }


    /**
     * Used to check if the expansion is able to register.
     * 
     * @return true or false depending on if the required plugin is installed
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * This method is called when a placeholder is used and maches the set
     * {@link #getIdentifier() identifier}
     *
     * @param  offlinePlayer
     *         The player to parse placeholders for
     * @param  params
     *         The part after the identifier ({@code %identifier_params%})
     *
     * @return Possible-null String
     */
    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        
        Player player = offlinePlayer.getPlayer();
        return String.valueOf(CashSpigot.getPlugin().cashAPI.getCash(offlinePlayer.getName()));

    }

}