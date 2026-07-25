package zone.moddev.patchy.updatecheckers.parchment;

import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zone.moddev.patchy.updatecheckers.AbstractUpdateChecker;
import zone.moddev.patchy.updatecheckers.UpdateCheckerType;
import zone.moddev.patchy.updatecheckers.parchment.ParchmentVersionHelper.ParchmentVersion;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class ParchmentUpdateChecker extends AbstractUpdateChecker<ParchmentVersion> {

    public ParchmentUpdateChecker() {
        super(ParchmentVersion.class, NotifierConfiguration.<ParchmentVersion>builder(UpdateCheckerType.PARCHMENT)
                .versionComparator(Comparator.comparing(ParchmentVersion::timestamp))
                .versionKeyExtractor(ParchmentVersion::parchmentVersion)
                .webhookInfo(new WebhookInfo("Parchment Updates", "https://github.com/parchmentmc.png"))
                .build());
    }

    @Override
    protected List<String> getUpdateKeys() throws IOException {
        return ParchmentVersionHelper.getVersionKeys();
    }

    @Override
    protected Map<String, ParchmentVersion> fetchLatest() throws IOException {
        return ParchmentVersionHelper.latestByMcRelease();
    }

    @NotNull
    @Override
    protected List<EmbedBuilder> getEmbeds(String key, @Nullable final ParchmentVersion oldVersion, final @NotNull ParchmentVersion newVersion) {
        return List.of(new EmbedBuilder()
                .setColor(0xFF0000)
                .setTitle("New %s Parchment Version is Available!".formatted(newVersion.mcVersion()))
                .addField("Version", newVersion.parchmentVersion(), false)
                .addField("Coordinate", "`org.parchmentmc.data:parchment-%s:%s`".formatted(newVersion.mcVersion(), newVersion.parchmentVersion()), false));
    }
}
