package net.mehvahdjukaar.polytone;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.mehvahdjukaar.polytone.content.particle.custom.render.ModelParticleRenderGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

public class PolytoneRenderTypes {

    public static void init() {
        PlatStuff.registerParticleGroup(e -> e.register(PARTICLE_MODEL_GROUP, ModelParticleRenderGroup::new));
    }

    public static final ParticleRenderType PARTICLE_MODEL_GROUP =
            new ParticleRenderType(Polytone.res("particle_model").toString(), "PM");

    // Minecraft 26.3's shader pipeline is still being ported. Keep particle modes
    // usable with vanilla rendering instead of registering Polytone's 26.2 shaders.
    public static final RenderPipeline ADDITIVE_TRANSLUCENT_PARTICLE_PIPELINE = RenderPipelines.TRANSLUCENT_PARTICLE;
    public static final RenderPipeline ADDITIVE_TRANSLUCENT_BLOCK_PIPELINE = RenderPipelines.TRANSLUCENT_BLOCK;
    public static final RenderType ADDITIVE_TRANSLUCENT_MOVING_BLOCK_RENDERTYPE = RenderTypes.translucentMovingBlock();
    public static final RenderPipeline SKY_DEPTH_WRITE_PIPELINE = RenderPipelines.SKY;
    public static final RenderPipeline LEASH_PIPELINE = RenderPipelines.LEASH;

    // Post chains are disabled in this alpha; there is no safe vanilla equivalent
    // for the depth-combine pass.
    public static final @Nullable RenderPipeline DEPTH_COMBINE_PIPELINE = null;

    public static RenderType getLeashRenderType() {
        return null;
    }

    public static boolean addLeashVertexPair(VertexConsumer builder, Matrix4fc pose,
                                             float dx, float dy, float dz,
                                             float fudge, float dxOff, float dzOff,
                                             int k, boolean backwards, EntityRenderState.LeashState state) {
        return false;
    }
}
