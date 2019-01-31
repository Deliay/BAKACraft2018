package com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper;

import com.mcbaka.bakacraft.Text.LoginText;
import com.mcbaka.bakacraft.Util.GameServerHelper;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class LoopingKickPlayer {

    public static Task.Builder TaskBuilder = Task
    .builder()
    .interval(5, TimeUnit.SECONDS)
    .execute(() -> {
        for (String key : LoginManager.GetPendingLoginSet()) {
            LocalDateTime time = LoginManager.GetPlayerJoinTime(key);
            Optional<Player> optionalPlayer = GameServerHelper.FindPlayerOnlineByName(key);
            final boolean isOvertime = time.plusSeconds(30).isBefore(LocalDateTime.now());
            optionalPlayer.ifPresent(player -> {
                if (isOvertime) {
                    player.kick(LoginText.TOO_LONG_IDLE);
                }
            });
        }
    });
}
