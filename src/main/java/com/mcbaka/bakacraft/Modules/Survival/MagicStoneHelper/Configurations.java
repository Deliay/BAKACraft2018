package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import com.mcbaka.bakacraft.Modules.Survival.MagicStone;
import com.mcbaka.bakacraft.ModulesLoader;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.TypeTokens;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class Configurations {
    private static Configurations Instance;
    public static Configurations getInstance() {
        return Instance;
    }
    public static void CreateInstance(ConfigurationNode initial){
        Instance = new Configurations(initial);
    }
    private ConfigurationNode node;
    private Configurations(ConfigurationNode initial) {
        this.node = initial;
    }

    public void Save() {
        ModulesLoader.GetModule(MagicStone.class).target().SaveCofiguration();
    }

    public Optional<Location<World>> getPlayerFlagPosition(String playerName) {
        return getPlayerT("FlagPosition", playerName, PersistentKeys.TELEPORT_POS_TOKEN);
    }

    public void setPlayerFlagPosition(Player player, Location<World> location) {
        setPlayerT("FlagPosition", player.getName(), PersistentKeys.TELEPORT_POS_TOKEN, location);
    }

    public Optional<Location<World>> getPlayerHome(String playerName) {
        return getPlayerT("PlayerHome", playerName, PersistentKeys.TELEPORT_POS_TOKEN);
    }

    public void setPlayerHome(String player, Location<World>     position) {
        setPlayerT("PlayerHome", player, PersistentKeys.TELEPORT_POS_TOKEN, position);
    }

    public <T> void setPlayerT(String path, String playerName, TypeToken<T> typeToken, T value) {
        ConfigurationNode playerNode = node.getNode(path, playerName);
        try {
            playerNode.setValue(typeToken, value);
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    public <T> Optional<T> getPlayerT(String path, String playerName, TypeToken<T> typeToken) {
        ConfigurationNode playerNode = node.getNode(path, playerName);
        try {
            return Optional.of(playerNode.getValue(typeToken));
        } catch (ObjectMappingException e) {
            return Optional.empty();
        }
    }
}
