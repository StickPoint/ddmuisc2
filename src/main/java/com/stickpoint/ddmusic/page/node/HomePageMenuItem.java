package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.common.utils.FontUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * 音乐播放器首页-左侧-菜单项（每一个子项）
 * @author fntp
 * @date 2025/8/24
 */
public class HomePageMenuItem extends HBox implements Toggle {

    private final SvgIcon iconLabel;
    private final Label textLabel;
    private final HBox contentContainer;
    // 使用SimpleObjectProperty来管理ToggleGroup
    private final ObjectProperty<ToggleGroup> toggleGroup;
    // 使用SimpleBooleanProperty来管理选中状态
    private final BooleanProperty selected;

    public HomePageMenuItem(String iconPath, Integer iconSize, String text) {
        this.toggleGroup = new SimpleObjectProperty<>();
        this.selected = new SimpleBooleanProperty(false);
        this.iconLabel = new SvgIcon(iconPath);
        if (Objects.nonNull(iconSize)) {
            iconLabel.setIconSize(iconSize, iconSize);
        }
        this.textLabel = new Label(text);
        // 创建内容容器来控制背景范围
        contentContainer = new HBox();
        contentContainer.getChildren().addAll(iconLabel, textLabel);
        contentContainer.setSpacing(15);
        contentContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        contentContainer.setPadding(new Insets(0, 50, 0, 10));
        iconLabel.setPadding(new Insets(0, 8, 0, 0));
        // 设置文字样式
        textLabel.setFont(FontUtil.loadFont("/font/Y-B008YeZiGongChangDanDanHei-2.ttf", 15));
        textLabel.setTextFill(Color.web("#7f7f7f"));

        // 组合布局
        getChildren().add(contentContainer);
        setPrefHeight(40);
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        setStyle("-fx-background-color: transparent;");

        // 添加点击事件处理
        setOnMouseClicked(event -> {
            if (getToggleGroup() != null) {
                // 通过ToggleGroup来管理选中状态，确保互斥
                getToggleGroup().selectToggle(this);
            } else {
                // 如果没有设置ToggleGroup，则独立切换状态
                setSelected(!isSelected());
            }
        });

        // 监听selected属性的变化，更新样式
        selectedProperty().addListener((obs, oldVal, newVal) -> updateStyle(newVal));

        // 初始化样式
        updateStyle(false);
    }

    @Override
    public ToggleGroup getToggleGroup() {
        return toggleGroup.get();
    }

    @Override
    public void setToggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup.set(toggleGroup);
    }

    @Override
    public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        return toggleGroup;
    }

    @Override
    public boolean isSelected() {
        return selected.get();
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * 更新样式
     * @param isSelected 是否选中
     */
    private void updateStyle(boolean isSelected) {
        if (isSelected) {
            // 选中状态样式
            iconLabel.setFill("#ff3c3c");
            // 红色图标
            textLabel.setTextFill(Color.RED);
            // 红色文字
            contentContainer.setStyle("-fx-background-color: #fff0f0; -fx-background-radius: 4px;");
            // 浅红色背景
        } else {
            // 未选中状态样式
            iconLabel.setFill("#7f7f7f");
            // 灰色图标
            textLabel.setTextFill(Color.web("#666"));
            // 灰色文字
            contentContainer.setStyle("-fx-background-color: transparent;");
            // 透明背景
        }
    }

    /**
     * 设置悬停效果
     * @param hoverable 是否启用悬停效果
     */
    public void setHoverable(boolean hoverable) {
        if (hoverable) {
            setOnMouseEntered(e -> {
                if (!isSelected()) {
                    contentContainer.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 4px;");
                }
            });
            setOnMouseExited(e -> {
                if (!isSelected()) {
                    updateStyle(isSelected());
                }
            });
        } else {
            setOnMouseEntered(null);
            setOnMouseExited(null);
        }
    }
}