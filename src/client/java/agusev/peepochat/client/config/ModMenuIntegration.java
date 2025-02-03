package agusev.peepochat.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import java.util.Arrays;
import java.util.ArrayList;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("peepochat.config.title"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // General category
            ConfigCategory general = builder.getOrCreateCategory(Text.translatable("peepochat.config.category.general"));
            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("peepochat.config.option.enable_filter"),
                            PeepochatConfig.getInstance().enableFilter)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> PeepochatConfig.getInstance().enableFilter = newValue)
                    .build()
            );

            // Friends category
            ConfigCategory friends = builder.getOrCreateCategory(Text.translatable("peepochat.config.category.friends"));

            friends.addEntry(entryBuilder.startStrList(
                            Text.translatable("peepochat.config.option.friend_list"),
                            PeepochatConfig.getInstance().friendList)
                    .setDefaultValue(() -> new ArrayList<>(Arrays.asList("PWGoood")))
                    .setTooltip(Text.translatable("peepochat.config.option.friend_list.tooltip"))
                    .setSaveConsumer(newValue -> PeepochatConfig.getInstance().friendList = newValue)
                    .build()
            );

            // Debug category
            ConfigCategory debug = builder.getOrCreateCategory(Text.translatable("peepochat.config.category.debug"));
            debug.addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("peepochat.config.option.enable_debug"),
                            PeepochatConfig.getInstance().enableDebug)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> PeepochatConfig.getInstance().enableDebug = newValue)
                    .build()
            );

            // Debug options (only visible when debug is enabled)
            if (PeepochatConfig.getInstance().enableDebug) {
                debug.addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("peepochat.config.option.show_raw_json"),
                                PeepochatConfig.getInstance().showRawJson)
                        .setDefaultValue(false)
                        .setSaveConsumer(newValue -> PeepochatConfig.getInstance().showRawJson = newValue)
                        .build()
                );
            }

            builder.setSavingRunnable(PeepochatConfig.getInstance()::save);

            return builder.build();
        };
    }
}