package com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper;

import com.mcbaka.bakacraft.Text.LoginText;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class Commands {
    public static CommandSpec LoginCommand = CommandSpec.builder()
    .description(LoginText.COMMAND_DESCRIPTION)
    .arguments(GenericArguments.string(LoginText.COMMAND_ARG_PASSWORD))
    .executor((sender, args) -> {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LoginText.COMMAND_MUST_RUN_BY_PLAYER);
            return CommandResult.empty();
        }
        Player player = (Player) sender;
        String playerName = player.getName().toLowerCase();
        if (!LoginManager.PlayerIsPendingLogin(playerName)) {
            sender.sendMessage(LoginText.ALREADY_LOGIN);
            return CommandResult.empty();
        }
        Optional<String> password = args.getOne(LoginText.COMMAND_ARG_PASSWORD);
        if(!password.isPresent()) {
            sender.sendMessage(LoginText.REQUIRE_PASSWORD);
            return CommandResult.empty();
        }
        if (!AuthenticationUtil.TryLogin(playerName, password.get())) {
            sender.sendMessage(LoginText.LOGIN_AUTH_FAIL);
            return CommandResult.empty();
        }
        sender.sendMessage(LoginText.LOGIN_SUCCESS);
        //登录成功，则将标识删掉
        LoginManager.RemovePlayerFromPendingLogin(playerName);
        player.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
        LoginManager.RestorePlayerPosition(player);
        return CommandResult.success();
    }).build();
}
