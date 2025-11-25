// Main.java - 暂时保持简单
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
        System.out.println("酒店预订系统启动中...");
        System.out.println("请运行 TestMain 进行开发测试");
        
        // 等JavaFX环境完善后再启动图形界面
        // MainApp.launch(MainApp.class, args);
    }
}