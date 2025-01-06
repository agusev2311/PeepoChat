package agusev.peepochat.client;

import agusev.peepochat.client.config.PeepochatConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public class PeepochatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Регистрируем обработчик сообщений чата
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, signedMessage) -> {
            if (!PeepochatConfig.getInstance().enableFilter) {
                return true;
            }

            String messageString = message.getString();
            return !(messageString.startsWith("[+]") || messageString.startsWith("[-]"));
        });
    }
}