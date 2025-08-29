package com.stickpoint.ddmusic.page.node;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * 公共音乐播放功能区域
 * @author fntp
 * @date 2025/8/27
 */
public class CommonMusicPlayOpContainer extends HBox {

    public CommonMusicPlayOpContainer() {
        // 设置整体样式
        setStyle("-fx-background-color: white; " +
                "-fx-padding: 12px; " +
                "-fx-border-width: 1px; " +
                "-fx-border-color: #ddd; " +
                "-fx-border-radius: 8px; " +
                "-fx-shadow: 0 2px 8px rgba(0,0,0,0.1);");

        // 设置水平布局优先级
        setHgrow(new Region(), Priority.ALWAYS);
    }



}
