package com.stickpoint.ddmusic.page.node;

import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * 首页内容区域
 * @author fntp
 * @date 2025/8/27
 */
public class HomePageContentContainer extends VBox {
    public HomePageContentContainer() {
        // 设置整体样式
        setStyle("-fx-background-color: #f5f5f5; " +
                "-fx-padding: 20px; " +
                "-fx-border-width: 1px; " +
                "-fx-border-color: #ddd; " +
                "-fx-border-radius: 8px;");

        // 设置垂直布局优先级
        setVgrow(new Region(), Priority.ALWAYS);
    }

}
