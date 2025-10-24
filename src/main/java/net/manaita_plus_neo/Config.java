package net.manaita_plus_neo;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue crafting_doubling;
    public static final ModConfigSpec.IntValue furnace_doubling;
    public static final ModConfigSpec.IntValue brewing_doubling;
    public static final ModConfigSpec.IntValue destroy_doubling;
    public static final ModConfigSpec.IntValue source_doubling;

    static {
        BUILDER.push("Manaita Plus Neo Config");

        crafting_doubling = BUILDER
                .comment("Crafting Doubling Multiplier")
                .defineInRange("crafting_doubling_value", 64, 1, Integer.MAX_VALUE);

        furnace_doubling = BUILDER
                .comment("Furnace Doubling Multiplier")
                .defineInRange("furnace_doubling_value", 64, 1, Integer.MAX_VALUE);

        brewing_doubling = BUILDER
                .comment("Brewing Stand Doubling Multiplier")
                .defineInRange("brewing_doubling_value", 64, 1, Integer.MAX_VALUE);

        destroy_doubling = BUILDER
                .comment("Destroy Doubling Multiplier")
                .defineInRange("destroy_doubling_value", 4, 1, Integer.MAX_VALUE);

        source_doubling = BUILDER
                .comment("Water Source Doubling Multiplier")
                .defineInRange("source_doubling_value", 64, 1, Integer.MAX_VALUE);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static int crafting_doubling_value;
    public static int furnace_doubling_value;
    public static int brewing_doubling_value;
    public static int destroy_doubling_value;
    public static int source_doubling_value;
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        crafting_doubling_value = crafting_doubling.get();
        furnace_doubling_value = furnace_doubling.get();
        brewing_doubling_value = brewing_doubling.get();
        destroy_doubling_value = destroy_doubling.get();
        source_doubling_value = source_doubling.get();
    }
}