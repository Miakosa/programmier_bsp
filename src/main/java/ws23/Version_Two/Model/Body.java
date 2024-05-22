package ws23.Version_Two.Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Body extends Objects{

    // snake's default body
    public static Image objectImage = new Image("/body.png");
    public Body(int x, int y) {
        super(x,y);
    }

    /**
     *  Method to change the Body image according to Mode
     * @param image defines the image for the body
     */
    public static void setSuper(Image image) {
        objectImage = image;
    }

    /**
     * Method to draw a bodypart on the canvas
     * @param gc Instance from GraphicsContext for the canvas
     */
    public void draw(GraphicsContext gc) {
        gc.drawImage(objectImage, X, Y, Snake.SNAKE_SIZE, Snake.SNAKE_SIZE);
    }

}
