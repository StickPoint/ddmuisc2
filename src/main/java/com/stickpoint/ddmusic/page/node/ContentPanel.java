package com.stickpoint.ddmusic.page.node;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author fntp
 * @date 2025/8/24
 */
public class ContentPanel extends VBox{
    public ContentPanel() {
        // 使用 VBox 或其他布局容器
        setStyle("-fx-background-color: #f5f5f5;");

        // 内容区域会自动填充剩余空间
        VBox.setVgrow(this, Priority.ALWAYS);
    }
}
