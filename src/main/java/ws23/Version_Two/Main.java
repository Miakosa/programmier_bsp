package ws23.Version_Two;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenuView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700 , 800);
        // window-icon
        Image icon = new Image(getClass().getResourceAsStream("/snake.png"));
        stage.getIcons().add(icon);
        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(true);

    }

    public static void main(String[] args) {
        launch();
    }
}