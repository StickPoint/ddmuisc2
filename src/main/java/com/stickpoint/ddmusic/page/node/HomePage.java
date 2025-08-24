package com.stickpoint.ddmusic.page.node;

import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;

/**
 * 首页-顶点音乐播放器首页
 * @author fntp
 * @date 2025/8/24
 */
public class HomePage extends AnchorPane {

    private final HomePageMenuPanel homePageMenuPanel;
    private final ContentPanel contentPanel;

    public HomePage() {
        homePageMenuPanel = new HomePageMenuPanel();
        contentPanel = new ContentPanel();

        // 设置锚点，实现响应式布局
        AnchorPane.setTopAnchor(homePageMenuPanel, 0.0);
        AnchorPane.setBottomAnchor(homePageMenuPanel, 0.0);
        AnchorPane.setLeftAnchor(homePageMenuPanel, 0.0);

        AnchorPane.setTopAnchor(contentPanel, 0.0);
        AnchorPane.setBottomAnchor(contentPanel, 0.0);
        AnchorPane.setLeftAnchor(contentPanel, 200.0);
        AnchorPane.setRightAnchor(contentPanel, 0.0);

        //// 设置左侧菜单
        //setLeft(homePageMenuPanel);
        //
        //// 设置右侧内容区（占位）
        //Region contentArea = new Region();
        //contentArea.setPrefSize(800, 600);
        //contentArea.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, null)));
        //setCenter(contentArea);

        getChildren().addAll(homePageMenuPanel, contentPanel);

        // 设置整体背景
        //setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));

        // 设置内边距
        setPadding(new Insets(10));

        // 使用Clip实现圆角
        //Rectangle clip = new Rectangle();
        //clip.setArcWidth(18);
        //clip.setArcHeight(18);
        //clip.widthProperty().bind(widthProperty());
        //clip.heightProperty().bind(heightProperty());
        //setClip(clip);

        // 在 HomePage 构造函数最后添加
        setStyle("-fx-background-color: white; -fx-background-radius: 18px;");

        //setStyle("-fx-background-color: white;");
    }

    public HomePageMenuPanel getMenuPanel() {
        return homePageMenuPanel;
    }
}
