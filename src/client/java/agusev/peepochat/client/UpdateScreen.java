package agusev.peepochat.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;

public class UpdateScreen extends Screen {
    private final Screen parent;
    private final VersionChecker.VersionResponse versionResponse;

    protected UpdateScreen(Screen parent, VersionChecker.VersionResponse versionResponse) {
        super(Text.translatable("peepochat.update_screen.title"));
        this.parent = parent;
        this.versionResponse = versionResponse;
    }

    @Override
    protected void init() {
        if (versionResponse.has_update) {
            addDrawableChild(ButtonWidget.builder(Text.translatable("peepochat.update_screen.download"), button -> {
                Util.getOperatingSystem().open(versionResponse.new_version_link);
            }).dimensions(width / 2 - 100, height / 2 + 30, 200, 20).build());
        }

        addDrawableChild(ButtonWidget.builder(Text.translatable("peepochat.update_screen.ignore"), button -> {
            assert client != null;
            client.setScreen(parent);
        }).dimensions(width / 2 - 100, height / 2 + 60, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0xAA000000); // option: semi-transparent background
        super.render(context, mouseX, mouseY, delta);

        // Title
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 60, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
