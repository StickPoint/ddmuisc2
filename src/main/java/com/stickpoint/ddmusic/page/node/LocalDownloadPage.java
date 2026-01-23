package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.common.utils.FontUtil;
import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * 本地下载页面
 * @author fntp
 * @date 2025/12/30
 */
public class LocalDownloadPage extends BorderPane {

    private LocalSongPanel localSongPanel;
    private DownloadSongPanel downloadSongPanel;
    private HBox menuBar;
    private HomePageMenuItem localSongMenu;
    private HomePageMenuItem downloadSongMenu;
    private ToggleGroup menuToggleGroup;
    private final MusicState musicState;
    private final BottomMusicContainer bottomMusicContainer;

    public LocalDownloadPage(MusicState musicState, BottomMusicContainer bottomMusicContainer) {
        this.musicState = musicState;
        this.bottomMusicContainer = bottomMusicContainer;
        initialize();
        setupLayout();
    }

    private void initialize() {
        // 创建切换组
        menuToggleGroup = new ToggleGroup();
        
        // 创建子菜单
        localSongMenu = new HomePageMenuItem(null, 16, "本地歌曲");
        localSongMenu.setToggleGroup(menuToggleGroup);
        localSongMenu.setHoverable(true);
        
        downloadSongMenu = new HomePageMenuItem(null, 16, "下载歌曲");
        downloadSongMenu.setToggleGroup(menuToggleGroup);
        downloadSongMenu.setHoverable(true);
        
        // 创建内容面板
        localSongPanel = new LocalSongPanel(musicState, bottomMusicContainer);
        downloadSongPanel = new DownloadSongPanel();
        
        // 创建菜单栏
        menuBar = new HBox();
        menuBar.setSpacing(10);
        menuBar.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0; -fx-padding: 15 20 10 20;");
        menuBar.getChildren().addAll(localSongMenu, downloadSongMenu);
        
        // 设置菜单选中事件
        menuToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == localSongMenu) {
                setCenter(localSongPanel);
            } else if (newToggle == downloadSongMenu) {
                setCenter(downloadSongPanel);
            }
        });
    }

    private void setupLayout() {
        // 设置标题
        Label title = new Label("本地下载");
        title.setFont(FontUtil.loadFont("/font/Y-B008YeZiGongChangDanDanHei-2.ttf", 24));
        title.setTextFill(Color.RED);
        
        // 创建顶部区域
        HBox topArea = new HBox();
        topArea.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20 20 10 20; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
        topArea.getChildren().add(title);
        
        // 设置布局
        setTop(topArea);
        setCenter(menuBar);
        
        // 默认选中本地歌曲
        menuToggleGroup.selectToggle(localSongMenu);
    }
}
