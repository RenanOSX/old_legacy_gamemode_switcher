package dev.beecube31.legacygmswitcher.core;

import dev.beecube31.legacygmswitcher.Tags;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
		modid = Tags.MODID,
		version = Tags.VERSION,
		name = Tags.MODNAME,
        acceptedMinecraftVersions = "[1.10,1.13)"
)
public class LegacyGMS {
	public static LegacyGMS instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LegacyGMS.instance = this;
	}

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new ClientHandler());
        }
    }
}