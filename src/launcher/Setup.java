package launcher;

import com.google.gson.Gson;
import helpers.exceptions.SystemSetupException;
import models.HighScores;
import models.Inputs;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Setup {
    public static final String RESOURCES_PATH = System.getProperty("user.home") +
            "\\AppData\\roaming\\tetris";
    public static final String INPUTS_PATH = System.getProperty("user.home") +
            "\\AppData\\roaming\\tetris\\inputs.json";
    public static final String SCORES_PATH = System.getProperty("user.home") +
            "\\AppData\\roaming\\tetris\\scores.json";

    public static void performFirstTimeSetup() {
        File file = new File(RESOURCES_PATH);
        if (file.exists())
            return;
        if (!file.mkdirs())
            throw new SystemSetupException("Failed to create required file directory! If the problem persists" +
                    " please try running the application as an administrator.");
        writeInputs(Inputs.createDefaultInputsFile());
        writeHighScores(HighScores.createDefaultScores());
    }

    public static void writeInputs(Inputs inputs) {
        try {
            PrintWriter writer = new PrintWriter(INPUTS_PATH);
            Gson gson = new Gson();
            String json = gson.toJson(inputs);
            writer.print(json);
            writer.close();
        } catch (IOException e) {
            throw new SystemSetupException("Failed to write default inputs json!");
        }
    }

    public static void writeHighScores(HighScores scores) {
        try {
            PrintWriter writer = new PrintWriter(SCORES_PATH);
            Gson gson = new Gson();
            String json = gson.toJson(scores);
            writer.print(json);
            writer.close();
        } catch (IOException e) {
            throw new SystemSetupException("Failed to write default high scores json!");
        }
    }
}
