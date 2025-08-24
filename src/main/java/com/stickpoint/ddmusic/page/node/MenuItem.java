package com.stickpoint.ddmusic.page.node;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author fntp
 * @date 2025/8/24
 */
public class MenuItem extends HBox {

    private final Label iconLabel;
    private final Label textLabel;

    public MenuItem(String icon, String text) {
        this.iconLabel = new Label(icon);
        this.textLabel = new Label(text);

        // 设置图标样式
        iconLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        iconLabel.setTextFill(Color.web("#666"));
        iconLabel.setPadding(new javafx.geometry.Insets(0, 8, 0, 12));

        // 设置文字样式
        textLabel.setFont(Font.font("Microsoft YaHei", 13));
        textLabel.setTextFill(Color.web("#666"));
        textLabel.setPadding(new javafx.geometry.Insets(0, 0, 0, 8));

        // 组合布局
        getChildren().addAll(iconLabel, textLabel);
        setSpacing(8);
        setPrefHeight(40);
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 0;");
    }

    public void setSelected(boolean selected) {
        if (selected) {
            iconLabel.setTextFill(Color.RED);
            textLabel.setTextFill(Color.RED);
            setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 4px;");
        } else {
            iconLabel.setTextFill(Color.web("#666"));
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
