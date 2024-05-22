package ws23.Version_Two.Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Shield extends Objects{
    // Shield image
    private final Image objectImage = new Image("/electricity.png");

    public Shield(int x, int y) {
        super(x, y);
    }

    /**
     * Method to draw a shield on the canvas
     * @param gc Instance from GraphicsContext for the canvas
     */
    public void draw(GraphicsContext gc) {
        gc.drawImage(objectImage, X, Y, Objects.SIZE, Objects.SIZE);

    }
}
