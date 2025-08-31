package com.stickpoint.ddmusic.page.node;

import com.leewyatt.rxcontrols.controls.RXLrcView;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * 音乐播放详情容器
 * @author fntp
 * @date 2025/8/31
 */
public class MusicPlayDetailContainer extends VBox {

    private RotateTransition rotateTransition;
    private StackPane recordPane;
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

    public MusicPlayDetailContainer() {
        initialize();
        setupLayout();
    }

    private void initialize() {
        // 初始化唱片区域
        recordPane = createRecord();

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
        setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #3498db);");

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
        leftPane.getChildren().add(recordPane);

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
        if (rotateTransition != null) {
            rotateTransition.play();
        }
    }

    public void pauseRotation() {
        if (rotateTransition != null) {
            rotateTransition.pause();
        }
    }

    public void stopRotation() {
        if (rotateTransition != null) {
            rotateTransition.stop();
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
}