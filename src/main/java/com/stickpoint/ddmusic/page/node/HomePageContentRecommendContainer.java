package com.stickpoint.ddmusic.page.node;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
        imageView.setPreserveRatio(false); // 不保持比例，强制填充
        // 宽度自适应容器
        imageView.setFitWidth(453); // 默认宽度，会根据容器调整

        // 将ImageView添加到容器中
        getChildren().add(imageView);
    }

}
