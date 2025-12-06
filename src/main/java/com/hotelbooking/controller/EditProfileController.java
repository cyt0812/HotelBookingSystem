/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProfileController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private TextField txtFullName;

    private User currentUser;

    // 回调接口
    private Runnable onSaveCallback;

    @FXML
    public void initialize() {
        currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            txtUsername.setText(currentUser.getUsername());
            txtEmail.setText(currentUser.getEmail());
            txtFullName.setText(currentUser.getFullName());
        }
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSave() {
        if (currentUser != null) {
            currentUser.setUsername(txtUsername.getText());
            currentUser.setEmail(txtEmail.getText());
            currentUser.setFullName(txtFullName.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Saved");
            alert.setHeaderText(null);
            alert.setContentText("Your profile has been updated!");
            alert.showAndWait();

            // 调用回调刷新 UserProfile 页面
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            closeWindow();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }
}