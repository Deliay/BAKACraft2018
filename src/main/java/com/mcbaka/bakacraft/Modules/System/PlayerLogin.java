package com.mcbaka.bakacraft.Modules.System;

import com.mcbaka.bakacraft.Main;
import com.mcbaka.bakacraft.Modules.AbstractModule;
import com.mcbaka.bakacraft.Modules.Util.MySQLHelper.SQLs;
import com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper.Commands;
import com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper.EventHandlers;
import com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper.LoginManager;
import com.mcbaka.bakacraft.Modules.Util.MySQL;
import com.mcbaka.bakacraft.Text.LoginText;
import com.mcbaka.bakacraft.Util.GameServerHelper;
import com.mcbaka.bakacraft.Util.IEventHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.scheduler.Task;
import java.util.*;

import com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper.ScheduleTask;

/**
 * Player login modules
 *
 * - Switch un-login player to spectator mode
 * - Restrict player move/sendMessage/sendCommand
 * - Require login in 30s (A schedule task to kick un-login player)
 * - When login success, switch back to survival mode and teleport to spawn
 * */
public class PlayerLogin extends AbstractModule implements IEventHandler {

    private Task scheduleKickPlayer = null;
    private CommandMapping commandLogin = null;
    public static boolean LoginServiceEnable = false;

    @Override
    public void Load() {
        LoginServiceEnable = true;
        LoginManager.CreateInstance();
        AbstractModule.RegisterListener(new EventHandlers());
        commandLogin = Sponge.getCommandManager().register(Main.GetInstance(), Commands.LoginCommand, LoginText.COMMAND_LOGIN.toPlain()).orElse(null);
        scheduleKickPlayer = ScheduleTask.LoopingKickPlayer.submit(Main.GetInstance());

        List<Integer> result = ModuleOf(MySQL.class)
        .Query(SQLs.GetUserCount)
        .QueryResult(rs -> rs.getInt(1));

        if (result.size() > 0) {
            logger.info(String.format("Loaded %d player(s) in database.", result.get(0)));
        } else {
            logger.info("No player loaded from database");
        }
    }

    @Override
    public void Unload() {
        LoginServiceEnable = false;
        scheduleKickPlayer.cancel();
        Optional.of(commandLogin).ifPresent(x -> Sponge.getCommandManager().removeMapping(x));
        if (LoginManager.GetPendingLoginSet().size() > 0) {
            LoginManager.GetPendingLoginSet().forEach(x -> GameServerHelper.FindPlayerOnlineByName(x).ifPresent(y -> y.kick(LoginText.LOGIN_SERVICE_SHUTDOWN)));
        }
    }

}
