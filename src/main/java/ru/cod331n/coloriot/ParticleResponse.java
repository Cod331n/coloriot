package ru.cod331n.coloriot;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import ru.cod331n.coloriot.util.Packets;
import ru.cod331n.coloriot.util.Tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ParticleResponse {

    List<Packet<?>> packets;

    /**
     * Отправляет пакеты с частицами после указанной задержки только игрокам,
     * которые удовлетворяют фильтру единожды.
     *
     * @param playerFilter фильтр для игроков, которым будут отправлены частицы
     * @param delay        задержка перед отправкой (в тиках)
     * @return BukkitTask
     */
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

    /**
     * Отправляет пакеты с частицами после указанной задержки указанной коллекции игроков единожды.
     *
     * @param players коллекция игроков
     * @param delay   задержка перед отправкой (в тиках)
     * @return BukkitTask
     */
    public @NotNull BukkitTask once(
            @NotNull Collection<Player> players,
            int delay
    ) {
        return Tasks.after(delay, task -> Packets.sendBulk(packets, players));
    }

    /**
     * Отправляет пакеты с частицами после указанной задержки указанному игроку единожды.
     *
     * @param player игрок, которому будут отправлены частицы
     * @param delay  задержка перед отправкой (в тиках)
     * @return BukkitTask
     */
    public @NotNull BukkitTask once(
            @NotNull Player player,
            int delay
    ) {
        return Tasks.after(delay, task -> Packets.sendBulk(packets, player));
    }

    /**
     * Отправляет пакеты с частицами повторно указанное количество раз игрокам,
     * которые удовлетворяют фильтру, с заданной задержкой и периодом.
     *
     * @param playerFilter фильтр для игроков, которым будут отправлены частицы
     * @param times        количество повторений
     * @param delay        задержка перед первым отправлением (в тиках)
     * @param period       интервал между повторениями (в тиках)
     * @return BukkitTask
     */
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

    /**
     * Отправляет пакеты с частицами повторно указанное количество раз указанной
     * коллекции игроков с заданной задержкой и периодом.
     *
     * @param players коллекция игроков
     * @param times   количество повторений
     * @param delay   задержка перед первым отправлением (в тиках)
     * @param period  интервал между повторениями (в тиках)
     * @return BukkitTask
     */
    public @NotNull BukkitTask repeat(
            @NotNull Collection<Player> players,
            int times,
            int delay,
            int period
    ) {
        return Tasks.repeat(times, delay, period, task -> Packets.sendBulk(packets, players));
    }

    /**
     * Отправляет пакеты с частицами повторно указанное количество раз указанному игроку
     * с заданной задержкой и периодом.
     *
     * @param player игрок
     * @param times  количество повторений
     * @param delay  задержка перед первым отправлением (в тиках)
     * @param period интервал между повторениями (в тиках)
     * @return BukkitTask
     */
    public @NotNull BukkitTask repeat(
            @NotNull Player player,
            int times,
            int delay,
            int period
    ) {
        return Tasks.repeat(times, delay, period, task -> Packets.sendBulk(packets, player));
    }

    /**
     * Запускает задачу с бесконечным повторением отправки пакетов с частицами игрокам,
     * которые удовлетворяют фильтру, с заданной задержкой и периодом.
     *
     * @param playerFilter фильтр для игроков
     * @param delay        задержка перед первым отправлением (в тиках)
     * @param period       интервал между повторениями (в тиках)
     * @return BukkitTask
     */
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

    /**
     * Запускает задачу с бесконечным повторением отправки пакетов с частицами указанной
     * коллекции игроков с заданной задержкой и периодом.
     *
     * @param players коллекция игроков
     * @param delay   задержка перед первым отправлением (в тиках)
     * @param period  интервал между повторениями (в тиках)
     * @return BukkitTask
     */
    public @NotNull BukkitTask schedule(
            @NotNull Collection<Player> players,
            int delay,
            int period
    ) {
        return Tasks.every(delay, period, task -> Packets.sendBulk(packets, players));
    }

    /**
     * Запускает задачу с бесконечным повторением отправки пакетов с частицами указанному
     * игроку с заданной задержкой и периодом.
     *
     * @param player игрок
     * @param delay  задержка перед первым отправлением (в тиках)
     * @param period интервал между повторениями (в тиках)
     * @return BukkitTask
     */
    public @NotNull BukkitTask schedule(
            @NotNull Player player,
            int delay,
            int period
    ) {
        return Tasks.every(delay, period, task -> Packets.sendBulk(packets, player));
    }
}
