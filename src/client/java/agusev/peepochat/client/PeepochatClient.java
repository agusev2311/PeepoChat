package agusev.peepochat.client;

import agusev.peepochat.client.config.PeepochatConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.*;

public class PeepochatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        VersionChecker.VersionResponse response = VersionChecker.checkForUpdate("0.5-mc1.21.3");
        if (response != null && response.has_update) {
            ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
                if (screen instanceof TitleScreen) {
                    client.execute(() -> {
                        client.setScreen(new UpdateScreen(screen));
                    });
                }
            });
        }

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            String rawMessage = getRawMessageContent(message);
            if (rawMessage.startsWith("✉✉✉")) {
                rawMessage = rawMessage.substring(5).trim(); // Убираем префикс
                boolean isMessageForMe = !rawMessage.startsWith("Вы →");
                String[] parts = rawMessage.split("→"); // Разделяем на отправителя и получателя
                String messageText = parts.length > 1 ? parts[1].split("]: ")[1].trim() : "";

                boolean isMessageForMeResult = isMessageForMe;

                String username;
                if (isMessageForMeResult) {
                    username = parts[0].trim();
                } else {
                    username = parts.length > 1 ? parts[1].split("]: ")[0].trim() : "";
                }

                MutableText text;
                MinecraftClient client = MinecraftClient.getInstance();

                assert client.player != null;
//                System.out.println(client.player.getName());

                if (username.equals("PWGoood")) {
                    text = PaintDirectMessage.PaintText(
                            isMessageForMeResult, username, messageText,
                            0xFDA524, 0xFBFF00,
                            PeepochatConfig.getInstance().selectedOption.equals("peepochat.config.option.color_scheme.2_colors")
                    );
                } else if (username.equals(client.player.getName().getLiteralString())) {
                    text = PaintDirectMessage.PaintText(
                            isMessageForMeResult, username, messageText,
                            0xC5C5C5, 0x393939,
                            PeepochatConfig.getInstance().selectedOption.equals("peepochat.config.option.color_scheme.2_colors")
                    );
                } else {
                    text = PaintDirectMessage.PaintText(
                            isMessageForMeResult, username, messageText,
                            PeepochatConfig.getInstance().customColor1, PeepochatConfig.getInstance().customColor2,
                            PeepochatConfig.getInstance().selectedOption.equals("peepochat.config.option.color_scheme.2_colors")
                    );
                }

                client.player.sendMessage(text, false);

                return false;
            }

            if (rawMessage.startsWith("✉✉") && !rawMessage.startsWith("✉✉✉")) {
                return true;
            }

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