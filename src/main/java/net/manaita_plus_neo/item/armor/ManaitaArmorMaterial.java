package net.manaita_plus_neo.item.armor;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.manaita_plus_neo.ManaitaPlusNeo;

import java.util.EnumMap;
import java.util.List;

public class ManaitaArmorMaterial {

    public static final Holder<ArmorMaterial> MANAITA = registerMaterial();

    private static Holder<ArmorMaterial> registerMaterial() {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(ManaitaPlusNeo.MOD_ID, "manaita_armor");
        return Registry.registerForHolder(
            BuiltInRegistries.ARMOR_MATERIAL,
            location,
            new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, 0);
                    map.put(ArmorItem.Type.LEGGINGS, 0);
                    map.put(ArmorItem.Type.CHESTPLATE, 0);
                    map.put(ArmorItem.Type.HELMET, 0);
                    map.put(ArmorItem.Type.BODY, 0);
                }),
                100,
                SoundEvents.ARMOR_EQUIP_GENERIC,
                () -> Ingredient.EMPTY,
                List.of(new ArmorMaterial.Layer(location)),
                0.0F,
                0.0F
            )
        );
    }
}