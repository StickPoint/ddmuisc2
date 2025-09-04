package com.stickpoint.ddmusic.page.node;

import com.leewyatt.rxcontrols.controls.RXLrcView;
import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Objects;

/**
 * 音乐播放详情容器
 * @author fntp
 * @date 2025/8/31
 */
public class MusicPlayDetailContainer extends VBox {

    private RotateTransition rotateTransition;
    private RotateTransition needleTransition;
    private StackPane recordPane;
    private ImageView needleImageView;
    private Label backButton;
    // 歌曲信息组件
    private Label songTitle;
    private Label songTags;
    private Label albumInfo;
    private Label artistInfo;
    private Label sourceInfo;
    private HBox infoBar;
    private RXLrcView lrcView;
    // 主内容区域
    private HBox mainContent;
    private ChangeListener<Boolean> playingStateListener;
    private MusicState musicState;

    public MusicPlayDetailContainer(MusicState musicState) {
        this.musicState = musicState;
        initialize();
        setupLayout();
        setSharedStateModel(musicState);
    }

    private void initialize() {
        // 初始化唱片区域
        recordPane = createRecord();
        
        // 初始化唱针
        needleImageView = createNeedle();

        // 初始化歌曲信息
        songTitle = new Label("My My My!");
        songTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        songTitle.setStyle("-fx-text-fill: white;");

        songTags = new Label("VIP MV");
        songTags.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 12px; -fx-padding: 2 6 2 6; -fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 4;");

        albumInfo = new Label("专辑: Bloom");
        albumInfo.setStyle("-fx-text-fill: #cccccc;");
        artistInfo = new Label("歌手: Troye Sivan");
        artistInfo.setStyle("-fx-text-fill: #cccccc;");
        sourceInfo = new Label("来源: Wild");
        sourceInfo.setStyle("-fx-text-fill: #cccccc;");

        // 操作按钮
        infoBar = new HBox(10);
        infoBar.setAlignment(Pos.CENTER_LEFT);
        infoBar.getChildren().addAll(
                createButton("歌词"),
                createButton("百科"),
                createButton("相似推荐")
        );

        // 歌词组件
        lrcView = new RXLrcView();
        lrcView.setPrefHeight(200);
        //lrcView.setLrcText("Oh my, my, my\n哦 我的挚爱\nLiving for your every move\n你的举手投足都让我沉迷...");

        // 添加返回按钮
        backButton = new Label("← 返回");
        backButton.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand; -fx-padding: 10;");
        backButton.setOnMouseClicked(e -> {
            // 这里需要获取到主容器来恢复显示其他内容
            // 实现方式取决于你的具体布局结构
        });
    }

    private void setupLayout() {
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: gray");

        // 返回按钮靠左对齐
        StackPane topPane = new StackPane();
        topPane.getChildren().add(backButton);
        StackPane.setAlignment(backButton, Pos.CENTER_LEFT);

        // 创建左右布局
        mainContent = new HBox(50);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPrefWidth(900); // 设置首选宽度

        // 左侧唱片区域
        VBox leftPane = new VBox();
        leftPane.setAlignment(Pos.CENTER);
        
        // 创建唱片和唱针的容器
        StackPane recordWithNeedle = new StackPane();
        recordWithNeedle.getChildren().addAll(recordPane, needleImageView);
        
        leftPane.getChildren().add(recordWithNeedle);

        // 右侧信息区域
        VBox rightPane = new VBox();
        rightPane.setSpacing(15);
        rightPane.setAlignment(Pos.CENTER_LEFT);
        rightPane.setPrefWidth(400);

        // 歌曲信息部分
        VBox songInfoPane = new VBox(8);
        songInfoPane.setAlignment(Pos.CENTER_LEFT);
        songInfoPane.getChildren().addAll(
                songTitle,
                songTags,
                albumInfo,
                artistInfo,
                sourceInfo,
                infoBar
        );

        // 歌词区域
        VBox lrcContainer = new VBox();
        lrcContainer.setAlignment(Pos.CENTER);
        lrcContainer.getChildren().add(lrcView);
        lrcContainer.setPrefHeight(200);

        rightPane.getChildren().addAll(songInfoPane, lrcContainer);
        mainContent.getChildren().addAll(leftPane, rightPane);

        // 添加到主容器
        getChildren().addAll(topPane, mainContent);

        // 绑定宽度自适应
        bindWidthProperties();
    }

    /**
     * 绑定宽度自适应属性
     */
    private void bindWidthProperties() {
        // 主内容区域水平居中
        mainContent.prefWidthProperty().bind(widthProperty().multiply(0.9));
        mainContent.maxWidthProperty().bind(widthProperty().multiply(0.9));

        // 唱片大小随窗口大小自适应
        recordPane.prefWidthProperty().bind(mainContent.widthProperty().multiply(0.4));
        recordPane.prefHeightProperty().bind(recordPane.prefWidthProperty());
    }

    // 添加设置返回按钮事件处理器的方法
    public void setOnReturnHandler(EventHandler<MouseEvent> handler) {
        if (backButton != null) {
            backButton.setOnMouseClicked(handler);
        }
    }

    private StackPane createRecord() {
        StackPane record = new StackPane();
        record.setPrefSize(380, 380);

        try {
            // 加载黑胶唱片素材
            Image vinylImage = new Image("https://qnm.hunliji.com/o_1j3vvv7781s3jvbe13pa1vu81o7le.png");
            ImageView vinylView = new ImageView(vinylImage);
            vinylView.setFitWidth(380);
            vinylView.setFitHeight(380);
            vinylView.setPreserveRatio(true);

            // 加载专辑图片
            Image albumImage = new Image("https://qnm.hunliji.com/o_1j4004gdbik71aj219fb1ahrqk4j.jpg");
            ImageView albumView = new ImageView(albumImage);
            albumView.setFitWidth(167);
            albumView.setFitHeight(167);
            albumView.setPreserveRatio(true);

            // 创建圆形裁剪区域
            Circle clip = new Circle(83.5);
            clip.setCenterX(83.5);
            clip.setCenterY(83.5);
            albumView.setClip(clip);

            // 创建白色边框
            Circle albumBorder = new Circle(83.5);
            albumBorder.setFill(Color.TRANSPARENT);
            albumBorder.setStroke(Color.WHITE);
            albumBorder.setStrokeWidth(2);

            // 创建专辑容器
            StackPane albumContainer = new StackPane();
            albumContainer.getChildren().addAll(albumView, albumBorder);
            albumContainer.setMaxSize(167, 167);

            // 将专辑图片放置在唱片中心
            StackPane.setAlignment(albumContainer, Pos.CENTER);

            // 只添加黑胶唱片和专辑容器
            record.getChildren().addAll(vinylView, albumContainer);

        } catch (Exception e) {
            // 如果图片加载失败，使用默认占位符
            System.err.println("图片加载失败: " + e.getMessage());

            Circle placeholder = new Circle(190);
            placeholder.setFill(Color.rgb(50, 50, 50));

            Label placeholderText = new Label("图片加载失败");
            placeholderText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            placeholderText.setTextFill(Color.LIGHTGRAY);

            StackPane placeholderContainer = new StackPane();
            placeholderContainer.getChildren().addAll(placeholder, placeholderText);

            record.getChildren().add(placeholderContainer);
        }

        // 创建旋转动画
        rotateTransition = new RotateTransition(Duration.seconds(5), record);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setRate(1.0);

        return record;
    }

    /**
     * 创建唱针
     */
    private ImageView createNeedle() {
        try {
            // 从本地资源加载唱针图片
            Image needleImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/cz.png")));
            ImageView needle = new ImageView(needleImage);
            
            // 设置唱针的尺寸
            needle.setFitWidth(100);
            needle.setFitHeight(500);
            needle.setPreserveRatio(true);
            
            // 设置唱针的初始位置（抬起状态）
            needle.setRotate(-30); // 初始抬起 30 度
            
            // 设置唱针的旋转中心点（左上角）
            needle.setTranslateX(100); // 向右偏移
            needle.setTranslateY(-50); // 向上偏移
            
            // 创建唱针的旋转动画
            needleTransition = new RotateTransition(Duration.millis(800), needle);
            needleTransition.setInterpolator(Interpolator.EASE_BOTH);
            
            return needle;
            
        } catch (Exception e) {
            System.err.println("唱针图片加载失败: " + e.getMessage());
            
            // 如果图片加载失败，创建一个简单的占位符
            ImageView placeholder = new ImageView();
            placeholder.setFitWidth(150);
            placeholder.setFitHeight(150);
            placeholder.setStyle("-fx-background-color: #666666;");
            return placeholder;
        }
    }

    private Label createButton(String text) {
        Label btn = new Label(text);
        btn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-padding: 8px 16px; -fx-text-fill: white; -fx-background-radius: 15px; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-padding: 8px 16px; -fx-text-fill: white; -fx-background-radius: 15px; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-padding: 8px 16px; -fx-text-fill: white; -fx-background-radius: 15px; -fx-cursor: hand;"));
        btn.setOnMouseClicked(e -> System.out.println("Clicked: " + text));
        return btn;
    }

    // 控制唱片旋转的方法
    public void startRotation() {
        if (needleTransition != null) {
            // 唱针放下（从 -30 度旋转到 -5 度）
            needleTransition.setFromAngle(-30);
            needleTransition.setToAngle(-5);
            needleTransition.setOnFinished(e -> {
                // 唱针放下后开始唱片旋转
                if (rotateTransition != null) {
                    rotateTransition.play();
                }
            });
            needleTransition.play();
        } else if (rotateTransition != null) {
            // 如果没有唱针，直接开始唱片旋转
            rotateTransition.play();
        }
    }

    public void pauseRotation() {
        if (rotateTransition != null) {
            rotateTransition.pause();
        }
        // 暂停时不抬起唱针，保持在唱片上
    }

    public void stopRotation() {
        if (rotateTransition != null) {
            rotateTransition.stop();
        }
        if (needleTransition != null) {
            // 停止时抬起唱针
            needleTransition.setFromAngle(needleImageView.getRotate());
            needleTransition.setToAngle(-30);
            needleTransition.setOnFinished(null); // 清除之前的事件处理
            needleTransition.play();
        }
    }

    // Setter 方法用于外部设置数据
    public void setSongTitle(String title) {
        songTitle.setText(title);
    }

    public void setAlbumInfo(String album) {
        albumInfo.setText("专辑: " + album);
    }

    public void setArtistInfo(String artist) {
        artistInfo.setText("歌手: " + artist);
    }

    public void setSourceInfo(String source) {
        sourceInfo.setText("来源: " + source);
    }

    public void setLrcText(String lrc) {
        //lrcView.setLrcText(lrc);
    }

    // 更新专辑封面的方法
    public void updateAlbumCover(String imageUrl) {
        // 这里需要重新创建专辑封面组件并替换原有的
        // 具体实现可以根据需要添加
    }

    private void setSharedStateModel(MusicState musicState) {
        this.musicState = musicState;
        // 创建监听器
        playingStateListener = (obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal) {
                    startRotation();
                } else {
                    pauseRotation();
                }
            });
        };
        // 注册弱监听器
        musicState.addWeakPlayingListener(playingStateListener);
        // 初始化唱片状态
        if (Objects.equals(musicState.getPlayerStatusProperty(), MediaPlayer.Status.PLAYING)) {
            startRotation();
        } else {
            pauseRotation();
        }
    }
}