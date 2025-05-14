package dev.ftb.mods.ftbezcrystals.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record HarvestableCrystal(Block bud, List<Block> buddingBlocks) {
    public static final Codec<HarvestableCrystal> CODEC = RecordCodecBuilder.create(in -> in.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("bud").forGetter(HarvestableCrystal::bud),
                    BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("budding_block").forGetter(HarvestableCrystal::buddingBlocks)
            )
            .apply(in, HarvestableCrystal::new));

    public HarvestableCrystal(Block bud, Block buddingBlock) {
        this(bud, List.of(buddingBlock));
    }
}
