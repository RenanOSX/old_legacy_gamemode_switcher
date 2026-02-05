package dev.renanosx.oldlegacygmswitcher.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class OpPermissionMessage implements IMessage {

    private boolean allowed;

    public OpPermissionMessage() {}

    public OpPermissionMessage(boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        allowed = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(allowed);
    }

    public static class Handler implements IMessageHandler<OpPermissionMessage, IMessage> {

        @Override
        public IMessage onMessage(OpPermissionMessage message, MessageContext ctx) {
            PermissionState.setHasServerPermission(message.allowed);
            return null;
        }
    }
}
