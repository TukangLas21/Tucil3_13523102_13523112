package com.tuciltiga.controller;

import java.io.File;

import game.BoardState;
import game.Piece;
import game.Move;
import game.Coordinate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import utils.IOHandler;
import utils.Utils;

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
        fileChooser.setTitle("Select config file");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        
        File file = fileChooser.showOpenDialog(configPathField.getScene().getWindow());
        if (file != null) {
            configPathField.setText(file.getAbsolutePath());
            loadBoard(file);
        }
    }

    private void loadBoard(File configFile) {
        try {
            BoardState boardState = IOHandler.convertInput(configFile);
            gameBoard.getChildren().clear();
            displayBoard(boardState);
        } catch (Exception e) {
            showAlert("Error", "Failed to load board configuration: " + e.getMessage());
        }
    }

    private void displayBoard(BoardState boardState) {
        int cellSize = 50; 
        gameBoard.setPrefSize(boardState.getCol() * cellSize, boardState.getRow() * cellSize);
        gameBoard.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");
        gameBoard.getChildren().clear();

        int row = boardState.getBoard().length;
        int col = boardState.getBoard()[0].length;
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (boardState.getBoard()[i][j] == '.') {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(Color.GRAY);
                    rect.setStroke(Color.BLACK);
                    rect.setTranslateX(j * cellSize);
                    rect.setTranslateY(i * cellSize);
                    gameBoard.getChildren().add(rect);
                } else if (boardState.getBoard()[i][j] == 'K') {
                    Rectangle rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(Color.GREEN);
                    rect.setTranslateX(j * cellSize);
                    rect.setTranslateY(i * cellSize);
                    gameBoard.getChildren().add(rect);
                }
            }
        }

        for (Piece piece : boardState.getPieces()) {
            Rectangle rect = new Rectangle(cellSize * piece.getWidth(), cellSize * piece.getHeight());
            rect.setFill(piece.isPrimary() ? Color.RED : Color.BLUE);
            rect.setStroke(Color.BLACK);
            rect.setTranslateX(piece.getCoordinates().get(0).getCol() * cellSize);
            rect.setTranslateY(piece.getCoordinates().get(0).getRow() * cellSize);
            gameBoard.getChildren().add(rect);
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