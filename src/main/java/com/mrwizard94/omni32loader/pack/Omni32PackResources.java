package com.mrwizard94.omni32loader.pack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class Omni32PackResources implements PackResources {
    private final String packId;
    private final AssetRootResolver.ResolvedRoot resolved;
    private final Set<String> namespaces;

    public Omni32PackResources(String packId, AssetRootResolver.ResolvedRoot resolved, Set<String> namespaces) {
        this.packId = packId;
        this.resolved = resolved;
        this.namespaces = namespaces;
    }

    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
        return null;
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... paths) {
        return null;
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        if (type != PackType.CLIENT_RESOURCES) {
            return null;
        }
        if (!namespaces.contains(location.getNamespace())) {
            return null;
        }

        Path file = resolveResourcePath(location);
        if (file == null || !Files.isRegularFile(file)) {
            return null;
        }
        return IoSupplier.create(file);
    }

    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput output) {
        if (type != PackType.CLIENT_RESOURCES || !namespaces.contains(namespace)) {
            return;
        }

        Path base = baseNamespacePath(namespace).resolve(path.replace('/', java.io.File.separatorChar));
        if (!Files.isDirectory(base)) {
            return;
        }

        try (var stream = Files.walk(base)) {
            stream.filter(Files::isRegularFile).forEach(file -> {
                String relative = base.relativize(file).toString().replace('\\', '/');
                ResourceLocation id = new ResourceLocation(namespace, relative);
                output.accept(id, IoSupplier.create(file));
            });
        } catch (Exception ignored) {
            // Best-effort listing for reload listeners; getResource is authoritative.
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return type == PackType.CLIENT_RESOURCES ? namespaces : Set.of();
    }

    @Override
    public void close() {
    }

    @Override
    public String packId() {
        return packId;
    }

    private Path resolveResourcePath(ResourceLocation location) {
        return baseNamespacePath(location.getNamespace()).resolve(location.getPath());
    }

    private Path baseNamespacePath(String namespace) {
        return resolved.layout() == AssetRootResolver.Layout.PACK_ASSETS
                ? resolved.root().resolve("assets").resolve(namespace)
                : resolved.root().resolve(namespace);
    }
}