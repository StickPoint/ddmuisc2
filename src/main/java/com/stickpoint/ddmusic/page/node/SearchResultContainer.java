package com.stickpoint.ddmusic.page.node;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stickpoint.ddmusic.common.utils.TuneHubApiUtils;
import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 搜索结果容器
 * @author fntp
 * @date 2026/1/23
 */
public class SearchResultContainer extends VBox {

    private static final Logger log = LoggerFactory.getLogger(SearchResultContainer.class);
    private static final Gson GSON = new Gson();
    private static final int PAGE_SIZE = 15;

    private final TableView<SearchResultItem> resultTable;
    private final Label searchKeywordLabel;
    private final Label resultCountLabel;
    private final HBox paginationBox;
    private final Button prevPageButton;
    private final Button nextPageButton;
    private final Label currentPageLabel;
    private final MusicState musicState;
    private final BottomMusicContainer bottomMusicContainer;

    private List<SearchResultItem> allResults;
    private int currentPage = 1;
    private int totalPages = 1;

    public SearchResultContainer(MusicState musicState, BottomMusicContainer bottomMusicContainer) {
        this.musicState = musicState;
        this.bottomMusicContainer = bottomMusicContainer;
        
        // 设置样式
        setStyle("-fx-background-color: white;");
        setPadding(new Insets(20, 20, 20, 20));
        setSpacing(15);
        
        // 创建搜索结果标题
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setSpacing(10);
        
        searchKeywordLabel = new Label("");
        searchKeywordLabel.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD, 18));
        searchKeywordLabel.setTextFill(Color.web("#333333"));
        
        resultCountLabel = new Label("");
        resultCountLabel.setFont(Font.font("Microsoft YaHei", 14));
        resultCountLabel.setTextFill(Color.web("#666666"));
        
        titleBox.getChildren().addAll(searchKeywordLabel, resultCountLabel);
        
        // 创建表格
        resultTable = new TableView<>();
        resultTable.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8px;");
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // 创建列
        TableColumn<SearchResultItem, String> nameColumn = new TableColumn<>("歌曲名称");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(250);
        nameColumn.setCellFactory(col -> {
            TableCell<SearchResultItem, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
                    }
                }
            };
            return cell;
        });
        
        TableColumn<SearchResultItem, String> artistColumn = new TableColumn<>("歌手");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        artistColumn.setPrefWidth(150);
        artistColumn.setCellFactory(col -> {
            TableCell<SearchResultItem, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");
                    }
                }
            };
            return cell;
        });
        
        TableColumn<SearchResultItem, String> albumColumn = new TableColumn<>("专辑");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        albumColumn.setPrefWidth(200);
        albumColumn.setCellFactory(col -> {
            TableCell<SearchResultItem, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");
                    }
                }
            };
            return cell;
        });
        
        TableColumn<SearchResultItem, String> durationColumn = new TableColumn<>("时长");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.setPrefWidth(100);
        durationColumn.setCellFactory(col -> {
            TableCell<SearchResultItem, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle("-fx-font-size: 14px; -fx-text-fill: #999999; -fx-alignment: CENTER;");
                    }
                }
            };
            return cell;
        });
        
        TableColumn<SearchResultItem, String> actionColumn = new TableColumn<>("操作");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        actionColumn.setPrefWidth(80);
        actionColumn.setCellFactory(col -> {
            TableCell<SearchResultItem, String> cell = new TableCell<>() {
                private final Button actionButton = new Button();
                
                {
                    actionButton.setText("⋮");
                    actionButton.setFont(Font.font(20));
                    actionButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; -fx-padding: 5; -fx-min-width: 30; -fx-min-height: 30;");
                    actionButton.setFocusTraversable(false);
                    
                    // 创建上下文菜单
                    ContextMenu contextMenu = new ContextMenu();
                    
                    MenuItem playItem = new MenuItem("播放");
                    playItem.setOnAction(e -> {
                        SearchResultItem item = getTableView().getItems().get(getIndex());
                        handlePlay(item);
                    });
                    
                    MenuItem downloadItem = new MenuItem("下载");
                    downloadItem.setOnAction(e -> {
                        SearchResultItem item = getTableView().getItems().get(getIndex());
                        handleDownload(item);
                    });
                    
                    MenuItem collectItem = new MenuItem("收藏");
                    collectItem.setOnAction(e -> {
                        SearchResultItem item = getTableView().getItems().get(getIndex());
                        handleCollect(item);
                    });
                    
                    MenuItem shareItem = new MenuItem("分享");
                    shareItem.setOnAction(e -> {
                        SearchResultItem item = getTableView().getItems().get(getIndex());
                        handleShare(item);
                    });
                    
                    contextMenu.getItems().addAll(playItem, downloadItem, collectItem, shareItem);
                    
                    actionButton.setOnAction(e -> {
                        contextMenu.show(actionButton, javafx.geometry.Side.RIGHT, 0, 0);
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
            };
            return cell;
        });
        
        resultTable.getColumns().addAll(nameColumn, artistColumn, albumColumn, durationColumn, actionColumn);
        
        // 双击播放
        resultTable.setRowFactory(tv -> {
            TableRow<SearchResultItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    SearchResultItem item = row.getItem();
                    handlePlay(item);
                }
            });
            return row;
        });
        
        // 创建分页控件
        paginationBox = new HBox();
        paginationBox.setAlignment(Pos.CENTER);
        paginationBox.setSpacing(10);
        
        prevPageButton = new Button("上一页");
        prevPageButton.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #333333; -fx-border-color: #e0e0e0; -fx-border-radius: 4px; -fx-padding: 8 16; -fx-font-size: 14px;");
        prevPageButton.setDisable(true);
        prevPageButton.setOnAction(e -> goToPage(currentPage - 1));
        
        currentPageLabel = new Label("第 1 页 / 共 1 页");
        currentPageLabel.setFont(Font.font("Microsoft YaHei", 14));
        currentPageLabel.setTextFill(Color.web("#666666"));
        
        nextPageButton = new Button("下一页");
        nextPageButton.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #333333; -fx-border-color: #e0e0e0; -fx-border-radius: 4px; -fx-padding: 8 16; -fx-font-size: 14px;");
        nextPageButton.setDisable(true);
        nextPageButton.setOnAction(e -> goToPage(currentPage + 1));
        
        paginationBox.getChildren().addAll(prevPageButton, currentPageLabel, nextPageButton);
        
        // 将组件添加到容器
        getChildren().addAll(titleBox, resultTable, paginationBox);
        
        // 设置表格高度
        VBox.setVgrow(resultTable, Priority.ALWAYS);
    }
    
    /**
     * 执行搜索
     * @param keyword 搜索关键词
     */
    public void search(String keyword) {
        searchKeywordLabel.setText("搜索结果: " + keyword);
        resultCountLabel.setText("正在搜索...");
        resultTable.getItems().clear();
        
        // 在后台线程执行搜索
        new Thread(() -> {
            try {
                log.info("开始搜索关键词: {}", keyword);
                String resultJson = TuneHubApiUtils.aggregateSearch(keyword);
                log.info("搜索结果JSON: {}", resultJson);
                
                // 使用 Gson 解析 JSON
                Type resultType = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> resultMap = GSON.fromJson(resultJson, resultType);
                
                if (resultMap.containsKey("code")) {
                    Object codeObj = resultMap.get("code");
                    int code = 0;
                    if (codeObj instanceof Integer) {
                        code = (int) codeObj;
                    } else if (codeObj instanceof Double) {
                        code = ((Double) codeObj).intValue();
                    }
                    if (code == 200) {
                        if (resultMap.containsKey("data")) {
                            Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
                            if (dataMap.containsKey("results")) {
                                // 解析 results 数组
                                List<Map<String, Object>> resultsList = (List<Map<String, Object>>) dataMap.get("results");
                                log.info("搜索结果数量: {}", resultsList.size());
                                
                                allResults = resultsList.stream().map(item -> {
                                    String id = (String) item.get("id");
                                    String name = (String) item.get("name");
                                    String artist = (String) item.get("artist");
                                    String album = (String) item.get("album");
                                    String platform = (String) item.get("platform");
                                    
                                    // 构建播放URL
                                    String playUrl = "https://music-dl.sayqz.com/api/?source=" + platform + "&id=" + id + "&type=url";
                                    // 构建专辑封面URL
                                    String picUrl = "https://music-dl.sayqz.com/api/?source=" + platform + "&id=" + id + "&type=pic";
                                    // 构建歌词URL
                                    String lrcUrl = "https://music-dl.sayqz.com/api/?source=" + platform + "&id=" + id + "&type=lrc";
                                    
                                    return new SearchResultItem(id, name, artist, album, "00:00", platform, playUrl, picUrl, lrcUrl);
                                }).toList();
                                
                                totalPages = (int) Math.ceil((double) allResults.size() / PAGE_SIZE);
                                currentPage = 1;
                                
                                Platform.runLater(() -> {
                                    resultCountLabel.setText("共找到 " + allResults.size() + " 条结果");
                                    updatePagination();
                                    showCurrentPage();
                                });
                            }
                        }
                    } else {
                        log.error("搜索失败，API返回错误: {}", resultMap.get("message"));
                        Platform.runLater(() -> {
                            resultCountLabel.setText("搜索失败: " + resultMap.get("message"));
                        });
                    }
                }
            } catch (Exception e) {
                log.error("搜索失败: {}", e.getMessage(), e);
                Platform.runLater(() -> {
                    resultCountLabel.setText("搜索失败，请重试: " + e.getMessage());
                });
            }
        }).start();
    }
    
    /**
     * 显示当前页
     */
    private void showCurrentPage() {
        if (allResults == null) {
            return;
        }
        
        int startIndex = (currentPage - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allResults.size());
        
        List<SearchResultItem> pageResults = allResults.subList(startIndex, endIndex);
        resultTable.getItems().setAll(pageResults);
    }
    
    /**
     * 更新分页控件状态
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
        updatePagination();
        showCurrentPage();
    }
    
    /**
     * 处理播放
     * @param item 搜索结果项
     */
    private void handlePlay(SearchResultItem item) {
        log.info("播放歌曲: {} - {}", item.getArtist(), item.getName());
        
        // 更新音乐状态
        musicState.playMusic(
                item.getName(),
                item.getArtist(),
                item.getAlbum(),
                item.getPicUrl(),
                item.getPlatform(),
                item.getPlayUrl(),
                item.getLrcUrl(),
                javafx.util.Duration.ZERO
        );
        
        // 播放音乐
        bottomMusicContainer.loadAndPlayMedia(item.getPlayUrl());
    }
    
    /**
     * 处理下载
     * @param item 搜索结果项
     */
    private void handleDownload(SearchResultItem item) {
        log.info("下载歌曲: {} - {}", item.getArtist(), item.getName());
        // 实现下载逻辑
    }
    
    /**
     * 处理收藏
     * @param item 搜索结果项
     */
    private void handleCollect(SearchResultItem item) {
        log.info("收藏歌曲: {} - {}", item.getArtist(), item.getName());
        // 实现收藏逻辑
    }
    
    /**
     * 处理分享
     * @param item 搜索结果项
     */
    private void handleShare(SearchResultItem item) {
        log.info("分享歌曲: {} - {}", item.getArtist(), item.getName());
        // 实现分享逻辑
    }
    
    /**
     * 清理搜索结果，释放资源
     */
    public void clear() {
        // 清理表格数据
        resultTable.getItems().clear();
        // 清理所有结果（不直接调用clear()，因为allResults可能是不可变列表）
        allResults = null;
        // 重置分页
        currentPage = 1;
        totalPages = 1;
        // 清空搜索关键词和结果数量
        searchKeywordLabel.setText("");
        resultCountLabel.setText("");
        // 更新分页控件
        updatePagination();
    }
    
    /**
     * 搜索结果项实体类
     */
    public static class SearchResultItem {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty artist;
        private final SimpleStringProperty album;
        private final SimpleStringProperty duration;
        private final String platform;
        private final String playUrl;
        private final String picUrl;
        private final String lrcUrl;
        
        public SearchResultItem(String id, String name, String artist, String album, String duration, 
                               String platform, String playUrl, String picUrl, String lrcUrl) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.artist = new SimpleStringProperty(artist);
            this.album = new SimpleStringProperty(album);
            this.duration = new SimpleStringProperty(duration);
            this.platform = platform;
            this.playUrl = playUrl;
            this.picUrl = picUrl;
            this.lrcUrl = lrcUrl;
        }
        
        public String getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getArtist() { return artist.get(); }
        public String getAlbum() { return album.get(); }
        public String getDuration() { return duration.get(); }
        public String getPlatform() { return platform; }
        public String getPlayUrl() { return playUrl; }
        public String getPicUrl() { return picUrl; }
        public String getLrcUrl() { return lrcUrl; }
    }
}
