package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.common.entity.PlaybackHistoryEntry;
import com.stickpoint.ddmusic.common.utils.PlaybackHistoryStore;
import com.stickpoint.ddmusic.page.state.MusicState;
import java.awt.Desktop;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * 统一音乐表格组件
 * 支持搜索结果、本地歌曲等多种数据源的统一展示
 */
public class UnifiedMusicTable extends VBox {

    private static final int PAGE_SIZE = 15;

    private static final Map<String, String> SOURCE_MAP = new HashMap<>();

    static {
        SOURCE_MAP.put("netease", "网易云");
        SOURCE_MAP.put("kuwo", "酷我");
        SOURCE_MAP.put("kugou", "酷狗");
        SOURCE_MAP.put("bilibili", "B站");
        SOURCE_MAP.put("本地", "本地");
    }

    private final TableView<MusicItem> musicTable;
    private final HBox paginationBox;
    private final Button prevPageButton;
    private final Button nextPageButton;
    private final Label currentPageLabel;
    private final Label placeholderLabel;

    private final MusicState musicState;
    private final BottomMusicContainer bottomMusicContainer;

    private ObservableList<MusicItem> allItems;
    private int currentPage = 1;
    private int totalPages = 1;
    private boolean isLocalMusic = false;
    private String emptyStateText;
    private Consumer<MusicItem> playActionListener;

    /**
     * 构造函数
     * @param musicState 音乐状态
     * @param bottomMusicContainer 底部音乐容器
     */
    public UnifiedMusicTable(MusicState musicState, BottomMusicContainer bottomMusicContainer) {
        this.musicState = musicState;
        this.bottomMusicContainer = bottomMusicContainer;

        musicTable = new TableView<>();
        paginationBox = new HBox();
        prevPageButton = new Button("上一页");
        nextPageButton = new Button("下一页");
        currentPageLabel = new Label();
        placeholderLabel = createPlaceholderLabel();

        setSpacing(0);
        setStyle("-fx-background-color: transparent;");
        getStyleClass().add("unified-music-table");

        initializeTable();
        initializePagination();

        getChildren().addAll(musicTable, paginationBox);
        VBox.setVgrow(musicTable, Priority.ALWAYS);

        getStylesheets().add(getClass().getResource("/css/UnifiedMusicTable.css").toExternalForm());
        updatePlaceholderText();
        updatePagination();
    }

    /**
     * 初始化表格
     */
    private void initializeTable() {
        musicTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        musicTable.setTableMenuButtonVisible(false);
        musicTable.setEditable(false);
        musicTable.setFocusTraversable(false);
        musicTable.setFixedCellSize(58);
        musicTable.setPlaceholder(placeholderLabel);
        musicTable.getStyleClass().add("music-table");

        TableColumn<MusicItem, MusicItem> indexColumn = new TableColumn<>("");
        indexColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        indexColumn.setSortable(false);
        indexColumn.setReorderable(false);
        indexColumn.setResizable(false);
        indexColumn.setMinWidth(56);
        indexColumn.setPrefWidth(56);
        indexColumn.setMaxWidth(56);
        indexColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(MusicItem item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("row-index");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%02d", (currentPage - 1) * PAGE_SIZE + getIndex() + 1));
                    getStyleClass().add("row-index");
                    setAlignment(Pos.CENTER);
                }
            }
        });

        TableColumn<MusicItem, String> nameColumn = new TableColumn<>("音乐标题");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setSortable(false);
        nameColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("song-name");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    getStyleClass().add("song-name");
                }
            }
        });

        TableColumn<MusicItem, String> artistColumn = new TableColumn<>("歌手");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        artistColumn.setSortable(false);
        artistColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("artist-name");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    getStyleClass().add("artist-name");
                }
            }
        });

        TableColumn<MusicItem, String> albumColumn = new TableColumn<>("专辑");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        albumColumn.setSortable(false);
        albumColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("album-name");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    getStyleClass().add("album-name");
                }
            }
        });

        TableColumn<MusicItem, String> sourceColumn = new TableColumn<>("来源");
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
        sourceColumn.setSortable(false);
        sourceColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("source-name");
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label sourceBadge = new Label(SOURCE_MAP.getOrDefault(item, item));
                    sourceBadge.getStyleClass().add("source-pill");
                    setText(null);
                    setGraphic(sourceBadge);
                    setAlignment(Pos.CENTER);
                    getStyleClass().add("source-name");
                }
            }
        });

        TableColumn<MusicItem, String> actionColumn = new TableColumn<>("");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        actionColumn.setSortable(false);
        actionColumn.setReorderable(false);
        actionColumn.setResizable(false);
        actionColumn.setMinWidth(64);
        actionColumn.setPrefWidth(64);
        actionColumn.setMaxWidth(64);
        actionColumn.setCellFactory(col -> new ActionTableCell());

        musicTable.getColumns().addAll(indexColumn, nameColumn, artistColumn, albumColumn, sourceColumn, actionColumn);
        musicTable.setRowFactory(tv -> {
            TableRow<MusicItem> row = new TableRow<>();
            row.setCursor(Cursor.HAND);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    handlePlay(row.getItem());
                }
            });
            return row;
        });
    }

    /**
     * 操作列单元格
     */
    private class ActionTableCell extends TableCell<MusicItem, String> {
        private final Button actionButton = new Button("⋯");

        private ActionTableCell() {
            actionButton.getStyleClass().add("action-button");
            actionButton.setFocusTraversable(false);
            actionButton.setCursor(Cursor.HAND);
            actionButton.setOnAction(e -> {
                if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    return;
                }
                showContextMenu(getTableView().getItems().get(getIndex()));
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            setText(null);
            setGraphic(actionButton);
            setAlignment(Pos.CENTER);
        }

        /**
         * 显示上下文菜单
         * @param item 音乐项
         */
        private void showContextMenu(MusicItem item) {
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getStyleClass().add("music-context-menu");

            MenuItem playItem = new MenuItem("播放");
            playItem.setOnAction(e -> handlePlay(item));

            MenuItem collectItem = new MenuItem("收藏");
            collectItem.setOnAction(e -> handleCollect(item));

            MenuItem shareItem = new MenuItem("分享");
            shareItem.setOnAction(e -> handleShare(item));

            MenuItem downloadItem = new MenuItem("下载");
            downloadItem.setOnAction(e -> handleDownload(item));

            MenuItem nextItem = new MenuItem("下一首播放");
            nextItem.setOnAction(e -> handleNext(item));

            contextMenu.getItems().addAll(playItem, collectItem, shareItem, downloadItem, nextItem);

            if (item.getFilePath() != null && !item.getFilePath().isBlank()) {
                MenuItem folderItem = new MenuItem("打开文件夹");
                folderItem.setOnAction(e -> handleOpenFolder(item));
                contextMenu.getItems().add(folderItem);
            }

            contextMenu.show(actionButton, javafx.geometry.Side.RIGHT, 0, 0);
        }
    }

    /**
     * 初始化分页控件
     */
    private void initializePagination() {
        paginationBox.getStyleClass().add("pagination-box");
        paginationBox.setAlignment(Pos.CENTER_RIGHT);

        prevPageButton.getStyleClass().add("page-button");
        nextPageButton.getStyleClass().add("page-button");
        currentPageLabel.getStyleClass().add("page-info");

        prevPageButton.setFocusTraversable(false);
        nextPageButton.setFocusTraversable(false);

        prevPageButton.setOnAction(e -> goToPage(currentPage - 1));
        nextPageButton.setOnAction(e -> goToPage(currentPage + 1));

        paginationBox.getChildren().addAll(prevPageButton, currentPageLabel, nextPageButton);
    }

    private Label createPlaceholderLabel() {
        Label placeholder = new Label();
        placeholder.getStyleClass().add("table-placeholder");
        return placeholder;
    }

    private void updatePlaceholderText() {
        placeholderLabel.setText(isLocalMusic ? "还没有本地音乐，扫描后会显示在这里" : "暂无音乐数据");
    }

    /**
     * 设置数据
     * @param items 音乐项列表
     * @param isLocal 是否为本地音乐
     */
    public void setEmptyStateText(String emptyStateText) {
        this.emptyStateText = emptyStateText;
        if (emptyStateText != null && !emptyStateText.isBlank()) {
            placeholderLabel.setText(emptyStateText);
        } else {
            updatePlaceholderText();
        }
    }

    public void setPlayActionListener(Consumer<MusicItem> playActionListener) {
        this.playActionListener = playActionListener;
    }

    public void setData(List<MusicItem> items, boolean isLocal) {
        this.isLocalMusic = isLocal;
        this.allItems = FXCollections.observableArrayList(items == null ? List.of() : items);

        totalPages = Math.max(1, (int) Math.ceil((double) allItems.size() / PAGE_SIZE));
        currentPage = 1;

        updatePlaceholderText();
        showCurrentPage();
        updatePagination();
    }

    /**
     * 显示当前页数据
     */
    private void showCurrentPage() {
        if (allItems == null || allItems.isEmpty()) {
            musicTable.setItems(FXCollections.observableArrayList());
            return;
        }

        int startIndex = (currentPage - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allItems.size());
        musicTable.setItems(FXCollections.observableArrayList(allItems.subList(startIndex, endIndex)));
    }

    /**
     * 更新分页控件
     */
    private void updatePagination() {
        int totalItems = allItems == null ? 0 : allItems.size();
        prevPageButton.setDisable(currentPage <= 1 || totalItems == 0);
        nextPageButton.setDisable(currentPage >= totalPages || totalItems == 0);

        if (totalItems == 0) {
            currentPageLabel.setText("0 首歌曲");
        } else {
            currentPageLabel.setText("第 " + currentPage + " 页 / 共 " + totalPages + " 页");
        }
    }

    /**
     * 跳转到指定页
     * @param page 页码
     */
    private void goToPage(int page) {
        if (page < 1 || page > totalPages) {
            return;
        }

        currentPage = page;
        showCurrentPage();
        updatePagination();
    }

    /**
     * 处理播放
     * @param item 音乐项
     */
    private void handlePlay(MusicItem item) {
        musicState.playMusic(
            item.getName(),
            item.getArtist(),
            item.getAlbum(),
            item.getPicUrl(),
            item.getSource(),
            item.getPlayUrl(),
            item.getLrcUrl(),
            javafx.util.Duration.ZERO
        );

        bottomMusicContainer.loadAndPlayMedia(item.getPlayUrl());
        PlaybackHistoryStore.recordPlayback(toPlaybackHistoryEntry(item));
        if (playActionListener != null) {
            playActionListener.accept(item);
        }
    }

    private PlaybackHistoryEntry toPlaybackHistoryEntry(MusicItem item) {
        return new PlaybackHistoryEntry(
            item.getId(),
            item.getName(),
            item.getArtist(),
            item.getAlbum(),
            item.getSource(),
            item.getPlayUrl(),
            item.getPicUrl(),
            item.getLrcUrl(),
            item.getFilePath(),
            System.currentTimeMillis()
        );
    }

    /**
     * 处理收藏
     * @param item 音乐项
     */
    private void handleCollect(MusicItem item) {
        System.out.println("收藏歌曲: " + item.getName());
    }

    /**
     * 处理分享
     * @param item 音乐项
     */
    private void handleShare(MusicItem item) {
        System.out.println("分享歌曲: " + item.getName());
    }

    /**
     * 处理下载
     * @param item 音乐项
     */
    private void handleDownload(MusicItem item) {
        System.out.println("下载歌曲: " + item.getName());
    }

    /**
     * 处理下一首
     * @param item 音乐项
     */
    private void handleNext(MusicItem item) {
        System.out.println("下一首播放: " + item.getName());
    }

    /**
     * 处理打开文件夹
     * @param item 音乐项
     */
    private void handleOpenFolder(MusicItem item) {
        if (!Desktop.isDesktopSupported() || item.getFilePath() == null || item.getFilePath().isBlank()) {
            return;
        }

        try {
            File file = new File(item.getFilePath());
            File parentDir = file.getParentFile();
            if (parentDir != null && parentDir.exists()) {
                Desktop.getDesktop().open(parentDir);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 清空表格数据
     */
    public void clearData() {
        allItems = FXCollections.observableArrayList();
        currentPage = 1;
        totalPages = 1;
        musicTable.getItems().clear();
        updatePlaceholderText();
        updatePagination();
    }

    /**
     * 音乐项实体类
     */
    public static class MusicItem {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty artist;
        private final SimpleStringProperty album;
        private final SimpleStringProperty source;
        private final String playUrl;
        private final String picUrl;
        private final String lrcUrl;
        private final String filePath;

        /**
         * 构造函数（网络歌曲）
         */
        public MusicItem(String id, String name, String artist, String album, String source,
                        String playUrl, String picUrl, String lrcUrl) {
            this(id, name, artist, album, source, playUrl, picUrl, lrcUrl, null);
        }

        /**
         * 构造函数（本地歌曲）
         */
        public MusicItem(String id, String name, String artist, String album, String source,
                        String playUrl, String picUrl, String lrcUrl, String filePath) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.artist = new SimpleStringProperty(artist);
            this.album = new SimpleStringProperty(album);
            this.source = new SimpleStringProperty(source);
            this.playUrl = playUrl;
            this.picUrl = picUrl;
            this.lrcUrl = lrcUrl;
            this.filePath = filePath;
        }

        public String getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getArtist() {
            return artist.get();
        }

        public String getAlbum() {
            return album.get();
        }

        public String getSource() {
            return source.get();
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public String getLrcUrl() {
            return lrcUrl;
        }

        public String getFilePath() {
            return filePath;
        }
    }
}
