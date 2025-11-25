package com.hotelbooking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 删除或注释掉这行：
        // DatabaseInitializer.main(new String[]{});
        
        Parent root = FXMLLoader.load(getClass().getResource("/com/hotelbooking/view/MainView.fxml"));
        primaryStage.setTitle("酒店预订系统");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}