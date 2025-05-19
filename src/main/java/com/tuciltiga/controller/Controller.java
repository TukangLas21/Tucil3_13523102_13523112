package com.tuciltiga.controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class Controller {
    @FXML 
    TextField configPathField = new TextField();

    @FXML
    Button fileBrowseButton = new Button();

    @FXML 
    ComboBox<Algorithm> algorithmCombo = new ComboBox<>();

    @FXML 
    Label moveCountLabel = new Label();

    @FXML 
    Pane gameBoard = new Pane();

    @FXML
    Button exitButton = new Button();

    @FXML
    Button resetButton = new Button();

    @FXML
    Button solveButton = new Button();

    @FXML
    Label runtimeLabel = new Label();

    @FXML
    Label nodeCountLabel = new Label();

    @FXML
    public void initialize() {
        // Setup algorithm choices
        algorithmCombo.getItems().setAll(Algorithm.values());
        algorithmCombo.getSelectionModel().selectFirst();
    }

    @FXML
    public void handleFileBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Rush Hour Config");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        
        File file = fileChooser.showOpenDialog(configPathField.getScene().getWindow());
        if (file != null) {
            configPathField.setText(file.getAbsolutePath());
            // TODO: Load and display initial board state
        }
    }

    @FXML
    private void handleSolve() {
        Algorithm algorithm = algorithmCombo.getValue();
        String configPath = configPathField.getText();
        
        if (configPath.isEmpty()) {
            showAlert("Error", "Please select a config file first");
            return;
        }

        // TODO: Execute solver with selected algorithm
        // Pseudocode:
        /*
        1. Start timer
        2. Run selected algorithm
        3. Record:
           - movesLabel.setText(solution.size())
           - runtimeLabel.setText(endTime - startTime)
           - nodeCountLabel.setText(algorithm.getNodesVisited())
        4. Start animation
        */
    }

    @FXML
    private void handleReset() {
        configPathField.clear();
        moveCountLabel.setText("0");
        runtimeLabel.setText("0.0");
        nodeCountLabel.setText("0");
        gameBoard.getChildren().clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}