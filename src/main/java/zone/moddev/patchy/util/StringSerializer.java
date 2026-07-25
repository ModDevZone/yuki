package zone.moddev.patchy.util;

public interface StringSerializer<T> {
    String serialize(T value);

    T deserialize(String value);
}
