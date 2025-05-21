package com.example.justanotherclicker.model;

public class AutoClickerItem extends Purchasable {

    public AutoClickerItem(long baseCost) {
        super("autoclicker", "adds one passive click generator.", baseCost, 1.15); // per level increase by x1.15, or 15%
    }

    @Override
    public void purchase(GameState gameState) {
        if (gameState.canAfford(currentCost)) {
            gameState.spendClicks(currentCost);
            gameState.addAutoClicker();
            increaseLevelAndCost();
        }
    }

    @Override
    public String getEffectDescription(GameState gameState) {
        return String.format("it clicks for you. gives %.1f clicks/sec each. you own: %d.",
                gameState.getIndividualAutoClickerPower(), gameState.getAutoClickerCount());
    }
}