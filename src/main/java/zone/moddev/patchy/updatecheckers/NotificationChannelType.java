package zone.moddev.patchy.updatecheckers;

public enum NotificationChannelType {
    MINECRAFT("Minecraft"),
    BLOCKBENCH("BlockBench"),
    NEOFORGE("NeoForge"),
    FORGE("Forge"),
    PARCHMENT("Parchment"),
    FABRIC("Fabric");

    private final String name;

    NotificationChannelType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
