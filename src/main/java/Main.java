import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // 1. Create test UI components
        Label label = new Label("Rush Hour Solver Test");
        Button loadButton = new Button("Load Config");
        Button solveButton = new Button("Solve");
        Label statusLabel = new Label("Status: Ready");
        
        // 2. Set button actions
        loadButton.setOnAction(e -> {
            statusLabel.setText("Status: Load button clicked!");
            System.out.println("Load button working");  // Verify in console
        });
        
        solveButton.setOnAction(e -> {
            statusLabel.setText("Status: Solving...");
            System.out.println("Solve button working");
        });
        
        // 3. Create layout
        VBox root = new VBox(10, label, loadButton, solveButton, statusLabel);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        // 4. Set up scene and stage
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Rush Hour Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}