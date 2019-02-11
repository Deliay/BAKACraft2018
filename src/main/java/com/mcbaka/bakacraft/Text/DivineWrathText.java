package com.mcbaka.bakacraft.Text;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DivineWrathText {
    public final static Text COMMAND_UNRESTRICTED_NO_ENOUGH_AMOUNT = Text.builder("你的节操不足~ 无法解除本次虚弱状态").color(TextColors.RED).build();
    public final static Text COMMAND_UNRESTRICTED_COMMAND = Text.of("unrestricted");
    public final static Text COMMAND_UNRESTRICTED_COMMAND_ARG_PLAYER_NAME = Text.of("playerName");
    public final static Text COMMAND_UNRESTRICTED_COMMAND_EXEC_MISSING_ARG = Text.of("Must give a player name to unrestricted");
    public final static Text COMMAND_UNRESTRICTED_COMMAND_EXEC_PLAYER_OFFLINE = Text.of("Unrestricted player must be online");
}
