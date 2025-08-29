package com.stickpoint.ddmusic.page.node;

import com.leewyatt.rxcontrols.controls.RXAvatar;
import com.leewyatt.rxcontrols.controls.RXMediaProgressBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Objects;

/**
 * 底部音乐播放控制容器
 * @author fntp
 * @date 2025/8/24
 */
public class BottomMusicContainer extends HBox {

    private final HBox imageSection;
    private final HBox controlSection;
    private final HBox progressSection;
    private final HBox extraControlsSection;
    public RXAvatar albumImageView;
    private Label songNameLabel;
    private Label artistNameLabel;

    private SvgIcon playPauseButton;
    private SvgIcon previousButton;
    private SvgIcon nextButton;

    /**
     * 播放器进度条
     */
    private RXMediaProgressBar progressSlider;
    /**
     * 播放器进度条当前时间标签
     */
    private Label playerTimeLabel;

    private Button volumeButton;
    private Button playlistButton;
    private Button lyricsButton;


    public BottomMusicContainer() {
        albumImageView = new RXAvatar("https://qnm.hunliji.com/o_1j3m01ogr2mm1qr61g8r6ud8ea9.jpg");
        songNameLabel = new Label();
        artistNameLabel = new Label();
        playPauseButton = new SvgIcon();
        previousButton = new SvgIcon();
        nextButton = new SvgIcon();
        volumeButton = new Button();
        playlistButton = new Button();
        lyricsButton = new Button();
        progressSlider = new RXMediaProgressBar();
        playerTimeLabel = new Label();

        // 设置容器基本属性
        setPrefHeight(70);
        setMinHeight(70);
        setMaxHeight(70);
        setAlignment(Pos.CENTER);
        setSpacing(0);
        setStyle("-fx-background-color: #f8f9fa; " +
                "-fx-border-color: #dee2e6; " +
                "-fx-border-width: 1 0 0 0;");

        // 创建四个部分
        imageSection = new HBox();
        controlSection = new HBox();
        progressSection = new HBox();
        extraControlsSection = new HBox();

        // 初始化各部分组件
        initImageSection();
        initControlSection();
        initProgressSection();
        initExtraControlsSection();

        // 设置各部分样式
        setupSectionStyles();

        // 添加弹性区域以占据剩余空间
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        // 添加所有部分到主容器
        getChildren().addAll(
                imageSection,
                controlSection,
                spacer1,
                progressSection,
                spacer2,
                extraControlsSection
        );

        // 在BottomMusicContainer构造函数或初始化方法中
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/BottomMusicContainer.css")).toExternalForm());

    }

    /**
     * 初始化图片部分
     */
    private void initImageSection() {
        imageSection.setAlignment(Pos.CENTER);
        imageSection.setPadding(new Insets(5, 15, 5, 15));
        imageSection.setSpacing(10);

        // 专辑图片（使用占位符）
        albumImageView.setPrefSize(50,50);
        //albumImageView.setStyle("-fx-background-color: #ddd; -fx-border-radius: 5px;");

        // 歌曲信息
        VBox songInfoBox = new VBox();
        songInfoBox.setAlignment(Pos.CENTER_LEFT);
        songInfoBox.setSpacing(2);

        songNameLabel = new Label("歌曲名称");
        songNameLabel.setFont(Font.font("Microsoft YaHei", 14));
        songNameLabel.setPrefWidth(120);
        songNameLabel.setMinWidth(80);
        songNameLabel.setStyle("-fx-text-fill: #212529;");
        songNameLabel.setEllipsisString("...");

        Tooltip songNameTooltip = new Tooltip("歌曲名称");
        Tooltip.install(songNameLabel, songNameTooltip);

        artistNameLabel = new Label("艺术家");
        artistNameLabel.setFont(Font.font("Microsoft YaHei", 12));
        artistNameLabel.setPrefWidth(120);
        artistNameLabel.setMinWidth(60);
        artistNameLabel.setStyle("-fx-text-fill: #6c757d;");
        artistNameLabel.setEllipsisString("...");

        Tooltip artistNameTooltip = new Tooltip("艺术家");
        Tooltip.install(artistNameLabel, artistNameTooltip);

        songInfoBox.getChildren().addAll(songNameLabel, artistNameLabel);

        imageSection.getChildren().addAll(albumImageView, songInfoBox);
    }

    /**
     * 初始化操作部分
     */
    private void initControlSection() {
        controlSection.setAlignment(Pos.CENTER);
        controlSection.setPadding(new Insets(0, 15, 0, 15));
        controlSection.setSpacing(15);

        // 上一首按钮
        previousButton = createSvgIcon("M512.269015 1024a511.992548 511.992548 0 1 1 511.73106-511.73106 512.515523 512.515523 0 0 1-511.73106 511.73106z m0-971.426105a459.695045 459.695045 0 1 0 459.433557 459.695045A460.21802 460.21802 0 0 0 512.269015 52.573895zM659.526718 509.210811m-17.289056 19.617485l0 0q-17.289056 19.617486-36.906541 2.32843l-221.089062-194.847658q-19.617486-17.289056-2.32843-36.906542l0 0q17.289056-19.617486 36.906542-2.328429l221.089061 194.847658q19.617486 17.289056 2.32843 36.906541ZM625.322329 476.041569m17.289055 19.617486l0 0q17.289056 19.617486-2.328429 36.906541l-221.089062 194.847658q-19.617486 17.289056-36.906541-2.328429l0 0q-17.289056-19.617486 2.328429-36.906542l221.089062-194.847658q19.617486-17.289056 36.906541 2.32843Z");
        previousButton.setRotate(180);
        previousButton.setFill("gray");

        // 播放/暂停按钮
        playPauseButton = createSvgIcon("M692.224 495.616 692.224 495.616 692.224 495.616c-4.096-4.096-4.096-4.096-4.096-4.096l-258.048-147.456 0 0c-4.096-4.096-8.192-4.096-12.288-4.096-12.288 0-20.48 8.192-20.48 20.48l0 303.104c0 12.288 8.192 20.48 20.48 20.48 4.096 0 8.192 0 12.288-4.096l0 0 258.048-147.456c0 0 0 0 0 0l4.096 0 0 0c4.096-4.096 8.192-8.192 8.192-16.384S696.32 499.712 692.224 495.616zM438.272 630.784 438.272 393.216l208.896 118.784L438.272 630.784zM512 98.304C282.624 98.304 98.304 282.624 98.304 512s184.32 413.696 413.696 413.696c229.376 0 413.696-184.32 413.696-413.696S741.376 98.304 512 98.304zM512 888.832c-208.896 0-376.832-167.936-376.832-376.832 0-208.896 167.936-376.832 376.832-376.832 208.896 0 376.832 167.936 376.832 376.832C888.832 720.896 720.896 888.832 512 888.832z");
        playPauseButton.setFill("gray");

        // 下一首按钮
        nextButton = createSvgIcon("M512.269015 1024a511.992548 511.992548 0 1 1 511.73106-511.73106 512.515523 512.515523 0 0 1-511.73106 511.73106z m0-971.426105a459.695045 459.695045 0 1 0 459.433557 459.695045A460.21802 460.21802 0 0 0 512.269015 52.573895zM659.526718 509.210811m-17.289056 19.617485l0 0q-17.289056 19.617486-36.906541 2.32843l-221.089062-194.847658q-19.617486-17.289056-2.32843-36.906542l0 0q17.289056-19.617486 36.906542-2.328429l221.089061 194.847658q19.617486 17.289056 2.32843 36.906541ZM625.322329 476.041569m17.289055 19.617486l0 0q17.289056 19.617486-2.328429 36.906541l-221.089062 194.847658q-19.617486 17.289056-36.906541-2.328429l0 0q-17.289056-19.617486 2.328429-36.906542l221.089062-194.847658q19.617486-17.289056 36.906541 2.32843Z");
        nextButton.setFill("gray");

        // 在initControlSection方法中，在添加到controlSection之前
        HBox.setMargin(previousButton, new Insets(10, 8, 10, 8));
        HBox.setMargin(playPauseButton, new Insets(10, 8, 10, 8));
        HBox.setMargin(nextButton, new Insets(10, 8, 10, 8));
        controlSection.getChildren().addAll(previousButton, playPauseButton, nextButton);
    }

    /**
     * 初始化进度条部分
     */
    private void initProgressSection() {
        progressSection.setAlignment(Pos.CENTER);
        progressSection.setPadding(new Insets(0, 15, 0, 15));
        progressSection.setSpacing(10);

        // 当前时间标签
        playerTimeLabel = new Label("00:00");
        playerTimeLabel.setFont(Font.font("Microsoft YaHei", 12));
        playerTimeLabel.setStyle("-fx-text-fill: #6c757d;");

        // 进度滑块
        progressSlider.setPrefWidth(350);
        // 为进度条设置样式类
        progressSlider.setMaxHeight(18);
        progressSlider.setMinHeight(18);
        progressSlider.getStyleClass().add("rx-media-progress-bar");
        progressSection.getChildren().addAll(progressSlider, playerTimeLabel);
    }

    /**
     * 初始化其他操作按钮部分
     */
    private void initExtraControlsSection() {
        extraControlsSection.setAlignment(Pos.CENTER);
        extraControlsSection.setPadding(new Insets(0, 15, 0, 15));
        extraControlsSection.setSpacing(15);

        // 音量按钮
        volumeButton = new Button();
        volumeButton.setPrefSize(30, 30);
        volumeButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        volumeButton.setGraphic(createSvgIcon("M512 320c35.3 0 64-28.7 64-64s-28.7-64-64-64-64 28.7-64 64 28.7 64 64 64z")); // 音量图标

        // 播放列表按钮
        playlistButton = new Button();
        playlistButton.setPrefSize(30, 30);
        playlistButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        playlistButton.setGraphic(createSvgIcon("M128 102.4h64v64h-64v-64zM128 230.4h64v64h-64v-64zM128 358.4h64v64h-64v-64z")); // 列表图标

        // 歌词按钮
        lyricsButton = new Button();
        lyricsButton.setPrefSize(30, 30);
        lyricsButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        lyricsButton.setGraphic(createSvgIcon("M256 64c106 0 192 86 192 192s-86 192-192 192S64 362 64 256 150 64 256 64z")); // 圆形图标

        extraControlsSection.getChildren().addAll(volumeButton, playlistButton, lyricsButton);
    }

    /**
     * 设置各部分样式
     */
    private void setupSectionStyles() {
        imageSection.setStyle("-fx-background-color: transparent;");
        controlSection.setStyle("-fx-background-color: transparent;");
        progressSection.setStyle("-fx-background-color: transparent;");
        extraControlsSection.setStyle("-fx-background-color: transparent;");
    }

    /**
     * 创建简单的SVG图标（占位符）
     */
    private SvgIcon createSvgIcon(String pathData) {
        SvgIcon icon = new SvgIcon(pathData);
        icon.setIconSize(30, 30);
        return icon;
    }

    // Getter方法
    public RXAvatar getAlbumImageView() {
        return albumImageView;
    }

    public Label getSongNameLabel() {
        return songNameLabel;
    }

    public Label getArtistNameLabel() {
        return artistNameLabel;
    }

    public Region getPlayPauseButton() {
        return playPauseButton;
    }

    public Region getPreviousButton() {
        return previousButton;
    }

    public Region getNextButton() {
        return nextButton;
    }

    public RXMediaProgressBar getProgressSlider() {
        return progressSlider;
    }


    public Button getVolumeButton() {
        return volumeButton;
    }

    public Button getPlaylistButton() {
        return playlistButton;
    }

    public Button getLyricsButton() {
        return lyricsButton;
    }
}