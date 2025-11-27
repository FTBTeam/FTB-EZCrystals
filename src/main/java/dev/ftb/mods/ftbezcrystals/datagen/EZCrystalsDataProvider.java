package dev.ftb.mods.ftbezcrystals.datagen;

import appeng.core.definitions.AEBlocks;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.shynieke.geore.registry.GeOreBlockReg;
import com.shynieke.geore.registry.GeOreRegistry;
import dev.ftb.mods.ftbezcrystals.FTBEZCrystals;
import dev.ftb.mods.ftbezcrystals.datamap.HarvestableCrystal;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EZCrystalsDataProvider implements DataProvider {
    protected final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final Map<Block, HarvestableCrystal> data = new HashMap<>();
    private final PackOutput.PathProvider pathProvider;

    public EZCrystalsDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.lookupProvider = lookupProvider;

        pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "harvestable_crystals");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        data.put(Blocks.AMETHYST_CLUSTER, new HarvestableCrystal(Blocks.SMALL_AMETHYST_BUD, Blocks.BUDDING_AMETHYST));

        addGeOres();
        addAe2Crystals();

        return lookupProvider.thenCompose(provider -> {
            JsonObject json = new JsonObject();
            data.forEach((block, rec) ->
                    HarvestableCrystal.CODEC.encodeStart(RegistryOps.create(JsonOps.INSTANCE, provider), rec)
                            .result().ifPresent(el -> json.add(ForgeRegistries.BLOCKS.getKey(block).toString(), el))
            );
            return DataProvider.saveStable(cache, json, pathProvider.json(FTBEZCrystals.id("crystals")));
        });
    }

    private void addAe2Crystals() {
        data.put(AEBlocks.QUARTZ_CLUSTER.block(), new HarvestableCrystal(AEBlocks.SMALL_QUARTZ_BUD.block(),
                List.of(AEBlocks.CHIPPED_BUDDING_QUARTZ.block(), AEBlocks.DAMAGED_BUDDING_QUARTZ.block(),
                        AEBlocks.FLAWED_BUDDING_QUARTZ.block(), AEBlocks.FLAWLESS_BUDDING_QUARTZ.block())));
    }

    private void addGeOres() {
        addGeOre(GeOreRegistry.COAL_GEORE);
        addGeOre(GeOreRegistry.COPPER_GEORE);
        addGeOre(GeOreRegistry.DIAMOND_GEORE);
        addGeOre(GeOreRegistry.EMERALD_GEORE);
        addGeOre(GeOreRegistry.GOLD_GEORE);
        addGeOre(GeOreRegistry.IRON_GEORE);
        addGeOre(GeOreRegistry.LAPIS_GEORE);
        addGeOre(GeOreRegistry.QUARTZ_GEORE);
        addGeOre(GeOreRegistry.REDSTONE_GEORE);
        addGeOre(GeOreRegistry.RUBY_GEORE);
        addGeOre(GeOreRegistry.SAPPHIRE_GEORE);
        addGeOre(GeOreRegistry.TOPAZ_GEORE);
        addGeOre(GeOreRegistry.ZINC_GEORE);
    }

    private void addGeOre(GeOreBlockReg reg) {
        data.put(reg.getCluster().get(), new HarvestableCrystal(reg.getSmallBud().get(), reg.getBudding().get()));
    }

    @Override
    public String getName() {
        return "Harvestable Crystals";
    }
}
