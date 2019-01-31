package com.mcbaka.bakacraft.Util;

import com.mcbaka.bakacraft.Text.ScavengerText;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GameServerHelper {
    public static Optional<Player> FindPlayerOnlineByName(String name) {
        Collection<Player> players = Sponge.getServer().getOnlinePlayers();
        String lowerCaseName = name.toLowerCase();
        for (Player player : players) {
            if (player.getName().toLowerCase().equals(lowerCaseName)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public static boolean SubscribeUserEconomy(Player player, DoubleAction<Currency, UniqueAccount> consumer) {
        return SubscribeEconomy(economyService -> {
            Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(player.getUniqueId());
            uOpt.ifPresent(acc -> consumer.apply(economyService.getDefaultCurrency(), acc));
            return true;
        });
    }

    public static boolean SubscribeEconomy(Function<EconomyService, Boolean> function) {
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (!serviceOpt.isPresent()) {
            return false;
        }
        return function.apply(serviceOpt.get());
    }
}
