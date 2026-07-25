package zone.moddev.patchy.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import zone.moddev.patchy.updatecheckers.NotificationChannelType;

public class GuildConfig {

    @JsonProperty("serverName")
    private String serverName;

    @JsonProperty("botControllerRoleId")
    private String botControllerRoleId;

    @JsonProperty("minecraftNewsChannelId")
    private String minecraftNewsChannelId;

    @JsonProperty("blockbenchNewsChannelId")
    private String blockbenchNewsChannelId;

    @JsonProperty("neoForgeNewsChannelId")
    private String neoForgeNewsChannelId;

    @JsonProperty("forgeNewsChannelId")
    private String forgeNewsChannelId;

    @JsonProperty("parchmentNewsChannelId")
    private String parchmentNewsChannelId;

    @JsonProperty("fabricNewsChannelId")
    private String fabricNewsChannelId;

    public GuildConfig() {
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getBotControllerRoleId() {
        return botControllerRoleId;
    }

    public void setBotControllerRoleId(String botControllerRoleId) {
        this.botControllerRoleId = botControllerRoleId;
    }

    public String getMinecraftNewsChannelId() {
        return minecraftNewsChannelId;
    }

    public void setMinecraftNewsChannelId(String minecraftNewsChannelId) {
        this.minecraftNewsChannelId = minecraftNewsChannelId;
    }

    public String getBlockbenchNewsChannelId() {
        return blockbenchNewsChannelId;
    }

    public void setBlockbenchNewsChannelId(String blockbenchNewsChannelId) {
        this.blockbenchNewsChannelId = blockbenchNewsChannelId;
    }

    public String getNeoForgeNewsChannelId() {
        return neoForgeNewsChannelId;
    }

    public void setNeoForgeNewsChannelId(String neoForgeNewsChannelId) {
        this.neoForgeNewsChannelId = neoForgeNewsChannelId;
    }

    public String getForgeNewsChannelId() {
        return forgeNewsChannelId;
    }

    public void setForgeNewsChannelId(String forgeNewsChannelId) {
        this.forgeNewsChannelId = forgeNewsChannelId;
    }

    public String getParchmentNewsChannelId() {
        return parchmentNewsChannelId;
    }

    public void setParchmentNewsChannelId(String parchmentNewsChannelId) {
        this.parchmentNewsChannelId = parchmentNewsChannelId;
    }

    public String getFabricNewsChannelId() {
        return fabricNewsChannelId;
    }

    public void setFabricNewsChannelId(String fabricNewsChannelId) {
        this.fabricNewsChannelId = fabricNewsChannelId;
    }

    @Nullable
    public String getChannelId(NotificationChannelType type) {
        return switch (type) {
            case MINECRAFT -> getMinecraftNewsChannelId();
            case BLOCKBENCH -> getBlockbenchNewsChannelId();
            case NEOFORGE -> getNeoForgeNewsChannelId();
            case FORGE -> getForgeNewsChannelId();
            case PARCHMENT -> getParchmentNewsChannelId();
            case FABRIC -> getFabricNewsChannelId();
        };
    }
}
