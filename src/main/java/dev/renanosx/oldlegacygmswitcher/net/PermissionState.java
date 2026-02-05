package dev.renanosx.oldlegacygmswitcher.net;

public final class PermissionState {

    private static volatile boolean hasServerPermission = false;

    private PermissionState() {}

    public static boolean hasServerPermission() {
        return hasServerPermission;
    }

    public static void setHasServerPermission(boolean allowed) {
        hasServerPermission = allowed;
    }

    public static void reset() {
        hasServerPermission = false;
    }
}
