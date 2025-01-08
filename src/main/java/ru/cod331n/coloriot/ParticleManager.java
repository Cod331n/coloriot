package de.seperinous.merger.particle;


import lombok.NoArgsConstructor;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
public class ParticleManager {

    public @NotNull ParticleResponse combine(@NotNull ParticleResponse... other) {
        List<Packet<?>> combinedPackets = new ArrayList<>();

        for (ParticleResponse response : other) {
            combinedPackets.addAll(response.packets);
        }

        return new ParticleResponse(combinedPackets);
    }

    public @NotNull ParticleResponse ray(
            @NotNull ParticleRequest particle,
            @NotNull Location from,
            @NotNull Location to,
            int particles
    ) {
        if (from.getWorld() != to.getWorld() || particles <= 0) {
            MinecraftServer.LOGGER.warn("Couldn't make particle ray");
            return new ParticleResponse(new ArrayList<>());
        }

        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();

        double stepX = dx / particles;
        double stepY = dy / particles;
        double stepZ = dz / particles;

        List<Packet<?>> packets = new LinkedList<>();

        for (int i = 0; i <= particles; i++) {
            double x = from.getX() + (stepX * i);
            double y = from.getY() + (stepY * i);
            double z = from.getZ() + (stepZ * i);

            packets.add(
                    particle.location(new Location(from.getWorld(), x, y, z))
                            .toPacket()
            );
        }

        return new ParticleResponse(packets);
    }

    public @NotNull ParticleResponse randomSphere(
            @NotNull ParticleRequest particle,
            @NotNull Location center,
            double radius,
            int particles,
            boolean hollow
    ) {
        if (radius <= 0 || particles <= 0) {
            MinecraftServer.LOGGER.warn("Couldn't make particle sphere");
            return new ParticleResponse(new ArrayList<>());
        }

        List<Packet<?>> packets = new LinkedList<>();
        Random random = new Random();

        for (int i = 0; i < particles; i++) {
            double phi = random.nextDouble() * 2 * Math.PI;
            double costheta = 2 * random.nextDouble() - 1;
            double u = random.nextDouble();

            double theta = Math.acos(costheta);
            double r = hollow ? radius : radius * Math.cbrt(u);

            double x = r * Math.sin(theta) * Math.cos(phi);
            double y = r * Math.sin(theta) * Math.sin(phi);
            double z = r * Math.cos(theta);

            packets.add(
                    particle.location(center.clone().add(x, y, z))
                            .toPacket()
            );
        }

        return new ParticleResponse(packets);
    }

    public @NotNull ParticleResponse intervalSphere(
            @NotNull ParticleRequest particle,
            @NotNull Location center,
            double radius,
            int particles,
            boolean hollow
    ) {
        if (radius <= 0 || particles <= 0) {
            MinecraftServer.LOGGER.warn("Couldn't make particle sphere");
            return new ParticleResponse(new ArrayList<>());
        }

        List<Packet<?>> packets = new LinkedList<>();

        double offset = 2.0 / particles;
        double increment = 2.4;

        Random random = new Random();

        for (int i = 0; i < particles; i++) {
            double y = i * offset - 1 + (offset / 2.0);
            double r = Math.sqrt(1 - y * y);
            double phi = i * increment;

            double x = Math.cos(phi) * r;
            double z = Math.sin(phi) * r;

            double finalRadius = hollow ? radius : radius * Math.cbrt(random.nextDouble());

            packets.add(
                    particle.location(center.clone().add(x * finalRadius, y * finalRadius, z * finalRadius))
                            .toPacket()
            );
        }

        return new ParticleResponse(packets);
    }
}
