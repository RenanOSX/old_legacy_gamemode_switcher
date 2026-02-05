package dev.renanosx.oldlegacygmswitcher.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import dev.renanosx.oldlegacygmswitcher.net.PermissionState;

@SuppressWarnings("deprecation")
public class ClientHandler extends Gui {

    private static class GameModeInfo {

        private final String translationKey;
        private final String commandName;
        private final ItemStack icon;

        public GameModeInfo(String translationKey, String commandName, ItemStack icon) {
            this.translationKey = translationKey;
            this.commandName = commandName;
            this.icon = icon;
        }
    }

    public static class GameSwitcherStubGui extends GuiScreen {

        private final ClientHandler handler;

        public GameSwitcherStubGui(ClientHandler handler) {
            this.handler = handler;
        }

        @Override
        public boolean doesGuiPauseGame() {
            return false;
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) {
            if (keyCode == Keyboard.KEY_F4) {
                handler.nextItem();
                return;
            }

            if (keyCode == Keyboard.KEY_ESCAPE) {
                handler.isVisible = false;
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
            }
        }
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(
        "textures/gui/container/gamemode_switcher.png");

    private boolean isVisible = false;
    private int selectedIndex = 0;
    private final List<GameModeInfo> gameModes = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();

    private String lastGameModeName = null;
    private boolean wasF4Down = false;
    private static Field currentGameTypeField = null;

    public ClientHandler() {
        gameModes.add(new GameModeInfo("gameMode.creative", "creative", new ItemStack(Blocks.grass)));
        gameModes.add(new GameModeInfo("gameMode.survival", "survival", new ItemStack(Items.iron_sword)));
        gameModes.add(new GameModeInfo("gameMode.adventure", "adventure", new ItemStack(Items.map)));
    }

    private boolean hasPermission() {
        if (mc.thePlayer == null) {
            return false;
        }

        if (mc.isSingleplayer()) {
            IntegratedServer server = mc.getIntegratedServer();
            if (server == null) {
                return false;
            }
            return server.getConfigurationManager()
                .func_152596_g(mc.thePlayer.getGameProfile());
        }

        return PermissionState.hasServerPermission();
    }

    @SubscribeEvent
    public void onClientTickEv(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && isVisible) {
            handleF3F4Combo();
            if (!Keyboard.isKeyDown(Keyboard.KEY_F3)) {
                applyGameMode();
                isVisible = false;
                mc.setIngameFocus();
            }
        } else if (event.phase == TickEvent.Phase.END) {
            handleF3F4Combo();
        }
    }

    @SubscribeEvent
    public void onGameOverlayEv(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || !isVisible) {
            return;
        }

        ScaledResolution res = event.resolution;
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();

        int renderSlotSize = 26;
        int count = gameModes.size();

        int windowWidth = 125;
        int windowHeight = 75;

        int centerX = width / 2;
        int centerY = height / 2;

        int windowX = centerX - windowWidth / 2;
        int windowY = centerY - windowHeight / 2 - 30;

        int mouseX = Mouse.getX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getY() * height / mc.displayHeight - 1;

        int totalSlotsWidth = count * renderSlotSize + (count - 1) * 5;
        int slotsStartX = centerX - totalSlotsWidth / 2;
        int slotsY = windowY + 27;

        for (int i = 0; i < count; i++) {
            int x = slotsStartX + i * (renderSlotSize) + i * 5;
            if (mouseX >= x && mouseX < x + renderSlotSize && mouseY >= slotsY && mouseY < slotsY + renderSlotSize) {
                selectedIndex = i;
            }
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        mc.getTextureManager()
            .bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        drawTexturedModalRect(windowX, windowY, 0, 0, windowWidth, windowHeight);

        String modeName = I18n.format(gameModes.get(selectedIndex).translationKey);

        drawCenteredString(mc.fontRenderer, modeName, centerX, windowY + 7, 0xFFFFFF);

        for (int i = 0; i < count; i++) {
            int x = slotsStartX + i * (renderSlotSize) + i * 5;
            boolean isSelected = (i == selectedIndex);

            mc.getTextureManager()
                .bindTexture(TEXTURE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            drawTexturedModalRect(x, slotsY, 0, 75, renderSlotSize, renderSlotSize);

            if (isSelected) {
                drawTexturedModalRect(x, slotsY, 26, 75, renderSlotSize, renderSlotSize);
            }

            ItemStack icon = gameModes.get(i).icon;
            RenderHelper.enableGUIStandardItemLighting();
            int iconOffset = (renderSlotSize - 16) / 2;
            RenderItem.getInstance()
                .renderItemAndEffectIntoGUI(
                    mc.fontRenderer,
                    mc.getTextureManager(),
                    icon,
                    x + iconOffset,
                    slotsY + iconOffset);
            RenderHelper.disableStandardItemLighting();
        }

        String selectNext = I18n.format(
            "debug.gamemodes.select_next",
            EnumChatFormatting.AQUA + I18n.format("debug.gamemodes.press_f4") + EnumChatFormatting.RESET + " -");
        drawCenteredString(mc.fontRenderer, selectNext, centerX, windowY + windowHeight - 12, 0xAAAAAA);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private void openMenu() {
        isVisible = true;

        if (mc.playerController != null) {
            String currentMode = getCurrentGameModeName();
            int currentModeIndex = 0;

            for (int i = 0; i < gameModes.size(); i++) {
                if (gameModes.get(i).commandName.equalsIgnoreCase(currentMode)) {
                    currentModeIndex = i;
                    break;
                }
            }

            if (lastGameModeName != null && !lastGameModeName.equalsIgnoreCase(currentMode)) {
                for (int i = 0; i < gameModes.size(); i++) {
                    if (gameModes.get(i).commandName.equalsIgnoreCase(lastGameModeName)) {
                        selectedIndex = i;
                        return;
                    }
                }
            }

            selectedIndex = (currentModeIndex + 1) % gameModes.size();
        }
    }

    private void nextItem() {
        selectedIndex++;
        if (selectedIndex >= gameModes.size()) {
            selectedIndex = 0;
        }
    }

    private void closeMenu() {
        mc.displayGuiScreen(null);
        isVisible = false;
    }

    private void applyGameMode() {
        if (selectedIndex >= 0 && selectedIndex < gameModes.size()) {
            GameModeInfo targetMode = gameModes.get(selectedIndex);

            if (mc.playerController != null) {
                lastGameModeName = getCurrentGameModeName();
            }

            if (lastGameModeName == null || !lastGameModeName.equalsIgnoreCase(targetMode.commandName)) {
                lastGameModeName = targetMode.commandName;
                mc.thePlayer.sendChatMessage("/gamemode " + targetMode.commandName);
                closeMenu();
            }
        }
    }

    private void handleF3F4Combo() {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GameSwitcherStubGui)) {
            return;
        }

        boolean f3Down = Keyboard.isKeyDown(Keyboard.KEY_F3);
        boolean f4Down = Keyboard.isKeyDown(Keyboard.KEY_F4);
        boolean f4Pressed = f3Down && f4Down && !wasF4Down;
        wasF4Down = f4Down;

        if (!f4Pressed) {
            return;
        }

        if (!isVisible) {
            if (!hasPermission()) {
                ChatComponentText textComp = new ChatComponentText(
                    EnumChatFormatting.RED + I18n.format("debug.gamemodes.error"));
                mc.thePlayer.addChatMessage(textComp);
                return;
            }
            openMenu();
            if (!(mc.currentScreen instanceof GameSwitcherStubGui)) {
                mc.displayGuiScreen(new GameSwitcherStubGui(this));
            }
            mc.setIngameNotInFocus();
        } else {
            nextItem();
        }
    }

    private String getCurrentGameModeName() {
        WorldSettings.GameType gameType = getGameTypeFromController();
        if (gameType == null) {
            if (mc.theWorld == null || mc.theWorld.getWorldInfo() == null) {
                return "";
            }
            gameType = mc.theWorld.getWorldInfo()
                .getGameType();
        }
        return gameType == null ? "" : gameType.getName();
    }

    private WorldSettings.GameType getGameTypeFromController() {
        if (mc.playerController == null) {
            return null;
        }

        if (!(mc.playerController instanceof PlayerControllerMP)) {
            return null;
        }

        try {
            if (currentGameTypeField == null) {
                currentGameTypeField = PlayerControllerMP.class.getDeclaredField("currentGameType");
                currentGameTypeField.setAccessible(true);
            }
            Object value = currentGameTypeField.get(mc.playerController);
            if (value instanceof WorldSettings.GameType) {
                return (WorldSettings.GameType) value;
            }
        } catch (ReflectiveOperationException ignored) {
            currentGameTypeField = null;
        }

        return null;
    }
}
