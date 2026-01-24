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
        
        // 创建文件夹选择器
        folderComboBox = new ComboBox<>();
        folderComboBox.setPromptText("选择音乐文件夹");
        folderComboBox.setPrefWidth(300);
        
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
        
        // 创建扫描按钮
        scanButton = new Button("扫描文件夹");
        scanButton.setStyle("-fx-background-color: #ff3c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        scanButton.setOnAction(e -> scanFolder());
        
        // 创建状态标签
        statusLabel = new Label("无数据");
        statusLabel.setFont(FontUtil.loadFont("/font/Y-B008YeZiGongChangDanDanHei-2.ttf", 16));
        statusLabel.setTextFill(Color.RED);
    }

    private void setupLayout() {
        // 创建顶部控制面板
        HBox topControl = new HBox();
        topControl.setSpacing(10);
        topControl.setPadding(new Insets(15, 20, 15, 20));
        topControl.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
        topControl.setAlignment(Pos.CENTER_LEFT);
        topControl.getChildren().addAll(folderComboBox, scanButton);
        
        // 创建底部信息面板
        HBox bottomInfo = new HBox();
        bottomInfo.setPadding(new Insets(10, 20, 15, 20));
        bottomInfo.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1 0 0 0;");
        bottomInfo.setAlignment(Pos.CENTER_LEFT);
        bottomInfo.getChildren().add(statusLabel);
        
        // 创建中间内容区域
        VBox centerContent = new VBox();
        centerContent.setStyle("-fx-background-color: white;");
        centerContent.getChildren().add(songTable);
        
        // 设置布局
        setTop(topControl);
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
