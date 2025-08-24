package com.stickpoint.ddmusic.page.node;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

/**
 * 音乐播放器首页-左侧-菜单项（每一个子项）
 * @author fntp
 * @date 2025/8/24
 */
public class HomePageMenuItem extends HBox {

    private final SvgIcon iconLabel;
    private final Label textLabel;
    private final HBox contentContainer;

    public HomePageMenuItem(String iconPath, Integer iconSize, String text) {
        this.iconLabel = new SvgIcon(iconPath);
        if (Objects.nonNull(iconSize)) {
            iconLabel.setIconSize(iconSize, iconSize);
        }
        this.textLabel = new Label(text);
        //iconLabel.setPadding(new Insets(0, 8, 0, 12));

        // 创建内容容器来控制背景范围
        contentContainer = new HBox();
        contentContainer.getChildren().addAll(iconLabel, textLabel);
        contentContainer.setSpacing(8);
        contentContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        contentContainer.setPadding(new Insets(0, 50, 0, 10));
        // 原来的设置保持不变
        iconLabel.setPadding(new Insets(0, 8, 0, 0));
        // 设置文字样式
        textLabel.setFont(Font.font("Microsoft YaHei", 13));
        textLabel.setTextFill(Color.web("#7f7f7f"));
        // 组合布局
        getChildren().add(contentContainer);
        setPrefHeight(40);
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        setStyle("-fx-background-color: transparent;");
    }

    public void setSelected(boolean selected) {
        if (selected) {
            iconLabel.setFill("#7f7f7f");
            textLabel.setTextFill(Color.RED);
            contentContainer.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 4px;");
        } else {
            iconLabel.setFill("#7f7f7f");
            textLabel.setTextFill(Color.web("#666"));
            contentContainer.setStyle("-fx-background-color: transparent;");
        }
    }

    public void setHoverable(boolean hoverable) {
        if (hoverable) {
            setOnMouseEntered(e -> contentContainer.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 4px;"));
            setOnMouseExited(e -> contentContainer.setStyle("-fx-background-color: transparent;"));
        }
    }
}
