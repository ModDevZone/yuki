package zone.moddev.patchy.updatecheckers.neoforge;

public record NeoForgeVersion(String id) {
    @Override
    public String toString() {
        return id();
    }
}
