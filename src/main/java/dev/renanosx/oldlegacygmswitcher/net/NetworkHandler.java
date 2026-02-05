package dev.renanosx.oldlegacygmswitcher.net;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public final class NetworkHandler {

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("oldlegacygmswitcher");

    private NetworkHandler() {}

    public static void init() {
        CHANNEL.registerMessage(OpPermissionMessage.Handler.class, OpPermissionMessage.class, 0, Side.CLIENT);
    }
}
