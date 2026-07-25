package zone.moddev.patchy.updatecheckers.minecraft;

import org.jetbrains.annotations.Nullable;
import zone.moddev.patchy.util.Constants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

public final class MinecraftVersionHelper {

    private MinecraftVersionHelper() {
        // Prevent instantiation
    }

    public static final String API_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";

    @Nullable
    public static PistonMeta getMeta() throws IOException {
        try (final var reader = new InputStreamReader(URI.create(API_URL).toURL().openStream())) {
            return Constants.GSON.fromJson(reader, PistonMeta.class);
        }
    }

    public static class PistonMeta {
        public VersionsInfo latest;
        public List<VersionInfo> versions;
    }

    public record VersionsInfo(String release, String snapshot) {
    }

    public record VersionInfo(String id, String type) {
    }
}
