package com.stickpoint.ddmusic;

import com.stickpoint.ddmusic.common.config.DdmusicSpiMonitor;
import com.stickpoint.ddmusic.common.utils.SystemPropertiesUtil;
import com.stickpoint.ddmusic.page.node.HomePage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.ServiceLoader;

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
    public void init() throws Exception {
        super.init();
        // 初始化系统配置
        initApplicationProperties();
    }


    @Override
    @SuppressWarnings("exports")
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("/fxml/player.fxml")));
        HomePage homePage = new HomePage();
        Scene scene = new Scene(homePage, null);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setFullScreenExitHint("1");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/logo.svg")).toExternalForm()));
        primaryStage.show();
        primaryStage.centerOnScreen();
        scene.setOnMousePressed(event -> {
            offsetX = event.getSceneX();
            offsetY = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX()-offsetX);
            primaryStage.setY(event.getScreenY()-offsetY);
        });
    }

    /**
     * 初始化软件基础本地与远程配置
     */
    private static void initApplicationProperties() {
        log.info("开始加载本地系统核心配置参数数据");
        SYSTEM_PROPERTIES_UTIL.loadProperties();
        log.info("开始加载加载远程系统配置");
        ServiceLoader<DdmusicSpiMonitor> s = ServiceLoader.load(DdmusicSpiMonitor.class);
        for (DdmusicSpiMonitor search : s) {
            search.loadRemoteProperties();
        }
    }

    /**
     * 首页关闭的时候，整个程序直接关闭
     */
    @Override
    public void stop() {
        System.exit(0);
    }
}
