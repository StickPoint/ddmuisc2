package com.stickpoint.ddmusic.page.component;


import com.stickpoint.ddmusic.App;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Image;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author fearless
 * @version v1.0
 * @description: 系统托盘图标组件（纯JavaFX实现）
 * @date 2021/12/30 下午6:23
 */
public class DdMusicTray extends TrayIcon {

    private static Stage trayMenuStage;

    public DdMusicTray(Image image, String tooltip) {
        super(image, tooltip);

        // 创建菜单面板
        VBox menuBox = createSystemTrayMenu();

        // 使用静态变量确保只创建一个Stage
        if (trayMenuStage == null) {
            trayMenuStage = new Stage();
            trayMenuStage.initStyle(StageStyle.TRANSPARENT);
            // 每次创建新的Pane
            StackPane pane = new StackPane();
            trayMenuStage.setScene(new Scene(pane));
            // 监听面板焦点
            trayMenuStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (Boolean.FALSE.equals(newValue)) {
                    trayMenuStage.hide();
                }
            });
            // 添加组件到面板中
            pane.getChildren().add(menuBox);
            // 设置面板的宽高
            trayMenuStage.setWidth(125);
            trayMenuStage.setHeight(160);
        }

        // 设置系统托盘图标为自适应
        this.setImageAutoSize(true);

        // 添加鼠标事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Platform.runLater(() -> {
                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        double screenWidth = screenBounds.getWidth();
                        double screenHeight = screenBounds.getHeight();

                        trayMenuStage.setX(screenWidth - trayMenuStage.getWidth());
                        trayMenuStage.setY(screenHeight - trayMenuStage.getHeight());
                        trayMenuStage.setAlwaysOnTop(true);

                        if (!trayMenuStage.isShowing()) {
                            trayMenuStage.show();
                        } else {
                            trayMenuStage.hide();
                        }
                    });
                }
            }
        });
    }

    /**
     * 创建系统托盘菜单（纯JavaFX代码实现）
     * @return VBox菜单容器
     */
    private VBox createSystemTrayMenu() {
        VBox contentBox = new VBox();
        contentBox.setId("contentBox");
        contentBox.setPrefWidth(125);
        contentBox.setPrefHeight(160);

        // 返回首页菜单项
        HBox openOriginalPage = createMenuItem("showMainIcon", "返回首页");
        openOriginalPage.setId("openOriginalPage");

        // 为返回首页菜单项添加点击事件
        openOriginalPage.setOnMouseClicked(event -> Platform.runLater(() -> {
            Stage mainStage = App.getStage();
            if (!mainStage.isShowing()) {
                mainStage.show();
            }
            mainStage.setIconified(false);
            mainStage.toFront();
            // 隐藏系统托盘菜单
            Stage trayStage = (Stage) contentBox.getScene().getWindow();
            trayStage.hide();
        }));

        // 一键静音菜单项
        HBox muteItem = createMenuItem("baseSettingIcon", "一键静音");

        // 桌面歌词菜单项
        HBox desktopLyricItem = createMenuItem("lockIcon", "桌面歌词");

        // 退出顶点菜单项
        HBox exitItem = createMenuItem("exitIcon", "退出顶点");

        contentBox.getChildren().addAll(openOriginalPage, muteItem, desktopLyricItem, exitItem);

        AnchorPane contentPane = new AnchorPane();
        contentPane.setId("contentPane");
        contentPane.setPrefWidth(125);
        contentPane.setPrefHeight(160);
        contentPane.getChildren().add(contentBox);
        AnchorPane.setLeftAnchor(contentBox, -1.0);

        AnchorPane rootPane = new AnchorPane();
        rootPane.setId("rootPane");
        rootPane.setPrefWidth(125);
        rootPane.setPrefHeight(160);
        rootPane.getChildren().add(contentPane);

        return contentBox;
    }

    /**
     * 创建菜单项
     * @param iconId 图标ID
     * @param text 菜单项文本
     * @return HBox菜单项容器
     */
    private HBox createMenuItem(String iconId, String text) {
        HBox menuItem = new HBox();
        menuItem.setId("menu-item");
        menuItem.setPrefWidth(125);
        menuItem.setPrefHeight(40);
        Region icon = new Region();
        icon.setId(iconId);
        icon.setMaxHeight(22);
        icon.setMaxWidth(22);
        icon.setMinHeight(22);
        icon.setMinWidth(22);
        icon.setPrefHeight(22);
        icon.setPrefWidth(22);

        HBox.setMargin(icon, new Insets(9, 5, 4, 12));

        Label label = new Label(text);
        label.setFont(new Font(14));

        if ("返回首页".equals(text)) {
            HBox.setMargin(label, new Insets(10, 0, 0, 7.5));
        } else if ("一键静音".equals(text)) {
            HBox.setMargin(label, new Insets(10, 0, 0, 7.5));
        } else if ("桌面歌词".equals(text)) {
            HBox.setMargin(label, new Insets(10, 0, 0, 7.5));
        } else if ("退出顶点".equals(text)) {
            HBox.setMargin(label, new Insets(10, 0, 0, 8));
        }

        menuItem.getChildren().addAll(icon, label);

        return menuItem;
    }

}
