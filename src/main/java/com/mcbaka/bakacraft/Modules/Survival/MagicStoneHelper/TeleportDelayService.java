package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.mcbaka.bakacraft.Modules.Survival.MagicStone;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TeleportDelayService {
    public final static TeleportDelayService Instance = new TeleportDelayService();
    public final static HashMap<Player, Task> TaskList = new HashMap<>();
    public static void TeleportPlayer(Player player, long delay, Vector3d position, World worldUUID) {
        TeleportPlayer(player, delay, new Location<>(worldUUID, position));
    }
    public static void TeleportPlayer(Player player, long delay, Location<World> location) {
        TaskList.put(player, MagicStone.RegisterTaskBuilder(Task.builder()
                .delay(delay, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    player.setLocation(location);
                    TaskList.get(player).cancel();
                    TaskList.remove(player);
                })
        ));
    }
}
