
package com.hotelbooking;

import com.hotelbooking.util.DatabaseInitializer;
import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
//    static {
//        // 在GUI启动前初始化数据库
//        DatabaseInitializer.initializeDatabase();
//        DatabaseInitializer.insertSampleData();
//    }

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitializer.initializeDatabase();
        DatabaseInitializer.insertSampleData();
        
        // 在任何导航前调用
        NavigationManager.getInstance().push(
            "/com/hotelbooking/view/main_dashboard.fxml",
            "Hotel Booking System"
        );
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