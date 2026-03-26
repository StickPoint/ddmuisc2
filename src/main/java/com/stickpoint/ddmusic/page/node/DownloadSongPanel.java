package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.common.utils.FontUtil;
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

/**
 * 下载管理面板
 * @author fntp
 * @date 2025/12/30
 */
public class DownloadSongPanel extends VBox {

    private TableView<DownloadItem> downloadTable;
    private ObservableList<DownloadItem> downloadItems;

    public DownloadSongPanel() {
        initialize();
        setupLayout();
    }

    private void initialize() {
        // 初始化下载列表
        downloadItems = FXCollections.observableArrayList();
        
        // 创建下载表格
        downloadTable = new TableView<>();
        downloadTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        downloadTable.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 8px;");
        
        // 创建表格列
        TableColumn<DownloadItem, String> nameColumn = new TableColumn<>("歌曲名");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(280);
        nameColumn.setStyle("-fx-font-weight: 600;");
        
        TableColumn<DownloadItem, String> artistColumn = new TableColumn<>("艺术家");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        artistColumn.setPrefWidth(160);
        
        TableColumn<DownloadItem, String> statusColumn = new TableColumn<>("状态");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(120);
        statusColumn.setCellFactory(col -> new StatusTableCell());
        
        TableColumn<DownloadItem, String> progressColumn = new TableColumn<>("进度");
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));
        progressColumn.setPrefWidth(150);
        progressColumn.setCellFactory(col -> new ProgressTableCell());
        
        TableColumn<DownloadItem, String> speedColumn = new TableColumn<>("速度");
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        speedColumn.setPrefWidth(100);
        
        TableColumn<DownloadItem, String> actionColumn = new TableColumn<>("操作");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        actionColumn.setPrefWidth(80);
        actionColumn.setCellFactory(col -> new ActionTableCell());
        
        // 添加列到表格
        downloadTable.getColumns().addAll(nameColumn, artistColumn, statusColumn, progressColumn, speedColumn, actionColumn);
        
        // 设置表格数据
        downloadTable.setItems(downloadItems);
        
        // 模拟一些下载数据
        downloadItems.add(new DownloadItem("1", "歌曲1", "艺术家1", "下载中", "50%", "1.2 MB/s"));
        downloadItems.add(new DownloadItem("2", "歌曲2", "艺术家2", "暂停中", "30%", "0 B/s"));
        downloadItems.add(new DownloadItem("3", "歌曲3", "艺术家3", "下载中", "80%", "2.5 MB/s"));
    }

    private void setupLayout() {
        // 创建中间内容区域，全铺满，无边框
        VBox centerContent = new VBox();
        centerContent.setStyle("-fx-background-color: #ffffff; -fx-padding: 0 24 20 24;");
        centerContent.setFillWidth(true);
        VBox.setVgrow(downloadTable, Priority.ALWAYS);
        centerContent.getChildren().add(downloadTable);
        
        // 设置布局
        setStyle("-fx-background-color: #ffffff;");
        getChildren().add(centerContent);
    }
    
    /**
     * 下载任务数据模型
     */
    public static class DownloadItem {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty artist;
        private final SimpleStringProperty status;
        private final SimpleStringProperty progress;
        private final SimpleStringProperty speed;
        
        public DownloadItem(String id, String name, String artist, String status, String progress, String speed) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.artist = new SimpleStringProperty(artist);
            this.status = new SimpleStringProperty(status);
            this.progress = new SimpleStringProperty(progress);
            this.speed = new SimpleStringProperty(speed);
        }
        
        public String getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getArtist() { return artist.get(); }
        public String getStatus() { return status.get(); }
        public String getProgress() { return progress.get(); }
        public String getSpeed() { return speed.get(); }
    }
    
    /**
     * 状态列单元格
     */
    private class StatusTableCell extends TableCell<DownloadItem, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setStyle("");
            } else {
                setText(item);
                setStyle("-fx-font-size: 13px; -fx-font-weight: 500; -fx-alignment: CENTER;");
                if ("下载中".equals(item)) {
                    setTextFill(Color.web("#3182ce"));
                } else if ("暂停中".equals(item)) {
                    setTextFill(Color.web("#d69e2e"));
                } else {
                    setTextFill(Color.web("#666666"));
                }
            }
        }
    }
    
    /**
     * 进度列单元格
     */
    private class ProgressTableCell extends TableCell<DownloadItem, String> {
        private HBox progressBox;
        private ProgressBar progressBar;
        private Label progressLabel;
        
        public ProgressTableCell() {
            progressBar = new ProgressBar();
            progressBar.setPrefHeight(8);
            progressBar.setStyle("-fx-accent: #e53e3e; -fx-progress-color: #e53e3e;");
            
            progressLabel = new Label();
            progressLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666666; -fx-padding: 0 8px;");
            
            progressBox = new HBox();
            progressBox.setAlignment(Pos.CENTER_LEFT);
            progressBox.setSpacing(8);
            progressBox.getChildren().addAll(progressBar, progressLabel);
            HBox.setHgrow(progressBar, Priority.ALWAYS);
        }
        
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                progressLabel.setText(item);
                double progress = Double.parseDouble(item.replace("%", "")) / 100;
                progressBar.setProgress(progress);
                setGraphic(progressBox);
                setStyle("-fx-alignment: CENTER_LEFT;");
            }
        }
    }
    
    /**
     * 操作列单元格
     */
    private class ActionTableCell extends TableCell<DownloadItem, String> {
        private final Button actionButton = new Button();
        
        public ActionTableCell() {
            actionButton.setText("⋮");
            actionButton.setFont(Font.font(20));
            actionButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #999999; -fx-padding: 8px; -fx-min-width: 36px; -fx-min-height: 36px; -fx-background-radius: 50%; -fx-transition: all 0.2s ease;");
            actionButton.setFocusTraversable(false);
            actionButton.setCursor(javafx.scene.Cursor.HAND);
            
            // 添加悬停效果
            actionButton.setOnMouseEntered(e -> {
                actionButton.setStyle("-fx-background-color: rgba(229, 62, 62, 0.1); -fx-text-fill: #e53e3e; -fx-padding: 8px; -fx-min-width: 36px; -fx-min-height: 36px; -fx-background-radius: 50%; -fx-transition: all 0.2s ease;");
            });
            
            actionButton.setOnMouseExited(e -> {
                actionButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #999999; -fx-padding: 8px; -fx-min-width: 36px; -fx-min-height: 36px; -fx-background-radius: 50%; -fx-transition: all 0.2s ease;");
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
    }
}
