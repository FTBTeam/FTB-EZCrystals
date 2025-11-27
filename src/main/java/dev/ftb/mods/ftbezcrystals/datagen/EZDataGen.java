package dev.ftb.mods.ftbezcrystals.datagen;

import dev.ftb.mods.ftbezcrystals.FTBEZCrystals;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = FTBEZCrystals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EZDataGen {
    @SubscribeEvent
    public static void dataGenEvent(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<Provider> lookupProvider = event.getLookupProvider();

        if (event.includeServer()) {
            gen.addProvider(true, new EZCrystalsDataProvider(output, lookupProvider));
        }
    }
}
