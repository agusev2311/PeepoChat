package agusev.peepochat.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class UpdateScreen extends Screen {
    private final Screen parent;

    protected UpdateScreen(Screen parent) {
        super(Text.literal("New Update Available!"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(Text.literal("Download update"), button -> {
            // Действие при нажатии
            Util.getOperatingSystem().open("https://example.com/download");
        }).dimensions(width / 2 - 100, height / 2 - 20, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Ignore"), button -> {
            assert client != null;
            client.setScreen(parent);
        }).dimensions(width / 2 - 100, height / 2 + 10, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0xAA000000); // option: semi-transparent background
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 50, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

