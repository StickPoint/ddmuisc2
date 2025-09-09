package com.stickpoint.ddmusic.page.node;

import com.leewyatt.rxcontrols.controls.RXAvatar;
import com.leewyatt.rxcontrols.controls.RXMediaProgressBar;
import com.stickpoint.ddmusic.page.skin.RedVerticalSliderSkin;
import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * 底部音乐播放控制容器
 * @author fntp
 * @date 2025/8/24
 */
public class BottomMusicContainer extends HBox {

    /**
     * 音乐播放：专辑封面 + 歌手信息 + 歌曲信息
     */
    private final HBox imageSection;
    /**
     * 音乐播放控制：播放/暂停、上一曲、下一曲
     */
    private final HBox controlSection;
    /**
     * 音乐播放进度：当前播放时间、总时长、进度条
     */
    private final HBox progressSection;
    /**
     * 其他控件：音量调节，变速，等等
     */
    private final HBox extraControlsSection;
    /**
     * 专辑封面
     */
    public RXAvatar albumImageView;
    /**
     * 歌曲信息标签
     */
    private Label songNameLabel;
    /**
     * 专辑封面下方的歌手信息标签
     */
    private Label artistNameLabel;
    /**
     * 音乐直接播放器
     */
    private MediaPlayer musicPlayer;
    /**
     * 播放/暂停按钮
     */
    private SvgIcon playPauseButton;
    /**
     * 上一曲按钮
     */
    private SvgIcon previousButton;
    /**
     * 下一曲按钮
     */
    private SvgIcon nextButton;
    /**
     * 播放器进度条
     */
    private RXMediaProgressBar playerProgressBar;
    /**
     * 播放器进度条当前时间标签
     */
    private Label playerTimeLabel;
    /**
     * 喜欢按钮
     */
    private SvgIcon likedButton;
    /**
     * 音量调节组件
     */
    private ContextMenu soundPopup;
    /**
     * 音量滑动条
     */
    private Slider soundSlider;
    /**
     * 音量调节按钮
     */
    private SvgIcon volumeButton;

    /**
     * 添加到列表按钮
     */
    private SvgIcon add2ListButton;
    /**
     * 歌词按钮
     */
    private SvgIcon lyricsButton;
    /**
     * 分享按钮
     */
    private SvgIcon shareButton;
    /**
     * 音乐播放核心State
     */
    private MusicState musicState;


    public BottomMusicContainer(MusicState musicState) {
        this.musicState = musicState;
        albumImageView = new RXAvatar("https://qnm.hunliji.com/o_1j3tjm1ceud2cup11k31hpfh7217.jpeg");
        songNameLabel = new Label();
        artistNameLabel = new Label();
        playPauseButton = new SvgIcon();
        previousButton = new SvgIcon();
        nextButton = new SvgIcon();
        likedButton = new SvgIcon();
        volumeButton = new SvgIcon();
        add2ListButton = new SvgIcon();
        lyricsButton = new SvgIcon();
        shareButton = new SvgIcon();
        playerProgressBar = new RXMediaProgressBar();
        playerTimeLabel = new Label();
        musicPlayer = new MediaPlayer(new Media("https://qnm.hunliji.com/o_1j4aiov931him1nmo1ujh2221jdqo.mp3"));

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

        // 初始化播放进度条的交互功能
        initProgressBar();

        // 为播放/暂停按钮添加点击事件处理器
        playPauseButton.setOnMouseClicked(event -> {
            MediaPlayer.Status currentStatus = musicPlayer.getStatus();
            if (currentStatus == MediaPlayer.Status.PLAYING) {
                // 更新按钮图标为播放图标
                playPauseButton.modifyContent("M692.224 495.616 692.224 495.616 692.224 495.616c-4.096-4.096-4.096-4.096-4.096-4.096l-258.048-147.456 0 0c-4.096-4.096-8.192-4.096-12.288-4.096-12.288 0-20.48 8.192-20.48 20.48l0 303.104c0 12.288 8.192 20.48 20.48 20.48 4.096 0 8.192 0 12.288-4.096l0 0 258.048-147.456c0 0 0 0 0 0l4.096 0 0 0c4.096-4.096 8.192-8.192 8.192-16.384S696.32 499.712 692.224 495.616zM438.272 630.784 438.272 393.216l208.896 118.784L438.272 630.784zM512 98.304C282.624 98.304 98.304 282.624 98.304 512s184.32 413.696 413.696 413.696c229.376 0 413.696-184.32 413.696-413.696S741.376 98.304 512 98.304zM512 888.832c-208.896 0-376.832-167.936-376.832-376.832 0-208.896 167.936-376.832 376.832-376.832 208.896 0 376.832 167.936 376.832 376.832C888.832 720.896 720.896 888.832 512 888.832z");
                // 播放暂停
                musicPlayer.pause();
                // 通过 MusicState 控制播放状态
                musicState.setPlayerStatusProperty(MediaPlayer.Status.PAUSED);
            } else {
                // 更新按钮图标为暂停图标 (你可以替换为实际的暂停图标路径)
                playPauseButton.modifyContent("M512 1024C228.266667 1024 0 795.733333 0 512S228.266667 0 512 0s512 228.266667 512 512-228.266667 512-512 512z m0-42.666667c260.266667 0 469.333333-209.066667 469.333333-469.333333S772.266667 42.666667 512 42.666667 42.666667 251.733333 42.666667 512s209.066667 469.333333 469.333333 469.333333z m-106.666667-682.666666c12.8 0 21.333333 8.533333 21.333334 21.333333v384c0 12.8-8.533333 21.333333-21.333334 21.333333s-21.333333-8.533333-21.333333-21.333333V320c0-12.8 8.533333-21.333333 21.333333-21.333333z m213.333334 0c12.8 0 21.333333 8.533333 21.333333 21.333333v384c0 12.8-8.533333 21.333333-21.333333 21.333333s-21.333333-8.533333-21.333334-21.333333V320c0-12.8 8.533333-21.333333 21.333334-21.333333z");
                // 播放
                musicPlayer.play();
                // 初始化播放音乐 TODO 这里后续根据接口初始化播放音乐
                musicState.playMusic("Jar Of Love",
                        "曲婉婷",
                        "我的歌声里",
                        //"https://imge.kugou.com/stdmusic/20250221/20250221180758694578.jpg",
                        "https://qnm.hunliji.com/o_1j4004gdbik71aj219fb1ahrqk4j.jpg",
                        "网易云",
                        "https://qnm.hunliji.com/o_1j4aiov931him1nmo1ujh2221jdqo.mp3",
                        "https://qnm.hunliji.com/o_1j4ai6c611vfmr8ql9nng1a8oe.lrc",
                        musicPlayer.getCurrentTime());
            }
        });

        // 监听播放器状态变化，同步更新按钮图标
        musicPlayer.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == MediaPlayer.Status.PLAYING) {
                // 可以设置为暂停图标
                playPauseButton.modifyContent("M512 1024C228.266667 1024 0 795.733333 0 512S228.266667 0 512 0s512 228.266667 512 512-228.266667 512-512 512z m0-42.666667c260.266667 0 469.333333-209.066667 469.333333-469.333333S772.266667 42.666667 512 42.666667 42.666667 251.733333 42.666667 512s209.066667 469.333333 469.333333 469.333333z m-106.666667-682.666666c12.8 0 21.333333 8.533333 21.333334 21.333333v384c0 12.8-8.533333 21.333333-21.333334 21.333333s-21.333333-8.533333-21.333333-21.333333V320c0-12.8 8.533333-21.333333 21.333333-21.333333z m213.333334 0c12.8 0 21.333333 8.533333 21.333333 21.333333v384c0 12.8-8.533333 21.333333-21.333333 21.333333s-21.333333-8.533333-21.333334-21.333333V320c0-12.8 8.533333-21.333333 21.333334-21.333333z");
            } else {
                // 设置为播放图标
                playPauseButton.modifyContent("M692.224 495.616 692.224 495.616 692.224 495.616c-4.096-4.096-4.096-4.096-4.096-4.096l-258.048-147.456 0 0c-4.096-4.096-8.192-4.096-12.288-4.096-12.288 0-20.48 8.192-20.48 20.48l0 303.104c0 12.288 8.192 20.48 20.48 20.48 4.096 0 8.192 0 12.288-4.096l0 0 258.048-147.456c0 0 0 0 0 0l4.096 0 0 0c4.096-4.096 8.192-8.192 8.192-16.384S696.32 499.712 692.224 495.616zM438.272 630.784 438.272 393.216l208.896 118.784L438.272 630.784zM512 98.304C282.624 98.304 98.304 282.624 98.304 512s184.32 413.696 413.696 413.696c229.376 0 413.696-184.32 413.696-413.696S741.376 98.304 512 98.304zM512 888.832c-208.896 0-376.832-167.936-376.832-376.832 0-208.896 167.936-376.832 376.832-376.832 208.896 0 376.832 167.936 376.832 376.832C888.832 720.896 720.896 888.832 512 888.832z");
            }
        });

        // 监听播放完成事件，释放资源
        musicPlayer.setOnEndOfMedia(() -> {
            // 播放完成后释放资源
            musicPlayer.dispose();

            // 重置按钮状态为播放图标
            playPauseButton.modifyContent("M692.224 495.616 692.224 495.616 692.224 495.616c-4.096-4.096-4.096-4.096-4.096-4.096l-258.048-147.456 0 0c-4.096-4.096-8.192-4.096-12.288-4.096-12.288 0-20.48 8.192-20.48 20.48l0 303.104c0 12.288 8.192 20.48 20.48 20.48 4.096 0 8.192 0 12.288-4.096l0 0 258.048-147.456c0 0 0 0 0 0l4.096 0 0 0c4.096-4.096 8.192-8.192 8.192-16.384S696.32 499.712 692.224 495.616zM438.272 630.784 438.272 393.216l208.896 118.784L438.272 630.784zM512 98.304C282.624 98.304 98.304 282.624 98.304 512s184.32 413.696 413.696 413.696c229.376 0 413.696-184.32 413.696-413.696S741.376 98.304 512 98.304zM512 888.832c-208.896 0-376.832-167.936-376.832-376.832 0-208.896 167.936-376.832 376.832-376.832 208.896 0 376.832 167.936 376.832 376.832C888.832 720.896 720.896 888.832 512 888.832z");

            // 重新加载媒体，这里重新创建 MediaPlayer
             musicPlayer = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/media/jar-of-love.mp3")).toString()));
             playerProgressBar.durationProperty().bind(musicPlayer.getMedia().durationProperty());
             musicPlayer.currentTimeProperty().addListener(durationChangeListener);
        });

        // 绑定音乐状态到 MusicState
        bindToMusicState();
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
        // 可以添加图像加载失败的处理
        //albumImageView.setImage(new Image("https://qnm.hunliji.com/o_1j3m01ogr2mm1qr61g8r6ud8ea9.jpg"));


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
        previousButton.modifyContent("M512.269015 1024a511.992548 511.992548 0 1 1 511.73106-511.73106 512.515523 512.515523 0 0 1-511.73106 511.73106z m0-971.426105a459.695045 459.695045 0 1 0 459.433557 459.695045A460.21802 460.21802 0 0 0 512.269015 52.573895zM659.526718 509.210811m-17.289056 19.617485l0 0q-17.289056 19.617486-36.906541 2.32843l-221.089062-194.847658q-19.617486-17.289056-2.32843-36.906542l0 0q17.289056-19.617486 36.906542-2.328429l221.089061 194.847658q19.617486 17.289056 2.32843 36.906541ZM625.322329 476.041569m17.289055 19.617486l0 0q17.289056 19.617486-2.328429 36.906541l-221.089062 194.847658q-19.617486 17.289056-36.906541-2.328429l0 0q-17.289056-19.617486 2.328429-36.906542l221.089062-194.847658q19.617486-17.289056 36.906541 2.32843Z");
        previousButton.setIconSize(30,30);
        previousButton.setRotate(180);
        previousButton.setFill("gray");

        // 播放/暂停按钮
        playPauseButton.modifyContent("M692.224 495.616 692.224 495.616 692.224 495.616c-4.096-4.096-4.096-4.096-4.096-4.096l-258.048-147.456 0 0c-4.096-4.096-8.192-4.096-12.288-4.096-12.288 0-20.48 8.192-20.48 20.48l0 303.104c0 12.288 8.192 20.48 20.48 20.48 4.096 0 8.192 0 12.288-4.096l0 0 258.048-147.456c0 0 0 0 0 0l4.096 0 0 0c4.096-4.096 8.192-8.192 8.192-16.384S696.32 499.712 692.224 495.616zM438.272 630.784 438.272 393.216l208.896 118.784L438.272 630.784zM512 98.304C282.624 98.304 98.304 282.624 98.304 512s184.32 413.696 413.696 413.696c229.376 0 413.696-184.32 413.696-413.696S741.376 98.304 512 98.304zM512 888.832c-208.896 0-376.832-167.936-376.832-376.832 0-208.896 167.936-376.832 376.832-376.832 208.896 0 376.832 167.936 376.832 376.832C888.832 720.896 720.896 888.832 512 888.832z");
        playPauseButton.setIconSize(30,30);
        playPauseButton.setFill("gray");

        // 下一首按钮
        nextButton.modifyContent("M512.269015 1024a511.992548 511.992548 0 1 1 511.73106-511.73106 512.515523 512.515523 0 0 1-511.73106 511.73106z m0-971.426105a459.695045 459.695045 0 1 0 459.433557 459.695045A460.21802 460.21802 0 0 0 512.269015 52.573895zM659.526718 509.210811m-17.289056 19.617485l0 0q-17.289056 19.617486-36.906541 2.32843l-221.089062-194.847658q-19.617486-17.289056-2.32843-36.906542l0 0q17.289056-19.617486 36.906542-2.328429l221.089061 194.847658q19.617486 17.289056 2.32843 36.906541ZM625.322329 476.041569m17.289055 19.617486l0 0q17.289056 19.617486-2.328429 36.906541l-221.089062 194.847658q-19.617486 17.289056-36.906541-2.328429l0 0q-17.289056-19.617486 2.328429-36.906542l221.089062-194.847658q19.617486-17.289056 36.906541 2.32843Z");
        nextButton.setIconSize(30,30);
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
        // 让进度条区域可以水平增长
        HBox.setHgrow(progressSection, Priority.ALWAYS);

        // 当前时间标签
        playerTimeLabel = new Label("00:00");
        playerTimeLabel.setFont(Font.font("Microsoft YaHei", 12));
        playerTimeLabel.setStyle("-fx-text-fill: #6c757d;");
        playerTimeLabel.setMinWidth(80);
        playerTimeLabel.setPrefWidth(80);

        // 进度滑块
        playerProgressBar.setPrefWidth(350);
        HBox.setHgrow(playerProgressBar, Priority.ALWAYS);
        playerProgressBar.setMinWidth(200);

        // 为进度条设置样式类
        playerProgressBar.setMaxHeight(18);
        playerProgressBar.setMinHeight(18);
        playerProgressBar.getStyleClass().add("rx-media-progress-bar");
        // 设置最小宽度
        // 音乐总时长
        playerProgressBar.durationProperty().bind(musicPlayer.getMedia().durationProperty());
        //播放器的进度修改监听器
        musicPlayer.currentTimeProperty().addListener(durationChangeListener);

        progressSection.getChildren().addAll(playerProgressBar, playerTimeLabel);
    }

    /**
     * 初始化其他操作按钮部分
     */
    private void initExtraControlsSection() {
        extraControlsSection.setAlignment(Pos.CENTER);
        extraControlsSection.setPadding(new Insets(0, 15, 0, 15));
        extraControlsSection.setSpacing(15);
        // 设置最小宽度以确保所有按钮都能显示
        extraControlsSection.setMinWidth(200);
        HBox.setHgrow(extraControlsSection, Priority.NEVER);

        // 音量调节按钮 初始化声音控制
        volumeButton.modifyContent("M580.096 907.264c-19.968 0-39.424-6.144-56.32-17.92l-215.04-157.696c-11.264-8.192-25.6-12.288-40.448-12.288H194.048c-53.76 0-97.792-44.032-97.792-97.792V402.944c0-53.76 44.032-97.792 97.792-97.792h74.24c14.848 0 29.184-4.608 40.96-12.8l214.016-156.672 0.512-0.512c35.84-25.088 82.432-23.552 116.736 3.072 23.552 18.432 37.376 48.128 37.376 79.36v589.312c0 28.16-10.752 54.272-29.696 72.704-18.432 17.92-42.496 27.648-68.096 27.648zM194.048 375.808c-14.848 0-27.136 12.288-27.136 27.136v218.624c0 14.848 12.288 27.136 27.136 27.136h74.24c29.184 0 57.856 8.704 81.92 25.6l215.552 157.696c4.096 2.56 9.216 4.608 14.848 4.608 5.12 0 12.288-1.536 19.456-8.192 5.12-4.608 7.68-12.8 7.68-20.992V217.6c0-10.24-3.584-18.944-10.24-23.552-9.728-7.68-22.528-8.192-32.256-1.024L350.72 349.696c-24.576 17.408-52.736 26.112-82.432 26.112H194.048zM837.632 694.784c-6.656 0-13.312-2.048-18.944-5.632-16.384-10.752-21.504-32.256-10.752-49.152 66.048-103.424 66.048-193.536 0-283.136-11.776-15.872-8.192-37.888 7.168-49.664s37.888-8.192 49.664 7.168c83.456 113.152 84.48 235.008 3.072 363.008-7.168 11.776-18.432 17.408-30.208 17.408z");
        volumeButton.setStyle("-fx-background-color: gray; -fx-cursor: hand;");
        volumeButton.setIconSize(20,20);
        volumeButton.setOnMouseClicked(event -> {
            if (soundPopup != null) {
                Bounds bounds = volumeButton.localToScreen(volumeButton.getBoundsInLocal());
                Stage mainStage = (Stage) this.getScene().getWindow();
                if (mainStage != null) {
                    soundPopup.show(mainStage, bounds.getMinX() - 20, bounds.getMinY() - 170);
                }
            }
        });

        // 我喜欢按钮
        likedButton.modifyContent("M959.317333 294.4a277.077333 277.077333 0 0 0-60.202666-89.514667C849.237333 155.306667 783.786667 128 714.88 128c-78.592 0-152.192 35.285333-202.965333 97.152C461.141333 163.285333 387.541333 128 308.949333 128c-68.821333 0-134.272 27.52-184.32 77.44A276.906667 276.906667 0 0 0 64.298667 295.253333 278.229333 278.229333 0 0 0 42.666667 405.802667c0.64 82.986667 36.778667 160.341333 99.114666 212.437333 2.816 2.346667 5.504 4.608 8.106667 7.04 13.696 12.117333 53.205333 47.786667 103.253333 92.928a33643.648 33643.648 0 0 0 236.032 211.882667 34.474667 34.474667 0 0 0 45.653334 0c31.616-27.776 96.981333-86.784 166.272-149.290667 68.010667-61.312 138.325333-124.842667 179.498666-161.194667C944.298667 567.210667 981.077333 488.96 981.333333 404.906667a282.154667 282.154667 0 0 0-22.016-110.506667zM563.072 273.066667c37.504-48.128 92.757333-75.733333 151.808-75.733334 50.218667 0 97.962667 20.096 134.528 56.448 19.2 19.029333 34.389333 41.557333 45.098667 67.029334a215.552 215.552 0 0 1-4.181334 175.146666 210.773333 210.773333 0 0 1-54.741333 70.144l-1.664 1.450667a26333.866667 26333.866667 0 0 0-181.034667 162.645333A37231.445333 37231.445333 0 0 1 512 857.045333a54667.946667 54667.946667 0 0 1-185.429333-166.613333l-43.349334-39.082667a15223.466667 15223.466667 0 0 0-86.357333-77.696l-2.218667-2.005333c-2.346667-2.090667-4.864-4.266667-7.509333-6.4A209.706667 209.706667 0 0 1 112.896 405.333333a212.224 212.224 0 0 1 16.298667-83.413333A208.085333 208.085333 0 0 1 174.506667 254.464c36.821333-36.693333 84.693333-56.874667 134.570666-56.874667 58.922667 0 114.304 27.477333 151.765334 75.52a65.28 65.28 0 0 0 51.2 24.746667 64.853333 64.853333 0 0 0 51.029333-24.746667z");
        likedButton.setIconSize(20,20);
        likedButton.setStyle("-fx-background-color: gray; -fx-cursor: hand;");

        // 音量调节Context初始化
        initSoundPopup();

        // 添加到列表按钮
        add2ListButton.setStyle("-fx-background-color: gray; -fx-cursor: hand;");
        add2ListButton.modifyContent("M450.56 860.16H122.88a40.96 40.96 0 0 1-40.96-40.96V122.88a40.96 40.96 0 0 1 40.96-40.96h655.36a40.96 40.96 0 0 1 40.96 40.96v286.72a40.96 40.96 0 0 0 81.92 0V122.88a122.88 122.88 0 0 0-122.88-122.88H122.88A122.88 122.88 0 0 0 0 122.88v696.32a122.88 122.88 0 0 0 122.88 122.88h327.68a40.96 40.96 0 0 0 0-81.92z",
                "M245.76 204.8a40.96 40.96 0 0 0 0 81.92h409.6a40.96 40.96 0 0 0 0-81.92zM573.44 450.56a40.96 40.96 0 0 0-40.96-40.96H245.76a40.96 40.96 0 0 0 0 81.92h286.72a40.96 40.96 0 0 0 40.96-40.96zM430.08 696.32a40.96 40.96 0 0 0 0-81.92H245.76a40.96 40.96 0 0 0 0 81.92zM942.08 706.56h-133.12v-133.12a40.96 40.96 0 0 0-81.92 0v133.12h-133.12a40.96 40.96 0 0 0 0 81.92h133.12v133.12a40.96 40.96 0 0 0 81.92 0v-133.12h133.12a40.96 40.96 0 0 0 0-81.92z");
        add2ListButton.setIconSize(20,20);

        // 分享按钮
        lyricsButton.setStyle("-fx-background-color: gray; -fx-cursor: hand;");
        lyricsButton.modifyContent("M1006.345 572.028V112.993s0-35.31-21.186-67.09C971.034 21.186 942.786 0 889.82 0H134.179S17.655 0 17.655 116.524v773.297s0 35.31 21.186 67.09c17.656 24.717 45.904 45.903 95.338 45.903h755.642s116.524 0 116.524-116.524V572.028zM949.848 889.82c-3.53 56.496-56.496 56.496-56.496 56.496H127.117c-7.062 0-24.717-7.062-35.31-17.655-14.124-14.124-17.655-31.78-17.655-38.841V116.524c3.53-56.496 56.496-56.496 56.496-56.496h766.235c7.062 0 24.717 7.062 35.31 17.655 14.124 14.124 17.655 31.78 17.655 38.841v773.297z",
                "M236.58 451.972c3.53-14.124 3.53-24.717 3.53-31.779 0-7.062 0-17.655-3.53-31.78 17.654 3.532 38.84 3.532 52.965 3.532h45.903c17.655 0 31.78 0 45.904-3.531-3.531 24.717-7.062 49.434-7.062 70.62V677.96c17.655-21.187 35.31-38.842 52.965-56.497 7.062 21.186 14.124 42.372 21.186 52.966-38.841 31.779-77.682 70.62-120.055 120.055-10.593-17.655-21.186-31.78-31.78-49.435 17.656-17.655 28.25-35.31 28.25-52.965V448.44h-35.311c-21.186 0-35.31 0-52.966 3.531z m49.434-211.862c17.655-7.062 35.31-14.124 49.434-24.717 10.593 31.78 31.78 63.559 60.028 95.338-17.655 7.062-35.31 14.124-49.435 24.717-24.717-45.903-45.903-77.682-60.027-95.338z m141.241 180.083c3.531-14.124 3.531-24.717 3.531-31.78s0-17.654-3.53-31.779c28.247 3.532 45.903 3.532 52.965 3.532h155.365c10.593 0 28.248 0 49.435-3.532-3.531 14.125-3.531 24.718-3.531 31.78 0 7.062 0 17.655 3.53 31.78-14.123-3.532-28.248-3.532-45.903-3.532H480.221c-17.655 0-35.31 0-52.966 3.531z m21.186-127.117c0-14.124 3.531-24.717 3.531-31.78 0-7.062 0-17.655-3.53-31.779 21.186 3.531 38.84 3.531 49.434 3.531h233.048c10.593 0 28.248 0 52.966-3.53-3.531 17.654-3.531 35.31-3.531 52.965v459.034c0 21.186-7.062 35.31-21.187 42.373s-42.372 17.655-81.213 21.186c-3.531-24.717-14.125-45.904-31.78-67.09 17.655 3.531 31.78 3.531 42.373 3.531 14.124 0 24.717 0 28.248-3.53 3.531-3.532 7.062-10.594 7.062-21.187V289.545H501.407c-14.124 0-31.78 3.53-52.966 3.53z m7.062 459.034c3.531-17.655 3.531-35.31 3.531-49.434V519.062c0-14.124 0-24.717-3.53-42.372 17.655 3.53 35.31 3.53 49.434 3.53h98.869c17.655 0 35.31 0 52.965-3.53-3.53 14.124-3.53 28.248-3.53 45.903V677.96c0 14.124 0 28.248 3.53 49.434h-52.965V681.49h-98.87v31.78c0 10.593 0 24.717 3.532 38.841-10.593 0-21.186-3.53-24.717-3.53-3.531 0-14.124 3.53-28.249 3.53z m148.304-222.455h-98.87v112.993h98.87V529.655z");
        lyricsButton.setIconSize(20,20);

        // 分享按钮
        shareButton.setStyle("-fx-background-color: gray; -fx-cursor: hand;");
        shareButton.modifyContent("M947.2 0l-185.6 0c-12.8 0-25.6 12.8-25.6 25.6s12.8 25.6 25.6 25.6l172.8 0L492.8 492.8c-12.8 12.8-12.8 25.6 0 38.4C499.2 537.6 505.6 537.6 512 537.6s12.8 0 19.2-6.4l441.6-441.6 0 185.6c0 12.8 12.8 25.6 25.6 25.6 12.8 0 25.6-12.8 25.6-25.6L1024 76.8C1024 32 992 0 947.2 0z",
                "M998.4 480c-12.8 0-25.6 12.8-25.6 25.6L972.8 896c0 44.8-32 76.8-76.8 76.8L128 972.8c-44.8 0-76.8-32-76.8-76.8L51.2 128c0-44.8 32-76.8 76.8-76.8l384 0c12.8 0 25.6-12.8 25.6-25.6S524.8 0 512 0L128 0C57.6 0 0 57.6 0 128l0 768c0 70.4 57.6 128 128 128l768 0c70.4 0 128-57.6 128-128L1024 505.6C1024 492.8 1011.2 480 998.4 480z");
        shareButton.setIconSize(20,20);

        extraControlsSection.getChildren().addAll(likedButton, lyricsButton, volumeButton, add2ListButton, shareButton);
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
     * 进度条的拖动 或者 点击 进行处理
     * @title: initProgressBar 初始化进度条方法
     * @description: void 不需要返回任何数据
     */
    private void initProgressBar() {
        EventHandler<MouseEvent> progressBarHandler = event -> {
            if (musicPlayer != null && playerProgressBar != null) {
                musicPlayer.seek(playerProgressBar.getCurrentTime());
                changeTimeLabel(playerProgressBar.getCurrentTime());
            }
        };
        if (playerProgressBar != null) {
            playerProgressBar.setOnMouseClicked(progressBarHandler);
            playerProgressBar.setOnMouseDragged(progressBarHandler);
        }
    }

    /**
     * 当播放时间改变的时候. 修改时间的显示
     * 将音乐播放时间进行动态刷新显示
     */
    private void changeTimeLabel(Duration duration) {
        if (musicPlayer != null && duration != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String currentTime = sdf.format(duration.toMillis());
            String bufferedTimer = sdf.format(musicPlayer.getBufferProgressTime().toMillis());
            playerTimeLabel.setText(currentTime + " / " + bufferedTimer);
        }
    }

    /**
     * 播放进度发生改变的时候..修改进度条的播放进度, 修改时间的显示
     */
    private final ChangeListener<Duration> durationChangeListener = (ob1, ov1, nv1) -> {
        playerProgressBar.setCurrentTime(nv1);
        changeTimeLabel(nv1);
        // 添加这一行将当前播放时间同步到MusicState
        if (musicState != null) {
            musicState.setCurrentTimeProperty(nv1);
        }
    };

    /**
     * 释放媒体播放器资源
     */
    public void disposeMediaPlayer() {
        if (musicPlayer != null) {
            // 停止播放
            if (musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                musicPlayer.stop();
            }
            // 释放资源
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    /**
     * 初始化音量弹出菜单
     * 使用纯JavaFX代码替代FXML实现
     */
    private void initSoundPopup() {
        // 创建音量滑块
        // 范围0-100，默认值50
        soundSlider = new Slider(0, 100, 50);
        soundSlider.setOrientation(javafx.geometry.Orientation.VERTICAL);
        soundSlider.setPrefHeight(114);
        soundSlider.setPrefWidth(10);
        // 使用默认主题配色
        soundSlider.setSkin(new RedVerticalSliderSkin(soundSlider));

        // 创建音量数值标签
        Label soundNumLabel = new Label("50%");
        soundNumLabel.setPrefHeight(15);
        soundNumLabel.setPrefWidth(47);
        soundNumLabel.setAlignment(javafx.geometry.Pos.CENTER);

        // 绑定标签文本与滑块值
        soundNumLabel.textProperty().bind(soundSlider.valueProperty().asString("%.0f%%"));

        // 创建分隔线
        Line separatorLine = new Line();
        separatorLine.setStartX(0);
        // 大约的宽度
        separatorLine.setEndX(40);
        separatorLine.setStartY(0);
        separatorLine.setEndY(0);
        separatorLine.setStroke(Color.GRAY);

        // 创建包含滑块和标签的容器
        // 5px间距
        VBox soundControlBox = new VBox(5);
        soundControlBox.setStyle("-fx-background-color: white; -fx-border-radius: 5px; -fx-background-radius: 5px");
        soundControlBox.setAlignment(javafx.geometry.Pos.CENTER);
        soundControlBox.getChildren().addAll(soundSlider, separatorLine, soundNumLabel);

        // 创建上下文菜单
        soundPopup = new ContextMenu();
        soundPopup.getScene().setRoot(soundControlBox);

        // 监听滑块值变化，同步调整播放器音量
        soundSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (musicPlayer != null) {
                musicPlayer.setVolume(newVal.doubleValue() / 100.0);
            }
        });


    }
    /**
     * 设置专辑封面点击事件处理器
     * @param handler 点击事件处理器
     */
    public void setAlbumImageClickHandler(EventHandler<MouseEvent> handler) {
        albumImageView.setOnMouseClicked(handler);
    }

    private void bindToMusicState() {
        if (musicState == null) {
            return;
        }

        // 监听 MediaPlayer 状态变化并同步到 MusicState
        musicPlayer.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus == MediaPlayer.Status.PLAYING) {
                musicState.setPlayerStatusProperty(MediaPlayer.Status.PLAYING);
            } else if (newStatus == MediaPlayer.Status.PAUSED) {
                musicState.setPlayerStatusProperty(MediaPlayer.Status.PAUSED);
            }else if (newStatus == MediaPlayer.Status.STOPPED) {
                musicState.setPlayerStatusProperty(MediaPlayer.Status.STOPPED);
            }
        });

        // 监听 MusicState 的播放状态变化并控制 MediaPlayer
        musicState.playerStatusPropertyProperty().addListener((obs, oldVal, newVal) -> {
            if (Objects.equals(newVal, MediaPlayer.Status.PLAYING) && Objects.equals(musicPlayer.getStatus(), MediaPlayer.Status.PLAYING)) {
                musicPlayer.play();
            } else if (Objects.equals(newVal, MediaPlayer.Status.PAUSED) && Objects.equals(musicPlayer.getStatus(), MediaPlayer.Status.PAUSED)) {
                musicPlayer.pause();
            }
        });
    }


}