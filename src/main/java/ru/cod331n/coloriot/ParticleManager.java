package ru.cod331n.coloriot;

import lombok.NoArgsConstructor;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Класс предоставляет небольшое количество готовых решений для создания форм из партиклов.
 * Если необходимы другие формы, можно комбинировать их.
 */
@NoArgsConstructor
public class ParticleManager {

    /**
     * Комбинирует несколько ParticleResponse в один.
     * Функция отправляет пакет с партиклами один раз для игрока, поэтому
     * лучше использовать комбинирование партиклов, если необходимо, через неё.
     *
     * @param other Массив ParticleResponse для комбинирования.
     * @return Новый объект ParticleResponse, содержащий все пакеты из переданных ParticleResponse.
     */
    public @NotNull ParticleResponse combine(@NotNull ParticleResponse... other) {
        List<Packet<?>> combinedPackets = new ArrayList<>();

        for (ParticleResponse response : other) {
            combinedPackets.addAll(response.packets);
        }

        return new ParticleResponse(combinedPackets);
    }

    /**
     * Создает луч частиц между двумя локациями.
     *
     * @param particle  Параметры для создания частиц.
     * @param from      Начальная точка луча.
     * @param to        Конечная точка луча.
     * @param particles Количество частиц.
     * @return Объект ParticleResponse, содержащий пакеты с частицами.
     */
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

    /**
     * Создает сферу частиц в случайных точках.
     *
     * @param particle  Параметры для создания частиц.
     * @param center    Центр сферы.
     * @param radius    Радиус сферы.
     * @param particles Количество частиц.
     * @param hollow    Флаг, указывающий на необходимость создания полой сферы.
     * @return Объект ParticleResponse, содержащий пакеты с частицами.
     */
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

    /**
     * Создает сферу частиц, равномерно распределенных по поверхности.
     *
     * @param particle  Параметры для создания частиц.
     * @param center    Центр сферы.
     * @param radius    Радиус сферы.
     * @param particles Количество частиц.
     * @param hollow    Флаг, указывающий на необходимость создания полой сферы.
     * @return Объект ParticleResponse, содержащий пакеты с частицами.
     */
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

    /**
     * Создает прямоугольник из частиц, равномерно распределённых по его границам.
     * Центр прямоугольника будет использоваться как начальная точка.
     *
     * @param particle  Параметры для создания частиц.
     * @param center    Центр прямоугольника.
     * @param width     Ширина прямоугольника.
     * @param height    Высота прямоугольника.
     * @param depth     Глубина прямоугольника.
     * @param particles Количество частиц.
     * @return Объект ParticleResponse, содержащий пакеты с частицами.
     */
    public @NotNull ParticleResponse rectangle(
            @NotNull ParticleRequest particle,
            @NotNull Location center,
            double width,
            double height,
            double depth,
            int particles
    ) {
        if (width <= 0 || height <= 0 || depth <= 0 || particles <= 0) {
            MinecraftServer.LOGGER.warn("Couldn't make particle rectangle");
            return new ParticleResponse(new ArrayList<>());
        }

        List<Packet<?>> packets = new LinkedList<>();

        int parts = (int) Math.cbrt(particles);

        for (int i = 0; i < parts; i++) {
            double x = center.getX() - (width / 2) + (i * width / parts);
            for (int j = 0; j < parts; j++) {
                double y = center.getY() - (height / 2) + (j * height / parts);
                for (int k = 0; k < parts; k++) {
                    double z = center.getZ() - (depth / 2) + (k * depth / parts);
                    packets.add(
                            particle.location(new Location(center.getWorld(), x, y, z))
                                    .toPacket()
                    );
                }
            }
        }

        return new ParticleResponse(packets);
    }
}
