package zone.moddev.patchy.util;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public final class NetworkUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger("Patchy Network Utils");

    private NetworkUtils() {
        // Utility class
    }

    /**
     * Checks if a given URL is valid and reachable (returns HTTP 200 OK).
     * Logs errors for unreachable URLs or non-200 status codes.
     *
     * @param urlString The URL to validate.
     * @return true if the URL is valid and returns HTTP 200 OK, false otherwise.
     */
    public static boolean isValidUrl(String urlString) {
        try {
            final URL url = new URI(urlString).toURL();
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000); // 5 seconds
            connection.setReadTimeout(5000);    // 5 seconds
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                LOGGER.warn("URL validation failed for {}: Received HTTP status code {}", urlString, responseCode);
                return false;
            }
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("URL validation failed for {}: {}", urlString, e.getMessage());
            return false;
        }
    }

    /**
     * Fetches the content of a given URL as a String.
     * Logs errors for unreachable URLs or non-200 status codes.
     *
     * @param urlString The URL to fetch content from.
     * @return The content of the URL as a String if successful and HTTP 200 OK, null otherwise.
     */
    @Nullable
    public static String getUrlContent(String urlString) {
        try (var reader = new InputStreamReader(readUrl(urlString), StandardCharsets.UTF_8)) {
            return reader.readAllAsString();
        } catch (IOException e) {
            LOGGER.error("Failed to get URL content for {}", urlString);
            return null;
        }
    }

    public static InputStream readUrl(String urlString) throws IOException {
        final URL url = URI.create(urlString).toURL();
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); // Use GET to fetch content
        connection.setConnectTimeout(5000); // 5 seconds
        connection.setReadTimeout(5000);    // 5 seconds
        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new ConnectException("Failed to get URL content for %s: Received HTTP status code %s".formatted(urlString, responseCode));
        }

        return new BufferedInputStream(connection.getInputStream());
    }
}
