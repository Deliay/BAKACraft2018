package com.mcbaka.bakacraft.Util;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Deprecated
public class MatchingLoreHelper {
    @Deprecated
    public static Optional<String> MatchingLoreValue(List<Text> loreList, Text key) {
        return MatchingLore(loreList, key, lore -> lore.toString().substring(key.toString().length()));
    }
    @Deprecated
    public static Optional<Text> MatchingLore(List<Text> loreList, Text match) {
        return MatchingLore(loreList, match, lore -> lore);
    }
    @Deprecated
    public static <T> Optional<T> MatchingLore(List<Text> loreList, Text match, Function<Text, T> onMatching) {
        for (Text lore : loreList) {
            String loreString = lore.toString();
            String prefixString = match.toString();
            if (loreString.startsWith(prefixString)) return Optional.of(onMatching.apply(lore));
        }
        return Optional.empty();
    }
}
