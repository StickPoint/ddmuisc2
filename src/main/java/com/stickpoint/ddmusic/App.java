package com.stickpoint.ddmusic;

import com.stickpoint.ddmusic.common.config.DdmusicSpiMonitor;
import com.stickpoint.ddmusic.common.utils.SystemPropertiesUtil;
import com.stickpoint.ddmusic.page.node.HomePage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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
        //initApplicationProperties();
    }


    @Override
    @SuppressWarnings("exports")
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("/fxml/player.fxml")));
        HomePage homePage = new HomePage();
        Scene scene = new Scene(homePage, 1060,750, Color.TRANSPARENT);
        primaryStage.setTitle("DdMusic");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setFullScreenExitHint("1");
        // 当点击了窗口的功能放大窗口后
        //primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/logo.svg")).toExternalForm()));
        primaryStage.show();
        addResizeSupport(primaryStage, scene);
        primaryStage.centerOnScreen();
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
     * 添加窗口缩放功能
     * @param primaryStage 主窗口
     * @param scene 主场景
     */
    private void addResizeSupport(Stage primaryStage, Scene scene) {
        // 存储窗口原始尺寸和位置
        // x, y, width, height
        final double[] original = new double[4];
        // 使用字符串代替 Cursor 枚举
        final String[] currentCursor = {""};
        scene.setOnMouseMoved(event -> {
            // 检测鼠标是否在窗口边缘，用于显示调整大小的光标
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = primaryStage.getWidth();
            double height = primaryStage.getHeight();
            // 边缘检测阈值
            int border = 5;
            if (x < border && y < border) {
                scene.setCursor(javafx.scene.Cursor.NW_RESIZE);
                currentCursor[0] = "NW_RESIZE";
            } else if (x < border && y > height - border) {
                scene.setCursor(javafx.scene.Cursor.SW_RESIZE);
                currentCursor[0] = "SW_RESIZE";
            } else if (x > width - border && y < border) {
                scene.setCursor(javafx.scene.Cursor.NE_RESIZE);
                currentCursor[0] = "NE_RESIZE";
            } else if (x > width - border && y > height - border) {
                scene.setCursor(javafx.scene.Cursor.SE_RESIZE);
                currentCursor[0] = "SE_RESIZE";
            } else if (x < border) {
                scene.setCursor(javafx.scene.Cursor.W_RESIZE);
                currentCursor[0] = "W_RESIZE";
            } else if (x > width - border) {
                scene.setCursor(javafx.scene.Cursor.E_RESIZE);
                currentCursor[0] = "E_RESIZE";
            } else if (y < border) {
                scene.setCursor(javafx.scene.Cursor.N_RESIZE);
                currentCursor[0] = "N_RESIZE";
            } else if (y > height - border) {
                scene.setCursor(javafx.scene.Cursor.S_RESIZE);
                currentCursor[0] = "S_RESIZE";
            } else {
                scene.setCursor(javafx.scene.Cursor.DEFAULT);
                currentCursor[0] = "DEFAULT";
            }
        });
        scene.setOnMousePressed(event -> {
            // 记录按下时的窗口状态
            original[0] = primaryStage.getX();
            original[1] = primaryStage.getY();
            original[2] = primaryStage.getWidth();
            original[3] = primaryStage.getHeight();

            offsetX = event.getSceneX();
            offsetY = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            // 如果鼠标在拖拽调整窗口大小的区域
            if (!"DEFAULT".equals(currentCursor[0]) && !"OPEN_HAND".equals(currentCursor[0])) {
                double deltaX = event.getScreenX() - original[0] - original[2];
                double deltaY = event.getScreenY() - original[1] - original[3];
                switch (currentCursor[0]) {
                    case "NW_RESIZE": // 左上角
                        primaryStage.setX(event.getScreenX() - offsetX);
                        primaryStage.setY(event.getScreenY() - offsetY);
                        primaryStage.setWidth(original[2] - (event.getScreenX() - original[0] - offsetX));
                        primaryStage.setHeight(original[3] - (event.getScreenY() - original[1] - offsetY));
                        break;
                    case "SW_RESIZE": // 左下角
                        primaryStage.setX(event.getScreenX() - offsetX);
                        primaryStage.setWidth(original[2] - (event.getScreenX() - original[0] - offsetX));
                        primaryStage.setHeight(original[3] + deltaY);
                        break;
                    case "NE_RESIZE": // 右上角
                        primaryStage.setY(event.getScreenY() - offsetY);
                        primaryStage.setWidth(original[2] + deltaX);
                        primaryStage.setHeight(original[3] - (event.getScreenY() - original[1] - offsetY));
                        break;
                    case "SE_RESIZE": // 右下角
                        primaryStage.setWidth(original[2] + deltaX);
                        primaryStage.setHeight(original[3] + deltaY);
                        break;
                    case "W_RESIZE": // 左边
                        primaryStage.setX(event.getScreenX() - offsetX);
                        primaryStage.setWidth(original[2] - (event.getScreenX() - original[0] - offsetX));
                        break;
                    case "E_RESIZE": // 右边
                        primaryStage.setWidth(original[2] + deltaX);
                        break;
                    case "N_RESIZE": // 上边
                        primaryStage.setY(event.getScreenY() - offsetY);
                        primaryStage.setHeight(original[3] - (event.getScreenY() - original[1] - offsetY));
                        break;
                    case "S_RESIZE": // 下边
                        primaryStage.setHeight(original[3] + deltaY);
                        break;
                }
            } else {
                // 原有的窗口拖拽移动功能
                primaryStage.setX(event.getScreenX() - offsetX);
                primaryStage.setY(event.getScreenY() - offsetY);
            }
        });
    }


    /**
     * 首页关闭的时候，整个程序直接关闭
     */
    @Override
    public void stop() {
        System.exit(0);
    }
}
