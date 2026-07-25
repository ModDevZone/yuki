package zone.moddev.patchy.updatecheckers.fabric.api;

import org.jetbrains.annotations.Nullable;

public record FabricApiVersion(String fullVersion, String apiPart, String mcPart) {

    /**
     * Parses a Fabric API version string (e.g., "0.87.0+1.20.1") into its components.
     *
     * @param fullVersion The full version string.
     * @return A {@link FabricApiVersion} record, or {@code null} if the string is not in the expected format.
     */
    @Nullable
    public static FabricApiVersion fromString(@Nullable String fullVersion) {
        if (fullVersion == null) {
            return null;
        }
        final String[] parts = fullVersion.split("\\+");
        if (parts.length != 2) {
            // Not a valid Fabric API version string, which must contain a "+"
            return null;
        }
        return new FabricApiVersion(fullVersion, parts[0], parts[1]);
    }
}
