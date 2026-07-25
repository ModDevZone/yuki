package zone.moddev.patchy.configs;

import net.dv8tion.jda.api.entities.Guild;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {

    private final ObjectMapper mapper;
    private final Path CONFIG_DIRECTORY = Paths.get("patchy-bot-configs");
    private final Path GUILD_CONFIG_DIRECTORY = CONFIG_DIRECTORY.resolve("guild-configs");
    private final PatchyConfig patchyConfig;

    public ConfigManager() {
        this.mapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
        try {
            Files.createDirectories(CONFIG_DIRECTORY);
            Files.createDirectories(GUILD_CONFIG_DIRECTORY);
            this.patchyConfig = loadPatchyConfig();
        } catch (IOException exception) {
            throw new RuntimeException("Could not create or load config directory/files: " + exception.getMessage());
        }
    }

    private PatchyConfig loadPatchyConfig() throws IOException {
        Path path = CONFIG_DIRECTORY.resolve("patchy-config.json");
        if (Files.notExists(path)) {
            PatchyConfig defaultConfig = new PatchyConfig();
            mapper.writeValue(path.toFile(), defaultConfig);
            return defaultConfig;
        } else {
            return mapper.readValue(path.toFile(), PatchyConfig.class);
        }
    }

    public PatchyConfig getPatchyConfig() {
        return patchyConfig;
    }

    public GuildConfig loadOrCreateGuildConfig(Guild guild) throws IOException {
        Path path = GUILD_CONFIG_DIRECTORY.resolve(guild.getId() + ".json");
        if (Files.notExists(path)) {
            GuildConfig defaultConfig = new GuildConfig();
            defaultConfig.setServerName(guild.getName());
            mapper.writeValue(path.toFile(), defaultConfig);
            return defaultConfig;
        } else {
            GuildConfig config = mapper.readValue(path.toFile(), GuildConfig.class);
            // Update the server name if it has changed
            if (!guild.getName().equals(config.getServerName())) {
                config.setServerName(guild.getName());
                saveGuildConfig(guild.getId(), config);
            }
            return config;
        }
    }

    public void saveGuildConfig(String guildId, GuildConfig config) throws IOException {
        Path path = GUILD_CONFIG_DIRECTORY.resolve(guildId + ".json");
        mapper.writeValue(path.toFile(), config);
    }
}
