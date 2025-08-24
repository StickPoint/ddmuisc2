package com.stickpoint.ddmusic.page.node;

/**
 * @author fntp
 * @date 2025/8/24
 */

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainView extends BorderPane {

    private final MenuPanel menuPanel;

    public MainView() {
        menuPanel = new MenuPanel();

        // 设置左侧菜单
        setLeft(menuPanel);

        // 设置右侧内容区（占位）
        Region contentArea = new Region();
        contentArea.setPrefSize(800, 600);
        contentArea.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, null)));
        setCenter(contentArea);

        // 设置整体背景
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));

        // 设置内边距
        setPadding(new Insets(10));

        // 使用Clip实现圆角
        Rectangle clip = new Rectangle();
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }
}
