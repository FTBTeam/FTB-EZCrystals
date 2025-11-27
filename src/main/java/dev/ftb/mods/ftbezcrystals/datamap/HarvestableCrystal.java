package dev.ftb.mods.ftbezcrystals.datamap;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbezcrystals.FTBEZCrystals;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

public record HarvestableCrystal(Block bud, List<Block> buddingBlocks) {
    public static final Codec<HarvestableCrystal> CODEC = RecordCodecBuilder.create(in -> in.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("bud").forGetter(HarvestableCrystal::bud),
                    BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("budding_block").forGetter(HarvestableCrystal::buddingBlocks)
            )
            .apply(in, HarvestableCrystal::new));

    public HarvestableCrystal(Block bud, Block buddingBlock) {
        this(bud, List.of(buddingBlock));
    }

    public static Optional<HarvestableCrystal> fromJson(JsonElement json, RegistryAccess registryAccess) {
        return CODEC.decode(RegistryOps.create(JsonOps.INSTANCE, registryAccess), json)
                .resultOrPartial(error -> FTBEZCrystals.LOGGER.error("JSON parse failure: {}", error))
                .map(Pair::getFirst);
    }
}
