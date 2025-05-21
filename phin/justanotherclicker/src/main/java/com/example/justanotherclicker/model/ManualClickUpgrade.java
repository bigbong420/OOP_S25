package com.example.justanotherclicker.model;

public class ManualClickUpgrade extends Purchasable {
    private double powerIncreaseAbsolute; // how much absolute power this upgrade adds per purchase

    public ManualClickUpgrade(String name, String description, long baseCost, double powerIncreaseAbsolute, double costScaling) {
        super(name, description, baseCost, costScaling);
        this.powerIncreaseAbsolute = powerIncreaseAbsolute;
    }

    @Override
    public void purchase(GameState gameState) {
        if (gameState.canAfford(currentCost)) {
            gameState.spendClicks(currentCost);
            gameState.addAbsoluteManualClickPower(powerIncreaseAbsolute);
            increaseLevelAndCost();
        }
    }

    @Override
    public String getEffectDescription(GameState gameState) {
        return String.format("current manual click power: %.1f. next: +%.1f power.",
                gameState.getManualClickPower(), powerIncreaseAbsolute);
    }
}