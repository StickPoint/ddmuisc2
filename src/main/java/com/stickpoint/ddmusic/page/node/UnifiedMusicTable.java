package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.common.utils.FontUtil;
import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一音乐表格组件
 * 支持搜索结果、本地歌曲等多种数据源的统一展示
 */
public class UnifiedMusicTable extends VBox {

    private static final int PAGE_SIZE = 15;
    
    // 音源中英文映射表
    private static final Map<String, String> SOURCE_MAP = new HashMap<>();
    static {
        SOURCE_MAP.put("netease", "网易");
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
    
    private final MusicState musicState;
    private final BottomMusicContainer bottomMusicContainer;
    
    private ObservableList<MusicItem> allItems;
    private int currentPage = 1;
    private int totalPages = 1;
    private boolean isLocalMusic = false;
    
    /**
     * 构造函数
     * @param musicState 音乐状态
     * @param bottomMusicContainer 底部音乐容器
     */
    public UnifiedMusicTable(MusicState musicState, BottomMusicContainer bottomMusicContainer) {
        this.musicState = musicState;
        this.bottomMusicContainer = bottomMusicContainer;
        
        // 初始化组件
        musicTable = new TableView<>();
        paginationBox = new HBox();
        prevPageButton = new Button("上一页");
        nextPageButton = new Button("下一页");
        currentPageLabel = new Label("第 1 页 / 共 1 页");
        
        // 设置样式
        setStyle("-fx-background-color: white;");
        setPadding(new Insets(20));
        setSpacing(15);
        
        // 添加CSS类
        getStyleClass().add("unified-music-table");
        
        // 初始化表格
        initializeTable();
        
        // 初始化分页控件
        initializePagination();
        
        // 添加组件
        getChildren().addAll(musicTable, paginationBox);
        
        // 设置表格高度
        VBox.setVgrow(musicTable, Priority.ALWAYS);
        
        // 添加CSS样式
        getStylesheets().add(getClass().getResource("/css/UnifiedMusicTable.css").toExternalForm());
    }
    
    /**
     * 初始化表格
     */
    private void initializeTable() {
        // 设置表格属性
        musicTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        musicTable.setTableMenuButtonVisible(false);
        musicTable.setEditable(false);
        
        // 添加CSS类
        musicTable.getStyleClass().add("music-table");
        
        // 创建列
        TableColumn<MusicItem, String> nameColumn = new TableColumn<>("歌曲名");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(250);
        nameColumn.setCellFactory(col -> {
            TableCell<MusicItem, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        getStyleClass().remove("song-name");
                    } else {
                        setText(item);
                        getStyleClass().add("song-name");
                    }
                }
            };
            return cell;
        });
        
        TableColumn<MusicItem, String> artistColumn = new TableColumn<>("艺术家");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        artistColumn.setPrefWidth(150);
        artistColumn.setCellFactory(col -> {
            TableCell<MusicItem, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        getStyleClass().remove("artist-name");
                    } else {
                        setText(item);
                        getStyleClass().add("artist-name");
                    }
                }
            };
            return cell;
        });
        
        TableColumn<MusicItem, String> albumColumn = new TableColumn<>("专辑");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        albumColumn.setPrefWidth(200);
        albumColumn.setCellFactory(col -> {
            TableCell<MusicItem, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        getStyleClass().remove("album-name");
                    } else {
                        setText(item);
                        getStyleClass().add("album-name");
                    }
                }
            };
            return cell;
        });
        
        TableColumn<MusicItem, String> sourceColumn = new TableColumn<>("音源");
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
        sourceColumn.setPrefWidth(100);
        sourceColumn.setCellFactory(col -> {
            TableCell<MusicItem, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        getStyleClass().remove("source-name");
                    } else {
                        // 转换为中文显示
                        String chineseSource = SOURCE_MAP.getOrDefault(item, item);
                        setText(chineseSource);
                        getStyleClass().add("source-name");
                        setAlignment(Pos.CENTER);
                    }
                }
            };
            return cell;
        });
        
        TableColumn<MusicItem, String> actionColumn = new TableColumn<>("操作");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        actionColumn.setPrefWidth(80);
        actionColumn.setCellFactory(col -> new ActionTableCell());
        
        // 添加列到表格
        musicTable.getColumns().addAll(nameColumn, artistColumn, albumColumn, sourceColumn, actionColumn);
        
        // 双击播放
        musicTable.setRowFactory(tv -> {
            TableRow<MusicItem> row = new TableRow<>();
            row.setCursor(javafx.scene.Cursor.HAND);
            
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    MusicItem item = row.getItem();
                    handlePlay(item);
                }
            });
            
            return row;
        });
    }
    

    
    /**
     * 操作列单元格
     */
    private class ActionTableCell extends TableCell<MusicItem, String> {
        private final Button actionButton = new Button();
        
        public ActionTableCell() {
            actionButton.setText("⋮");
            actionButton.setFont(Font.font(20));
            actionButton.setFocusTraversable(false);
            
            // 添加CSS类
            actionButton.getStyleClass().add("action-button");
            actionButton.setCursor(javafx.scene.Cursor.HAND);
            
            // 创建上下文菜单
            actionButton.setOnAction(e -> {
                MusicItem item = getTableView().getItems().get(getIndex());
                showContextMenu(item);
            });
        }
        
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(actionButton);
                setAlignment(Pos.CENTER);
            }
        }
        
        /**
         * 显示上下文菜单
         * @param item 音乐项
         */
        private void showContextMenu(MusicItem item) {
            ContextMenu contextMenu = new ContextMenu();
            
            // 为上下文菜单和菜单项添加CSS类
            contextMenu.getStyleClass().add("custom-context-menu");
            
            // 播放菜单项
            MenuItem playItem = new MenuItem("播放");
            playItem.getStyleClass().add("custom-menu-item");
            playItem.setOnAction(e -> handlePlay(item));
            
            // 收藏菜单项
            MenuItem collectItem = new MenuItem("收藏");
            collectItem.getStyleClass().add("custom-menu-item");
            collectItem.setOnAction(e -> handleCollect(item));
            
            // 分享菜单项
            MenuItem shareItem = new MenuItem("分享");
            shareItem.getStyleClass().add("custom-menu-item");
            shareItem.setOnAction(e -> handleShare(item));
            
            // 下载菜单项
            MenuItem downloadItem = new MenuItem("下载");
            downloadItem.getStyleClass().add("custom-menu-item");
            downloadItem.setOnAction(e -> handleDownload(item));
            
            // 下一首菜单项
            MenuItem nextItem = new MenuItem("下一首");
            nextItem.getStyleClass().add("custom-menu-item");
            nextItem.setOnAction(e -> handleNext(item));
            
            // 添加菜单项
            contextMenu.getItems().addAll(playItem, collectItem, shareItem, downloadItem, nextItem);
            
            // 如果是本地歌曲，添加文件夹菜单项
            if (isLocalMusic) {
                MenuItem folderItem = new MenuItem("文件夹");
                folderItem.getStyleClass().add("custom-menu-item");
                folderItem.setOnAction(e -> handleOpenFolder(item));
                contextMenu.getItems().add(folderItem);
            }
            
            // 显示上下文菜单
            contextMenu.show(actionButton, javafx.geometry.Side.RIGHT, 0, 0);
        }
    }
    
    /**
     * 初始化分页控件
     */
    private void initializePagination() {
        // 添加CSS类
        paginationBox.getStyleClass().add("pagination-box");
        prevPageButton.getStyleClass().add("page-button");
        nextPageButton.getStyleClass().add("page-button");
        currentPageLabel.getStyleClass().add("page-info");
        
        paginationBox.setAlignment(Pos.CENTER);
        paginationBox.setSpacing(16);
        
        // 设置按钮属性
        prevPageButton.setCursor(javafx.scene.Cursor.HAND);
        nextPageButton.setCursor(javafx.scene.Cursor.HAND);
        
        // 添加事件监听器
        prevPageButton.setOnAction(e -> goToPage(currentPage - 1));
        nextPageButton.setOnAction(e -> goToPage(currentPage + 1));
        
        // 初始状态
        prevPageButton.setDisable(true);
        nextPageButton.setDisable(true);
        
        // 添加到分页容器
        paginationBox.getChildren().addAll(prevPageButton, currentPageLabel, nextPageButton);
    }
    
    /**
     * 设置数据
     * @param items 音乐项列表
     * @param isLocal 是否是本地音乐
     */
    public void setData(List<MusicItem> items, boolean isLocal) {
        this.isLocalMusic = isLocal;
        this.allItems = FXCollections.observableArrayList(items);
        
        // 计算总页数
        totalPages = (int) Math.ceil((double) allItems.size() / PAGE_SIZE);
        currentPage = 1;
        
        // 更新表格和分页
        showCurrentPage();
        updatePagination();
    }
    
    /**
     * 显示当前页数据
     */
    private void showCurrentPage() {
        if (allItems == null) {
            return;
        }
        
        int startIndex = (currentPage - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allItems.size());
        
        ObservableList<MusicItem> pageItems = FXCollections.observableArrayList(
            allItems.subList(startIndex, endIndex)
        );
        
        musicTable.setItems(pageItems);
    }
    
    /**
     * 更新分页控件
     */
    private void updatePagination() {
        prevPageButton.setDisable(currentPage <= 1);
        nextPageButton.setDisable(currentPage >= totalPages);
        currentPageLabel.setText("第 " + currentPage + " 页 / 共 " + totalPages + " 页");
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
        // 更新音乐状态
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
        
        // 播放音乐
        bottomMusicContainer.loadAndPlayMedia(item.getPlayUrl());
    }
    
    /**
     * 处理收藏
     * @param item 音乐项
     */
    private void handleCollect(MusicItem item) {
        // 实现收藏逻辑
        System.out.println("收藏歌曲: " + item.getName());
    }
    
    /**
     * 处理分享
     * @param item 音乐项
     */
    private void handleShare(MusicItem item) {
        // 实现分享逻辑
        System.out.println("分享歌曲: " + item.getName());
    }
    
    /**
     * 处理下载
     * @param item 音乐项
     */
    private void handleDownload(MusicItem item) {
        // 实现下载逻辑
        System.out.println("下载歌曲: " + item.getName());
    }
    
    /**
     * 处理下一首
     * @param item 音乐项
     */
    private void handleNext(MusicItem item) {
        // 实现下一首逻辑
        System.out.println("下一首播放: " + item.getName());
    }
    
    /**
     * 处理打开文件夹
     * @param item 音乐项
     */
    private void handleOpenFolder(MusicItem item) {
        // 实现打开文件夹逻辑
        try {
            File file = new File(item.getFilePath());
            File parentDir = file.getParentFile();
            if (parentDir != null && parentDir.exists()) {
                java.awt.Desktop.getDesktop().open(parentDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        private final String filePath; // 本地文件路径，仅本地歌曲使用
        
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
        
        // getter方法
        public String getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getArtist() { return artist.get(); }
        public String getAlbum() { return album.get(); }
        public String getSource() { return source.get(); }
        public String getPlayUrl() { return playUrl; }
        public String getPicUrl() { return picUrl; }
        public String getLrcUrl() { return lrcUrl; }
        public String getFilePath() { return filePath; }
    }
}