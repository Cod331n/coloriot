package de.seperinous.merger.particle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticleRequest {
    float speed = 0.1F;
    int amount = 1;

    final Particle particle;
    Location location;
    Material material;
    byte data;

    public ParticleRequest(@NotNull Particle particle, @NotNull Location location) {
        this.particle = particle;
        this.location = location;
    }

    public ParticleRequest(@NotNull Particle particle) {
        this.particle = particle;
    }

    public @NotNull Packet<?> toPacket() {
        int[] particleData = getParticleData();

        return new PacketPlayOutWorldParticles(
                EnumParticle.valueOf(particle.name()),
                false,
                (float) location.getX(),
                (float) location.getY(),
                (float) location.getZ(),
                0, 0, 0,
                this.speed,
                this.amount,
                particleData
        );
    }

    private int[] getParticleData() {
        if (material == null) return new int[0];

        if (particle == Particle.BLOCK_CRACK || particle == Particle.BLOCK_DUST) {
            int blockId = material.getId();
            int blockData = data;
            return new int[]{blockId, blockData};
        } else if (particle == Particle.ITEM_CRACK) {
            ItemStack item = new ItemStack(material);
            int itemId = item.getType().getId();
            int itemData = item.getDurability();
            return new int[]{itemId, itemData};
        }

        return new int[0];
    }
}
