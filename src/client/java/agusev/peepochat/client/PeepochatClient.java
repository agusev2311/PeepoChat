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
        if (message.matches(".*\\[([+-])\\].*")) {
            String username = message.replaceAll(".*\\[([+-])\\]\\s*([^§\\s]+).*", "$2");
            if (username.isEmpty()) {
                return "";
            }
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