package com.mrwizard94.omni32loader;

import com.mojang.logging.LogUtils;
import com.mrwizard94.omni32loader.config.Omni32Config;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Omni32Loader.MOD_ID)
public class Omni32Loader {
    public static final String MOD_ID = "omni32_loader";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Omni32Loader() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Omni32Config.CLIENT_SPEC);
        LOGGER.info("Omni32 Loader initialized — awaiting client pack registration.");
    }
}