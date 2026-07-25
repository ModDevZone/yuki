package zone.moddev.patchy.configs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatchyConfig {

    @JsonProperty
    private String botOwnerId = "<THE_BOT_OWNERS_ID>";

    @JsonProperty
    private String discordToken = "<THE_BOTS_TOKEN>";

    public PatchyConfig() {
    }

    /**
     * Gets the ID for the bots owner, aka the person running the bot.
     *
     * @return the owners user ID.
     */
    public String getBotOwnerId() {
        return botOwnerId;
    }

    /**
     * Gets the api key or token for the bot to connect to Discord services.
     *
     * @return the Discord api key.
     */
    public String getDiscordToken() {
        return discordToken;
    }
}
