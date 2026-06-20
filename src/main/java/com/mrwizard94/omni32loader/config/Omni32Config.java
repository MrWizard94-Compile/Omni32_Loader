package com.mrwizard94.omni32loader.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public final class Omni32Config {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec.ConfigValue<String> ASSETS_ROOT;
    public static final ForgeConfigSpec.BooleanValue ACTIVE_ONLY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_NAMESPACES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCLUDED_NAMESPACES;
    public static final ForgeConfigSpec.BooleanValue LOG_DISCOVERY;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("assets");
        ASSETS_ROOT = builder
                .comment(
                        "Absolute or game-dir-relative path to the Omni32 asset store.",
                        "Use AssetConverter output/assets (namespace folders) or a pack assets/ directory.",
                        "Environment override: OMNI32_ASSETS_ROOT")
                .define("root", "");
        ACTIVE_ONLY = builder
                .comment("When true, only mount namespaces for mods present in the instance.")
                .define("activeOnly", true);
        EXTRA_NAMESPACES = builder
                .comment("Always mount these namespaces when present on disk (e.g. minecraft).")
                .defineList("extraNamespaces", List.of(), o -> o instanceof String);
        EXCLUDED_NAMESPACES = builder
                .comment("Never mount these namespaces.")
                .defineList("excludedNamespaces", List.of(), o -> o instanceof String);
        LOG_DISCOVERY = builder
                .comment("Log resolved asset root and mounted namespace count at client startup.")
                .define("logDiscovery", true);
        builder.pop();

        CLIENT_SPEC = builder.build();
    }

    private Omni32Config() {
    }
}