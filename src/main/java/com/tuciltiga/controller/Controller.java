package com.tuciltiga.controller;

import java.io.File;
import java.util.List;

import game.AStar;
import game.BlockingPiecesHeuristic;
import game.BoardState;
import game.DistanceToExitHeuristic;
import game.GBFS;
import game.IDAStar;
import game.Move;
import game.Piece;
import game.UCS;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;
import utils.IOHandler;

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

    BoardState initialState;

    private int stateIdx = 0;

    private List<BoardState> solutionPath;
    private int nodeCount;

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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        
        File file = fileChooser.showOpenDialog(configPathField.getScene().getWindow());
        if (file != null) {
            configPathField.setText(file.getAbsolutePath());
            loadBoard(file);
        }
    }

    private void loadBoard(File configFile) {
        try {
            initialState = IOHandler.convertInput(configFile);
            gameBoard.getChildren().clear();
            displayBoard(initialState);
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

        if (initialState == null) {
            showAlert("Error", "No board configuration loaded");
            return;
        }

        if (algorithm == null) {
            showAlert("Error", "Please select an algorithm");
            return;
        }

        if (heuristic == null) {
            showAlert("Error", "Please select a heuristic");
            return;
        }

        switch (heuristic) {
            case DistanceToExit -> {
                initialState.setHeuristic(new DistanceToExitHeuristic());
                break;
            }
            case BlockingVehicles -> {
                initialState.setHeuristic(new BlockingPiecesHeuristic());
                break;
            }
        }

        long startTime = System.currentTimeMillis();

        solver(algorithm, initialState);
        if (solutionPath == null) {
            showAlert("Error", "No solution found");
            return;
        }

        updateSolutionPath();
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        runtimeLabel.setText(String.valueOf(runTime));
        moveCountLabel.setText(String.valueOf(solutionPath.size()-1));
        nodeCountLabel.setText(String.valueOf(nodeCount));

        if (!solutionPath.isEmpty()) {
            displayBoard(solutionPath.get(0)); 
        }
        
        animate();
        if (timeline != null) { 
           timeline.play(); 
        }
    }

    private void animate() {
        if (solutionPath == null || solutionPath.size() <= 1) {
            return;
        }

        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline();
        stateIdx = 0;

        for (int i = 1; i < solutionPath.size(); i++) {
            int toIdx = i; 

            KeyFrame frame = new KeyFrame(Duration.seconds(0.5 * i), e -> {
                animateTransition(toIdx);
            });
            timeline.getKeyFrames().add(frame);
        }

        timeline.setOnFinished(event -> {
            if (solutionPath != null && !solutionPath.isEmpty()) {
                stateIdx = solutionPath.size() - 1;
            }
            System.out.println("Animation finished.");
        });
    }

    private void animateTransition(int toStateIndex) {
        if (solutionPath == null || toStateIndex < 1 || toStateIndex >= solutionPath.size()) {
            return; 
        }

        BoardState targetState = solutionPath.get(toStateIndex);

        Move move = targetState.getLastMove(); 

        if (move == null) {
            System.err.println("Error: Move is null for state at index " + toStateIndex);
            displayBoard(targetState);
            return;
        }

        Rectangle pieceRect = findPieceBlock(move.getPieceName());

        if (pieceRect == null) {
            System.err.println("Error: Could not find piece " + move.getPieceName() + " on the game board for animation.");
            displayBoard(targetState);
            return;
        }

        TranslateTransition moveAnimation = new TranslateTransition(Duration.seconds(0.2), pieceRect);
        switch (move.getDirection()) {
            case LEFT:
                moveAnimation.setByX(-cellSize);
                break;
            case RIGHT:
                moveAnimation.setByX(cellSize);
                break;
            case UP:
                moveAnimation.setByY(-cellSize);
                break;
            case DOWN:
                moveAnimation.setByY(cellSize);
                break;
        }

        moveAnimation.setOnFinished(event -> {
            displayBoard(targetState);
        });

        moveAnimation.play();
    }

    private void updateSolutionPath() {
        if (solutionPath == null) {
            showAlert("Error", "No solution path available.");
        }

        BoardState lastState = solutionPath.get(solutionPath.size() - 1);
        Move.Direction exitDir = lastState.getDirectionToExit();
        switch (exitDir) {
            case UP -> {
                while (lastState.getPrimaryPiece().getCoordinates().size() > 1) {
                    lastState.getPrimaryPiece().removeCoordinateAtCoordinate(lastState.getExitCoordinate());
                    BoardState newState = lastState.movePiece(lastState.getPrimaryPiece(), Move.Direction.UP);
                    solutionPath.add(newState);
                    lastState = solutionPath.get(solutionPath.size() - 1);
                }
            }
            case DOWN -> {
                while (lastState.getPrimaryPiece().getCoordinates().size() > 1) {
                    lastState.getPrimaryPiece().removeCoordinateAtCoordinate(lastState.getExitCoordinate());
                    BoardState newState = lastState.movePiece(lastState.getPrimaryPiece(), Move.Direction.DOWN);
                    solutionPath.add(newState);
                    lastState = solutionPath.get(solutionPath.size() - 1);
                }
            }
            case LEFT -> {
                while (lastState.getPrimaryPiece().getCoordinates().size() > 1) {
                    lastState.getPrimaryPiece().removeCoordinateAtCoordinate(lastState.getExitCoordinate());
                    BoardState newState = lastState.movePiece(lastState.getPrimaryPiece(), Move.Direction.LEFT);
                    solutionPath.add(newState);
                    lastState = solutionPath.get(solutionPath.size() - 1);
                }
            }
            case RIGHT -> {
                while (lastState.getPrimaryPiece().getCoordinates().size() > 1) {
                    lastState.getPrimaryPiece().removeCoordinateAtCoordinate(lastState.getExitCoordinate());
                    BoardState newState = lastState.movePiece(lastState.getPrimaryPiece(), Move.Direction.RIGHT);
                    solutionPath.add(newState);
                    lastState = solutionPath.get(solutionPath.size() - 1);
                }
            }
        }
    }

    private Rectangle findPieceBlock(char pieceName) {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Rectangle && node.getUserData() != null && node.getUserData().equals(pieceName)) {
                return (Rectangle) node;
            }
        }
        return null; 
    }

    private void solver(Algorithm algorithm, BoardState initialState) {
        switch (algorithm) {
            case GBFS -> {
                Object[] result = GBFS.solve(initialState);
                if (result == null) {
                    showAlert("Error", "No solution found");
                    return;
                }
                solutionPath = (List<BoardState>) result[0];
                nodeCount = (int) result[1];
                System.out.println("Count Node: " + nodeCount);
                System.out.println("Count Node: " + result[1]);
            }
            case ASTAR -> {
                Object[] result = AStar.solve(initialState);
                if (result == null) {
                    showAlert("Error", "No solution found");
                    return;
                }
                solutionPath = (List<BoardState>) result[0];
                nodeCount = (int) result[1];
            }
            case UCS -> {
                Object[] result = UCS.solve(initialState);
                if (result == null) {
                    showAlert("Error", "No solution found");
                    return;
                }
                solutionPath = (List<BoardState>) result[0];
                nodeCount = (int) result[1];
            }
            case IDA -> {
                Object[] result = IDAStar.solve(initialState);
                if (result == null) {
                    showAlert("Error", "No solution found");
                    return;
                }
                solutionPath = (List<BoardState>) result[0];
                nodeCount = (int) result[1];
            }
        } 
    }

    private boolean isPathValid(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && file.canRead();
    }

    @FXML
    private void handleReset() {
        configPathField.clear();
        solutionPath = null;
        stateIdx = 0;
        nodeCount = 0;
        initialState = null;
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
        if (solutionPath == null) {
            showAlert("Error", "No solution path available to save.");
            return;
        }
        
        String fileName = IOHandler.getOutputFileName();
        if (fileName == null || fileName.isEmpty()) {
            showAlert("Error", "Invalid file name.");
            return;
        }

        IOHandler.outputToFile(fileName, solutionPath);

        showAlert("Success", "Solution path saved to " + fileName);
    }
}