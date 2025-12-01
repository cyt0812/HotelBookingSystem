
package com.hotelbooking;

//import com.hotelbooking.util.DatabaseInitializer;
import com.hotelbooking.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    static {
        // 在GUI启动前初始化数据库
        //DatabaseInitializer.initialize();
    }
    
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        
//        SceneManager.setStage(primaryStage);
//        
//        Parent root = FXMLLoader.load(getClass().getResource("/com/hotelbooking/view/login.fxml"));
//        
//        Scene scene = new Scene(root, 800, 600); // 你可以自定义
//        scene.getStylesheets().add(
//                getClass().getResource("/com/hotelbooking/assets/theme.css").toExternalForm()
//        );
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Hotel Booking System - Login");
//        primaryStage.show();
//        
//
//    }

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("/com/hotelbooking/view/main_dashboard.fxml"));

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(
                getClass().getResource("/com/hotelbooking/assets/theme.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.setTitle("Hotel Booking System - Main Dashboard");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }


}