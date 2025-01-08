package de.seperinous.merger.utility;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@UtilityClass
public class Tasks {
    @Setter
    private static JavaPlugin javaPlugin;

    public @NotNull BukkitTask after(int delay, boolean async, @NotNull Consumer<BukkitTask> handler) {
        val runnable = new BukkitRunnable() {
            @Override
            public void run() {
                handler.accept(task);
            }
        };

        return async
                ? runnable.runTaskLaterAsynchronously(javaPlugin, delay)
                : runnable.runTaskLater(javaPlugin, delay);
    }

    public @NotNull BukkitTask after(int delay, @NotNull Consumer<BukkitTask> handler) {
        return after(delay, false, handler);
    }

    public @NotNull BukkitTask after(@NotNull Consumer<BukkitTask> handler) {
        return after(1, handler);
    }

    public @NotNull BukkitTask afterAsync(int delay, @NotNull Consumer<BukkitTask> handler) {
        return after(delay, true, handler);
    }

    public @NotNull BukkitTask afterAsync(@NotNull Consumer<BukkitTask> handler) {
        return afterAsync(1, handler);
    }

    public @NotNull BukkitTask every(int delay, int period, boolean async, @NotNull Consumer<BukkitTask> handler) {
        val runnable = new BukkitRunnable() {
            @Override
            public void run() {
                handler.accept(task);
            }
        };

        return async
                ? runnable.runTaskTimerAsynchronously(javaPlugin, delay, period)
                : runnable.runTaskTimer(javaPlugin, delay, period);
    }

    public @NotNull BukkitTask every(int delay, int period, @NotNull Consumer<BukkitTask> handler) {
        return every(delay, period, false, handler);
    }

    public @NotNull BukkitTask every(int delayAndPeriod, @NotNull Consumer<BukkitTask> handler) {
        return every(delayAndPeriod, delayAndPeriod, false, handler);
    }

    public @NotNull BukkitTask everyAsync(int delay, int period, @NotNull Consumer<BukkitTask> handler) {
        return every(delay, period, true, handler);
    }

    public @NotNull BukkitTask everyAsync(int delayAndPeriod, @NotNull Consumer<BukkitTask> handler) {
        return everyAsync(delayAndPeriod, delayAndPeriod, handler);
    }

    public @NotNull BukkitTask repeat(int times, int delay, int period, @NotNull Consumer<BukkitTask> handler) {
        return new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= times) {
                    cancel();
                    return;
                }

                handler.accept(task);
                count++;
            }
        }.runTaskTimer(javaPlugin, delay, period);
    }
}
