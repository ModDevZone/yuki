package zone.moddev.patchy.updatecheckers.blockbench;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import zone.moddev.patchy.Patchy;
import zone.moddev.patchy.util.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BlockbenchVersionHelper {
    @Nullable
    public static GithubRelease getLatest(final Marker loggingMarker) throws IOException {
        try {
            final HttpResponse<List<GithubRelease>> response = Constants.HTTP_CLIENT.send(HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/repos/JannisX11/blockbench/releases"))
                    .header("Accept", "application/json")
                    .build(), Constants.ofGson(Constants.GSON, new TypeToken<>() {
            }));

            if (response.statusCode() != 200) {
                Patchy.LOGGER.error(loggingMarker, "Server replied with non-200 status code {}.", response.statusCode());
                return null;
            }

            final List<GithubRelease> releases = response.body();
            return releases.isEmpty() ? null : releases.get(0);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
