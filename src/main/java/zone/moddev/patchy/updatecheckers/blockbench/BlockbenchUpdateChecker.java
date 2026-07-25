package zone.moddev.patchy.updatecheckers.blockbench;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zone.moddev.patchy.updatecheckers.AbstractUpdateChecker;
import zone.moddev.patchy.updatecheckers.SharedVersionHelpers;
import zone.moddev.patchy.updatecheckers.UpdateCheckerType;
import zone.moddev.patchy.util.NetworkUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockbenchUpdateChecker extends AbstractUpdateChecker.Single<GithubRelease> {

    public BlockbenchUpdateChecker() {
        super(GithubRelease.class, AbstractUpdateChecker.NotifierConfiguration.<GithubRelease>builder(UpdateCheckerType.BLOCKBENCH)
                .versionComparator(Comparator.comparing(release -> Instant.parse(release.published_at())))
                .versionKeyExtractor(GithubRelease::tag_name)
                .webhookInfo(new AbstractUpdateChecker.WebhookInfo("Blockbench Updates", "https://www.blockbench.net/favicon.png"))
                .build());
    }

    @Nullable
    @Override
    protected GithubRelease fetchLatestSingle() throws IOException {
        return BlockbenchVersionHelper.getLatest(loggingMarker);
    }

    @NotNull
    @Override
    protected List<EmbedBuilder> getEmbedsSingle(@Nullable final GithubRelease oldVersion, final @NotNull GithubRelease newVersion) {
        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("New Blockbench %s: %s".formatted(newVersion.prerelease() ? "pre-release" : "release", newVersion.name()))
                .setColor(newVersion.prerelease() ? 0x29CFD8 : 0x1E93D9)
                .setDescription(SharedVersionHelpers.truncate(Stream.of(newVersion.body().split("\n"))
                        .map(str -> str.trim().startsWith("#") ? "**" + str.replace("#", "") + "**" : str)
                        .collect(Collectors.joining("\n")), MessageEmbed.DESCRIPTION_MAX_LENGTH / 2));

        if (NetworkUtils.isValidUrl(newVersion.html_url())) {
            embed.setUrl(newVersion.html_url());
        }

        return List.of(embed);
    }
}
