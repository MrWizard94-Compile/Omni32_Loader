package com.mrwizard94.omni32loader.pack;

import com.mojang.logging.LogUtils;
import com.mrwizard94.omni32loader.Omni32Loader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Omni32Loader.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Omni32PackBootstrap {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PACK_ID = "omni32_loader/dynamic";

    private Omni32PackBootstrap() {
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) {
            return;
        }

        event.addRepositorySource(consumer -> {
            AssetRootResolver.ResolvedRoot resolved = AssetRootResolver.resolve();
            if (resolved == null) {
                LOGGER.warn(
                        "Omni32 Loader: assets.root is unset or invalid. "
                                + "Set assets.root in config/omni32_loader-client.toml or OMNI32_ASSETS_ROOT.");
                return;
            }

            Set<String> namespaces = NamespaceDiscovery.discoverValidated(resolved);
            if (namespaces.isEmpty()) {
                LOGGER.warn(
                        "Omni32 Loader: no namespaces matched at {} (layout={}, activeOnly={}).",
                        resolved.root(),
                        resolved.layout(),
                        com.mrwizard94.omni32loader.config.Omni32Config.ACTIVE_ONLY.get());
                return;
            }

            if (com.mrwizard94.omni32loader.config.Omni32Config.LOG_DISCOVERY.get()) {
                String sample = namespaces.stream()
                        .sorted(Comparator.naturalOrder())
                        .limit(12)
                        .collect(Collectors.joining(", "));
                LOGGER.info(
                        "Omni32 Loader: mounting {} namespaces from {} ({}) — sample: {}{}",
                        namespaces.size(),
                        resolved.root(),
                        resolved.layout(),
                        sample,
                        namespaces.size() > 12 ? ", ..." : "");
            }

            Pack.ResourcesSupplier supplier = id -> new Omni32PackResources(PACK_ID, resolved, namespaces);
            Pack pack = Pack.readMetaAndCreate(
                    PACK_ID,
                    Component.literal("Omni32"),
                    false,
                    supplier,
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    PackSource.BUILT_IN);

            consumer.accept(pack);
        });
    }
}