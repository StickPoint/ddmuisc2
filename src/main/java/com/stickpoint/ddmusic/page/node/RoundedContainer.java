package com.stickpoint.ddmusic.page.node;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author fntp
 * @date 2025/8/24
 */
public class RoundedContainer extends StackPane {
    public RoundedContainer(HomePage homePage) {
        getChildren().add(homePage);

        // 设置圆角矩形作为遮罩
        Rectangle clip = new Rectangle();
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

        // 设置背景
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, null)));
    }
}
