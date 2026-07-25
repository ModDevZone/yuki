package zone.moddev.patchy.util;

public class SelfSerializer implements StringSerializer<String> {
    @Override
    public String serialize(String value) {
        return value;
    }

    @Override
    public String deserialize(String value) {
        return value;
    }
}
