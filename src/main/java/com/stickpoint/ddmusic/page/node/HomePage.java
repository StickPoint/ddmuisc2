package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
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
    private LocalDownloadPage localDownloadPage;
    private final MusicState musicState;
    
    // 保存右侧内容容器的引用
    private final BorderPane centerPanel;
    private SearchResultContainer searchResultContainer;
    private boolean isSearching = false;
    
    // 歌单详情页面
    private PlaylistDetailContainer playlistDetailContainer;
    
    // 保存上一个页面的引用，用于从详情页面返回时回到正确的页面
    private Node previousCenterContent;
    
    public HomePage() {
        musicState = new MusicState();
        bottomMusicContainer = new BottomMusicContainer(musicState);
        headerContainer = new HomePageHeaderContainer();
        homePageMenuPanel = new HomePageMenuPanel();
        homePageContentContainer = new HomePageContentContainer();
        musicPlayDetailContainer = new MusicPlayDetailContainer(musicState);
        musicPlayDetailContainer.setVisible(false);
        localDownloadPage = new LocalDownloadPage(musicState, bottomMusicContainer);
        localDownloadPage.setVisible(false);
        searchResultContainer = new SearchResultContainer(musicState, bottomMusicContainer);
        searchResultContainer.setVisible(false);
        
        // 初始化歌单详情页面
        playlistDetailContainer = new PlaylistDetailContainer(musicState, bottomMusicContainer);
        playlistDetailContainer.setVisible(false);

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

        // 为搜索框添加回车键事件
        headerContainer.getSearchField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String keyword = headerContainer.getSearchField().getText().trim();
                if (!keyword.isEmpty()) {
                    performSearch(keyword);
                }
            }
        });

        // 添加菜单点击事件处理
        // 直接从HomePageMenuPanel获取ToggleGroup
        ToggleGroup menuToggleGroup = homePageMenuPanel.getToggleGroup();
        
        // 监听菜单选中事件
        if (menuToggleGroup != null) {
            menuToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle instanceof HomePageMenuItem) {
                    HomePageMenuItem selectedMenuItem = (HomePageMenuItem) newToggle;
                    String menuId = selectedMenuItem.getId();
                    if ("myMusic".equals(menuId)) {
                        // 显示本地下载页面
                        showLocalDownloadPage();
                    } else if ("recommend".equals(menuId)) {
                        // 显示发现音乐页面（首页）
                        backToHome();
                    } else {
                        // 其他菜单项，返回主页
                        backToHome();
                    }
                }
            });
        }

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
        
        // 设置发现音乐容器的歌单点击监听器
        homePageContentContainer.getDiscoverMusicContainer().setPlaylistClickListener(
            (playlistId, playlistName, coverUrl, source) -> {
                // 保存当前页面
                previousCenterContent = centerPanel.getCenter();
                
                // 设置歌单详情数据
                playlistDetailContainer.setPlaylistData(playlistId, playlistName, coverUrl, source);
                
                // 显示歌单详情页面
                playlistDetailContainer.setVisible(true);
                centerPanel.setCenter(playlistDetailContainer);
            }
        );
    }

    /**
     * 显示音乐详情页面
     */
    public void showMusicDetail() {
        System.out.println("调用了显示音乐播放器详情页面");
        // 隐藏原有的中心内容
        if (getCenter() != musicPlayDetailContainer) {
            // 保存当前的中心内容作为上一个页面
            previousCenterContent = getCenter();
            // 隐藏左侧菜单栏实现全屏效果
            setLeft(null);
            // 显示音乐详情页面
            musicPlayDetailContainer.setVisible(true);
            // 直接切换中心内容，而不是隐藏其他组件
            setCenter(musicPlayDetailContainer);
        }
    }

    /**
     * 执行搜索
     * @param keyword 搜索关键词
     */
    private void performSearch(String keyword) {
        isSearching = true;
        // 设置搜索结果页面
        searchResultContainer.setVisible(true);
        centerPanel.setCenter(searchResultContainer);
        // 执行搜索
        searchResultContainer.search(keyword);
    }

    /**
     * 返回主页或上一个页面
     */
    public void backToHome() {
        System.out.println("调用了返回主页");
        // 恢复左侧菜单栏
        setLeft(homePageMenuPanel);
        
        // 恢复原来的布局结构
        centerPanel.setTop(headerContainer);
        centerPanel.setCenter(homePageContentContainer);
        setCenter(centerPanel);
        
        // 重置previousCenterContent
        previousCenterContent = null;
        
        // 隐藏音乐详情页面
        musicPlayDetailContainer.setVisible(false);
        
        // 清理搜索结果数据
        searchResultContainer.clear();
    }
    
    /**
     * 显示本地下载页面
     */
    public void showLocalDownloadPage() {
        System.out.println("调用了显示本地下载页面");
        // 显示本地下载页面
        localDownloadPage.setVisible(true);
        // 切换中心内容到本地下载页面
        centerPanel.setCenter(localDownloadPage);
        // 更新previousCenterContent为当前中心内容
        if (getCenter() == centerPanel) {
            previousCenterContent = centerPanel;
        }
    }

    public HomePageMenuPanel getMenuPanel() {
        return homePageMenuPanel;
    }
}