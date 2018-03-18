package com.thelostnomad.tone.entities.nature_sprite;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;

public class RenderNatureSprite extends RenderLiving<NatureSpriteEntity> {

    private ResourceLocation mobTexture = new ResourceLocation(ThingsOfNaturalEnergies.MODID + ":textures/entity/nature_sprite.png");

    public static final Factory FACTORY = new Factory();

    public RenderNatureSprite(RenderManager rendermanagerIn) {
        // We use the vanilla zombie model here and we simply
        // retexture it. Of course you can make your own model
        super(rendermanagerIn, new ModelNatureSprite(), 0.5F);
    }

    protected void preRenderCallback(NatureSpriteEntity entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.25F, 0.25F, 0.25F);
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull NatureSpriteEntity entity) {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<NatureSpriteEntity> {

        @Override
        public Render<? super NatureSpriteEntity> createRenderFor(RenderManager manager) {
            return new RenderNatureSprite(manager);
        }

    }

}