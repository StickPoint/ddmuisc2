package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.common.utils.FontUtil;
import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地歌曲面板
 * @author fntp
 * @date 2025/12/30
 */
public class LocalSongPanel extends BorderPane {

    private UnifiedMusicTable songTable;
    private ComboBox<String> folderComboBox;
    private Button scanButton;
    private Label statusLabel;
    private final MusicState musicState;
    private final BottomMusicContainer bottomMusicContainer;

    public LocalSongPanel(MusicState musicState, BottomMusicContainer bottomMusicContainer) {
        this.musicState = musicState;
        this.bottomMusicContainer = bottomMusicContainer;
        initialize();
        setupLayout();
    }

    private void initialize() {
        // 创建统一音乐表格
        songTable = new UnifiedMusicTable(musicState, bottomMusicContainer);
        
        // 创建文件夹选择器，优化样式
        folderComboBox = new ComboBox<>();
        folderComboBox.setPromptText("选择音乐文件夹");
        folderComboBox.setPrefWidth(350);
        folderComboBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-font-size: 14px; -fx-padding: 8px 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 4, 0, 0, 2);");
        
        // 显式创建并设置ObservableList，确保初始时有一个选项
        ObservableList<String> folderItems = FXCollections.observableArrayList();
        folderItems.add("选择本地文件夹");
        folderComboBox.setItems(folderItems);
        
        // 添加下拉框选择事件
        folderComboBox.setOnAction(e -> {
            String selectedItem = folderComboBox.getValue();
            if ("选择本地文件夹".equals(selectedItem)) {
                // 打开文件夹选择对话框
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("选择音乐文件夹");
                directoryChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
                Stage stage = (Stage) folderComboBox.getScene().getWindow();
                java.io.File selectedDirectory = directoryChooser.showDialog(stage);
                
                if (selectedDirectory != null) {
                    String directoryPath = selectedDirectory.getAbsolutePath();
                    ObservableList<String> items = folderComboBox.getItems();
                    // 检查是否已经存在该路径
                    if (!items.contains(directoryPath)) {
                        // 移除第一个选项，添加新路径，再添加回第一个选项
                        items.remove(0);
                        items.add(directoryPath);
                        items.add(0, "选择本地文件夹");
                    }
                    // 不重置选择，保持当前选择的路径，避免再次触发onAction事件
                    folderComboBox.setValue(directoryPath);
                }
            }
        });
        
        // 创建扫描按钮，优化样式
        scanButton = new Button("扫描文件夹");
        scanButton.setStyle("-fx-background-color: #e53e3e; -fx-text-fill: white; -fx-font-weight: 600; -fx-font-size: 14px; -fx-padding: 10px 24px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(229,62,62,0.2), 8, 0, 0, 4); -fx-transition: all 0.2s ease;");
        scanButton.setOnMouseEntered(e -> scanButton.setStyle("-fx-background-color: #c53030; -fx-text-fill: white; -fx-font-weight: 600; -fx-font-size: 14px; -fx-padding: 10px 24px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(229,62,62,0.3), 10, 0, 0, 5); -fx-transition: all 0.2s ease;")
        );
        scanButton.setOnMouseExited(e -> scanButton.setStyle("-fx-background-color: #e53e3e; -fx-text-fill: white; -fx-font-weight: 600; -fx-font-size: 14px; -fx-padding: 10px 24px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(229,62,62,0.2), 8, 0, 0, 4); -fx-transition: all 0.2s ease;")
        );
        scanButton.setOnAction(e -> scanFolder());
        
        // 创建状态标签，优化样式
        statusLabel = new Label("无数据");
        statusLabel.setFont(FontUtil.loadFont("/font/Y-B008YeZiGongChangDanDanHei-2.ttf", 14));
        statusLabel.setTextFill(Color.web("#666666"));
    }

    private void setupLayout() {
        // 创建底部信息面板，简化样式
        HBox bottomInfo = new HBox();
        bottomInfo.setPadding(new Insets(12, 24, 16, 24));
        bottomInfo.setStyle("-fx-background-color: #ffffff;");
        bottomInfo.setAlignment(Pos.CENTER_LEFT);
        bottomInfo.getChildren().add(statusLabel);
        
        // 创建中间内容区域，全铺满，无边框
        VBox centerContent = new VBox();
        centerContent.setStyle("-fx-background-color: #ffffff; -fx-padding: 0 24 20 24;");
        centerContent.setFillWidth(true);
        VBox.setVgrow(songTable, Priority.ALWAYS);
        centerContent.getChildren().add(songTable);
        
        // 设置布局，移除顶部控制面板（扫描和选择文件夹按钮）
        setCenter(centerContent);
        setBottom(bottomInfo);
    }

    private void scanFolder() {
        // 这里实现扫描文件夹的逻辑
        String selectedFolder = folderComboBox.getValue();
        if (selectedFolder == null || selectedFolder.isEmpty() || "选择本地文件夹".equals(selectedFolder)) {
            statusLabel.setText("请选择有效的文件夹路径");
            return;
        }
        
        statusLabel.setText("正在扫描文件夹: " + selectedFolder);
        
        // 真实的文件夹扫描
        List<UnifiedMusicTable.MusicItem> musicItems = new ArrayList<>();
        java.io.File folder = new java.io.File(selectedFolder);
        
        // 查找音乐文件
        scanMusicFiles(folder, musicItems);
        
        // 设置表格数据（本地歌曲）
        songTable.setData(musicItems, true);
        statusLabel.setText("扫描完成，找到 " + musicItems.size() + " 首歌曲");
    }
    
    /**
     * 递归扫描文件夹中的音乐文件
     * @param folder 要扫描的文件夹
     * @param data 存储扫描结果的列表
     */
    private void scanMusicFiles(java.io.File folder, List<UnifiedMusicTable.MusicItem> data) {
        if (folder.isDirectory()) {
            java.io.File[] files = folder.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    if (file.isDirectory()) {
                        // 递归扫描子文件夹
                        scanMusicFiles(file, data);
                    } else {
                        // 检查是否为音乐文件
                        String fileName = file.getName().toLowerCase();
                        if (fileName.endsWith(".mp3") || fileName.endsWith(".wav") || fileName.endsWith(".flac") || 
                            fileName.endsWith(".aac") || fileName.endsWith(".ogg") || fileName.endsWith(".wma")) {
                            // 提取歌曲名（去除扩展名）
                            String songName = fileName.substring(0, fileName.lastIndexOf("."));
                            // 实际应用中可以使用库来解析音乐文件的元数据，这里简化处理
                            // 假设文件名格式为 "歌手 - 歌曲名"，尝试解析
                            String artist = "未知歌手";
                            String name = songName;
                            if (songName.contains(" - ")) {
                                int separatorIndex = songName.indexOf(" - ");
                                artist = songName.substring(0, separatorIndex);
                                name = songName.substring(separatorIndex + 3);
                            }
                            
                            // 使用本地文件路径创建URL，进行URL编码处理
                            String filePath = file.getAbsolutePath();
                            String encodedFilePath = filePath.replace("\\", "/").replace(" ", "%20");
                            String fileUrl = "file:///" + encodedFilePath;
                            
                            // 添加到统一音乐项列表
                            data.add(new UnifiedMusicTable.MusicItem(
                                    "local-" + file.hashCode(),
                                    name,
                                    artist,
                                    "本地专辑",
                                    "本地",
                                    fileUrl,
                                    "",
                                    "",
                                    filePath
                            ));
                        }
                    }
                }
            }
        }
    }
}
