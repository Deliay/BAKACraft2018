package com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper;

import com.mcbaka.bakacraft.Text.DivineWrathText;
import com.mcbaka.bakacraft.Util.GameServerHelper;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

public class PlayerDeadTimeServices {

    public static HashMap<Player, LocalDateTime> PlayerDeadTime = new HashMap<>();

    public static boolean IsPlayerInRestrictTime(Player player) {
        return  (PlayerDeadTime.containsKey(player) && PlayerDeadTime.get(player).plusMinutes(5).isAfter(LocalDateTime.now()));
    }

    public static void UnrestrictPlayerViaEco(Player player, Cause playerInteract) {
        GameServerHelper.SubscribeUserEconomy(player, (currency, account) -> {
            TransactionResult result = account.withdraw(currency, BigDecimal.valueOf(200), playerInteract);
            if (result.getResult() == ResultType.SUCCESS) {
                //只有是SUCCESS的时候才算解除成功
                PlayerDeadManager.RemovePlayerRestrict(player);
            } else {
                player.sendMessage(DivineWrathText.COMMAND_UNRESTRICTED_NO_ENOUGH_AMOUNT);
            }
        });
    }
}
