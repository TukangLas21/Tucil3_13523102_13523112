package com.tuciltiga.controller;

import java.io.File;
import java.util.List;

import game.BoardState;
import game.Piece;
import game.Move;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import utils.IOHandler;

import javafx.util.Duration;

public class Controller {
    @FXML 
    TextField configPathField = new TextField();

    @FXML 
    ComboBox<Algorithm> algorithmCombo = new ComboBox<>();

    @FXML
    ComboBox<Heuristic> heuristicCombo = new ComboBox<>();

    @FXML 
    Label moveCountLabel = new Label();

    @FXML 
    Pane gameBoard = new Pane();

    @FXML
    Label runtimeLabel = new Label();

    @FXML
    Label nodeCountLabel = new Label();

    @FXML
    Timeline timeline;

    private int stateIdx = 0;

    private List<BoardState> solutionPath;

    private final int cellSize = 50;

    @FXML
    public void initialize() {
        // Setup algorithm choices
        algorithmCombo.getItems().setAll(Algorithm.values());
        algorithmCombo.getSelectionModel().selectFirst();
        heuristicCombo.getItems().setAll(Heuristic.values());
        heuristicCombo.getSelectionModel().selectFirst();
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
            rect.setUserData(piece.getName());
            gameBoard.getChildren().add(rect);
        }

    }

    @FXML
    private void handleSolve() {
        Algorithm algorithm = algorithmCombo.getValue();
        Heuristic heuristic = heuristicCombo.getValue();
        String configPath = configPathField.getText();
        
        if (configPath.isEmpty() || !isPathValid(configPath)) {
            showAlert("Error", "Please select a valid config file first");
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
        long startTime = System.currentTimeMillis();



        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        runtimeLabel.setText(String.valueOf(runTime));
        animate();
        timeline.play();
    }

    private void animate() {
        if (timeline != null) timeline.stop();

        timeline = new Timeline();
        stateIdx = 0;

        for (int i = 1; i < solutionPath.size(); i++) {
            int toIdx = i;

            KeyFrame frame = new KeyFrame(Duration.seconds(0.5 * (i+1)), e -> animateTransition(toIdx));
            timeline.getKeyFrames().add(frame);
        }
    }

    private void animateTransition(int toIdx) {
        BoardState to = solutionPath.get(toIdx);

        Move move = to.getLastMove();

        Rectangle rect = findPieceBlock(move.getPieceName());

        TranslateTransition moveAnimation = new TranslateTransition(Duration.seconds(0.5), rect);

        // TODO: tambahin distance
        if (move.getDirection() == Move.Direction.LEFT || move.getDirection() == Move.Direction.RIGHT) {
            moveAnimation.setByX(move.getDirection() == Move.Direction.LEFT ? -cellSize : cellSize);
        } else {
            moveAnimation.setByY(move.getDirection() == Move.Direction.UP ? -cellSize : cellSize);
        }

        moveAnimation.setOnFinished(event -> {
            displayBoard(to);
        });

        moveAnimation.play();
    }

    private Rectangle findPieceBlock(char pieceName) {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Rectangle && node.getUserData().equals(pieceName)) {
                return (Rectangle) node;
            }
        }
        throw new IllegalArgumentException("Piece not found: " + pieceName);
    }

    private boolean isPathValid(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && file.canRead();
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

    @FXML
    private void handlePause() {
        if (timeline != null) {
            timeline.pause();
        }
    }

    @FXML
    private void handleResume() {
        if (timeline != null) {
            timeline.play();
        }
    }

    @FXML
    private void handleReverse() {
        if (solutionPath != null && stateIdx > 0) {
            stateIdx--;
            BoardState previousState = solutionPath.get(stateIdx);
            displayBoard(previousState);
        }
    }

    @FXML
    private void handleForward() {
        if (solutionPath != null && stateIdx < solutionPath.size() - 1) {
            stateIdx++;
            BoardState nextState = solutionPath.get(stateIdx);
            displayBoard(nextState);
        }
    }   

    @FXML
    private void handleSave() {
        // TODO: implement save in test/result
        if (solutionPath == null) {
            showAlert("Error", "No solution path available to save.");
            return;
        }
    }
}