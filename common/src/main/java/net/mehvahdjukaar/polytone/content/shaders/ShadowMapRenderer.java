package net.mehvahdjukaar.polytone.content.shaders;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Camera;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

/** Shadow rendering is disabled until its render-pass API is ported to 26.3. */
public class ShadowMapRenderer {
    public void setSettings(ShadowMapSettings settings) {
    }

    @Nullable
    public GpuTextureView getShadowTexture() {
        return null;
    }

    @Nullable
    public GpuBufferSlice getUniformsSlice() {
        return null;
    }

    public void renderShadowPassIfNeeded(GpuBufferSlice shaderFog, Camera camera,
                                         Matrix4fc cameraFrustumMatrix, Matrix4f cameraProjectionMatrix) {
    }

    public void close() {
    }
}
