package com.example.justanotherclicker;

import com.example.justanotherclicker.model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private GameState gameState;
    private Label clicksLabel;
    private List<PurchasableUI> purchasableUIs;

    private static final double NANOS_IN_SECOND = 1_000_000_000.0;

    @Override
    public void start(Stage primaryStage) {
        gameState = new GameState();
        purchasableUIs = new ArrayList<>();

        primaryStage.setTitle("just another clicker game");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // title
        Label titleLabel = new Label("just another clicker game");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        root.setTop(titleBox);
        BorderPane.setMargin(titleBox, new Insets(0, 0, 20, 0));

        // click area
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);

        clicksLabel = new Label("clicks: 0");
        clicksLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button clickButton = new Button("click");
        clickButton.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        clickButton.setPrefSize(200, 200);
        // styling
        clickButton.setStyle(
                "-fx-background-radius: 100em; " +
                        "-fx-min-width: 200px; " +
                        "-fx-min-height: 200px; " +
                        "-fx-max-width: 200px; " +
                        "-fx-max-height: 200px;" +
                        "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #388E3C;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-radius: 100em;"
        );
        clickButton.setOnAction(e -> {
            gameState.performManualClick();
            updateUI();
        });

        centerBox.getChildren().addAll(clicksLabel, clickButton);
        root.setCenter(centerBox);

        // "store" which gives you upgrades
        VBox storePane = new VBox(15);
        storePane.setPadding(new Insets(10));
        storePane.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        storePane.setMinWidth(350); // Give store some width

        Label storeTitle = new Label("upgrades");
        storeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        storePane.getChildren().add(storeTitle);

        // initialize purchasable items
        List<Purchasable> items = new ArrayList<>();
        items.add(new AutoClickerItem(10)); // base cost 10
        items.add(new ManualClickUpgrade("cups-o-coffee", "boosts your clicking power.", 50, 1, 1.25)); // Base cost 50, +1 power, 25% cost scaling
        items.add(new ManualClickUpgrade("trip to the gym", "get buff. boosts your clicking power more.", 250, 5, 1.30)); // Base cost 250, +5 power, 30% cost scaling
        items.add(new AutoClickerUpgrade("algorithmic improvements", "boosts your autoclickers.", 100, 1.2, 1.4)); // Base cost 100, x1.2 power, 40% cost scaling
        items.add(new AutoClickerUpgrade("breakthrough in training", "find a breakthrough. massively boosts your autoclickers.", 1000, 1.5, 1.5)); // Base cost 1000, x1.5 power, 50% cost scaling

        for (Purchasable item : items) {
            PurchasableUI pui = new PurchasableUI(item);
            storePane.getChildren().add(pui.getContainer());
            purchasableUIs.add(pui);
        }

        root.setRight(storePane);
        BorderPane.setMargin(storePane, new Insets(0, 0, 0, 20));


        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // loop for the autoclickers
        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                double deltaTime = (now - lastUpdate) / NANOS_IN_SECOND; // time elapsed in seconds
                lastUpdate = now;

                if (gameState.getAutoClickerCount() > 0) {
                    gameState.performAutoClickTick(deltaTime);
                }
                updateUI(); // update ui every frame - includes enabling/disabling buttons
            }
        };
        gameLoop.start();

        updateUI(); // initial ui setup
    }

    private void updateUI() {
        clicksLabel.setText(String.format("clicks: %,d", gameState.getCurrentClicks()));
        clicksLabel.setTooltip(new javafx.scene.control.Tooltip(String.format("manual Click: %.1f\nautoclicks/sec: %.1f",
                gameState.getManualClickPower(), gameState.getAutoClickerTotalPowerPerSecond())));


        for (PurchasableUI pui : purchasableUIs) {
            pui.update();
        }
    }

    // inner class to manage ui for each purchasable item
    private class PurchasableUI {
        private Purchasable item;
        private VBox container;
        private Label nameLabel;
        private Label effectLabel;
        private Label costLabel;
        private Label levelLabel;
        private Button buyButton;

        public PurchasableUI(Purchasable item) {
            this.item = item;

            container = new VBox(5);
            container.setPadding(new Insets(8));
            container.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1))));

            nameLabel = new Label(item.getName());
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            effectLabel = new Label(); // will be updated
            effectLabel.setWrapText(true);

            costLabel = new Label(); // will be updated
            levelLabel = new Label(); // will be updated

            buyButton = new Button("Buy");
            buyButton.setOnAction(e -> {
                item.purchase(gameState);
                updateUI(); // global ui update after purchase
            });

            HBox costAndLevelBox = new HBox(10, costLabel, levelLabel);

            container.getChildren().addAll(nameLabel, effectLabel, costAndLevelBox, buyButton);
            update(); // initial state
        }

        public VBox getContainer() {
            return container;
        }

        public void update() {
            effectLabel.setText(item.getEffectDescription(gameState));
            costLabel.setText(String.format("Cost: %,d", item.getCurrentCost()));
            levelLabel.setText("Lvl: " + item.getLevel());
            buyButton.setDisable(!item.canPlayerAfford(gameState));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}