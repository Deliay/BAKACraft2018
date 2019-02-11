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

import com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper.LoopingKickPlayer;

/**
 * Player login modules
 *
 * - Switch un-login player to spectator mode
 * - Restrict player move/sendMessage/sendCommand
 * - Require login in 30s (A schedule task to kick un-login player)
 * - When login success, switch back to survival mode and teleport to spawn
 * */
public class PlayerLogin extends AbstractModule implements IEventHandler {

    /**
     *  A schedule task for kick idle player
     */
    private Task scheduleKickPlayer = null;
    /**
     * Login command
     */
    private CommandMapping commandLogin = null;
    /**
     * A Flag to current login service usage
     */
    public static boolean LoginServiceEnable = false;

    @Override
    public void Load() {
        /**
         * Initial login manager
         */
        LoginManager.CreateInstance();
        /**
         * Register global event handler
         */
        AbstractModule.RegisterListener(new EventHandlers());
        /**
         * Register global command handler
         */
        commandLogin = Sponge.getCommandManager().register(
                Main.GetInstance(),
                Commands.LoginCommand,
                LoginText.COMMAND_LOGIN.toPlain()
        ).orElse(null);
        /**
         * Register schedule task
         */
        scheduleKickPlayer = RegisterTaskBuilder(LoopingKickPlayer.TaskBuilder);

        /**
         * Try query player count in database to check current mysql connection
         */
        List<Integer> result = ModuleOf(MySQL.class)
        .Query(SQLs.GetUserCount)
        .QueryResult(rs -> rs.getInt(1));

        /**
         * If no any exception throws, mean MySQL working correctly
         */
        if (result.size() > 0) {
            logger.info(String.format("Loaded %d player(s) in database.", result.get(0)));
        } else {
            logger.info("No player loaded from database");
        }
        /**
         * Set service available
         */
        LoginServiceEnable = true;
    }

    @Override
    public void Unload() {
        /**
         * Set service dis-available
         */
        LoginServiceEnable = false;
        /**
         * Cancel schedule task
         */
        scheduleKickPlayer.cancel();
        /**
         * Unregister command register
         */
        Optional.of(commandLogin).ifPresent(x -> Sponge.getCommandManager().removeMapping(x));
        /**
         * Kick all not login player
         */
        if (LoginManager.GetPendingLoginSet().size() > 0) {
            LoginManager.GetPendingLoginSet().forEach(x -> GameServerHelper.FindPlayerOnlineByName(x).ifPresent(y -> y.kick(LoginText.LOGIN_SERVICE_SHUTDOWN)));
        }
    }

}
