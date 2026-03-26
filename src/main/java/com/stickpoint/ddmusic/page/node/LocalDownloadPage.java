package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.page.state.MusicState;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * 本地下载页面
 * @author fntp
 * @date 2025/12/30
 */
public class LocalDownloadPage extends BorderPane {

    private final LocalSongPanel localSongPanel;
    private final DownloadSongPanel downloadSongPanel;
    private final ToggleButton localSongMenu;
    private final ToggleButton downloadSongMenu;
    private final ToggleGroup menuToggleGroup;
    private final StackPane contentContainer;
    private final Label pageSubtitle;

    public LocalDownloadPage(MusicState musicState, BottomMusicContainer bottomMusicContainer) {
        localSongPanel = new LocalSongPanel(musicState, bottomMusicContainer);
        downloadSongPanel = new DownloadSongPanel();
        menuToggleGroup = new ToggleGroup();
        localSongMenu = createTabButton("本地音乐");
        downloadSongMenu = createTabButton("下载管理");
        pageSubtitle = new Label("管理你的本地曲库与下载任务");
        contentContainer = new StackPane();

        initialize();
        setupLayout();
    }

    private void initialize() {
        localSongMenu.setToggleGroup(menuToggleGroup);
        downloadSongMenu.setToggleGroup(menuToggleGroup);
        localSongMenu.setSelected(true);

        menuToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                if (oldToggle != null) {
                    ((ToggleButton) oldToggle).setSelected(true);
                }
                return;
            }
            updateSelectedTabStyle();
            switchContent(newToggle == localSongMenu);
        });

        updateSelectedTabStyle();
        switchContent(true);
    }

    private void setupLayout() {
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/LocalDownloadPage.css")).toExternalForm());

        Label pageTitle = new Label("本地与下载");
        pageTitle.getStyleClass().add("download-page-title");

        pageSubtitle.getStyleClass().add("download-page-subtitle");

        HBox menuBar = new HBox(10, localSongMenu, downloadSongMenu);
        menuBar.setAlignment(Pos.CENTER_LEFT);
        menuBar.getStyleClass().add("download-tab-bar");

        VBox headerCard = new VBox(12, pageTitle, pageSubtitle, menuBar);
        headerCard.getStyleClass().add("download-header-card");

        contentContainer.getStyleClass().add("download-content-card");
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        VBox mainContainer = new VBox(16, headerCard, contentContainer);
        mainContainer.setPadding(new Insets(18, 18, 18, 18));
        VBox.setVgrow(contentContainer, Priority.ALWAYS);
        mainContainer.getStyleClass().add("download-main-container");

        setCenter(mainContainer);
        setStyle("-fx-background-color: transparent;");
    }

    private ToggleButton createTabButton(String text) {
        ToggleButton button = new ToggleButton(text);
        button.getStyleClass().add("download-tab-button");
        button.setMinWidth(108);
        button.setPrefHeight(36);
        button.setFocusTraversable(false);
        return button;
    }

    private void updateSelectedTabStyle() {
        styleTab(localSongMenu, localSongMenu.isSelected());
        styleTab(downloadSongMenu, downloadSongMenu.isSelected());
    }

    private void styleTab(ToggleButton tab, boolean selected) {
        tab.getStyleClass().removeAll("is-selected", "is-unselected");
        tab.getStyleClass().add(selected ? "is-selected" : "is-unselected");
    }

    private void switchContent(boolean showLocal) {
        contentContainer.getChildren().setAll(showLocal ? localSongPanel : downloadSongPanel);
        if (showLocal) {
            pageSubtitle.setText("管理你的本地曲库与下载任务");
        } else {
            pageSubtitle.setText("查看下载进度、状态与操作");
        }
    }
}
