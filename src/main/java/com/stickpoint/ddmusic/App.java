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

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * @author fntp
 * @date 2025/8/23
 */
public class App extends Application {

    private static Stage stage;

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
    public void start(Stage primaryStage) throws Exception {
        log.info("=== 应用启动调试信息 ===");
        log.info("Java 版本: " + System.getProperty("java.version"));
        log.info("JavaFX 版本: " + System.getProperty("javafx.version", "未知"));
        log.info("工作目录: " + System.getProperty("user.dir"));

        try {
            log.info("1. 开始创建 HomePage");
            HomePage homePage = new HomePage();
            log.info("2. HomePage 创建成功");

            log.info("3. 开始创建场景");
            Scene scene = new Scene(homePage, 1060, 750, Color.TRANSPARENT);
            log.info("4. 场景创建成功");

            log.info("5. 设置舞台属性");
            primaryStage.setTitle("DdMusic");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(878);
            primaryStage.setMinHeight(600);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setFullScreenExitHint("1");
            setStage(primaryStage);
            log.info("6. 舞台属性设置完成");

            log.info("7. 加载图标");
            java.net.URL logoUrl = getClass().getResource("/img/logo.svg");
            if (logoUrl != null) {
                log.info("图标URL: " + logoUrl);
                Image logoImage = new Image(logoUrl.toExternalForm());
                primaryStage.getIcons().add(logoImage);
                log.info("8. 图标加载成功");
            } else {
                log.info("警告: 找不到图标文件，继续启动");
            }

            log.info("9. 显示舞台");
            primaryStage.show();
            log.info("10. 添加窗口缩放支持");
            addResizeSupport(primaryStage, scene);
            log.info("11. 居中显示");
            primaryStage.centerOnScreen();
            log.info("12. 应用启动完成");

        } catch (Exception e) {
            System.err.println("=== 应用启动异常详情 ===");
            System.err.println("异常类型: " + e.getClass().getName());
            System.err.println("异常信息: " + e.getMessage());
            System.err.println("堆栈跟踪:");
            e.printStackTrace();

            Throwable cause = e.getCause();
            int level = 1;
            while (cause != null) {
                System.err.println("第 " + level + " 层原因:");
                System.err.println("  类型: " + cause.getClass().getName());
                System.err.println("  信息: " + cause.getMessage());
                cause.printStackTrace();
                cause = cause.getCause();
                level++;
            }

            throw e;
        }
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
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            for (TrayIcon icon : systemTray.getTrayIcons()) {
                systemTray.remove(icon);
            }
        }
        System.exit(0);
    }

    public void setStage(Stage stage) {
        App.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

}
