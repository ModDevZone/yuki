package zone.moddev.yuki;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.google.common.io.Resources;
import com.jagrosh.jdautilities.commons.utils.SafeIdUtil;
import net.dv8tion.jda.api.entities.Activity;

import java.nio.file.Path;
import java.util.Optional;

public class Config {


    private final CommentedFileConfig config;
    private boolean newlyGenerated;

    public Config(final Path configFile) {
        this.newlyGenerated = false;
        this.config = CommentedFileConfig.builder(configFile, TomlFormat.instance())
                .autoreload()
                .onFileNotFound((file, format) -> {
                    this.newlyGenerated = true;
                    return FileNotFoundAction.copyData(Resources.getResource("default-config.toml"))
                            .run(file, format);
                })
                .preserveInsertionOrder()
                .build();
        config.load();
    }


    public boolean isNewlyGenerated() {
        return newlyGenerated;
    }

    public CommentedFileConfig getConfig() {
        return config;
    }

    public String getToken() {
        return config.<String>getOptional("bot.token")
                .filter(string -> string.indexOf('!') == -1 || string.isEmpty())
                .orElse("");
    }

    public long getGuildID() {
        return SafeIdUtil.safeConvert(getAliased("bot.guildId", getAliases()));
    }

    private String getAliased(final String key, final Optional<UnmodifiableConfig> aliases) {
        return config.<String>getOptional(key)
                .map(str -> aliases.flatMap(cfg -> cfg.<String>getOptional(str)).orElse(str))
                .orElse("");
    }

    public Optional<UnmodifiableConfig> getAliases() {
        return config.getOptional("aliases");
    }

    public Activity.ActivityType getActivityType() {
        return Activity.ActivityType.valueOf(config.getOrElse("bot.activity.type", "PLAYING"));
    }

    public String getActivityName() {
        return config.getOrElse("bot.activity.name", "");
    }
}
