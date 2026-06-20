package com.mrwizard94.omni32loader.namespace;

import net.minecraftforge.fml.ModList;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Maps Forge mod ids to Minecraft asset namespaces.
 * Sourced from AssetConverter config/registry.py MOD_NAMESPACES.
 */
public final class NamespaceMapper {
    private static final Map<String, String> MOD_TO_NAMESPACE = buildModToNamespace();
    private static final Map<String, Set<String>> NAMESPACE_TO_MODS = buildReverse(MOD_TO_NAMESPACE);

    private static final Set<String> IGNORED_MOD_IDS = Set.of(
            "forge", "minecraft", "omni32_loader", "fabric", "fabric_api"
    );

    private NamespaceMapper() {
    }

    public static Set<String> candidateNamespacesForLoadedMods() {
        Set<String> candidates = new HashSet<>();
        ModList.get().getMods().forEach(info -> {
            String modId = info.getModId();
            if (IGNORED_MOD_IDS.contains(modId)) {
                return;
            }
            candidates.add(modId);
            String mapped = MOD_TO_NAMESPACE.get(modId);
            if (mapped != null) {
                candidates.add(mapped);
            }
        });
        return candidates;
    }

    public static Set<String> modIdsForNamespace(String namespace) {
        return NAMESPACE_TO_MODS.getOrDefault(namespace, Collections.emptySet());
    }

    public static String namespaceForMod(String modId) {
        return MOD_TO_NAMESPACE.getOrDefault(modId, modId);
    }

    private static Map<String, String> buildModToNamespace() {
        Map<String, String> map = new HashMap<>();
        map.put("oh_the_biomes_weve_gone", "biomeswevegone");
        map.put("railcraft_reborn", "railcraft");
        map.put("relics_mod", "relics");
        map.put("extreme_reactors", "bigreactors");
        map.put("reliquified_artifacts", "relics");
        map.put("iron_furnaces", "ironfurnaces");
        map.put("the_undergarden", "undergarden");
        map.put("storage_delight", "storagedelight");
        map.put("integrated_terminals", "integratedterminals");
        map.put("rftools_utility", "rftoolsutility");
        map.put("rftools_base", "rftoolsbase");
        map.put("rftools_storage", "rftoolsstorage");
        map.put("ars_energistique", "arseng");
        map.put("wireless_chargers", "wirelesschargers");
        map.put("functional_storage", "functionalstorage");
        map.put("creeper_overhaul", "creeperoverhaul");
        map.put("yungs_better_mineshafts", "bettermineshafts");
        map.put("yungs_better_dungeons", "betterdungeons");
        map.put("yungs_better_strongholds", "betterstrongholds");
        map.put("yungs_better_ocean_monuments", "betteroceanmonuments");
        map.put("yungs_better_nether_fortresses", "betterfortresses");
        map.put("yungs_extras", "yungsextras");
        map.put("yungs_better_desert_temples", "betterdeserttemples");
        map.put("yungs_better_witch_huts", "betterwitchhuts");
        map.put("yungs_better_jungle_temples", "betterjungletemples");
        map.put("yungs_better_end_island", "betterendisland");
        map.put("mo_structures", "mostructures");
        map.put("simple_magnets", "simplemagnets");
        map.put("creeperhost_presents_steves_carts", "stevescarts");
        map.put("mega", "megacells");
        map.put("cable_tiers", "cabletiers");
        map.put("extra_disks", "extradisks");
        map.put("applied_mekanistics", "appmek");
        map.put("ranged_pumps", "rangedpumps");
        map.put("ae2_import_export_card", "ae2importexportcard");
        map.put("item_collectors", "itemcollectors");
        map.put("universal_grid", "universalgrid");
        map.put("interdimensional_wireless_transmitter", "creativewirelesstransmitter");
        map.put("silents_gems", "silentgems");
        map.put("advancedae", "advanced_ae");
        map.put("rftools_power", "rftoolspower");
        map.put("rftools_builder", "rftoolsbuilder");
        map.put("living_things", "livingthings");
        map.put("baubley_heart_canisters", "bhc");
        map.put("cpm_fabric", "compactmachines");
        map.put("mekanistic_routers", "mekanisticrouters");
        map.put("natures_aura", "naturesaura");
        map.put("more_red", "morered");
        map.put("integrated_tunnels", "integratedtunnels");
        map.put("integrated_crafting", "integratedcrafting");
        map.put("integrated_scripting", "integratedscripting");
        map.put("explorers_compass", "explorerscompass");
        map.put("industrial_foregoing_souls", "industrialforegoingsouls");
        map.put("laser_bridges_and_doors", "laserbridges");
        map.put("thermal_core", "thermal");
        map.put("thermal_foundation", "thermal");
        map.put("thermal_expansion", "thermal");
        map.put("thermal_innovation", "thermal");
        map.put("mekanismgenerators", "mekanism");
        map.put("mekanismtools", "mekanism");
        return Collections.unmodifiableMap(map);
    }

    private static Map<String, Set<String>> buildReverse(Map<String, String> forward) {
        Map<String, Set<String>> reverse = new HashMap<>();
        forward.forEach((modId, namespace) ->
                reverse.computeIfAbsent(namespace, key -> new HashSet<>()).add(modId));
        return Collections.unmodifiableMap(reverse);
    }
}