# Omni32 Loader

Forge **1.20.1** client mod that mounts [Omni32](https://github.com/MrWizard94-Compile/AssetConverter) upscaled textures dynamically — no monolithic `resourcepacks/Omni32/` copy required.

## How it works

1. Point `assets.root` at the AssetConverter store (`output/assets/`) or a standard pack `assets/` tree.
2. On client startup, the loader intersects **namespaces on disk** with **mods in the instance** (when `activeOnly` is true).
3. A built-in resource pack at `Pack.Position.TOP` serves matching `textures/` PNGs.

Namespace ↔ mod id mapping mirrors AssetConverter `config/registry.py` (`MOD_NAMESPACES`).

## Configuration

File: `config/omni32_loader-client.toml`

```toml
[assets]
    root = "C:/Projects/AssetConverter/output/assets"
    activeOnly = true
    logDiscovery = true
    extraNamespaces = []
    excludedNamespaces = []
```

Environment override: `OMNI32_ASSETS_ROOT`

Relative paths resolve against the Minecraft instance directory.

## Build

```powershell
.\gradlew.bat build
```

Output: `build/libs/omni32_loader-0.1.0-1.20.1.jar`

## Instance setup

1. Install `omni32_loader` in `mods/`.
2. Set `assets.root` to your Omni32 asset store.
3. **Disable** the static `resourcepacks/Omni32` entry in `options.txt` to avoid double-loading.

## Related repos

- [AssetConverter / Omni32 engine](https://github.com/MrWizard94-Compile/AssetConverter)
- [Omni32 Modpack](https://github.com/MrWizard94-Compile/Omni32-Modpack)