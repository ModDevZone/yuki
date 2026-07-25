package zone.moddev.patchy.updatecheckers.parchment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import zone.moddev.patchy.updatecheckers.minecraft.MinecraftVersionHelper;
import zone.moddev.patchy.util.NetworkUtils;
import zone.moddev.patchy.util.SemVer;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ParchmentVersionHelper {

    private ParchmentVersionHelper() {
        // Prevent instantiation
    }

    public static final String METADATA_URL = "https://maven.parchmentmc.org/org/parchmentmc/data/parchment-%s/maven-metadata.xml";
    public static final SemVer INITIAL_VERSION = SemVer.from("1.16.5");

    private static final DateTimeFormatter PARCHMENT_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final Logger LOGGER = LoggerFactory.getLogger(ParchmentVersionHelper.class);

    public static List<String> getVersionKeys() throws IOException {
        final var meta = MinecraftVersionHelper.getMeta();
        if (meta == null) return List.of();

        return meta.versions.stream()
                .filter(it -> it.type().equals("release"))
                .map(v -> SemVer.from(v.id()))
                .filter(v -> v.compareTo(INITIAL_VERSION) >= 0)
                .map(SemVer::toString)
                .toList();
    }

    public static Map<String, ParchmentVersion> latestByMcRelease() throws IOException {
        final var meta = MinecraftVersionHelper.getMeta();
        if (meta == null) return Map.of();

        final var parser = DocumentBuilderFactory.newInstance();
        return meta.versions.stream()
                .filter(it -> it.type().equals("release"))
                .map(v -> SemVer.from(v.id()))
                .filter(v -> v.compareTo(INITIAL_VERSION) >= 0)
                .map(mcVersion -> {
                    try (final var is = NetworkUtils.readUrl(METADATA_URL.formatted(mcVersion))) {
                        final var xml = parser.newDocumentBuilder().parse(is);
                        xml.getDocumentElement().normalize();
                        final var latestVersion = ((Element) ((Element) (xml.getElementsByTagName("metadata").item(0)))
                                .getElementsByTagName("versioning").item(0))
                                .getElementsByTagName("release").item(0)
                                .getTextContent();

                        return ParchmentVersion.of(mcVersion.toString(), latestVersion);
                    } catch (Exception e) {
                        LOGGER.debug("Unable to look up parchment versions for MC {}", mcVersion, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableMap(ParchmentVersion::mcVersion, Function.identity()));
    }

    public record ParchmentVersion(String mcVersion, String parchmentVersion, Instant timestamp) {
        public static ParchmentVersion of(String mcVersion, String parchmentVersion) {
            var date = LocalDate.parse(parchmentVersion, PARCHMENT_FORMAT).atStartOfDay(ZoneId.of("UTC"));
            return new ParchmentVersion(mcVersion, parchmentVersion, date.toInstant());
        }
    }
}
