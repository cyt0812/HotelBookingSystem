/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.util;

/**
 *
 * @author LENOVO
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;

public class SceneManager {

    private static Stage mainStage;

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchScene(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
            mainStage.setTitle(title);
            mainStage.setScene(new Scene(root, 800, 600));
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void loadIntoPane(String fxmlPath, Pane targetPane) {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/com/hotelbooking/view/" + fxmlPath));
            targetPane.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

