package agusev.peepochat.client;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaintDirectMessage {
    public static MutableText PaintText(boolean to_or_from, String name, String text, int color1, int color2, boolean is2colors) {
        String sender = to_or_from ? name : "Вы";
        String receiver = to_or_from ? "Вы" : name;
        String fullMessage = "✉✉ [" + sender + " → " + receiver + "]: ";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        String receivedOrSent = to_or_from ? "Получено " : "Отправлено ";
        MutableText hoverText = Text.literal("Личное сообщение\n")
            .formatted(Formatting.WHITE)
            .append(Text.literal(receivedOrSent)
                .formatted(Formatting.DARK_AQUA)
                .append(Text.literal(formattedDateTime)
                    .formatted(Formatting.WHITE))
                .append("\n")
                .append(Text.literal("От ")
                    .formatted(Formatting.DARK_AQUA)
                    .append(Text.literal(name)
                        .formatted(Formatting.WHITE))
                    .append("\n\n")
                    .append(Text.literal("Нажмите для ответа")
                        .formatted(Formatting.GRAY))));

        MutableText message = Text.literal("").formatted(Formatting.RESET);

        if (is2colors) {
            // Двухцветное окрашивание
            message.append(Text.literal("✉✉ [").setStyle(Style.EMPTY.withColor(color1)))
                .append(Text.literal(sender).setStyle(Style.EMPTY.withColor(color2).withBold(true)))
                .append(Text.literal(" → ").setStyle(Style.EMPTY.withColor(color1)))
                .append(Text.literal(receiver).setStyle(Style.EMPTY.withColor(color2).withBold(true)))
                .append(Text.literal("]: ").setStyle(Style.EMPTY.withColor(color1)));
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

        message.append(Text.literal(text).setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        return message
            .styled(s -> s
                .withClickEvent(new ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND,
                    String.format("/tell %s ", name)
                ))
                .withHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    hoverText
                ))
            );
    }

    private static int interpolateColor(int color1, int color2, float ratio) {
        int r1 = (color1 >> 16) & 0xFF, g1 = (color1 >> 8) & 0xFF, b1 = color1 & 0xFF;
        int r2 = (color2 >> 16) & 0xFF, g2 = (color2 >> 8) & 0xFF, b2 = color2 & 0xFF;

        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return (r << 16) | (g << 8) | b;
    }
}