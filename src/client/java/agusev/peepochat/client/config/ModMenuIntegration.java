package agusev.peepochat.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Objects;

import static agusev.peepochat.client.PaintDirectMessage.PaintText;
import static agusev.peepochat.client.config.GradientTextExample.interpolateColor;

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

            // Direct messages category
            ConfigCategory direct_messages = builder.getOrCreateCategory(Text.translatable("peepochat.config.category.direct_messages"));

            direct_messages.addEntry(entryBuilder.startSelector(
                            Text.translatable("peepochat.config.option.color_scheme"),
                            new String[]{
                                    "peepochat.config.option.color_scheme.2_colors",
                                    "peepochat.config.option.color_scheme.gradient"
                            },
                            PeepochatConfig.getInstance().selectedOption)
                    .setDefaultValue("peepochat.config.option.color_scheme.2_colors")
                    .setSaveConsumer(newValue -> {
                        PeepochatConfig.getInstance().selectedOption = newValue;
                        // Здесь можно добавить обновление примера при изменении опции
                    })
                    .build()
            );

            direct_messages.addEntry(entryBuilder.startColorField(
                            Text.translatable("peepochat.config.option.custom_color1"),
                            PeepochatConfig.getInstance().customColor1)
                    .setDefaultValue(0x67E8F9)
                    .setSaveConsumer(newValue -> {
                        PeepochatConfig.getInstance().customColor1 = newValue;
                        // Здесь можно добавить обновление примера при изменении цвета
                    })
                    .build()
            );

            direct_messages.addEntry(entryBuilder.startColorField(
                            Text.translatable("peepochat.config.option.custom_color2"),
                            PeepochatConfig.getInstance().customColor2)
                    .setDefaultValue(0x22D3EE)
                    .setSaveConsumer(newValue -> {
                        PeepochatConfig.getInstance().customColor2 = newValue;
                        // Здесь можно добавить обновление примера при изменении цвета
                    })
                    .build()
            );
            
            Text exampleText = PaintText(false, "PWGoood", "Привет пугод когда новое видео", PeepochatConfig.getInstance().customColor1, PeepochatConfig.getInstance().customColor2, Objects.equals(PeepochatConfig.getInstance().selectedOption, "peepochat.config.option.color_scheme.2_colors"));

            direct_messages.addEntry(entryBuilder.startTextDescription(exampleText)
                    .build());

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

            builder.setSavingRunnable(PeepochatConfig.getInstance()::save);

            return builder.build();
        };
    }
}