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
    /**
     * Search player name from current online player list
     * @param name player name
     * @return {@link Optional<Player>}
     */
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

    /**
     * Subscribe Sponge Account and Currency
     * @param player player instance
     * @param consumer Action
     */
    public static void SubscribeUserEconomy(Player player, DoubleAction<Currency, UniqueAccount> consumer) {
        SubscribeEconomy(economyService -> {
            Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(player.getUniqueId());
            uOpt.ifPresent(acc -> consumer.apply(economyService.getDefaultCurrency(), acc));
        });
    }

    public static <T> T SubscribeUserEconomy(Player player, DoubleFunction<Currency, UniqueAccount, T> consumer, T defaultValue) {
        return SubscribeEconomy(economyService -> {
            Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(player.getUniqueId());
            if (uOpt.isPresent()) {
                return consumer.apply(economyService.getDefaultCurrency(), uOpt.get());
            }
            return defaultValue;
        }, defaultValue);
    }

    public static <T> T SubscribeEconomy(Function<EconomyService, T> function, T defaultValue) {
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (!serviceOpt.isPresent()) {
            return defaultValue;
        }
        return function.apply(serviceOpt.get());
    }
    public static void SubscribeEconomy(Consumer<EconomyService> action) {
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (!serviceOpt.isPresent()) {
            return;
        }
        action.accept(serviceOpt.get());
    }
}
