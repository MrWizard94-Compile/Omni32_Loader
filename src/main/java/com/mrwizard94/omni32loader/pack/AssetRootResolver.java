package com.mrwizard94.omni32loader.pack;

import com.mrwizard94.omni32loader.config.Omni32Config;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;

public final class AssetRootResolver {
    public enum Layout {
        /** AssetConverter output/assets/<namespace>/textures/ */
        ASSET_STORE,
        /** Standard pack layout: <root>/assets/<namespace>/textures/ */
        PACK_ASSETS
    }

    public record ResolvedRoot(Path root, Layout layout) {
    }

    private AssetRootResolver() {
    }

    public static ResolvedRoot resolve() {
        String configured = Omni32Config.ASSETS_ROOT.get();
        if (configured == null || configured.isBlank()) {
            String env = System.getenv("OMNI32_ASSETS_ROOT");
            configured = env != null ? env : "";
        }

        if (configured.isBlank()) {
            return null;
        }

        Path candidate = Path.of(configured);
        if (!candidate.isAbsolute()) {
            candidate = FMLPaths.GAMEDIR.get().resolve(candidate).normalize();
        }

        if (!Files.isDirectory(candidate)) {
            return null;
        }

        if (Files.isDirectory(candidate.resolve("assets"))) {
            return new ResolvedRoot(candidate, Layout.PACK_ASSETS);
        }

        return new ResolvedRoot(candidate, Layout.ASSET_STORE);
    }
}