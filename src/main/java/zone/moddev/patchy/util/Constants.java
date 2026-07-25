package zone.moddev.patchy.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import zone.moddev.patchy.util.serialization.InstantAdapter;

import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.function.Function;

public class Constants {

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static <T> HttpResponse.BodyHandler<T> ofGson(Gson gson, TypeToken<T> token) {
        return responseInfo -> HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofInputStream(),
                rethrowFunction(stream -> {
                    final InputStreamReader reader = new InputStreamReader(stream);
                    return gson.fromJson(reader, token);
                })
        );
    }

    @FunctionalInterface
    private interface ThrowingFunction<T, R> {
        R apply(T t) throws Exception;
    }

    private static <T, R> Function<T, R> rethrowFunction(ThrowingFunction<T, R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
