package com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper;

import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class LoopRestrictPlayer {
    public static Task.Builder TaskBuilder = Task
    .builder().interval(5, TimeUnit.SECONDS)
    .execute(() -> {

    });
}
