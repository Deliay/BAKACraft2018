package com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper;

import com.mcbaka.bakacraft.Text.DivineWrathText;
import com.mcbaka.bakacraft.Util.GameServerHelper;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Optional;

public class Commands {
    public static Cause CauseByCommand = Cause.builder().build(EventContext.builder().build());

    public static CommandSpec UnrestrictedCommand = CommandSpec.builder()
    .description(Text.of("Un-restrict current player restrict status"))
    .arguments(GenericArguments.string(DivineWrathText.COMMAND_UNRESTRICTED_COMMAND_ARG_PLAYER_NAME))
    .executor((sender, args) -> {
        if (!(sender instanceof Player)) {
            Optional<String> unrestrictedPlayer = args.getOne(DivineWrathText.COMMAND_UNRESTRICTED_COMMAND_ARG_PLAYER_NAME);
            if (!unrestrictedPlayer.isPresent()) {
                sender.sendMessage(DivineWrathText.COMMAND_UNRESTRICTED_COMMAND_EXEC_MISSING_ARG);
                return CommandResult.empty();
            }

            Optional<Player> player = GameServerHelper.FindPlayerOnlineByName(unrestrictedPlayer.get());
            if (!player.isPresent()) {
                sender.sendMessage(DivineWrathText.COMMAND_UNRESTRICTED_COMMAND_EXEC_PLAYER_OFFLINE);
                return CommandResult.empty();
            }
        }
        Player player = (Player) sender;
        // subscribe economy and pay to un-restrict player
        boolean unrestrictedResult = GameServerHelper.SubscribeUserEconomy(player, (currency, account) -> {
            // try trigger transaction and get result
            TransactionResult result = account.withdraw(currency, BigDecimal.valueOf(200), CauseByCommand);
            if (result.getResult() == ResultType.SUCCESS) {
                // if transaction get {@link ResultType.SUCCESS} result
                PlayerDeadManager.RemovePlayerRestrict(player);
                return true;
            } else {
                player.sendMessage(DivineWrathText.COMMAND_UNRESTRICTED_NO_ENOUGH_AMOUNT);
                return false;
            }
        }, false);
        if (unrestrictedResult) {
            return CommandResult.success();
        } else {
            return CommandResult.empty();
        }
    }).build();
}
