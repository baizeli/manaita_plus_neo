package net.manaita_plus_neo;

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
}