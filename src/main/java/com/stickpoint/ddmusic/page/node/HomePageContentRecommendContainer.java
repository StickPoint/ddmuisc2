package com.stickpoint.ddmusic.page.node;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

/**
 * 首页-顶点音乐顶部推荐模块（公益寻亲推荐内容板块）
 * @author fntp
 * @date 2025/8/30
 */
public class HomePageContentRecommendContainer extends HBox {


    public HomePageContentRecommendContainer() {
        // 设置样式
        //getStyleClass().add("recommend-container");
        // 设置容器高度
        setMinHeight(138);
        setPrefHeight(138);

        // 设置水平增长策略
        setHgrow(this, Priority.ALWAYS);

        // 设置容器圆角
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.setHeight(138);
        // 设置圆角宽度
        clip.setArcWidth(20);
        // 设置圆角高度
        clip.setArcHeight(20);
        setClip(clip);

        // 创建ImageView并加载图片
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/img/ddgy.jpg")).toExternalForm()));
        // 设置图片高度
        imageView.setFitHeight(138);
        // 不保持比例，强制填充
        imageView.setPreserveRatio(false);
        // 宽度自适应容器
        // 默认宽度，会根据容器调整
        imageView.setFitWidth(453);

        // 将ImageView添加到容器中
        getChildren().add(imageView);

        // 【替换最后两行】使用属性绑定实现宽度自适应
        imageView.fitWidthProperty().bind(this.widthProperty());
        // 确保HBox本身在父布局中也能扩展（如果必要）
        HBox.setHgrow(this, Priority.ALWAYS);
        //HBox.setMargin(this, new Insets(0, 5, 0, 0));
        // 为当前container增加宽度变化监听，如果宽度小于200 泽打印日志
        widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() < 300) {
                HBox.setMargin(this, new Insets(0, 10, 0, 0));
            }else {
                HBox.setMargin(this, new Insets(0, 0, 0, 0));
            }
        });
    }

}
