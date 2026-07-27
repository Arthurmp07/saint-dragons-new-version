package com.leon.saintsdragons.client.ui;

import com.leon.saintsdragons.common.network.MessageDragonSpawnChanceRequest;
import com.leon.saintsdragons.common.network.MessageDragonSpawnChanceSet;
import com.leon.saintsdragons.common.network.NetworkHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Client-only menu that lets a player tune down how often dragons naturally spawn near them.
 */
public final class DragonSpawnChanceScreen extends Screen {
    private static final int SLIDER_WIDTH = 220;
    private static final int SLIDER_HEIGHT = 20;

    private final Screen parent;
    private float pendingMultiplier = 1.0f;
    private boolean receivedServerValue = false;
    private ChanceSlider slider;

    public DragonSpawnChanceScreen(Screen parent) {
        super(Component.translatable("saintsdragons.spawn_chance_screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int centerX = width / 2;
        int sliderY = height / 2 - 10;

        slider = new ChanceSlider(centerX - SLIDER_WIDTH / 2, sliderY, SLIDER_WIDTH, SLIDER_HEIGHT, pendingMultiplier);
        addRenderableWidget(slider);

        addRenderableWidget(Button.builder(Component.translatable("saintsdragons.spawn_chance_screen.confirm"), button -> {
            NetworkHandler.sendToServer(new MessageDragonSpawnChanceSet(pendingMultiplier));
            onClose();
        }).bounds(centerX - 102, sliderY + 32, 100, 20).build());

        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(centerX + 2, sliderY + 32, 100, 20).build());

        NetworkHandler.sendToServer(MessageDragonSpawnChanceRequest.INSTANCE);
    }

    public void applyServerValue(float multiplier) {
        this.pendingMultiplier = multiplier;
        this.receivedServerValue = true;
        if (slider != null) {
            slider.setValueFromMultiplier(multiplier);
        }
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, height / 2 - 40, 0xFFFFFF);

        Component subtitle = receivedServerValue
                ? Component.translatable("saintsdragons.spawn_chance_screen.subtitle")
                : Component.translatable("saintsdragons.spawn_chance_screen.loading");
        graphics.drawCenteredString(font, subtitle, width / 2, height / 2 - 26, 0xA0A0A0);
    }

    private final class ChanceSlider extends AbstractSliderButton {
        ChanceSlider(int x, int y, int width, int height, float initialMultiplier) {
            super(x, y, width, height, Component.empty(), initialMultiplier);
            updateMessage();
        }

        void setValueFromMultiplier(float multiplier) {
            this.value = multiplier;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            int percent = Math.round((float) this.value * 100.0f);
            setMessage(Component.translatable("saintsdragons.spawn_chance_screen.percent", percent));
        }

        @Override
        protected void applyValue() {
            pendingMultiplier = (float) this.value;
        }
    }
}
