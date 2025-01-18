package agusev.peepochat.client;

import agusev.peepochat.client.config.PeepochatConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.client.MinecraftClient;

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

    /**
     * Проверяет, можно ли показывать сообщение.
     */
    private boolean shouldAllowMessage(String message) {
        if (!PeepochatConfig.getInstance().enableFilter) {
            return true; // Если фильтр отключен, показываем все сообщения
        }

        // Условие фильтрации (пример)
        return !(message.startsWith("[+]") || message.startsWith("[-]"));
    }

    /**
     * Получает raw-содержимое сообщения.
     */
    private String getRawMessageContent(Text message) {
        StringBuilder raw = new StringBuilder();
        appendTextContent(message, raw);
        return raw.toString();
    }

    /**
     * Рекурсивно добавляет содержимое текста в raw-строку.
     */
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
