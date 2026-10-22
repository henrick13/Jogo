import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class JogoJavaFX extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Atributos do jogador
    private double playerX = 400;
    private double playerY = 300;
    private final double playerSize = 30;
    private final double speed = 5;

    // Atributos do coletável (alvo)
    private double targetX;
    private double targetY;
    private final double targetSize = 20;

    private int score = 0;
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private final Random random = new Random();

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        spawnTarget();

        Scene scene = new Scene(new StackPane(canvas), WIDTH, HEIGHT);

        // Registro de teclas pressionadas
        scene.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));

        // Game Loop principal
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render(gc);
            }
        };
        gameLoop.start();

        primaryStage.setTitle("Jogo Simples em JavaFX");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Atualiza a lógica do jogo
    private void update() {
        if (activeKeys.contains(KeyCode.UP) || activeKeys.contains(KeyCode.W)) {
            playerY = Math.max(0, playerY - speed);
        }
        if (activeKeys.contains(KeyCode.DOWN) || activeKeys.contains(KeyCode.S)) {
            playerY = Math.min(HEIGHT - playerSize, playerY + speed);
        }
        if (activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A)) {
            playerX = Math.max(0, playerX - speed);
        }
        if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D)) {
            playerX = Math.min(WIDTH - playerSize, playerX + speed);
        }

        // Checagem de colisão (Bounding Box AABB)
        if (playerX < targetX + targetSize &&
                playerX + playerSize > targetX &&
                playerY < targetY + targetSize &&
                playerY + playerSize > targetY) {
            score++;
            spawnTarget();
        }
    }

    // Reposiciona o coletável em uma posição aleatória
    private void spawnTarget() {
        targetX = random.nextInt(WIDTH - (int) targetSize);
        targetY = random.nextInt(HEIGHT - (int) targetSize);
    }

    // Renderiza os elementos gráficos na tela
    private void render(GraphicsContext gc) {
        // Limpa o fundo
        gc.setFill(Color.web("#1e1e2e"));
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Desenha o jogador
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(playerX, playerY, playerSize, playerSize);

        // Desenha o item
        gc.setFill(Color.CORAL);
        gc.fillOval(targetX, targetY, targetSize, targetSize);

        // Desenha o placar
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 22));
        gc.fillText("Pontuação: " + score, 20, 40);
    }

    public static void main(String[] args) {
        launch(args);
    }
}