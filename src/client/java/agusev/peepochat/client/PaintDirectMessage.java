package agusev.peepochat.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PaintDirectMessage {
    public static MutableText PaintText(boolean to_or_from, String name, String text, int color1, int color2, boolean is2colors) {
        String sender = to_or_from ? name : "Вы";
        String receiver = to_or_from ? "Вы" : name;
        String fullMessage = "✉✉ [" + sender + " → " + receiver + "]: ";

        MutableText message = Text.literal("").formatted(Formatting.RESET);

        if (is2colors) {
            // Двухцветное окрашивание
            message.append(Text.literal("✉✉ [").setStyle(Style.EMPTY.withColor(color1)));
            message.append(Text.literal(sender).setStyle(Style.EMPTY.withColor(color2).withBold(true)));
            message.append(Text.literal(" → ").setStyle(Style.EMPTY.withColor(color1)));
            message.append(Text.literal(receiver).setStyle(Style.EMPTY.withColor(color2).withBold(true)));
            message.append(Text.literal("]: ").setStyle(Style.EMPTY.withColor(color1)));
        } else {
            // Градиентное окрашивание
            int startBold1 = fullMessage.indexOf(sender);
            int endBold1 = startBold1 + sender.length();
            int startBold2 = fullMessage.indexOf(receiver);
            int endBold2 = startBold2 + receiver.length();

            for (int i = 0; i < fullMessage.length(); i++) {
                char c = fullMessage.charAt(i);
                int color = interpolateColor(color1, color2, (float) i / (fullMessage.length() - 1));
                Style style = Style.EMPTY.withColor(color);

                if ((i >= startBold1 && i < endBold1) || (i >= startBold2 && i < endBold2)) {
                    style = style.withBold(true);
                }

                message.append(Text.literal(String.valueOf(c)).setStyle(style));
            }
        }

        message.append(Text.literal(text));
        return message;
    }

    private static int interpolateColor(int color1, int color2, float ratio) {
        int r1 = (color1 >> 16) & 0xFF, g1 = (color1 >> 8) & 0xFF, b1 = color1 & 0xFF;
        int r2 = (color2 >> 16) & 0xFF, g2 = (color2 >> 8) & 0xFF, b2 = color2 & 0xFF;

        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return (r << 16) | (g << 8) | b;
    }

//    public static void sendLocalMessage(boolean to_or_from, String name, String text, int color1, int color2, boolean is2colors) {
//        if (MinecraftClient.getInstance().player != null) {
//            MutableText coloredMessage = PaintText(to_or_from, name, text, color1, color2, is2colors, true);
//
//        }
//    }

    // Вариант с настройкой цветов по умолчанию
//    public static void sendLocalMessage(boolean to_or_from, String name, String text) {
//        // Используем стандартные цвета, например фиолетовый градиент
//        int defaultColor1 = 0xFF00FF; // Ярко-розовый
//        int defaultColor2 = 0x800080; // Фиолетовый
//        sendLocalMessage(to_or_from, name, text, defaultColor1, defaultColor2, false);
//    }
}
