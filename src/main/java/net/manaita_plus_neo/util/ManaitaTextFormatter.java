package net.manaita_plus_neo.util;

import net.minecraft.ChatFormatting;

import java.util.Arrays;

public class ManaitaTextFormatter {
    
    public enum ManaitaText {
        manaita_infinity(new ChatFormatting[] { 
            ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW,
            ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE, 
            ChatFormatting.LIGHT_PURPLE },80.0D
        ),
        manaita_mode(new ChatFormatting[] { ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW,
            ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.GOLD,
            ChatFormatting.RED, ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW,
            ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.GOLD,
            ChatFormatting.RED }, 120.0D
        ),
        manaita_enchantment(new ChatFormatting[] { ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE,
            ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.BLUE,
            ChatFormatting.DARK_PURPLE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE,
            ChatFormatting.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.AQUA,
            ChatFormatting.DARK_PURPLE }, 120.0D
        );

        private final String[] chatFormattings;
        private final double delay;

        ManaitaText(ChatFormatting[] chatFormattings, double delay) {
            this.chatFormattings = Arrays.stream(chatFormattings).map(ChatFormatting::toString).toArray(String[]::new);
            if (delay <= 0.0D) delay = 0.001D;
            this.delay = delay;
        }

        public String formatting(String input) {
            String[] colours = this.chatFormattings;
            StringBuilder sb = new StringBuilder(input.length() * 3);
            int offset = (int) Math.floor((System.currentTimeMillis() & 0x3FFFL) / delay) % colours.length;
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                int col = (i + colours.length - offset) % colours.length;
                sb.append(colours[col]);
                sb.append(c);
            }
            return sb.toString();
        }
    }
}