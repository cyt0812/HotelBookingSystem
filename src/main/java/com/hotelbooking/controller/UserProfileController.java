///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.hotelbooking.controller;
//
///**
// *
// * @author a1-6
// */
//import com.hotelbooking.entity.User;
//import com.hotelbooking.util.SessionManager;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.stage.FileChooser;
//
//import java.io.File;
//
//public class UserProfileController {
//
//    @FXML private Label lblUsername;
//    @FXML private Label lblEmail;
//    @FXML private Label lblMemberSince;
//    @FXML private ImageView avatarImage;
//
//    private User currentUser;
//
//    @FXML
//    public void initialize() {
//        // 从 SessionManager 获取当前登录用户
//        currentUser = SessionManager.getCurrentUser();
//
//        if (currentUser == null) {
//            System.out.println("No user logged in!");
//            return;
//        }
//
//        lblUsername.setText(currentUser.getUsername());
//        lblEmail.setText(currentUser.getEmail());
//        lblMemberSince.setText(currentUser.getMemberSince());
//
//        // 默认头像
//        if (currentUser.getAvatarPath() != null) {
//            avatarImage.setImage(new Image(currentUser.getAvatarPath()));
//        } else {
//            avatarImage.setImage(new Image(
//                getClass().getResource("/com/hotelbooking/assets/default_avatar.png").toString()
//            ));
//        }
//    }
//
//    @FXML
//    private void handleEditProfile() {
//        System.out.println("User clicked edit profile.");
//        // 这里未来可以弹出一个弹窗或跳转到编辑界面
//    }
//
//    @FXML
//    private void handleChangeAvatar() {
//        FileChooser chooser = new FileChooser();
//        chooser.setTitle("Choose Avatar Image");
//        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg"));
//
//        File file = chooser.showOpenDialog(null);
//        if (file != null) {
//            avatarImage.setImage(new Image(file.toURI().toString()));
//            currentUser.setAvatarPath(file.toURI().toString());
//            System.out.println("Avatar updated: " + file.getAbsolutePath());
//        }
//    }
//}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.controller;

/**
 *
 * @author a1-6
 */
import com.hotelbooking.entity.User;
import com.hotelbooking.util.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserProfileController {

    @FXML private Label lblUsername;
    @FXML private Label lblEmail;
    @FXML private Label lblFullName;

    private User currentUser;

    @FXML
    public void initialize() {
        // 从 SessionManager 获取当前登录用户
        currentUser = SessionManager.getCurrentUser();

        if (currentUser == null) {
            System.out.println("No user logged in!");
            return;
        }

        lblUsername.setText(currentUser.getUsername());
        lblEmail.setText(currentUser.getEmail());
        lblFullName.setText(currentUser.getFullName());

    }

    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/edit_profile.fxml")
            );
            Parent root = loader.load();

            EditProfileController controller = loader.getController();
            // 设置回调：保存后刷新页面
            controller.setOnSaveCallback(this::refreshProfile);

            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            stage.setScene(new Scene(root));
            stage.show();

            System.out.println("➡ 打开编辑资料窗口");

        } catch (Exception e) {
    System.err.println("❌ 无法打开编辑资料窗口");
    e.printStackTrace();
}
    }



    private void refreshProfile() {
    currentUser = SessionManager.getCurrentUser();
    if (currentUser != null) {
        lblUsername.setText(currentUser.getUsername());
        lblEmail.setText(currentUser.getEmail());
        lblFullName.setText(currentUser.getFullName()); // 如果 lblMemberSince 要显示 fullName
    }
        System.out.println("🔄 用户资料已刷新");
    }
}