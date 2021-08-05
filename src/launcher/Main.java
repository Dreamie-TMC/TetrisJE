package launcher;

import audio.MusicPlayer;
import controllers.IController;
import helpers.ImageHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResourceAsStream("/fxml/game.fxml"));
        IController controller = loader.getController();
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            try {
                controller.loadInput(e.getText().charAt(0));
            } catch (StringIndexOutOfBoundsException ignored) { }
        });

        scene.setOnKeyReleased(e -> {
            try {
                controller.flushInput(e.getText().charAt(0));
            } catch (StringIndexOutOfBoundsException ignored) { }
        });

        Setup.performFirstTimeSetup();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tetris");
        primaryStage.getIcons().add(ImageHandler.getIcon());

        primaryStage.setOnCloseRequest(e -> {
            MusicPlayer.getInstance().stop();
            Platform.exit();
        });
        primaryStage.show();
    }
}
