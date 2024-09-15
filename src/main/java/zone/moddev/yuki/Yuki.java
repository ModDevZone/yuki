package zone.moddev.yuki;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.sqlobject.customizer.TimestampedConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.util.Set;

public class Yuki {

    /**
     * Where needed for events being fired, errors and other misc stuff, log things to console using this.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(Yuki.class);

    private static final Set<GatewayIntent> INTENTS = Set.of(
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_MODERATION,
            GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS);

    private static Config config;

    private static JDA instance;

    private static Jdbi database_jdbi;
    private static SQLiteDataSource database_source;


    public static void main(String[] args) throws InterruptedException {
        LOGGER.info("I am Yuki! Welcome to the winter wonderland!");
        final var configPath = Paths.get("mmdbot_config.toml");
        config = new Config(configPath);

        database_source = new SQLiteDataSource();
        database_source.setUrl("jdbc:sqlite:./data.db");
        database_source.setDatabaseName("YukiDB");
        database_jdbi = Jdbi.create(database_source);
        database_jdbi.installPlugin(new SqlObjectPlugin());
        database_jdbi.getConfig(TimestampedConfig.class).setTimezone(ZoneOffset.UTC);
        Flyway.configure().dataSource(database_source).load().migrate();

        instance = JDABuilder
                .create(config.getToken(), INTENTS)
                .disableCache(CacheFlag.VOICE_STATE)
                .disableCache(CacheFlag.ACTIVITY)
                .disableCache(CacheFlag.CLIENT_STATUS)
                .disableCache(CacheFlag.ONLINE_STATUS)
                .build().awaitReady();


        System.exit(1);
    }
}
