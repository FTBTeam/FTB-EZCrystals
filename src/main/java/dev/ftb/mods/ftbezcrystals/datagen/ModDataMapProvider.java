package dev.ftb.mods.ftbezcrystals.datagen;

import appeng.core.definitions.AEBlocks;
import com.shynieke.geore.registry.GeOreBlockReg;
import com.shynieke.geore.registry.GeOreRegistry;
import dev.ftb.mods.ftbezcrystals.datamap.EZDataMaps;
import dev.ftb.mods.ftbezcrystals.datamap.HarvestableCrystal;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void gather(@NotNull Provider provider) {
        final Builder<HarvestableCrystal, Block> crystals = builder(EZDataMaps.HARVESTABLE_CRYSTALS);
        crystals.add(Blocks.AMETHYST_CLUSTER.builtInRegistryHolder(), new HarvestableCrystal(Blocks.SMALL_AMETHYST_BUD, Blocks.BUDDING_AMETHYST), false);

        addGeOres(crystals);

        crystals.add(AEBlocks.QUARTZ_CLUSTER.block().builtInRegistryHolder(), new HarvestableCrystal(AEBlocks.SMALL_QUARTZ_BUD.block(),
                        List.of(AEBlocks.CHIPPED_BUDDING_QUARTZ.block(), AEBlocks.DAMAGED_BUDDING_QUARTZ.block(),
                                AEBlocks.FLAWED_BUDDING_QUARTZ.block(), AEBlocks.FLAWLESS_BUDDING_QUARTZ.block())),
                false, new ModLoadedCondition("ae2"));
    }

    private void addGeOres(Builder<HarvestableCrystal, Block> crystals) {

        addGeOre(crystals, GeOreRegistry.COAL_GEORE);
        addGeOre(crystals, GeOreRegistry.COPPER_GEORE);
        addGeOre(crystals, GeOreRegistry.DIAMOND_GEORE);
        addGeOre(crystals, GeOreRegistry.EMERALD_GEORE);
        addGeOre(crystals, GeOreRegistry.GOLD_GEORE);
        addGeOre(crystals, GeOreRegistry.IRON_GEORE);
        addGeOre(crystals, GeOreRegistry.LAPIS_GEORE);
        addGeOre(crystals, GeOreRegistry.QUARTZ_GEORE);
        addGeOre(crystals, GeOreRegistry.REDSTONE_GEORE);
        addGeOre(crystals, GeOreRegistry.RUBY_GEORE);
        addGeOre(crystals, GeOreRegistry.SAPPHIRE_GEORE);
        addGeOre(crystals, GeOreRegistry.TOPAZ_GEORE);
        addGeOre(crystals, GeOreRegistry.ZINC_GEORE);
    }

    private void addGeOre(Builder<HarvestableCrystal, Block> crystals, GeOreBlockReg reg) {
        crystals.add(reg.getCluster(), new HarvestableCrystal(reg.getSmallBud().get(), reg.getBudding().get()), false, new ModLoadedCondition("geore"));
    }
}
