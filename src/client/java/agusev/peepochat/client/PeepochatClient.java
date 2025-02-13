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
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            String rawMessage = getRawMessageContent(message);
//            if (PeepochatConfig.getInstance().enableDebug) {
//                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
//                        Text.literal("§7[RAW] §r" + rawMessage)
//                );
//            }
            return shouldAllowMessage(rawMessage);
        });
    }

    private boolean shouldAllowMessage(String message) {
        if (!PeepochatConfig.getInstance().enableFilter) {
            return true;
        }
//        if (!PeepochatConfig.getInstance().enableMessages) {
//            return false;
//        }
        String username = extractUsername(message);

        return (!(message.startsWith("[+]") || message.startsWith("[-]")) || isFriend(username));
    }

    private boolean isFriend(String username) {
        return PeepochatConfig.getInstance().friendList.contains(username);
    }

    private String extractUsername(String message) {
        if (message.matches(".*\\[([+-])\\].*")) {
            String username = message.replaceAll(".*\\[([+-])\\]\\s*([^§\\s]+).*", "$2");
            if (username.isEmpty()) {
//                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
//                        Text.literal("Username: не удалось извлечь")
//                );
                return "";
            }

//            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
//                    Text.literal("Username: " + username)
//            );
            return username;
        }
        return "";
    }

    private String getRawMessageContent(Text message) {
        return message.getString();
    }

    private void appendTextContent(Text text, StringBuilder builder) {
        builder.append(text.getString());
        for (Text sibling : text.getSiblings()) {
            builder.append(sibling.getString());
        }
    }
}