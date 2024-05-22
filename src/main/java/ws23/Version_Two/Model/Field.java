package ws23.Version_Two.Model;

import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ws23.Version_Two.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
    // Random variable used for placing Objects at random places
    private final Random random = new Random();
    public long superModeEndTime = 0;

    // Fruits
    private final List<Fruit> fruits = new ArrayList<>();
    private int numberofFruitsEaten = 0;
    private final int numberofFruits = 1;
    private int addedFruits = 0;
    private final int numberofObstacles = 7;
    private int addedObstalces = 0;
    private final int numberofShields = 1;
    private int addedShield = 0;

    // Obstacles
    private final List<Obstacle> obstacles = new ArrayList<>();

    // Shield
    private final List<Shield> shields = new ArrayList<>();

    // Boolean to change the timelineDuration back to normal
    private boolean leBonk = false;

    // Mode that activates when Shield is active
    public static boolean superMode = false;

    //Snake
    private final Snake snake;

    // Body
    public  Body tummy, tail;

    // State of the game
    public State state;

    public Field(){
        // Initialize Snake
        snake = new Snake(GameController.WIDTH/2, GameController.HEIGHT/2);
        superMode = false;
        state = State.Started;



    }
    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Method to place Fruits at Random places
     */
    public void placeFruit() {
        int fruitX = 0,fruitY = 0 ;

        if (addedFruits < numberofFruits) {
            fruitX = (random.nextInt((GameController.WIDTH - Objects.SIZE) / Snake.SNAKE_SIZE) * Snake.SNAKE_SIZE);
            fruitY = (random.nextInt((GameController.HEIGHT - Objects.SIZE) / Snake.SNAKE_SIZE) * Snake.SNAKE_SIZE);

             addFruit(fruitX, fruitY);
             addedFruits++;
            }
    }
    /**
     * Method to place Obstacles at Random places
     */
    public void placeObstacle() {
        int obstacleX = 0,obstacleY = 0 ;

            if (addedObstalces < numberofObstacles){
               obstacleX = (random.nextInt((GameController.WIDTH - Objects.SIZE) / Snake.SNAKE_SIZE) * Snake.SNAKE_SIZE);
               obstacleY = (random.nextInt((GameController.HEIGHT - Objects.SIZE) / Snake.SNAKE_SIZE) * Snake.SNAKE_SIZE);

                addObstacle(obstacleX, obstacleY);
                addedObstalces++;
            }
    }
    /**
     *  Method to place a Shield at a Random place
     */
    public void placeShield() {
        int shieldX = 0, shieldY = 0;

        if (numberofShields > addedShield) {
             shieldX = random.nextInt((GameController.WIDTH - Objects.SIZE) / Snake.SNAKE_SIZE) * Snake.SNAKE_SIZE;
             shieldY = random.nextInt((GameController.HEIGHT - Objects.SIZE) / Snake.SNAKE_SIZE) * Snake.SNAKE_SIZE;

            addShield(shieldX, shieldY);
            addedShield++;
        }
    }

    /**
     * Method to add Fruit to the canvas
     */
    private void addFruit(int X, int Y) {
        if (X % Snake.SNAKE_SIZE == 0 && Y % Snake.SNAKE_SIZE == 0) {
            fruits.add(new Fruit(X,Y));
        }
    }
    /**
     * Method to add Obstacle to the canvas
     */
    private void addObstacle(int X, int Y) {
        if (X % Snake.SNAKE_SIZE == 0 && Y % Snake.SNAKE_SIZE == 0) {
            obstacles.add(new Obstacle(X,Y));
        }
    }

    /**
     * Metod to add a Shield to the canvas
     * @param X
     * @param Y
     */
    private void addShield(int X, int Y){
        if (X % Snake.SNAKE_SIZE == 0 && Y % Snake.SNAKE_SIZE == 0) {
            shields.add(new Shield(X,Y));
        }
    }

    /**
     * Method to check for Collision with a fruit or obstacle
     */
    public State checkCollision() {
        int helpX, helpY, headX, headY, tailX, tailY;
        headX = snake.getSnakeX();
        headY = snake.getSnakeY();

        // Collision with edge
        if (snake.getSnakeX() < 0 ||snake.getSnakeX() >= GameController.WIDTH||snake.getSnakeY() >= GameController.HEIGHT ||snake.getSnakeY() < 0) {
            gameController.showOver();
        }

        // check if snake's head hit itself
        for (int i = 1; i < snake.getSize(); i++) {
            tummy = snake.getTummy(i);
            helpX = tummy.getX();
            helpY = tummy.getY();

            if (headX == helpX && headY == helpY) {
                gameController.showOver();
                return State.Finished;
            }
        }

        tail = snake.getTail();
        // if head hit tail
        if (tail != null && snake.getSize() > 3) {

            tailX = tail.getX();
            tailY = tail.getY();


            if (headX == tailX && headY == tailY) {
                gameController.score = 0;
                gameController.updateScore(gameController.score);
                gameController.showOver();
                return State.Finished;
            }
        }

        // Check all fruits on field
        for (int i = 0; i < fruits.size(); i++) {

            Fruit fruit = fruits.get(i);
            helpX = fruit.getX();
            helpY = fruit.getY();

            // Check if fruit's position is equal to snake's head
            if (helpX == headX && helpY == headY) {
                gameController.score += 10;
                gameController.updateScore(gameController.score);
                numberofFruitsEaten++;
                fruits.remove(i);
                addedFruits--;
                snake.addTummy();
                // play sound
                gameController.addBody.play();

                // change the speed of the snake everytime numberofFruitsEaten gets eaten
                if (numberofFruitsEaten == 5) {
                    speedUP();
                    numberofFruitsEaten = 0;

                }
            }

        }
        // Check if collision with Shield
        for (int l = 0; l < shields.size(); l++) {
            Shield shield = shields.get(l);
            helpX = shield.getX();
            helpY = shield.getY();

            // Check if shield position is equal to snake's head
            if (helpX == headX && helpY == headY) {

                // play sound
                gameController.shieldActivated.play();

                // obstacles won't damage the snake
                superMode = true;
                snake.updateHead();
                gameController.supermode.playLoop();
                superModeEndTime = System.currentTimeMillis() + 10 * 1000;
                shields.remove(l);

                for (int i = 0; i < snake.getSize(); i++) {
                    tummy = snake.getTummy(i);
                    Body.setSuper(new Image("body_Super.png"));
                }
                addedShield--;
            }
            if (superMode && System.currentTimeMillis() >= superModeEndTime) {
                snake.updateHead();
                superMode = false;
                gameController.supermode.stop();

                for (int i = 0; i < snake.getSize(); i++) {
                    tummy = snake.getTummy(i);
                    Body.setSuper(new Image("body.png"));
                }

            }

            }

        // Check all obstacles on field
        for (int j = 0; j < obstacles.size(); j++) {
            Obstacle obstacle = obstacles.get(j);
            helpX = obstacle.getX();
            helpY = obstacle.getY();

            // Check if obstacle's position is equal to snake's head
            if (helpX == headX && helpY == headY ) {

                // if in supermode obstacle won't damage the snake
                if (superMode){
                obstacles.remove(j);
                addedObstalces--;
                gameController.noDamage.play();

                } else {

                if (gameController.score == 0) { // If the score is 0, the game ends
                    gameController.showOver();
                    gameController.supermode.stop();


                } else { // If the score is above 0, remove obstacle since it has been hit
                    obstacles.remove(j);
                    addedObstalces--;
                    gameController.score -= 10;
                    gameController.updateScore(gameController.score);
                    // reset timelineDuration
                    leBonk = true;
                    speedUP();

                    //play sound
                    gameController.bonk.play();
                }
                snake.removeTummy();
            }
            }
        }

        leBonk = false;

        return GameController.getState();
    }


    /**
     * Method to change the speed of the Snake
     */
    private void speedUP(){
        if (leBonk) {
            gameController.timelineDuration = 0.12;
        } else {
            gameController.timelineDuration -= 0.05;
            if (gameController.timelineDuration < 0.05) {
                gameController.timelineDuration = 0.05; // the speed won't go higher than this
            }

        }

        // stop existing timeline
        GameController.timeline.stop();
        // delete all Keyframes
        GameController.timeline.getKeyFrames().clear();
        // create a new timeline
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(gameController.timelineDuration), event -> {
            if (!GameController.pause) {
                gameController.update();
                gameController.draw();
            }
        });
        GameController.timeline.getKeyFrames().add(keyFrame);
        GameController.timeline.play();

    }

    /**
     * Returns the snake
     * @return Snake object
     */
    public Snake getSnake() {
        return snake;
    }
    /**
     * Returns the fruits
     * @return Fruit object
     */
    public List<Fruit> getFruits() {
        return  fruits;
    }
    /**
     * Returns the obstacles
     * @return Obstacle object
     */
    public List<Obstacle> getObstacles() {
        return  obstacles;
    }

    /**
     * Return the shield
     * @return Shield object
     *
     */
    public List<Shield> getShield() {
        return shields;
    }
}
