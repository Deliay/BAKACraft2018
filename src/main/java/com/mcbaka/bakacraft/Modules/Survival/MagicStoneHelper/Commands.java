package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper;

import com.mcbaka.bakacraft.Main;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class Commands {
    public static final CommandSpec SetMainCity = CommandSpec.builder()
    .description(Text.of("Set mainly city position of current player"))
    .permission("minecraft.command.op")
    .executor((sender, arg) -> {
        if (sender instanceof Player) {
            Player player = (Player) sender;
        }
        return CommandResult.empty();
    })
    .build();
}
