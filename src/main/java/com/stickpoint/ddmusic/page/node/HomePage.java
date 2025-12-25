package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;

/**
 * 首页-顶点音乐播放器首页
 * @author fntp
 * @date 2025/8/24
 */
public class HomePage extends BorderPane {

    private final HomePageMenuPanel homePageMenuPanel;
    private final HomePageHeaderContainer headerContainer;
    private final HomePageContentContainer homePageContentContainer;
    private final BottomMusicContainer bottomMusicContainer;
    private final MusicPlayDetailContainer musicPlayDetailContainer;
    private final MusicState musicState;

    // 保存右侧内容容器的引用
    private final BorderPane centerPanel;

    public HomePage() {
        musicState = new MusicState();
        bottomMusicContainer = new BottomMusicContainer(musicState);
        headerContainer = new HomePageHeaderContainer();
        homePageMenuPanel = new HomePageMenuPanel();
        homePageContentContainer = new HomePageContentContainer();
        musicPlayDetailContainer = new MusicPlayDetailContainer(musicState);
        musicPlayDetailContainer.setVisible(false);

        // 设置左侧菜单栏
        homePageMenuPanel.setPrefWidth(200);
        setLeft(homePageMenuPanel);

        // 创建右侧内容容器
        centerPanel = new BorderPane();
        centerPanel.setTop(headerContainer);
        centerPanel.setCenter(homePageContentContainer);

        // 设置主区域
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

        // 在构造函数末尾添加专辑封面点击监听
        bottomMusicContainer.setAlbumImageClickHandler(event -> {
            if (getCenter() == musicPlayDetailContainer) {
                // 如果当前显示的是音乐详情页面，则返回主页
                backToHome();
            } else {
                // 如果当前显示的是主页内容，则显示音乐详情
                showMusicDetail();
            }
        });
    }

    /**
     * 显示音乐详情页面
     */
    public void showMusicDetail() {
        System.out.println("调用了显示音乐播放器详情页面");
        // 隐藏原有的中心内容
        if (getCenter() != musicPlayDetailContainer) {
            // 隐藏左侧菜单栏实现全屏效果
            setLeft(null);
            // 显示音乐详情页面
            musicPlayDetailContainer.setVisible(true);
            // 直接切换中心内容，而不是隐藏其他组件
            setCenter(musicPlayDetailContainer);
        }
    }

    /**
     * 返回主页
     */
    public void backToHome() {
        System.out.println("调用了返回主页");
        // 恢复左侧菜单栏
        setLeft(homePageMenuPanel);
        // 恢复原来的布局结构
        setCenter(centerPanel);
        // 隐藏音乐详情页面
        musicPlayDetailContainer.setVisible(false);
    }

    public HomePageMenuPanel getMenuPanel() {
        return homePageMenuPanel;
    }
}