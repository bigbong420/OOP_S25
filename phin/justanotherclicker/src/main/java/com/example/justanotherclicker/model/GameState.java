package com.example.justanotherclicker.model;

public class GameState {
    private long currentClicks;
    private int autoClickerCount;
    private double manualClickPower; // clicks per manual click
    private double autoClickerPower; // clicks per individual auto-clicker per second

    private static final int BASE_MANUAL_CLICK_POWER = 1;
    private static final double BASE_AUTO_CLICKER_POWER_PER_UNIT = 1.0; // each autoclicker generates this much base power

    // Accumulator for fractional autoclicks
    private double fractionalAutoClicksAccumulator;

    public GameState() {
        this.currentClicks = 0;
        this.autoClickerCount = 0;
        this.manualClickPower = BASE_MANUAL_CLICK_POWER;
        this.autoClickerPower = 0; // initially 0 because no auto-lickers
        this.fractionalAutoClicksAccumulator = 0.0;
    }

    // Getters
    public long getCurrentClicks() {
        return currentClicks;
    }

    public int getAutoClickerCount() {
        return autoClickerCount;
    }

    public double getManualClickPower() {
        return manualClickPower;
    }

    public double getAutoClickerTotalPowerPerSecond() {
        return autoClickerCount * autoClickerPower;
    }

    public double getIndividualAutoClickerPower() {
        if (autoClickerCount == 0 && autoClickerPower == 0) {
            return BASE_AUTO_CLICKER_POWER_PER_UNIT; // display what new autoclickers would generate
        }
        return autoClickerPower;
    }

    // Mutators / Actions
    public void performManualClick() {
        this.currentClicks += (long) manualClickPower;
    }

    public void performAutoClickTick(double deltaTimeSeconds) {
        if (autoClickerCount > 0 && autoClickerPower > 0) {
            double clicksGeneratedThisTick = getAutoClickerTotalPowerPerSecond() * deltaTimeSeconds;
            this.fractionalAutoClicksAccumulator += clicksGeneratedThisTick;

            if (this.fractionalAutoClicksAccumulator >= 1.0) {
                long wholeClicksToAdd = (long) this.fractionalAutoClicksAccumulator;
                this.currentClicks += wholeClicksToAdd;
                this.fractionalAutoClicksAccumulator -= wholeClicksToAdd; // keep the remaining fraction
            }
        }
    }

    public boolean canAfford(long cost) {
        return this.currentClicks >= cost;
    }

    public void spendClicks(long cost) {
        if (canAfford(cost)) {
            this.currentClicks -= cost;
        } else {
            System.err.println("error: attempted to spend more clicks than available");
        }
    }

    public void addAutoClicker() {
        this.autoClickerCount++;
        if (this.autoClickerPower == 0 && this.autoClickerCount > 0) {
            this.autoClickerPower = BASE_AUTO_CLICKER_POWER_PER_UNIT;
        }
    }

    public void upgradeManualClickPower(double multiplier) {
        this.manualClickPower *= multiplier;
    }

    public void addAbsoluteManualClickPower(double amount) {
        this.manualClickPower += amount;
    }

    public void upgradeAutoClickerPower(double multiplier) {
        if (this.autoClickerCount > 0) {
            // ensure base power is set if it was somehow 0
            if (this.autoClickerPower == 0) {
                this.autoClickerPower = BASE_AUTO_CLICKER_POWER_PER_UNIT;
            }
            this.autoClickerPower *= multiplier;
        }
    }

    public void addAbsoluteAutoClickerPower(double amount) {
        // this method might not be used by current upgrades but is good to have
        if (this.autoClickerCount > 0) {
            if (this.autoClickerPower == 0) {
                this.autoClickerPower = BASE_AUTO_CLICKER_POWER_PER_UNIT;
            }
            this.autoClickerPower += amount; // adds to the power per autoclicker
        }
    }
}