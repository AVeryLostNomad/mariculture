package com.thelostnomad.tone.block.gui;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.block.container.ContainerKeeper;
import com.thelostnomad.tone.block.container.ContainerPuller;
import com.thelostnomad.tone.block.tileentity.TEKeeper;
import com.thelostnomad.tone.block.tileentity.TEPuller;
import com.thelostnomad.tone.proxy.ClientProxy;
import com.thelostnomad.tone.util.gui.ToneCheckbox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class GuiKeeper extends GuiContainer {

    // This is the resource location for the background image for the GUI
    private static final ResourceLocation texture = new ResourceLocation("thingsofnaturalenergies", "textures/gui/keeper.png");
    private TEKeeper tileEntityKeeper;

    ToneCheckbox includeInInventory;

    public GuiKeeper(InventoryPlayer invPlayer, TEKeeper tile) {
        super(new ContainerKeeper(invPlayer, tile));
        tileEntityKeeper = tile;
        // Set the width and height of the gui.  Should match the size of the texture!
        xSize = 176;
        ySize = 133;
    }

    @Override
    public void initGui() {
        super.initGui();
        List<String> lines = new ArrayList<>();
        lines.add(TextFormatting.ITALIC + "Useful for keeping x amount");
        lines.add(TextFormatting.ITALIC + "of an item crafted. Checked");
        lines.add(TextFormatting.ITALIC + "includes this keeper's stack");
        lines.add(TextFormatting.ITALIC + "in the tree's inventory.");
        includeInInventory = new ToneCheckbox(0, 66 + guiLeft, 6 + guiTop, "Include Result in Inventory?", tileEntityKeeper.isIncludeInInventory(), lines);
        this.addButton(includeInInventory);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        tileEntityKeeper.setIncludeInInventory(includeInInventory.isChecked());
    }

    // draw the background for the GUI - rendered first
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        // Bind the image texture of our custom container
        this.drawDefaultBackground();
        // Draw the image
        this.mc.getTextureManager().bindTexture(texture);
        drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 1.0f, 1.0f, 175, 132, 256, 256);
    }

    // draw the foreground for the GUI - rendered after the slots, but before the dragged items and tooltips
    // renders relative to the top left corner of the background
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        final int LABEL_XPOS = 5;
        final int LABEL_YPOS = 5;
        fontRenderer.drawString("Keeper", LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
        this.renderHoveredToolTip(mouseX - guiLeft, mouseY - guiTop);

        List<String> lines = new ArrayList<>();
        lines.add(includeInInventory.getText());

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            lines.addAll(includeInInventory.getExtraInfo());
        }

        if(includeInInventory.isHovered()){
            this.drawHoveringText(lines, mouseX + 5 - guiLeft, mouseY - 5 - guiTop, fontRenderer);
        }
    }

}
