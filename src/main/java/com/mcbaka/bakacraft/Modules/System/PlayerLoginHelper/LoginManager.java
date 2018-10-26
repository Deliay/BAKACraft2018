package com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

    public static void StorePlayerPosition(Player player) {
        GetInstance().lastLoginPosition.put(player.getName(), player.getLocation());
        player.setLocationSafely(player.getWorld().getSpawnLocation());
    }

    public static void RestorePlayerPosition(Player player) {
        Location<World> location = GetInstance().lastLoginPosition.get(player.getName());
        if (location != null) {
            player.setLocation(location);
        }
    }

    public static Set<String> GetPendingLoginSet() {
        return GetInstance().pendingLogin;
    }

    public static HashMap<String, LocalDateTime> GetPlayerEnterTime() {
        return GetInstance().enterTime;
    }

    public static boolean PlayerIsPendingLogin(Player player) {
        return GetPendingLoginSet().contains(player.getName().toLowerCase());
    }

    public static boolean PlayerIsPendingLogin(String name) {
        return GetPendingLoginSet().contains(name);
    }

    public static boolean SetPlayerToPendingLogin(Player player) {
        return SetPlayerToPendingLogin(player.getName().toLowerCase());
    }

    public static boolean SetPlayerToPendingLogin(String name) {
        GetPlayerEnterTime().put(name, LocalDateTime.now());
        return GetPendingLoginSet().add(name);
    }
    public static boolean RemovePlayerFromPendingLogin(String name) {
        return GetPendingLoginSet().remove(name);
    }

    public static LocalDateTime GetPlayerJoinTime(String name) {
        return GetPlayerEnterTime().get(name);
    }

}
