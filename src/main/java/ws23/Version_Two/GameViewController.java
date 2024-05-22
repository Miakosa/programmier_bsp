package ws23.Version_Two;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class GameViewController {


    @FXML
    private FXMLLoader loader;
    @FXML
    private Parent scene;

    @FXML
    private Parent proot;

    @FXML
    private Stage stage;
    @FXML
    private  GameController gameController;

    @FXML
    private Button backButton, restart, resumeButton;

    @FXML
    private Label scoreLabel;


    /**
     * Method to get the Score from  GameController
     *
     */
    public void Score(int score){
        scoreLabel.setText("" + score);
    }
    public void setGameController(GameController gameController){
        this.gameController = gameController;
    }

    /**
     * Method to get back to the MainmenuView
     */
    @FXML
    private void showMain() {
        // use Method in GameController to return to MainMenuView
        gameController.returnMainMenu();

        try {
            loader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
            proot = loader.load();

            stage = (Stage) gameController.canvas.getScene().getWindow();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Close GameOverView
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to resume the game at paused point
     */
    @FXML
    private void resumeGame()  {
        // use Method in GameController to resume the paused game
        gameController.resumeGame();
        stage = (Stage) resumeButton.getScene().getWindow();
        // close Pauseview
        stage.close();


    }
    /**
     * Method to restart the game
     */
    @FXML
    private void restart()  {
        stage = (Stage) restart.getScene().getWindow();
        stage.close();

        gameController.initialize();

    }

    /**
     * Method to return to the MainMenuView
     * @param event
     * @throws IOException
     */
    @FXML
    public void showMainMenu(ActionEvent event) throws IOException {
        loader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        scene = loader.load();

        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();

    }
    /**
     *  Method to change th GameView
     * @param event button click
     * @throws IOException
     */
    @FXML
    public void showGame(ActionEvent event) throws IOException {

        loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
        scene = loader.load();

        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));

        stage.show();

        gameController = loader.getController();
        gameController.adjustSize();
        gameController.setObjects();


    }

    /**
     *  Method to change th HistoryView
     * @param event button click
     * @throws IOException
     */
    @FXML
    public void showHistory(ActionEvent event) throws IOException {
        loader = new FXMLLoader(getClass().getResource("HistoryView.fxml"));
        scene = loader.load();

        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
