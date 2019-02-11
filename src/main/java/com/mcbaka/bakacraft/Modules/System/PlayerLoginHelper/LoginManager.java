package com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Login Manager
 * A player login status manager.
 *
 * This is a singleton instance, when it complete instant see:
 * @see com.mcbaka.bakacraft.Modules.System.PlayerLogin
 */
public class LoginManager {

    private Set<String> pendingLogin;
    private HashMap<String, LocalDateTime> enterTime;
    private HashMap<String, Location<World>> lastLoginPosition;

    private LoginManager() {
        pendingLogin = new HashSet<>();
        enterTime = new HashMap<>();
        lastLoginPosition = new HashMap<>();
    }

    private static LoginManager ourInstance;
    public static LoginManager GetInstance() {
        return ourInstance;
    }
    public static void CreateInstance() {
        ourInstance = new LoginManager();
    }

    /**
     * Store player position into LoginManager
     * @param player Sponge Player object
     */
    public static void StorePlayerPosition(Player player) {
        GetInstance().lastLoginPosition.put(player.getName(), player.getLocation());
        player.setLocationSafely(player.getWorld().getSpawnLocation());
    }

    /**
     * Restore player position from store
     * @param player Sponge Player object
     */
    public static void RestorePlayerPosition(Player player) {
        Location<World> location = GetInstance().lastLoginPosition.get(player.getName());
        if (location != null) {
            player.setLocation(location);
        }
    }

    /**
     * Get a set of player who not login yet
     * @return
     */
    public static Set<String> GetPendingLoginSet() {
        return GetInstance().pendingLogin;
    }

    /**
     * Get a hash map for each player enter time
     * @return A HashMap of player
     */
    public static HashMap<String, LocalDateTime> GetPlayerEnterTime() {
        return GetInstance().enterTime;
    }

    /**
     * Get a player is pending to login via player instance
     * @param player player instance
     * @return {true} if player is pending to login
     */
    public static boolean PlayerIsPendingLogin(Player player) {
        return GetPendingLoginSet().contains(player.getName().toLowerCase());
    }

    /**
     * Get a player is pending to login via player name
     * @param name player name
     * @return {true} if player is pending to login
     */
    public static boolean PlayerIsPendingLogin(String name) {
        return GetPendingLoginSet().contains(name);
    }

    /**
     * Set a player to pending login status
     * @param player player instance
     * @return {true} if Set player status set success
     */
    public static boolean SetPlayerToPendingLogin(Player player) {
        return SetPlayerToPendingLogin(player.getName().toLowerCase());
    }

    /**
     * Set a player to pending login status
     * @param name player name
     * @return {true} if Set player status set success
     */
    public static boolean SetPlayerToPendingLogin(String name) {
        GetPlayerEnterTime().put(name, LocalDateTime.now());
        return GetPendingLoginSet().add(name);
    }

    /**
     * Remove a player from pending login status
     * @param name player name
     * @return {true} if Remove player success
     */
    public static boolean RemovePlayerFromPendingLogin(String name) {
        return GetPendingLoginSet().remove(name);
    }

    /**
     * Get a player when connect to server
     * @param name
     * @return
     */
    public static LocalDateTime GetPlayerJoinTime(String name) {
        return GetPlayerEnterTime().get(name);
    }

}
