package zone.moddev.patchy.updatecheckers;

public enum UpdateCheckerType {
    MINECRAFT("minecraft", NotificationChannelType.MINECRAFT),
    BLOCKBENCH("blockbench", NotificationChannelType.BLOCKBENCH),
    NEOFORGE("neoforge", NotificationChannelType.NEOFORGE),
    FORGE("forge", NotificationChannelType.FORGE),
    PARCHMENT("parchment", NotificationChannelType.PARCHMENT),
    FABRIC_LOADER("fabric_loader", NotificationChannelType.FABRIC),
    FABRIC_API("fabric_api", NotificationChannelType.FABRIC);

    private final String name;
    private final NotificationChannelType channelType;

    UpdateCheckerType(String name, NotificationChannelType channelType) {
        this.name = name;
        this.channelType = channelType;
    }

    String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public NotificationChannelType getChannelType() {
        return channelType;
    }
}
