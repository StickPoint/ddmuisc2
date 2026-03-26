package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import java.util.List;

/**
 * 歌单详情页面容器
 * @author fntp
 * @date 2026/1/24
 */
public class PlaylistDetailContainer extends VBox {

    /**
     * 歌单封面
     */
    private ImageView coverImageView;
    
    /**
     * 歌单名称
     */
    private Label playlistNameLabel;
    
    /**
     * 播放全部按钮
     */
    private Button playAllButton;
    
    /**
     * 收藏歌单按钮
     */
    private Button collectButton;
    
    /**
     * 分享歌单按钮
     */
    private Button shareButton;
    
    /**
     * 歌曲列表表格
     */
    private UnifiedMusicTable musicTable;
    
    /**
     * 音乐状态
     */
    private final MusicState musicState;
    
    /**
     * 底部音乐容器
     */
    private final BottomMusicContainer bottomMusicContainer;
    
    /**
     * 构造函数
     * @param musicState 音乐状态
     * @param bottomMusicContainer 底部音乐容器
     */
    public PlaylistDetailContainer(MusicState musicState, BottomMusicContainer bottomMusicContainer) {
        this.musicState = musicState;
        this.bottomMusicContainer = bottomMusicContainer;
        
        // 初始化UI
        initUI();
    }
    
    /**
     * 初始化UI
     */
    private void initUI() {
        // 设置容器样式
        setSpacing(15);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: white;");
        
        // 创建上半部分容器
        HBox topContainer = new HBox();
        topContainer.setSpacing(15);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        
        // 创建歌单封面
        coverImageView = new ImageView();
        coverImageView.setFitWidth(120);
        coverImageView.setFitHeight(120);
        // 设置圆角
        Rectangle clip = new Rectangle(120, 120);
        clip.setArcWidth(8);
        clip.setArcHeight(8);
        coverImageView.setClip(clip);
        // 设置默认封面
        coverImageView.setImage(new Image(getClass().getResourceAsStream("/img/logo.png")));
        
        // 创建右侧信息容器
        VBox infoContainer = new VBox();
        infoContainer.setSpacing(10);
        infoContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoContainer, javafx.scene.layout.Priority.ALWAYS);
        
        // 歌单名称
        playlistNameLabel = new Label("歌单名称");
        playlistNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        
        // 操作按钮容器
        HBox buttonContainer = new HBox();
        buttonContainer.setSpacing(10);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        
        // 播放全部按钮
        playAllButton = new Button("播放全部");
        playAllButton.setStyle("-fx-background-color: #ff6a6a; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        playAllButton.setCursor(javafx.scene.Cursor.HAND);
        
        // 收藏歌单按钮
        collectButton = new Button("收藏歌单");
        collectButton.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333333; -fx-font-size: 14px; -fx-padding: 8 20;");
        collectButton.setCursor(javafx.scene.Cursor.HAND);
        
        // 分享歌单按钮
        shareButton = new Button("分享歌单");
        shareButton.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333333; -fx-font-size: 14px; -fx-padding: 8 20;");
        shareButton.setCursor(javafx.scene.Cursor.HAND);
        
        // 添加按钮到容器
        buttonContainer.getChildren().addAll(playAllButton, collectButton, shareButton);
        
        // 添加组件到右侧信息容器
        infoContainer.getChildren().addAll(playlistNameLabel, buttonContainer);
        
        // 添加组件到上半部分容器
        topContainer.getChildren().addAll(coverImageView, infoContainer);
        
        // 创建歌曲列表表格
        musicTable = new UnifiedMusicTable(musicState, bottomMusicContainer);
        
        // 添加组件到主容器
        getChildren().addAll(topContainer, musicTable);
        
        // 设置优先增长
        VBox.setVgrow(musicTable, javafx.scene.layout.Priority.ALWAYS);
        
        // 添加事件监听
        addEventListeners();
    }
    
    /**
     * 添加事件监听
     */
    private void addEventListeners() {
        // 播放全部按钮点击事件
        playAllButton.setOnAction(event -> {
            // 实现播放全部逻辑
            System.out.println("播放全部");
        });
        
        // 收藏歌单按钮点击事件
        collectButton.setOnAction(event -> {
            // 实现收藏歌单逻辑
            System.out.println("收藏歌单");
        });
        
        // 分享歌单按钮点击事件
        shareButton.setOnAction(event -> {
            // 实现分享歌单逻辑
            System.out.println("分享歌单");
        });
    }
    
    /**
     * 设置歌单数据
     * @param playlistId 歌单ID
     * @param playlistName 歌单名称
     * @param coverUrl 封面URL
     * @param source 音源
     */
    public void setPlaylistData(String playlistId, String playlistName, String coverUrl, String source) {
        // 更新歌单名称
        this.playlistNameLabel.setText(playlistName);
        
        // 更新歌单封面
        if (coverUrl != null && !coverUrl.isEmpty()) {
            // 异步加载图片
            javafx.concurrent.Task<Image> task = new javafx.concurrent.Task<>() {
                @Override
                protected Image call() {
                    // 在后台线程加载图片
                    return new Image(coverUrl, 120, 120, true, true, true);
                }
                
                @Override
                protected void succeeded() {
                    // 加载成功后在UI线程更新图片
                    coverImageView.setImage(getValue());
                }
                
                @Override
                protected void failed() {
                    // 加载失败时可以设置默认图片或处理错误
                    System.err.println("Failed to load image: " + coverUrl);
                }
            };
            
            // 启动后台线程
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
        
        // 请求歌单歌曲列表数据
        loadPlaylistSongs(playlistId, source);
    }
    
    /**
     * 加载歌单歌曲列表
     * @param playlistId 歌单ID
     * @param source 音源
     */
    private void loadPlaylistSongs(String playlistId, String source) {
        System.out.println("请求歌单歌曲列表: " + playlistId + ", 音源: " + source);
        
        // 清空表格
        musicTable.clearData();
        
        // 转换音源为Platform枚举
        com.stickpoint.ddmusic.common.utils.TuneHubApiUtils.Platform platform;
        if ("wangyi".equals(source)) {
            platform = com.stickpoint.ddmusic.common.utils.TuneHubApiUtils.Platform.NETEASE;
        } else if ("kuwo".equals(source)) {
            platform = com.stickpoint.ddmusic.common.utils.TuneHubApiUtils.Platform.KUWO;
        } else {
            platform = com.stickpoint.ddmusic.common.utils.TuneHubApiUtils.Platform.NETEASE;
        }
        
        // 发送请求获取歌单歌曲列表
        new Thread(() -> {
            try {
                // 获取排行榜歌曲列表
                String result = com.stickpoint.ddmusic.common.utils.TuneHubApiUtils.getToplistSongs(platform, playlistId);
                
                System.out.println("歌单歌曲列表响应: " + result);
                
                // 解析歌单歌曲列表
                List<UnifiedMusicTable.MusicItem> musicItems = parsePlaylistSongs(result, platform.getValue());
                
                // 打印解析结果
                System.out.println("解析得到 " + musicItems.size() + " 首歌曲");
                
                // 更新UI
                javafx.application.Platform.runLater(() -> {
                    System.out.println("更新表格数据，共 " + musicItems.size() + " 首歌曲");
                    musicTable.setData(musicItems, false);
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("获取歌单歌曲列表失败: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 解析歌单歌曲列表
     * @param jsonData JSON数据
     * @param source 音源
     * @return 音乐项列表
     */
    private List<UnifiedMusicTable.MusicItem> parsePlaylistSongs(String jsonData, String source) {
        List<UnifiedMusicTable.MusicItem> musicItems = new java.util.ArrayList<>();
        
        try {
            // 使用Gson解析JSON
            com.google.gson.JsonObject rootObject = com.google.gson.JsonParser.parseString(jsonData).getAsJsonObject();
            
            // 获取data字段
            if (rootObject.has("data")) {
                com.google.gson.JsonObject dataObject = rootObject.getAsJsonObject("data");
                
                // 获取歌曲列表（直接在data字段下，而不是songs字段）
                if (dataObject.has("list")) {
                    com.google.gson.JsonArray songsArray = dataObject.getAsJsonArray("list");
                    
                    // 遍历歌曲列表
                    for (int i = 0; i < songsArray.size(); i++) {
                        com.google.gson.JsonObject songObject = songsArray.get(i).getAsJsonObject();
                        
                        // 获取歌曲ID
                        String songId = songObject.has("id") ? songObject.get("id").getAsString() : "";
                        
                        // 获取歌曲名称
                        String songName = songObject.has("name") ? songObject.get("name").getAsString() : "未知歌曲";
                        
                        // 获取艺术家
                        String artist = songObject.has("artist") ? songObject.get("artist").getAsString() : "未知艺术家";
                        
                        // 获取专辑名称
                        String album = songObject.has("album") ? songObject.get("album").getAsString() : "未知专辑";
                        
                        // 获取封面URL
                        String picUrl = songObject.has("pic") ? songObject.get("pic").getAsString() : "";
                        
                        // 获取播放URL
                        String playUrl = songObject.has("url") ? songObject.get("url").getAsString() : "";
                        
                        // 获取歌词URL
                        String lrcUrl = songObject.has("lrc") ? songObject.get("lrc").getAsString() : "";
                        
                        // 创建音乐项
                        if (!songId.isEmpty() && !songName.isEmpty()) {
                            UnifiedMusicTable.MusicItem musicItem = new UnifiedMusicTable.MusicItem(
                                songId,
                                songName,
                                artist,
                                album,
                                source,
                                playUrl,
                                picUrl,
                                lrcUrl
                            );
                            musicItems.add(musicItem);
                        }
                    }
                }
            }
            
            System.out.println("成功解析 " + musicItems.size() + " 首歌曲");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("解析歌单歌曲列表失败: " + e.getMessage());
        }
        
        return musicItems;
    }
    
    /**
     * 清空数据
     */
    public void clearData() {
        musicTable.clearData();
        playlistNameLabel.setText("歌单名称");
        coverImageView.setImage(new Image(getClass().getResourceAsStream("/img/logo.png")));
    }
}