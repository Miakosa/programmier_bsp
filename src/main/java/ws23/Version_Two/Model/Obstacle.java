package ws23.Version_Two.Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.Random;

public class Obstacle extends Objects{



    // Array with different Obstacles
    private static final String [] OBSTACLE_IMAGES = {
            "/stone.png",
            "/stone (1).png",
            "/stone (2).png",
            "/stone (3).png"
    };
    private final Image objectImage;

    public Obstacle(int x, int y) {
        super(x, y);

        // choose a random Obstacle-Image
        Random random = new Random();
        int randomIndex = random.nextInt(OBSTACLE_IMAGES.length);
        String selectedImage = OBSTACLE_IMAGES[randomIndex];

        objectImage = new Image(selectedImage);

    }
    /**
     * Method to draw an obstacle on the canvas
     * @param gc Instance from GraphicsContext for the canvas
     */
    public void draw(GraphicsContext gc) {
        gc.drawImage(objectImage, X, Y, Objects.SIZE, Objects.SIZE);

    }
}

