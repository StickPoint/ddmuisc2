package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.common.utils.FontUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * 下载歌曲面板
 * @author fntp
 * @date 2025/12/30
 */
public class DownloadSongPanel extends VBox {

    public DownloadSongPanel() {
        initialize();
        setupLayout();
    }

    private void initialize() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(50));
        setStyle("-fx-background-color: white;");
        
        // 创建标题
        Label titleLabel = new Label("下载歌曲");
        titleLabel.setFont(FontUtil.loadFont("/font/Y-B008YeZiGongChangDanDanHei-2.ttf", 24));
        titleLabel.setTextFill(Color.RED);
        
        // 创建提示信息
        Label infoLabel = new Label("正在下载的歌曲将显示在这里");
        infoLabel.setFont(FontUtil.loadFont("/font/Y-B008YeZiGongChangDanDanHei-2.ttf", 16));
        infoLabel.setTextFill(Color.GRAY);
        infoLabel.setPadding(new Insets(20, 0, 0, 0));
        
        // 添加到布局
        getChildren().addAll(titleLabel, infoLabel);
    }

    private void setupLayout() {
        // 已经在initialize中设置了布局
    }
}
