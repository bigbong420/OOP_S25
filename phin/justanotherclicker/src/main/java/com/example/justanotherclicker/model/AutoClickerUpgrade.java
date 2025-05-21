package com.example.justanotherclicker.model;

public class AutoClickerUpgrade extends Purchasable {
    private double powerMultiplier; // how much this upgrade multiplies existing power by

    public AutoClickerUpgrade(String name, String description, long baseCost, double powerMultiplier, double costScaling) {
        super(name, description, baseCost, costScaling);
        this.powerMultiplier = powerMultiplier; // ex: 1.2 for a 20% boost
    }

    @Override
    public void purchase(GameState gameState) {
        if (gameState.canAfford(currentCost) && gameState.getAutoClickerCount() > 0) {
            gameState.spendClicks(currentCost);
            gameState.upgradeAutoClickerPower(powerMultiplier); // multiplies total power per autoclicker
            increaseLevelAndCost();
        }
    }

    @Override
    public boolean canPlayerAfford(GameState gameState) {
        // also check if there are any autoclickers to upgrade
        return super.canPlayerAfford(gameState) && gameState.getAutoClickerCount() > 0;
    }

    @Override
    public String getEffectDescription(GameState gameState) {
        if (gameState.getAutoClickerCount() == 0) {
            return "you need an autoclicker first.";
        }
        return String.format("boosts each autoclicker by x%.2f. current multiplier/unit: %.1f.",
                powerMultiplier, gameState.getIndividualAutoClickerPower());
    }
}