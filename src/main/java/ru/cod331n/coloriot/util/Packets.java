package de.seperinous.merger.utility;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@UtilityClass
public class Packets {

    public void sendPacket(@NotNull Packet<?> packet, @NotNull Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendPacket(@NotNull Packet<?> packet, @NotNull Collection<Player> players) {
        players.forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet));
    }

    public void sendBulk(@NotNull Collection<Packet<?>> packets, @NotNull Player player) {
        packets.forEach(packet -> sendPacket(packet, player));

    }

    public void sendBulk(@NotNull Collection<Packet<?>> packets, @NotNull Collection<Player> players) {
        players.forEach(player -> sendBulk(packets, player));
    }
}