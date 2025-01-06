package agusev.peepochat.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("peepochat.config.title"));

            ConfigCategory general = builder.getOrCreateCategory(Text.translatable("peepochat.config.category.general"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("peepochat.config.option.enable_filter"),
                            PeepochatConfig.getInstance().enableFilter)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> PeepochatConfig.getInstance().enableFilter = newValue)
                    .build()
            );

            builder.setSavingRunnable(PeepochatConfig.getInstance()::save);

            return builder.build();
        };
    }
}