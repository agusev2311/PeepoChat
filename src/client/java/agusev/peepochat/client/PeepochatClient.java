package agusev.peepochat.client;

import agusev.peepochat.client.config.PeepochatConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.client.MinecraftClient;

import java.awt.*;

public class PeepochatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Регистрируем обработчик сообщений чата
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            // Получаем строковое представление сообщения
            String rawMessage = getRawMessageContent(message);

            // Логируем raw-сообщения в режиме отладки
            if (PeepochatConfig.getInstance().enableDebug) {
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
                        Text.literal("§7[RAW] §r" + rawMessage)
                );
            }

            // Применяем фильтрацию
            return shouldAllowMessage(rawMessage);
        });
    }

    private boolean shouldAllowMessage(String message) {
        if (!PeepochatConfig.getInstance().enableFilter) {
            return true;
        }
        String username = extractUsername(message);

        return (!(message.startsWith("[+]") || message.startsWith("[-]")) || isFriend(username));
    }

    private boolean isFriend(String username) {
        return PeepochatConfig.getInstance().friendList.contains(username);
    }


    private String extractUsername(String message) {
        if (message.startsWith("[+]") || message.startsWith("[-]")) {
            String username = message.substring(3).trim();
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
                    Text.literal("Username: /" + message.substring(3).trim() + "/")
            );
            return username;
        }
        return "";
    }

    private String getRawMessageContent(Text message) {
        StringBuilder raw = new StringBuilder();
        appendTextContent(message, raw);
        return raw.toString();
    }

    private void appendTextContent(Text text, StringBuilder builder) {
        Style style = text.getStyle();

        // Добавляем информацию о стиле
        if (style != null) {
            // Цветовое форматирование
            if (style.getColor() != null) {
                builder.append("§{color:").append(style.getColor().toString()).append("}");
            }

            // Click event
            ClickEvent clickEvent = style.getClickEvent();
            if (clickEvent != null) {
                builder.append("§{click:").append(clickEvent.getAction().name())
                        .append(",").append(clickEvent.getValue()).append("}");
            }

            // Hover event
            HoverEvent hoverEvent = style.getHoverEvent();
            if (hoverEvent != null) {
                builder.append("§{hover:").append(hoverEvent.getAction().toString());
                if (hoverEvent.getValue(hoverEvent.getAction()) != null) {
                    builder.append(",").append(hoverEvent.getValue(hoverEvent.getAction()).toString());
                }
                builder.append("}");
            }
        }

        // Добавляем сам текст
        builder.append(text.getString());

        // Рекурсивно обрабатываем дочерние элементы
        for (Text sibling : text.getSiblings()) {
            appendTextContent(sibling, builder);
        }
    }
}
