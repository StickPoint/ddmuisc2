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

    public MusicPlayDetailContainer() {
        initialize();
        setupLayout(); // 修改方法名
    }

    private void initialize() {
        // 初始化唱片区域
        recordPane = createRecord();

        // 初始化歌曲信息
        songTitle = new Label("My My My!");
        songTitle.setFont(Font.font("Arial", 24));
        songTitle.setStyle("-fx-text-fill: white;");

        songTags = new Label("VIP MV");
        songTags.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 12px;");

        albumInfo = new Label("专辑: Bloom");
        artistInfo = new Label("歌手: Troye Sivan");
        sourceInfo = new Label("来源: Wild");

        // 操作按钮
        infoBar = new HBox();
        infoBar.setSpacing(10);
        infoBar.getChildren().addAll(
                createButton("歌词"),
                createButton("百科"),
                createButton("相似推荐")
        );

        // 歌词组件
        lrcView = new RXLrcView();
        //lrcView.setLrcText("Oh my, my, my\n哦 我的挚爱\nLiving for your every move\n你的举手投足都让我沉迷...");

        // 添加返回按钮
        backButton = new Label("← 返回");
        backButton.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        backButton.setOnMouseClicked(e -> {
            // 这里需要获取到主容器来恢复显示其他内容
            // 实现方式取决于你的具体布局结构
        });
    }

    private void setupLayout() { // 修改方法名
        setSpacing(20);

        // 添加返回按钮
        getChildren().add(backButton);

        // 创建左右布局
        HBox mainContent = new HBox();
        mainContent.setSpacing(50);
        mainContent.setAlignment(Pos.CENTER);

        // 左侧唱片区域
        HBox leftPane = new HBox();
        leftPane.setAlignment(Pos.CENTER);
        leftPane.getChildren().add(recordPane);

        // 右侧信息区域
        VBox rightPane = new VBox();
        rightPane.setSpacing(8);
        rightPane.getChildren().addAll(
                songTitle,
                songTags,
                albumInfo,
                artistInfo,
                sourceInfo,
                infoBar
        );

        mainContent.getChildren().addAll(leftPane, rightPane);

        // 添加到主容器
        getChildren().addAll(mainContent, lrcView);
    }


    // 添加设置返回按钮事件处理器的方法
    public void setOnReturnHandler(EventHandler<MouseEvent> handler) {
        if (backButton != null) {
            backButton.setOnMouseClicked(handler);
        }
    }

    private StackPane createRecord() {
        StackPane record = new StackPane();
        record.setPrefSize(495, 495);

        try {
            // 加载黑胶唱片素材
            Image vinylImage = new Image("https://qnm.hunliji.com/o_1j3vvv7781s3jvbe13pa1vu81o7le.png");
            ImageView vinylView = new ImageView(vinylImage);
            vinylView.setFitWidth(495);
            vinylView.setFitHeight(495);
            vinylView.setPreserveRatio(true);

            // 加载专辑图片
            Image albumImage = new Image("https://qnm.hunliji.com/o_1j4004gdbik71aj219fb1ahrqk4j.jpg");
            ImageView albumView = new ImageView(albumImage);
            albumView.setFitWidth(222);
            albumView.setFitHeight(222);
            albumView.setPreserveRatio(true);

            // 创建圆形裁剪区域
            Circle clip = new Circle(111);
            clip.setCenterX(111);
            clip.setCenterY(111);
            albumView.setClip(clip);

            // 创建白色边框
            Circle albumBorder = new Circle(111);
            albumBorder.setFill(Color.TRANSPARENT);
            albumBorder.setStroke(Color.WHITE);
            albumBorder.setStrokeWidth(2);

            // 创建专辑容器
            StackPane albumContainer = new StackPane();
            albumContainer.getChildren().addAll(albumView, albumBorder);
            albumContainer.setMaxSize(222, 222);

            // 将专辑图片放置在唱片中心
            StackPane.setAlignment(albumContainer, Pos.CENTER);

            // 移除了中心黑点的创建和添加
            // Circle centerHole = new Circle(5);
            // centerHole.setFill(Color.BLACK);

            // 只添加黑胶唱片和专辑容器，不再添加中心黑点
            record.getChildren().addAll(vinylView, albumContainer);

        } catch (Exception e) {
            // 如果图片加载失败，使用默认占位符
            System.err.println("图片加载失败: " + e.getMessage());

            Circle placeholder = new Circle(247.5);
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
        btn.setStyle("-fx-background-color: #333; -fx-padding: 4px 8px; -fx-text-fill: white; -fx-border-radius: 4px;");
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
