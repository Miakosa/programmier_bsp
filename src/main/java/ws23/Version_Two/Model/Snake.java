package ws23.Version_Two.Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


import java.util.ArrayList;

public class Snake {
    // size of the Snake
    public static final int SNAKE_SIZE = 30;

    // believe me, I REALLY TRIED HARD to use proper Methods to rotate that stupid PNG.
    private Image objectImage;
    private final Image headup = new Image("/head_up.png");
    private final Image headdown = new Image("/head_down.png");
    private final Image headright = new Image("/head_right.png");
    private final Image headleft = new Image("/head_left.png");

    private final Image// if snake is in supermode
                headupSuper = new Image("/head_up_Super.png");
    private final Image headdownSuper = new Image("/head_down_Super.png");
    private final Image headrightSuper = new Image("/head_right_Super.png");
    private final Image headleftSuper = new Image("/head_left_Super.png");

    // variables used for the snake's movement
    private int snakeX, snakeY, directionX, directionY ;

    // Array with all the added body-parts
    public ArrayList<Body> tummy;
    private int size;



    /**
     * Method to initialize the Snake itself
     * @param startX Start-X-Position
     * @param startY Start-Y-Position
     */
    public Snake(int startX, int startY) {
        tummy = new ArrayList<>();
        size = 0;
        this.snakeX = startX;
        this.snakeY = startY;
        this.directionX = SNAKE_SIZE;
        this.directionY = 0;
        objectImage = headright;


    }

    /**
     * Method to update head images according to Mode
     */
 public void updateHead(){
     if (Field.superMode) {
         if (directionX == 0 && directionY == -Snake.SNAKE_SIZE) {
             objectImage = headupSuper;
         } else if (directionX == -Snake.SNAKE_SIZE && directionY == 0) {
             objectImage = headleftSuper;
         } else if (directionX == Snake.SNAKE_SIZE && directionY == 0) {
             objectImage = headrightSuper;
         } else if (directionY == Snake.SNAKE_SIZE) {
             objectImage = headdownSuper;
         }
     } else {
         if (directionX == 0 && directionY == -Snake.SNAKE_SIZE) {
             objectImage = headup;
         } else if (directionX == -Snake.SNAKE_SIZE && directionY == 0) {
             objectImage = headleft;
         } else if (directionX == Snake.SNAKE_SIZE && directionY == 0) {
             objectImage = headright;
         } else if (directionY == Snake.SNAKE_SIZE) {
             objectImage = headdown;
         }
     }
 }


    /**
     * Method to move the Snake
     */
    public void move() {

        // previous position of head
        int prevX = snakeX;
        int prevY = snakeY;

        // each bodypart gets the position of the one in front
        if (!tummy.isEmpty()) {
            for (int i = tummy.size() - 1; i >= 0; i--) {
                Body tummyPart = tummy.get(i);
                int prevTummyX = tummyPart.getX();
                int prevTummyY = tummyPart.getY();
                tummyPart.setX(prevX);
                tummyPart.setY(prevY);
                prevX = prevTummyX;
                prevY = prevTummyY;
            }
        }

        // move head to next position
        snakeX = (snakeX + directionX) / Snake.SNAKE_SIZE *Snake.SNAKE_SIZE;
        snakeY = (snakeY + directionY) / Snake.SNAKE_SIZE *Snake.SNAKE_SIZE;

    }

    /**
     * Method to get the information form the Keypress
     */
    public void setDirection(int dx, int dy){

        if (dx != -directionX || dy != -directionY) {
            directionX = dx;
            directionY = dy;

        }
    }
    /**
     * Method to draw the snake's head on the canvas
     * @param gc Instance from GraphicsContext for the canvas
     */
    public void draw(GraphicsContext gc){

        gc.drawImage(objectImage, snakeX, snakeY, Objects.SIZE, Objects.SIZE);

        for(Body tummys: tummy){
            tummys.draw(gc);
        }
    }
    
    /**
     * get the snake's actual size
     * @return size
     */
    public int getSize() {
        return this.size;
    }

    /**
     *  get the snake's tail as a Body object
     *  @return tail
     */
    public  Body getTail(){
        if(!tummy.isEmpty()){
            // each new tummy is added right behind the head, so the tail is the first element in the tummy-Array
            // it took me btw WAY TO LONG to figure that out.
            return tummy.get(0);
        }
        return null;
    }

    /**
     * get the snake's body as a Body object
     * @param i  position of part in body array
     * @return tummy
     */
    public Body getTummy(int i){
        return tummy.get(i);
    }
    public int getSnakeX() {
        return snakeX;
    }

    public int getSnakeY() {
        return snakeY;
    }

    /**
     *  Method adds a new bodypart at given position
     */
    public void addTummy() {
        if (Field.superMode) {
            Body.setSuper(new Image("body_Super.png"));
        } else {
            Body.setSuper(new Image("body.png"));
        }
        int tummyX = snakeX - directionX;
        int tummyY = snakeY - directionY;
        tummy.add(new Body(tummyX,tummyY));
        ++size;

    }
    /**
     * Method to remove a bodypart
     */
    public void removeTummy() {
        if (!tummy.isEmpty()) {
        tummy.remove(0);
        --size;
    }}

}
