package dev.example.breedstatus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public final class BreedStatusClient implements ClientModInitializer {
    private static KeyBinding toggleKey;
    private static boolean enabled = true;
    private static final int RANGE_BLOCKS = 24;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.breed_status.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KeyBinding.Category.MISC
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.sendMessage(Text.translatable(
                            enabled ? "message.breed_status.enabled" : "message.breed_status.disabled"
                    ).formatted(enabled ? Formatting.GREEN : Formatting.RED), true);
                }
            }
        });
    }

    public static boolean isEnabled() { return enabled; }
    public static int getRange() { return RANGE_BLOCKS; }

    public static Status status(Entity entity, World world) {
        if (!(entity instanceof AnimalEntity animal)) return Status.UNSUPPORTED;

        // A positive breeding age is the vanilla post-breeding cooldown.
        // A negative age means the animal is a baby.
        int age = animal.getBreedingAge();
        if (age > 0) return Status.COOLDOWN;
        if (age < 0) return Status.NOT_READY;

        var player = net.minecraft.client.MinecraftClient.getInstance().player;
        if (player == null) return Status.NOT_READY;
        boolean hasFood = animal.isBreedingItem(player.getMainHandStack())
                || animal.isBreedingItem(player.getOffHandStack());
        return hasFood ? Status.READY : Status.NOT_READY;
    }

    public enum Status {
        READY(Formatting.GREEN, "G"),
        COOLDOWN(Formatting.GOLD, "C"),
        NOT_READY(Formatting.RED, "—"),
        UNSUPPORTED(Formatting.GRAY, "");

        public final Formatting color;
        public final String glyph;
        Status(Formatting color, String glyph) { this.color = color; this.glyph = glyph; }
    }
}
