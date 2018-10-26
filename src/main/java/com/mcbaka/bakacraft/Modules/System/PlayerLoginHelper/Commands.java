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
    .description(Text.of("LoginText to server"))
    .arguments(GenericArguments.string(LoginText.COMMAND_ARG_PASSWORD))
    .executor((src, args) -> {
        if (src instanceof Player) {
            Player player = (Player) src;
            String playerName = player.getName().toLowerCase();
            if (LoginManager.PlayerIsPendingLogin(playerName)) {
                Optional<String> password = args.getOne(LoginText.COMMAND_ARG_PASSWORD);
                if(password.isPresent()) {
                    if (AuthenticationUtil.TryLogin(playerName, password.get())) {
                        src.sendMessage(LoginText.LOGIN_SUCCESS);
                        //登录成功，则将标识删掉
                        LoginManager.RemovePlayerFromPendingLogin(playerName);
                        player.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
                        LoginManager.RestorePlayerPosition(player);
                    } else {
                        src.sendMessage(LoginText.LOGIN_AUTH_FAIL);
                    }
                } else {
                    src.sendMessage(LoginText.REQUIRE_PASSWORD);
                }
            } else {
                src.sendMessage(LoginText.ALREADY_LOGIN);
            }
        } else {
            src.sendMessage(Text.of("Must execute in client side"));
        }
        return CommandResult.success();
    }).build();
}
