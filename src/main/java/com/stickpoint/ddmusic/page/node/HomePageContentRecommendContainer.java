package com.stickpoint.ddmusic.page.node;

import javafx.scene.layout.HBox;

/**
 * 首页-顶点音乐顶部推荐模块（公益寻亲推荐内容板块）
 * @author fntp
 * @date 2025/8/30
 */
public class HomePageContentRecommendContainer extends HBox {


    public HomePageContentRecommendContainer() {
        // 设置样式
        getStyleClass().add("recommend-container");
        // 设置垂直布局优先级，确保与轮播图等高
        setMinHeight(138);
    }

}
