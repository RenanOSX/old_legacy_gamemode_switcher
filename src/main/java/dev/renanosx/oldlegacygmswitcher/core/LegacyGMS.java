package dev.renanosx.oldlegacygmswitcher.core;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dev.renanosx.oldlegacygmswitcher.net.ClientConnectionHandler;
import dev.renanosx.oldlegacygmswitcher.net.NetworkHandler;
import dev.renanosx.oldlegacygmswitcher.net.PermissionSyncHandler;

@Mod(
    modid = LegacyGMS.MODID,
    version = LegacyGMS.VERSION,
    name = LegacyGMS.MODNAME,
    acceptedMinecraftVersions = "[1.7.10]")
public class LegacyGMS {

    public static final String MODID = "oldlegacygmswitcher";
    public static final String MODNAME = "OldLegacyGMSwitcher";
    public static final String VERSION = "1.0.0";

    public static LegacyGMS instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LegacyGMS.instance = this;
        NetworkHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            ClientHandler handler = new ClientHandler();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance()
                .bus()
                .register(handler);
            FMLCommonHandler.instance()
                .bus()
                .register(new ClientConnectionHandler());
        }

        FMLCommonHandler.instance()
            .bus()
            .register(new PermissionSyncHandler());
    }
}
