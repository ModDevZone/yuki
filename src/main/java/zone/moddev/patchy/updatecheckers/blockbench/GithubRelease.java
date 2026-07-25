package zone.moddev.patchy.updatecheckers.blockbench;

public record GithubRelease(String html_url, String name, boolean prerelease, String tag_name, String body,
                            String published_at) {
}
