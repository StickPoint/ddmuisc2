package com.stickpoint.ddmusic.page.node;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * 发现音乐页面容器
 * @author fntp
 * @date 2026/1/23
 */
public class DiscoverMusicContainer extends VBox {

    /**
     * 音源切换按钮组
     */
    private HBox sourceToggleContainer;
    
    /**
     * 网易按钮
     */
    private Button wangyiButton;
    
    /**
     * 酷我按钮
     */
    private Button kuwoButton;
    
    /**
     * 榜单列表容器（包含Scrollpane和FlowPane）
     */
    private Node playlistContainer;
    
    /**
     * 当前选中的音源
     */
    private String currentSource = "wangyi";
    
    public DiscoverMusicContainer() {
        // 加载CSS样式
        getStylesheets().add(getClass().getResource("/css/DiscoverMusicContainer.css").toExternalForm());
        getStyleClass().add("discover-music-container");
        
        // 设置间距和内边距
        setSpacing(20);
        setPadding(new Insets(0, 10, 0, 10));
        
        // 初始化音源切换按钮组
        initSourceToggle();
        
        // 初始化榜单列表容器
        initPlaylistContainer();
        
        // 添加组件到容器
        getChildren().addAll(sourceToggleContainer, playlistContainer);
        
        // 设置优先增长
        VBox.setVgrow(playlistContainer, Priority.ALWAYS);
        
        // 加载默认数据
        loadPlaylistData();
    }
    
    /**
     * 初始化音源切换按钮组
     */
    private void initSourceToggle() {
        sourceToggleContainer = new HBox();
        sourceToggleContainer.setSpacing(15);
        sourceToggleContainer.setAlignment(Pos.CENTER_LEFT);
        sourceToggleContainer.getStyleClass().add("source-toggle-container");
        
        // 创建网易按钮
        wangyiButton = new Button("网易");
        wangyiButton.getStyleClass().addAll("source-button", "active");
        wangyiButton.setOnAction(event -> {
            switchSource("wangyi");
        });
        
        // 创建酷我按钮
        kuwoButton = new Button("酷我");
        kuwoButton.getStyleClass().add("source-button");
        kuwoButton.setOnAction(event -> {
            switchSource("kuwo");
        });
        
        // 添加间距填充
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        sourceToggleContainer.getChildren().addAll(wangyiButton, kuwoButton, spacer);
    }
    
    /**
     * 初始化榜单列表容器
     */
    private void initPlaylistContainer() {
        // 创建流式布局用于显示榜单
        FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(10, 0, 10, 0));
        flowPane.setHgap(12);
        flowPane.setVgap(15);
        flowPane.setPrefWrapLength(850); // 调整每行宽度，减少右边空隙
        flowPane.getStyleClass().add("playlist-container");
        
        // 创建滚动容器，并将流式布局设置为其内容
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("playlist-scroll-pane");
        scrollPane.setContent(flowPane);
        
        // 将scrollPane赋值给playlistContainer成员变量，而不是直接添加到主容器
        // 这样构造函数中的getChildren().addAll(sourceToggleContainer, playlistContainer)就会正确添加顺序
        playlistContainer = scrollPane;
    }
    
    /**
     * 切换音源
     * @param source 音源类型
     */
    private void switchSource(String source) {
        currentSource = source;
        
        // 更新按钮样式
        wangyiButton.getStyleClass().remove("active");
        kuwoButton.getStyleClass().remove("active");
        
        if ("wangyi".equals(source)) {
            wangyiButton.getStyleClass().add("active");
        } else {
            kuwoButton.getStyleClass().add("active");
        }
        
        // 加载对应音源的榜单数据
        loadPlaylistData();
    }
    
    /**
     * 加载榜单数据
     */
    private void loadPlaylistData() {
        // 获取FlowPane
        FlowPane flowPane;
        if (playlistContainer instanceof ScrollPane) {
            // 如果playlistContainer是ScrollPane，获取其内容（FlowPane）
            flowPane = (FlowPane) ((ScrollPane) playlistContainer).getContent();
        } else {
            // 否则直接使用
            flowPane = (FlowPane) playlistContainer;
        }
        
        // 清空现有数据
        flowPane.getChildren().clear();
        
        // 根据当前音源获取榜单数据
        com.stickpoint.ddmusic.common.utils.TuneHubApiUtils.Platform platform = "wangyi".equals(currentSource) 
                ? com.stickpoint.ddmusic.common.utils.TuneHubApiUtils.Platform.NETEASE
                : com.stickpoint.ddmusic.common.utils.TuneHubApiUtils.Platform.KUWO;
        
        // 构建正确的API URL，带有source参数
        String apiUrl = "https://music-dl.sayqz.com/api/";
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("type", "toplists");
        params.put("source", platform.getValue());
        
        String result = com.stickpoint.ddmusic.common.utils.HttpUtils.doTuneFreeGetWithParams(apiUrl, params);
        
        // 解析JSON数据
        List<PlaylistItem> playlistItems = parseRankData(result);
        
        // 添加榜单项到容器
        for (PlaylistItem item : playlistItems) {
            flowPane.getChildren().add(createPlaylistItem(item));
        }
    }
    
    /**
     * 解析榜单数据
     * @param jsonData JSON数据
     * @return 榜单列表
     */
    private List<PlaylistItem> parseRankData(String jsonData) {
        if (jsonData == null || jsonData.isEmpty()) {
            return List.of();
        }
        
        try {
            // 使用Gson解析JSON
            com.google.gson.JsonObject rootObject = com.google.gson.JsonParser.parseString(jsonData).getAsJsonObject();
            
            // 获取data字段的JsonObject
            com.google.gson.JsonObject dataObject = rootObject.getAsJsonObject("data");
            
            // 从data对象中获取list字段的JsonArray
            com.google.gson.JsonArray rankList = dataObject.getAsJsonArray("list");
            
            java.util.ArrayList<PlaylistItem> playlistItems = new java.util.ArrayList<>();
            
            // 遍历榜单列表
            for (int i = 0; i < rankList.size(); i++) {
                com.google.gson.JsonObject rankItem = rankList.get(i).getAsJsonObject();
                
                // 获取榜单封面图（pic字段）
                String picUrl = rankItem.has("pic") ? rankItem.get("pic").getAsString() : "";
                
                // 获取榜单标题（name字段）
                String title = rankItem.has("name") ? rankItem.get("name").getAsString() : "未知榜单";
                
                System.out.println("解析到榜单: " + title + "，封面: " + picUrl);
                
                // 只添加有封面图的榜单
                if (!picUrl.isEmpty()) {
                    playlistItems.add(new PlaylistItem(picUrl, title));
                }
            }
            
            System.out.println("成功解析 " + playlistItems.size() + " 个榜单");
            
            return playlistItems;
        } catch (Exception e) {
            System.err.println("解析榜单数据失败: " + e.getMessage());
            System.err.println("原始数据: " + jsonData);
            return List.of();
        }
    }
    
    /**
     * 创建榜单项组件
     * @param item 榜单数据
     * @return 榜单项组件
     */
    private VBox createPlaylistItem(PlaylistItem item) {
        VBox playlistItem = new VBox();
        playlistItem.setSpacing(8);
        playlistItem.setAlignment(Pos.CENTER);
        playlistItem.setPrefWidth(160); // 调整宽度，减少右边空隙
        playlistItem.setMaxWidth(160);
        playlistItem.setPadding(new Insets(10));
        playlistItem.getStyleClass().add("playlist-item");
        
        // 创建封面图片
        AsyncImageView coverImage = new AsyncImageView(item.getCoverUrl(), 160, 160);
        Rectangle clip = new Rectangle(160, 160);
        clip.setArcWidth(8);
        clip.setArcHeight(8);
        coverImage.setClip(clip);
        
        // 创建榜单标题
        javafx.scene.text.Text title = new javafx.scene.text.Text(item.getTitle());
        title.getStyleClass().add("playlist-title");
        title.setWrappingWidth(140);
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        playlistItem.getChildren().addAll(coverImage, title);
        
        return playlistItem;
    }
    
    /**
     * 榜单项数据类
     */
    private static class PlaylistItem {
        private final String coverUrl;
        private final String title;
        
        public PlaylistItem(String coverUrl, String title) {
            this.coverUrl = coverUrl;
            this.title = title;
        }
        
        public String getCoverUrl() {
            return coverUrl;
        }
        
        public String getTitle() {
            return title;
        }
    }
}