package com.mrwizard94.omni32loader.pack;

import com.mrwizard94.omni32loader.config.Omni32Config;
import com.mrwizard94.omni32loader.namespace.NamespaceMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class NamespaceDiscovery {
    private NamespaceDiscovery() {
    }

    public static Set<String> discover(AssetRootResolver.ResolvedRoot resolved) {
        Set<String> onDisk = listTextureNamespaces(resolved);
        if (onDisk.isEmpty()) {
            return Set.of();
        }

        Set<String> excluded = new HashSet<>(Omni32Config.EXCLUDED_NAMESPACES.get());
        onDisk.removeAll(excluded);

        Set<String> selected = new HashSet<>(Omni32Config.EXTRA_NAMESPACES.get());
        if (Omni32Config.ACTIVE_ONLY.get()) {
            Set<String> candidates = NamespaceMapper.candidateNamespacesForLoadedMods();
            selected.addAll(onDisk.stream().filter(candidates::contains).collect(Collectors.toSet()));
        } else {
            selected.addAll(onDisk);
        }

        selected.retainAll(onDisk);
        return Set.copyOf(selected);
    }

    private static Set<String> listTextureNamespaces(AssetRootResolver.ResolvedRoot resolved) {
        Path namespacesDir = resolved.layout() == AssetRootResolver.Layout.PACK_ASSETS
                ? resolved.root().resolve("assets")
                : resolved.root();

        if (!Files.isDirectory(namespacesDir)) {
            return Set.of();
        }

        try (Stream<Path> children = Files.list(namespacesDir)) {
            return children
                    .filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .filter(namespace -> !namespace.isBlank())
                    .collect(Collectors.toCollection(HashSet::new));
        } catch (IOException e) {
            return Set.of();
        }
    }

    static boolean namespaceHasTextures(AssetRootResolver.ResolvedRoot resolved, String namespace) {
        Path texturesDir = resolved.layout() == AssetRootResolver.Layout.PACK_ASSETS
                ? resolved.root().resolve("assets").resolve(namespace).resolve("textures")
                : resolved.root().resolve(namespace).resolve("textures");

        if (!Files.isDirectory(texturesDir)) {
            return false;
        }

        try (Stream<Path> walk = Files.walk(texturesDir, 4)) {
            return walk.anyMatch(path -> {
                String name = path.getFileName().toString().toLowerCase();
                return name.endsWith(".png") || name.endsWith(".mcmeta");
            });
        } catch (IOException e) {
            return false;
        }
    }

    public static Set<String> discoverValidated(AssetRootResolver.ResolvedRoot resolved) {
        return discover(resolved).stream()
                .filter(namespace -> namespaceHasTextures(resolved, namespace))
                .collect(Collectors.toUnmodifiableSet());
    }
}