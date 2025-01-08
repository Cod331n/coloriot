package de.seperinous.merger.particle;

import de.seperinous.merger.utility.Packets;
import de.seperinous.merger.utility.Tasks;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ParticleResponse {
    List<Packet<?>> packets;

    public @NotNull BukkitTask once(
            @NotNull Predicate<Player> playerFilter,
            int delay
    ) {
        Collection<Player> players = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (playerFilter.test(player)) {
                players.add(player);
            }
        });

        return Tasks.after(delay, task -> Packets.sendBulk(packets, players));
    }

    public @NotNull BukkitTask once(
            @NotNull Collection<Player> players,
            int delay
    ) {
        return Tasks.after(delay, task -> Packets.sendBulk(packets, players));
    }

    public @NotNull BukkitTask once(
            @NotNull Player player,
            int delay
    ) {
        return Tasks.after(delay, task -> Packets.sendBulk(packets, player));
    }

    public @NotNull BukkitTask repeat(
            @NotNull Predicate<Player> playerFilter,
            int times,
            int delay,
            int period
    ) {
        Collection<Player> players = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (playerFilter.test(player)) {
                players.add(player);
            }
        });

        return Tasks.repeat(times, delay, period, task -> Packets.sendBulk(packets, players));
    }

    public @NotNull BukkitTask repeat(
            @NotNull Collection<Player> players,
            int times,
            int delay,
            int period
    ) {
        return Tasks.repeat(times, delay, period, task -> Packets.sendBulk(packets, players));
    }

    public @NotNull BukkitTask repeat(
            @NotNull Player player,
            int times,
            int delay,
            int period
    ) {
        return Tasks.repeat(times, delay, period, task -> Packets.sendBulk(packets, player));
    }

    public @NotNull BukkitTask schedule(
            @NotNull Predicate<Player> playerFilter,
            int delay,
            int period
    ) {
        Collection<Player> players = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (playerFilter.test(player)) {
                players.add(player);
            }
        });

        return Tasks.every(delay, period, task -> Packets.sendBulk(packets, players));
    }

    public @NotNull BukkitTask schedule(
            @NotNull Collection<Player> players,
            int delay,
            int period
    ) {
        return Tasks.every(delay, period, task -> Packets.sendBulk(packets, players));
    }

    public @NotNull BukkitTask schedule(
            @NotNull Player player,
            int delay,
            int period
    ) {
        return Tasks.every(delay, period, task -> Packets.sendBulk(packets, player));
    }
}
