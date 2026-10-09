package dev.example.breedstatus.mixin;

import dev.example.breedstatus.BreedStatusClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Inject(method = "render", at = @At("TAIL"))
    private void breedStatus$renderMarker(T entity, float yaw, float tickDelta,
                                          MatrixStack matrices, VertexConsumerProvider consumers,
                                          int light, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!BreedStatusClient.isEnabled() || client.player == null || client.world == null) return;
        if (entity == client.player || entity.isRemoved() || !entity.isAlive()) return;
        if (client.player.squaredDistanceTo(entity) > (double) BreedStatusClient.getRange() * BreedStatusClient.getRange()) return;

        BreedStatusClient.Status status = BreedStatusClient.status(entity, client.world);
        if (status == BreedStatusClient.Status.UNSUPPORTED) return;

        Text label = Text.literal(status.glyph).formatted(status.color);
        TextRenderer textRenderer = client.textRenderer;
        matrices.push();
        matrices.translate(0.0, entity.getHeight() + 0.28, 0.0);
        matrices.multiply(client.getEntityRenderDispatcher().getRotation());
        matrices.scale(-0.025F, -0.025F, 0.025F);
        float x = -textRenderer.getWidth(label) / 2.0F;
        textRenderer.draw(label, x, 0, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(),
                consumers, TextRenderer.TextLayerType.NORMAL, 0x55000000, light);
        matrices.pop();
    }
}
