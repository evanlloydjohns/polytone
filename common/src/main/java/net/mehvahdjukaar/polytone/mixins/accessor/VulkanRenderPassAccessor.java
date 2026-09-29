package net.mehvahdjukaar.polytone.mixins.accessor;

import com.mojang.renderpearl.backend.vulkan.VulkanRenderPass;
import com.mojang.renderpearl.backend.vulkan.VulkanRenderPipeline;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VulkanRenderPass.class)
public interface VulkanRenderPassAccessor {
    @Accessor("pipeline")
    @Nullable
    VulkanRenderPipeline polytone$getPipeline();
}
