package com.stickpoint.ddmusic.page.node;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * 首页-顶点音乐播放器首页
 * @author fntp
 * @date 2025/8/24
 */
public class HomePage extends BorderPane {

    private final HomePageMenuPanel homePageMenuPanel;
    private final ContentPanel contentPanel;
    private final HomePageHeaderContainer headerContainer;
    private final HomePageContentContainer homePageContentContainer;
    private final CommonMusicPlayOpContainer musicPlaybackControlContaine;
    private final BottomMusicContainer bottomMusicContainer;

    public HomePage() {
        headerContainer = new HomePageHeaderContainer();
        homePageMenuPanel = new HomePageMenuPanel();
        contentPanel = new ContentPanel();
        homePageContentContainer = new HomePageContentContainer();
        musicPlaybackControlContaine = new CommonMusicPlayOpContainer();
        bottomMusicContainer = new BottomMusicContainer();

        // 设置左侧菜单栏
        homePageMenuPanel.setPrefWidth(200);
        setLeft(homePageMenuPanel);

        // 创建右侧内容容器
        BorderPane centerPanel = new BorderPane();

        // 将header放在右侧内容区域的顶部
        centerPanel.setTop(headerContainer);

        // 创建主内容区域容器
        VBox mainContentContainer = new VBox();
        mainContentContainer.getChildren().addAll(homePageContentContainer, musicPlaybackControlContaine);
        VBox.setVgrow(homePageContentContainer, Priority.ALWAYS);

        // 将内容面板放在右侧内容区域的中部
        centerPanel.setCenter(contentPanel);

        // 将右侧内容容器设置为主区域
        setCenter(centerPanel);

        setBottom(bottomMusicContainer);

        // 设置内边距
        setPadding(new Insets(0));

        // 设置背景样式
        setStyle("-fx-background-color: white;");

        Rectangle clip = new Rectangle();
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

    }

    public HomePageMenuPanel getMenuPanel() {
        return homePageMenuPanel;
    }
}
