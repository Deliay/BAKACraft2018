package com.mcbaka.bakacraft.Modules.Survival;

import com.mcbaka.bakacraft.Modules.AbstractModule;
import com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper.LoopRestrictPlayer;
import com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper.PlayerDeadManager;
import com.mcbaka.bakacraft.Util.IEventHandler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.scheduler.Task;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Divine Wrath of death
 * 1. 死亡复活只有一半的血量，饱食度也只有一半
 * 2. 死亡后追加虚弱状态，在饱食度填满之后10s恢复
 *    如果饱食度一直没有恢复，血量最多只能恢复到一半。
 *    多恢复的血量以伤害的形式减扣。
 * 3. 虚弱模式下，造成伤害减半，附加缓慢I
 * 4. 本状态5分钟自动恢复
 * 5. 200节操可立即解除本状态
 */
public class DivineWrath extends AbstractModule implements IEventHandler {

    private Task restrictTask = null;
    @Override
    public void Load() {
        restrictTask = RegisterTaskBuilder(LoopRestrictPlayer.TaskBuilder);
    }

    @Override
    public void Unload() {
        restrictTask.cancel();
    }

    @Listener
    public void onPlayerDead(DestructEntityEvent.Death event, @First Player player) {
        if (event.getTargetEntity() instanceof Player) {
            PlayerDeadManager.SetPlayerRestrict(player);
        }
    }
}
