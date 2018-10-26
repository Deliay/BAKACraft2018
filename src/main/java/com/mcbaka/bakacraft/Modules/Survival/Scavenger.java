package com.mcbaka.bakacraft.Modules.Survival;

import com.mcbaka.bakacraft.Modules.AbstractModule;
import com.mcbaka.bakacraft.Text.ScavengerText;
import com.mcbaka.bakacraft.Util.IEventHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Inventory keeper
 */
public class Scavenger extends AbstractModule implements IEventHandler {
    @Override
    public void Load() {
    }

    @Override
    public void Unload() {

    }

    private void payMoneyWhenDead(Player player, Cause deadCause) {
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (!serviceOpt.isPresent()) {
            return;
        }
        EconomyService economyService = serviceOpt.get();
        Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(player.getUniqueId());
        if (uOpt.isPresent()) {
            UniqueAccount account = uOpt.get();

            TransactionResult result = account.withdraw(economyService.getDefaultCurrency(),
                    BigDecimal.valueOf(5), deadCause);

            if (result.getResult() == ResultType.SUCCESS) {
                player.sendMessage(ScavengerText.PAY_5_JIECAO_WHEN_DEAD);
            }
        }
    }

    @Listener(order = Order.PRE)
    public void onPlayerDead(DestructEntityEvent.Death event, @First Player player) {
        if (event.getTargetEntity() instanceof Player) {
            //此处有钱就扣，没钱就算了
            this.payMoneyWhenDead(player, event.getCause());
            event.setKeepInventory(true);
            player.sendMessage(ScavengerText.SCAVENGER_FOR_FREE);
        }
    }
}
