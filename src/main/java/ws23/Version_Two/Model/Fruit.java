package ws23.Version_Two.Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.Random;

public class Fruit extends Objects {


    // Array with different Fruits
    private static final String [] FRUIT_IMAGES = {
            "/blueberries.png",
            "/cherries.png",
            "/grape.png",
            "/pear.png",
            "/orange.png"
    };
    private final Image objectImage;

    public Fruit(int x, int y){
        super(x,y);

        // choose a random Fruit-Image
        Random random = new Random();
        int randomIndex = random.nextInt(FRUIT_IMAGES.length);
        String selectedImage = FRUIT_IMAGES[randomIndex];

        objectImage = new Image(selectedImage);
    }
    /**
     * Method to draw a fruit on the canvas
     * @param gc Instance from GraphicsContext for the canvas
     */
    public void draw(GraphicsContext gc) {
        gc.drawImage(objectImage, X, Y, Objects.SIZE, Objects.SIZE);

    }
}

