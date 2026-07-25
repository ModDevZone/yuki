package zone.moddev.patchy.configs;

import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import zone.moddev.patchy.Patchy;

import java.io.IOException;

public class GuildConfigListener extends ListenerAdapter {

    private final ConfigManager configManager;

    public GuildConfigListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void onReady(ReadyEvent event) {
        Patchy.LOGGER.info("Checking for and loading configs for all guilds...");
        event.getJDA().getGuilds().forEach(guild -> {
            try {
                configManager.loadOrCreateGuildConfig(guild);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent event) {
        try {
            GuildConfig config = configManager.loadOrCreateGuildConfig(event.getGuild());
            config.setServerName(event.getNewName());
            configManager.saveGuildConfig(event.getGuild().getId(), config);
        } catch (IOException e) {
            Patchy.LOGGER.error("Failed to update server name for guild {}", event.getGuild().getId(), e);
        }
    }
}
