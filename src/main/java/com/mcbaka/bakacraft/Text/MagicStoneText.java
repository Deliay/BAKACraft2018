package com.mcbaka.bakacraft.Text;

import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast.Teleport;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast.TeleportHelper;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast.TeleportType;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.TeleportDelayService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
import java.util.UUID;

public class MagicStoneText {
    public final static Text OwnerPrefix = Text.of(TextColors.GRAY, "拥有者:", TextColors.GOLD);
    public final static Text CurrentLevelPrefix = Text.of(TextColors.GRAY, "目前等级:", TextColors.GOLD);
    public static String MagicStoneLvnRecipeId(int n) {
        return "magic-stone-recipe-lv." + n;
    }
    public static String TeleportRecipeId(int n, TeleportType type) {
        return "magic-teleport-stone-recipe-lv." + n + type.toString();
    }
    public final static Text CastTypePrefix = Text.of(TextColors.GRAY, "咒语:", TextColors.WHITE);
    public final static Text CastCountPrefix = Text.of(TextColors.GRAY, "使用次数:", TextColors.WHITE);
    public final static Text CastFriendlyNamePrefix = Text.of(TextColors.GRAY, "魔法类型:", TextColors.AQUA);
    public final static Text CastNoMana = Text.of(TextColors.RED, "你的魔法卷轴魔力已经用尽，请充能后再使用");
    public final static Text CastNotSetHome = Text.of(TextColors.RED, "你还没有设置传送点，请先用居住信标进行定位");
    public final static Text CastRecordTeleportPosition = Text.of(TextColors.BLUE, "已经记录相关传送位置");
    public final static Text CastAlreadyTeleport = Text.of(TextColors.BLUE, "已经传送到相关位置");
    public static Text CastTeleportPlayerList(UUID stoneUUID, int delay) {
        Collection<Player> players = Sponge.getServer().getOnlinePlayers();
        Text.Builder builder = Text.builder()
            .append(Text.of("请点击想TP玩家的名称: "));
        for (Player player : players) {
            builder.append(Text.builder(player.getName())
                    .onClick(TextActions.executeCallback(sendPlayer -> {
                        if (sendPlayer instanceof Player) {
                            Player fromPlayer = (Player) sendPlayer;
                            TeleportHelper.TeleportPlayerCallback(stoneUUID, player, fromPlayer, delay);
                        }
                    }))
                    .build()
            );
        }
        return builder.build();
    }
    public final static Text PlayerAcceptTeleport = Text.of(TextColors.GREEN, "玩家接受了你的传送请求");
    public final static Text PlayerDenyTeleport = Text.of(TextColors.RED, "玩家拒绝了你的传送请求");
    public static Text CastTeleportToPlayer(UUID stoneUUID, Player fromPlayer, Player toPlayer, int delay) {
        return Text.builder()
                .append(Text.of(TextColors.GRAY, "玩家:"))
                .append(Text.of(TextColors.WHITE, fromPlayer.getName()))
                .append(Text.of(TextColors.GRAY, "想传送到你的身边。请点击选择你要执行的操作"))
                .append(Text.builder("同意")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.executeCallback(sendPlayer -> {
                            TeleportDelayService.TeleportPlayer(fromPlayer, delay, toPlayer.getLocation());
                            TeleportHelper.TeleportPlayerClean(stoneUUID);
                            fromPlayer.sendMessage(PlayerAcceptTeleport);
                        }))
                        .build()
                ).append(Text.builder("拒绝")
                        .color(TextColors.BLUE)
                        .onClick(TextActions.executeCallback(sendPlayer -> {
                            TeleportHelper.TeleportPlayerClean(stoneUUID);
                            fromPlayer.sendMessage(PlayerDenyTeleport);
                        }))
                        .build()
                ).build();
    }
    public final static Text CastTeleportPending = Text.of(TextColors.RED, "这个传送卷轴在冷却中。");
    public final static Text CastFoodLevelNE = Text.of(TextColors.RED, "你太饿了，不能使用这个魔法");
}
