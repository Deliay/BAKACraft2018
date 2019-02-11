package com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper;

import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;

public class Potions {
    public static PotionEffect DivineWrathPotion = PotionEffect.builder()
            .potionType(PotionEffectTypes.SLOWNESS)
            .amplifier(1)
            .duration(Short.MAX_VALUE)
            .build();
}
