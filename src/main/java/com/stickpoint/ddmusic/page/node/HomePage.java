package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;

/**
 * Main application home page.
 */
public class HomePage extends BorderPane {

    private final HomePageMenuPanel homePageMenuPanel;
    private final HomePageHeaderContainer headerContainer;
    private final HomePageContentContainer homePageContentContainer;
    private final BottomMusicContainer bottomMusicContainer;
    private final MusicPlayDetailContainer musicPlayDetailContainer;
    private final LocalSongPanel localSongPage;
    private final DownloadSongPanel downloadSongPage;
    private final PlaybackHistoryPanel playbackHistoryPage;
    private final MusicState musicState;
    private final BorderPane centerPanel;
    private final SearchResultContainer searchResultContainer;
    private final PlaylistDetailContainer playlistDetailContainer;

    private Node previousCenterContent;

    public HomePage() {
        musicState = new MusicState();
        bottomMusicContainer = new BottomMusicContainer(musicState);
        headerContainer = new HomePageHeaderContainer();
        homePageMenuPanel = new HomePageMenuPanel();
        homePageContentContainer = new HomePageContentContainer();
        musicPlayDetailContainer = new MusicPlayDetailContainer(musicState);
        localSongPage = new LocalSongPanel(musicState, bottomMusicContainer);
        downloadSongPage = new DownloadSongPanel();
        playbackHistoryPage = new PlaybackHistoryPanel(musicState, bottomMusicContainer);
        searchResultContainer = new SearchResultContainer(musicState, bottomMusicContainer);
        playlistDetailContainer = new PlaylistDetailContainer(musicState, bottomMusicContainer);

        musicPlayDetailContainer.setVisible(false);
        localSongPage.setVisible(false);
        downloadSongPage.setVisible(false);
        playbackHistoryPage.setVisible(false);
        searchResultContainer.setVisible(false);
        playlistDetailContainer.setVisible(false);

        homePageMenuPanel.setPrefWidth(200);
        setLeft(homePageMenuPanel);

        centerPanel = new BorderPane();
        centerPanel.setTop(headerContainer);
        centerPanel.setCenter(homePageContentContainer);

        setCenter(centerPanel);
        setBottom(bottomMusicContainer);
        setPadding(new Insets(0));
        setStyle("-fx-background-color: white;");

        Rectangle clip = new Rectangle();
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

        wireSearch();
        wireMenuNavigation();
        wireAlbumToggle();
        wirePlaylistNavigation();
    }

    private void wireSearch() {
        headerContainer.getSearchField().setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.ENTER) {
                return;
            }

            String keyword = headerContainer.getSearchField().getText().trim();
            if (!keyword.isEmpty()) {
                performSearch(keyword);
            }
        });
    }

    private void wireMenuNavigation() {
        ToggleGroup menuToggleGroup = homePageMenuPanel.getToggleGroup();
        if (menuToggleGroup == null) {
            return;
        }

        menuToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (!(newToggle instanceof HomePageMenuItem selectedMenuItem)) {
                return;
            }

            String menuId = selectedMenuItem.getId();
            if ("myMusic".equals(menuId)) {
                showLocalMusicPage();
            } else if ("downloadMusic".equals(menuId)) {
                showDownloadMusicPage();
            } else if ("myPlaylist".equals(menuId)) {
                showPlaybackHistoryPage();
            } else if ("recommend".equals(menuId)) {
                backToHome();
            } else {
                backToHome();
            }
        });
    }

    private void wireAlbumToggle() {
        bottomMusicContainer.setAlbumImageClickHandler(event -> {
            if (getCenter() == musicPlayDetailContainer) {
                backToHome();
            } else {
                showMusicDetail();
            }
        });
    }

    private void wirePlaylistNavigation() {
        homePageContentContainer.getDiscoverMusicContainer().setPlaylistClickListener(
            (playlistId, playlistName, coverUrl, source) -> {
                previousCenterContent = centerPanel.getCenter();
                playlistDetailContainer.setPlaylistData(playlistId, playlistName, coverUrl, source);
                playlistDetailContainer.setVisible(true);
                centerPanel.setCenter(playlistDetailContainer);
            }
        );
    }

    public void showMusicDetail() {
        if (getCenter() == musicPlayDetailContainer) {
            return;
        }

        previousCenterContent = getCenter();
        setLeft(null);
        musicPlayDetailContainer.setVisible(true);
        setCenter(musicPlayDetailContainer);
    }

    private void performSearch(String keyword) {
        searchResultContainer.setVisible(true);
        centerPanel.setCenter(searchResultContainer);
        searchResultContainer.search(keyword);
    }

    public void backToHome() {
        setLeft(homePageMenuPanel);
        centerPanel.setTop(headerContainer);
        centerPanel.setCenter(homePageContentContainer);
        setCenter(centerPanel);

        previousCenterContent = null;
        musicPlayDetailContainer.setVisible(false);
        localSongPage.setVisible(false);
        downloadSongPage.setVisible(false);
        playbackHistoryPage.setVisible(false);
        playlistDetailContainer.setVisible(false);
        searchResultContainer.setVisible(false);
        searchResultContainer.clear();
    }

    public void showLocalMusicPage() {
        setLeft(homePageMenuPanel);
        centerPanel.setTop(headerContainer);
        centerPanel.setCenter(localSongPage);
        setCenter(centerPanel);

        localSongPage.setVisible(true);
        downloadSongPage.setVisible(false);
        playbackHistoryPage.setVisible(false);
        playlistDetailContainer.setVisible(false);
        musicPlayDetailContainer.setVisible(false);
        searchResultContainer.setVisible(false);
        previousCenterContent = centerPanel;
    }

    public void showDownloadMusicPage() {
        setLeft(homePageMenuPanel);
        centerPanel.setTop(headerContainer);
        centerPanel.setCenter(downloadSongPage);
        setCenter(centerPanel);

        downloadSongPage.setVisible(true);
        localSongPage.setVisible(false);
        playbackHistoryPage.setVisible(false);
        playlistDetailContainer.setVisible(false);
        musicPlayDetailContainer.setVisible(false);
        searchResultContainer.setVisible(false);
        previousCenterContent = centerPanel;
    }

    public void showPlaybackHistoryPage() {
        setLeft(homePageMenuPanel);
        centerPanel.setTop(headerContainer);
        playbackHistoryPage.refreshHistory();
        centerPanel.setCenter(playbackHistoryPage);
        setCenter(centerPanel);

        playbackHistoryPage.setVisible(true);
        downloadSongPage.setVisible(false);
        localSongPage.setVisible(false);
        playlistDetailContainer.setVisible(false);
        musicPlayDetailContainer.setVisible(false);
        searchResultContainer.setVisible(false);
        previousCenterContent = centerPanel;
    }

    public HomePageMenuPanel getMenuPanel() {
        return homePageMenuPanel;
    }
}
