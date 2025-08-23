package com.stickpoint.ddmusic;

import com.stickpoint.ddmusic.utils.SystemPropertiesUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fntp
 * @date 2025/8/23
 */
public class App extends Application {

    private double offsetX,offsetY;
    /**
     * 构建日志工具
     */
    private static final Logger log = LoggerFactory.getLogger(App.class);
    /**
     * 系统配置加载工具
     */
    private static final SystemPropertiesUtil SYSTEM_PROPERTIES_UTIL = new SystemPropertiesUtil();


    @Override
    @SuppressWarnings("exports")
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/player.fxml"));
        Scene scene = new Scene(root, null);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setFullScreenExitHint("");
        primaryStage.getIcons().add(new Image(getClass().getResource("/img/logo.png").toExternalForm()));
        primaryStage.show();
        scene.setOnMousePressed(event -> {
            offsetX = event.getSceneX();
            offsetY = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX()-offsetX);
            primaryStage.setY(event.getScreenY()-offsetY);
        });
    }
}
