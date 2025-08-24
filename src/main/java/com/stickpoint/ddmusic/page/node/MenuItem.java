package com.stickpoint.ddmusic.page.node;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

/**
 * @author fntp
 * @date 2025/8/24
 */
public class MenuItem extends HBox {

    private final SvgIcon iconLabel;
    private final Label textLabel;

    public MenuItem(String iconPath, Integer iconSize, String text) {
        this.iconLabel = new SvgIcon(iconPath);
        if (Objects.nonNull(iconSize)) {
            iconLabel.setIconSize(iconSize, iconSize);
        }
        this.textLabel = new Label(text);
        iconLabel.setPadding(new Insets(0, 8, 0, 12));

        // 设置文字样式
        textLabel.setFont(Font.font("Microsoft YaHei", 13));
        textLabel.setTextFill(Color.web("#7f7f7f"));
        textLabel.setPadding(new Insets(0, 0, 0, 8));
        // 组合布局
        getChildren().addAll(iconLabel, textLabel);
        setSpacing(8);
        setPrefHeight(40);
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 0;");
    }

    public void setSelected(boolean selected) {
        if (selected) {
            iconLabel.setFill("#7f7f7f");
            textLabel.setTextFill(Color.RED);
            setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 4px;");
        } else {
            iconLabel.setFill("#7f7f7f");
            textLabel.setTextFill(Color.web("#666"));
            setStyle("-fx-background-color: transparent;");
        }
    }

    public void setHoverable(boolean hoverable) {
        if (hoverable) {
            setOnMouseEntered(e -> setStyle("-fx-background-color: #f0f0f0;"));
            setOnMouseExited(e -> setStyle("-fx-background-color: transparent;"));
        }
    }
}
