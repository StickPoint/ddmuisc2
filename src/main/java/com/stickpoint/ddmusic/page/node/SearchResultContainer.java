package com.stickpoint.ddmusic.page.node;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stickpoint.ddmusic.common.utils.TuneHubApiUtils;
import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 搜索结果容器
 * @author fntp
 * @date 2026/1/23
 */
public class SearchResultContainer extends VBox {

    private static final Logger log = LoggerFactory.getLogger(SearchResultContainer.class);
    private static final Gson GSON = new Gson();

    private final UnifiedMusicTable musicTable;
    private final Label searchKeywordLabel;
    private final Label resultCountLabel;
    private final MusicState musicState;
    private final BottomMusicContainer bottomMusicContainer;

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
        
        // 创建统一音乐表格
        musicTable = new UnifiedMusicTable(musicState, bottomMusicContainer);
        
        // 将组件添加到容器
        getChildren().addAll(titleBox, musicTable);
    }
    
    /**
     * 执行搜索
     * @param keyword 搜索关键词
     */
    public void search(String keyword) {
        searchKeywordLabel.setText("搜索结果: " + keyword);
        resultCountLabel.setText("正在搜索...");
        
        // 在后台线程执行搜索
        new Thread(() -> {
            try {
                log.info("开始搜索关键词: {}", keyword);
                String resultJson = TuneHubApiUtils.aggregateSearch(keyword);
                log.info("搜索结果JSON: {}", resultJson);
                
                // 使用 Gson 解析 JSON
                Type resultType = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> resultMap = GSON.fromJson(resultJson, resultType);
                
                if (resultMap != null && resultMap.containsKey("code")) {
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
                                
                                // 转换为统一音乐项
                                List<UnifiedMusicTable.MusicItem> musicItems = resultsList.stream().map(item -> {
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
                                    
                                    // 创建统一音乐项（网络歌曲）
                                    return new UnifiedMusicTable.MusicItem(
                                            id, name, artist, album, platform,
                                            playUrl, picUrl, lrcUrl
                                    );
                                }).collect(Collectors.toList());
                                
                                Platform.runLater(() -> {
                                    resultCountLabel.setText("共找到 " + musicItems.size() + " 条结果");
                                    // 设置表格数据（非本地歌曲）
                                    musicTable.setData(musicItems, false);
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
     * 清理搜索结果，释放资源
     */
    public void clear() {
        // 清空表格数据
        musicTable.clearData();
        // 清空搜索关键词和结果数量
        searchKeywordLabel.setText("");
        resultCountLabel.setText("");
    }
}
