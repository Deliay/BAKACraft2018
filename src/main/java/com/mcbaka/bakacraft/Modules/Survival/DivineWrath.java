package com.mcbaka.bakacraft.Modules.Survival;

import com.mcbaka.bakacraft.Main;
import com.mcbaka.bakacraft.Modules.AbstractModule;
import com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper.Commands;
import com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper.EventHandlers;
import com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper.LoopRestrictPlayer;
import com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper.PlayerDeadManager;
import com.mcbaka.bakacraft.Text.DivineWrathText;
import com.mcbaka.bakacraft.Util.IEventHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;

/**
 * Divine Wrath of death
 * [Y] 1. 死亡复活只有一半的血量，饱食度也只有一半
 * [Y] 2. 死亡后追加虚弱状态，持续5分钟。
 * [Y] 3. 虚弱模式下，造成伤害减半，附加缓慢I
 * [Y] 4. 本状态5分钟自动恢复
 * [Y] 5. 200节操可立即解除本状态
 */
public class DivineWrath extends AbstractModule implements IEventHandler {

    private Task restrictTask = null;
    private CommandMapping unrestrictedCommand = null;
    @Override
    public void Load() {
        restrictTask = RegisterTaskBuilder(LoopRestrictPlayer.TaskBuilder);
        AbstractModule.RegisterListener(new EventHandlers());
        unrestrictedCommand = Sponge.getCommandManager().register(
                Main.GetInstance(),
                Commands.UnrestrictedCommand,
                DivineWrathText.COMMAND_UNRESTRICTED_COMMAND.toPlain()
        ).orElse(null);
    }

    @Override
    public void Unload() {
        restrictTask.cancel();
        Optional.of(unrestrictedCommand).ifPresent(x -> Sponge.getCommandManager().removeMapping(x));
    }

    @Listener
    public void onPlayerDead(DestructEntityEvent.Death event, @First Player player) {
        if (event.getTargetEntity() instanceof Player) {
            PlayerDeadManager.SetPlayerRestrict(player);
        }
    }
}
