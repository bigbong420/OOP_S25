package com.example.justanotherclicker.model;

public abstract class Purchasable {
    protected String name;
    protected String descriptionFormat; // ex: "cost: %d, level: %d. effect: +%.1f power"
    protected long baseCost;
    protected long currentCost;
    protected int level;
    protected double costScalingFactor;

    public Purchasable(String name, String descriptionFormat, long baseCost, double costScalingFactor) {
        this.name = name;
        this.descriptionFormat = descriptionFormat;
        this.baseCost = baseCost;
        this.currentCost = baseCost;
        this.level = 0;
        this.costScalingFactor = costScalingFactor;
    }

    public String getName() {
        return name;
    }

    public long getCurrentCost() {
        return currentCost;
    }

    public int getLevel() {
        return level;
    }

    public abstract String getEffectDescription(GameState gameState); // to show current effect

    public void increaseLevelAndCost() {
        this.level++;
        this.currentCost = (long) (this.baseCost * Math.pow(this.costScalingFactor, this.level));
        if (this.currentCost <= 0) this.currentCost = Long.MAX_VALUE / 1000; // prevent overflow issues with max_value
    }

    // polymorphic method
    public abstract void purchase(GameState gameState);

    // common method for UI to check
    public boolean canPlayerAfford(GameState gameState) {
        return gameState.canAfford(currentCost);
    }
}