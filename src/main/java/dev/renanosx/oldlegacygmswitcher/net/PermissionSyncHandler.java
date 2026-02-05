package dev.renanosx.oldlegacygmswitcher.net;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class PermissionSyncHandler {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        boolean allowed = player.canCommandSenderUseCommand(2, "gamemode");
        NetworkHandler.CHANNEL.sendTo(new OpPermissionMessage(allowed), player);
    }
}
