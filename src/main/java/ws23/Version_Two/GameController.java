package ws23.Version_Two;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.Duration;
import ws23.Version_Two.Model.*;

import java.io.IOException;
import java.util.List;

public class GameController  {


    @FXML
    private BorderPane canvasContainer;

    @FXML
    public   Canvas canvas;
    @FXML
    private Button backButton;
    public static Timeline timeline;
    public static int WIDTH, HEIGHT;

    // new Field instanz
    private Field field;
    @FXML
    private Label scoreLabel;
    public  int score = 0;
    private GameViewController gameOverController,pauseController;

    public static boolean pause = false;

    // creates an Instanz from GrapicsContext for the canvas
    private GraphicsContext gc;

    public double timelineDuration = 0.18;

    // Fruits
    private List<Fruit> fruits;

    // Obstacles
    private List<Obstacle> obstacles;

    // Shield
    private List<Shield> shields;

    // Timer frutis, obstacles and shield
    private int time = 0;

    public void setPauseController(GameViewController pauseController) {
        this.pauseController = pauseController;
    }
    public void setGameOverController(GameViewController gameOverController) {
        this.gameOverController = gameOverController;
    }
    private Snake snake;
    /**
     * create Sound Objects
     */
    public Sound addBody, bonk, gameOver, levelUp, shieldActivated, supermode, noDamage;

    /**
     * Boolean block to prevent strange direction changes when pressing keys to fast
     */
    private boolean keyActive;

    protected static State state;

    /**
     * Method for the visuals and the handling of Key Press
     */
    @FXML
    public void initialize() {
        scoreLabel.setText("");
        score = 0;
        keyActive = true;


        // adjust Canvas size
        adjustSize();

        Listener();

        //creates an Instance from GraphicsContext for the canvas
        gc = canvas.getGraphicsContext2D();

        // Sounds
        setSounds();

        setObjects();

        // animation
        animate();
        // analyses Key input
        handleKey();
        // needed so the down button can be recognised as input for the snake movement instead of navigation
        backButton.setFocusTraversable(false);

    }

    /**
     * Method to "run" the game
     */
    private void animate(){
        // while animation is running, do every 0.1 sec update(),draw()
        timeline = new Timeline(new KeyFrame(Duration.seconds(timelineDuration), event -> {
            if (!pause) {
                update();
                draw();
            }
        }));
        // start Animation
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Method that sets all the Objects on the canvas
     */
    public void setObjects(){
        // here so the canvas can be resizeable and the snake is centered
        field = new Field();
        snake = field.getSnake();

        // initialize objects on field
        fruits = field.getFruits();
        obstacles = field.getObstacles();
        shields = field.getShield();
        state = field.state;
    }

    /**
     * Method that adjusts the Canvas size according to Window size
     */
    public void adjustSize(){

        // Width and Height of the BorderPane the canvas is in
        double containerWidth = canvasContainer.getWidth();
        double containerHeight = canvasContainer.getHeight();

        // Width and Height % the canvas should fill the BorderPane
        double widthPercentage = 0.9;
        double heightPercentage = 0.8;

        // set the Canvas size according to BorderPane * %
        canvas.setWidth(containerWidth * widthPercentage);
        canvas.setHeight(containerHeight * heightPercentage);


        WIDTH = (int) canvas.getWidth();
        HEIGHT = (int) canvas.getHeight();


    }

    /**
     * Method that sets all the Sounds
     */
    private void setSounds(){
        addBody = new Sound("src/main/resources/add_Body.wav");
        bonk = new Sound("src/main/resources/bonk.wav");
        gameOver = new Sound("src/main/resources/GameOver.wav");
        levelUp = new Sound("src/main/resources/Levelup.wav");
        shieldActivated = new Sound("src/main/resources/shield_activated.wav");
        supermode = new Sound("src/main/resources/supermode.wav");
        noDamage = new Sound("src/main/resources/no_damage.wav");
    }

    /**
     * Method that reacts when an assigned Key is pressed
     */
    private void handleKey(){

        canvas.setOnKeyPressed(keyEvent -> {
            state = State.Running;
            KeyCode code = keyEvent.getCode();

            if (code == KeyCode.UP && keyActive && state == State.Running) {
                snake.setDirection(0, -Snake.SNAKE_SIZE);
                keyActive= false;


            } else if (code == KeyCode.DOWN && keyActive && state == State.Running) {
                snake.setDirection(0, Snake.SNAKE_SIZE);
                keyActive= false;


            } else if (code == KeyCode.LEFT && keyActive && state == State.Running) {
                snake.setDirection(-Snake.SNAKE_SIZE,0);
                keyActive= false;


            } else if (code == KeyCode.RIGHT && keyActive && state == State.Running) {
                snake.setDirection(Snake.SNAKE_SIZE,0);
                keyActive= false;


            } else if (code == KeyCode.SPACE || code == KeyCode.P && state == State.Running){
                    showPause();
            } else if (code == KeyCode.ESCAPE){
                Platform.exit();
            }
            keyActive = true;

        });
    }

    /**
     * Method to react to change of Window size
     */
    public void Listener(){
        canvasContainer.widthProperty().addListener((observable, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue());
        });
        canvasContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue());
        });
    }

    /**
     * Method to update the score
     * @param score holds the value for the score
     */
    @FXML
    public void updateScore(int score){
        scoreLabel.setText("" + score);
        if (score < 0){
            showOver();
        }
    }

    /**
     * Returns the actual score
     * @return Value of score
     */
    public int getScore() {
        return score;
    }
    /**
     * Method to change to MainMenuView
     */
    @FXML
    public void returnMainMenu() {
        pause = false;
        timeline.pause();
        supermode.stop();
        state = State.Started;


    }

    /**
     * Method to show the PauseView and pause the animation
     */
    @FXML
    private void showPause() {
        pause = true;
        timeline.pause();
        supermode.stop();
        state = State.Paused;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PauseView.fxml"));
            Parent proot = loader.load();
            pauseController = loader.getController();

            // Setzen Sie den GameController im PauseController
            pauseController.setGameController(this);

            Stage pauseStage = new Stage();
            Image icon = new Image(getClass().getResourceAsStream("/pause.png"));
            pauseStage.getIcons().add(icon);
            pauseStage.setResizable(false);
            pauseStage.setOnCloseRequest(event -> {
                // Verhindert das Schließen des Fensters
                event.consume();
                // Fügen Sie hier den Code hinzu, den Sie ausführen möchten, wenn der Schließ-Button geklickt wird
            });
            pauseStage.setTitle("Pausiert");
            pauseStage.initModality(Modality.APPLICATION_MODAL);
            pauseStage.initOwner(canvas.getScene().getWindow());
            pauseStage.setScene(new Scene(proot));
            pauseStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to show the GameOverView, also resets basic Game parameters
     */
    @FXML
    public void showOver() {
        stopGame();
        snake = new Snake(WIDTH/2,HEIGHT/2); // reset snake
        pause = false;

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GameOverView.fxml"));
                Parent proot = loader.load();
                gameOverController = loader.getController();

                // Setzen Sie den GameController im PauseController
                gameOverController.setGameController(this);

                gameOverController.Score(score);

                Stage overStage = new Stage();
                Image icon = new Image(getClass().getResourceAsStream("/skull.png"));
                overStage.getIcons().add(icon);
                overStage.setResizable(false);
                overStage.setOnCloseRequest(event -> {
                    // Verhindert das Schließen des Fensters
                    event.consume();
                    // Fügen Sie hier den Code hinzu, den Sie ausführen möchten, wenn der Schließ-Button geklickt wird
                });
                overStage.setTitle("Verloren");
                overStage.initModality(Modality.APPLICATION_MODAL);
                overStage.initOwner(canvas.getScene().getWindow());
                overStage.setScene(new Scene(proot));
                overStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Method to stop the animation of the snake
     */
    @FXML
    public void stopGame() {
        if (timeline != null) {
            timeline.stop();
            fruits.clear();
            obstacles.clear();
            shields.clear();
            gameOver.play();
            supermode.stop();
            Field.superMode = false;
            state = State.Finished;

        }
    }

    /**
     * Method to resume the game at the point it was paused
     */
    @FXML
    public  void resumeGame(){
        pause = false;
        timeline.play();
        state = State.Running;
        if (Field.superMode) {
            supermode.playLoop();
        }
    }

    /**
     * Method to update the position of the snake on the canvas
     */
    public void update()
    {
        if (pause){
            return;
        }
        // Move Snake based on input
        snake.updateHead();
        snake.move();
        field.setGameController(this);
        field.checkCollision();
        adjustSize();

    }

    /**
     *  Method that draws the Snake onto the canvas

     */
    public void draw() {

        // if game is paused stop the Method
        if ( pause){
            return;
        }

        // remove anything on the canvas

        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.ALICEBLUE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());

        time += 1;
        if (time % 60 == 0){
            // timer for fruit
            field.placeFruit();

        } else if (time % 90 == 0) {
            // timer for obstacles
            field.placeObstacle();
        } else if (time % 130 == 0){
            // timer for shield
            field.placeShield();

        }

        for (Fruit fruit : fruits) {
            fruit.draw(gc);
        }

        for (Obstacle obstacle : obstacles){
            obstacle.draw(gc);
        }

        for (Shield shield : shields) {
            shield.draw(gc);
        }

        snake.draw(gc); // Draw Snake

    }
    public static State getState() {
        return state;
    }

}