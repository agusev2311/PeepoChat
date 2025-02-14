package agusev.peepochat.client;

import agusev.peepochat.client.config.PeepochatConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.CharUtils;

public class PeepochatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
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

//                System.out.println("Is message for me: " + isMessageForMeResult);
//                System.out.println("RandomUsername: э" + username + "э");
//                System.out.println("RandomUsername: " + parts[0].trim());
//                System.out.println("RandomUsername2: " + (parts.length > 1 ? parts[1].split("]: ")[0].trim() : ""));
//                System.out.println("RandomText: " + messageText);

                MutableText text;
                if (username.equals("PWGoood")) {
                    text = PaintDirectMessage.PaintText(
                            isMessageForMeResult, username, messageText,
                            0xFDA524, 0xFBFF00,
                            PeepochatConfig.getInstance().selectedOption.equals("peepochat.config.option.color_scheme.2_colors")
                    );
                } else {
                    text = PaintDirectMessage.PaintText(
                            isMessageForMeResult, username, messageText,
                            PeepochatConfig.getInstance().customColor1, PeepochatConfig.getInstance().customColor2,
                            PeepochatConfig.getInstance().selectedOption.equals("peepochat.config.option.color_scheme.2_colors")
                    );
                }

                MinecraftClient client = MinecraftClient.getInstance();
                assert client.player != null;
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