package com.stickpoint.ddmusic.page.node;

import com.leewyatt.rxcontrols.controls.RXLrcView;
import com.leewyatt.rxcontrols.pojo.LrcDoc;
import com.stickpoint.ddmusic.common.utils.EncodingDetectUtil;
import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 音乐播放详情容器
 * @author fntp
 * @date 2025/8/31
 */
public class MusicPlayDetailContainer extends VBox {

    private StackPane recordPane;
    private ImageView needleImageView;
    private SvgIcon backButton;
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
    private ChangeListener<MediaPlayer.Status> playingStateListener;
    private MusicState musicState;
    private ChangeListener<String> lrcUrlListener;
    /**
     * 构建日志工具
     */
    private static final Logger log = LoggerFactory.getLogger(MusicPlayDetailContainer.class);

    public MusicPlayDetailContainer(MusicState paramState) {
        musicState = paramState;
        initialize();
        setupLayout();
        setSharedStateModel(musicState);
    }

    private void initialize() {
        // 初始化的时候加载css文件
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/MusicPlayDetailContainer.css")).toExternalForm());

        // 初始化唱片区域
        recordPane = createRecord();
        
        // 初始化唱针
        needleImageView = createNeedle();

        // 初始化歌曲信息
        songTitle = new Label("歌曲名称");
        songTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        songTitle.setStyle("-fx-text-fill: #141313;");

        songTags = new Label("VIP MV");
        songTags.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-padding: 2 6 2 6; -fx-background-color: rgba(0,0,0,0.48); -fx-background-radius: 4; -fx-font-weight: bold;");

        albumInfo = new Label("专辑: Bloom");
        albumInfo.setStyle("-fx-text-fill: #262626;");
        artistInfo = new Label("歌手: Troye Sivan");
        artistInfo.setStyle("-fx-text-fill: #262626;");
        sourceInfo = new Label("来源: Wild");
        sourceInfo.setStyle("-fx-text-fill: #262626;");

        // 操作按钮
        infoBar = new HBox(10);
        infoBar.setAlignment(Pos.CENTER_LEFT);

        // 歌词组件
        lrcView = new RXLrcView();
        lrcView.setPrefHeight(350);
        lrcView.getStyleClass().add("rx-lrc-view");

        // 添加返回按钮
        backButton = new SvgIcon("M333.436236 512.002048l363.098222-362.900598c18.226434-18.226434 18.226434-47.770666 0-65.998124s-47.770666-18.226434-65.998124 0L234.422666 479.000938c-18.226434 18.226434-18.226434 47.770666 0 65.998124l396.112643 395.942666c18.227458 18.18138 47.77169 18.18138 65.998124 0 18.226434-18.227458 18.226434-47.77169 0-65.998124L333.436236 512.002048z");
        backButton.setFill("#030303");
        backButton.setIconSize(15,22);
    }

    private void setupLayout() {
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: rgba(43,40,40,0.02)");

        // 返回按钮靠左对齐
        Pane topPane = new Pane();
        backButton.setLayoutX(30);
        backButton.setLayoutY(35);
        topPane.getChildren().add(backButton);

        // 创建左右布局
        mainContent = new HBox(50);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPrefWidth(900);

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
            ImageView albumView = new ImageView();
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

            // 绑定专辑封面属性
            musicState.albumCoverPropertyProperty().addListener((obs, oldVal, newVal) -> {
                Platform.runLater(() -> {
                    if (newVal != null && !newVal.isEmpty()) {
                        try {
                            Image newAlbumImage = new Image(newVal);
                            albumView.setImage(newAlbumImage);
                        } catch (Exception e) {
                            System.err.println("专辑封面加载失败: " + e.getMessage());
                        }
                    }
                });
            });
            // 初始化专辑封面
            if (musicState.getAlbumCoverProperty() != null && !musicState.getAlbumCoverProperty().isEmpty()) {
                try {
                    Image initialAlbumImage = new Image(musicState.getAlbumCoverProperty());
                    albumView.setImage(initialAlbumImage);
                } catch (Exception e) {
                    System.err.println("初始专辑封面加载失败: " + e.getMessage());
                }
            }

            record.setTranslateX(-25);
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
            
            // 设置唱针的初始位置（抬起状态） 初始抬起 30 度
            needle.setRotate(-95);

            // 设置唱针的旋转中心点（左上角） 向右偏移
            needle.setTranslateX(175);
            // 向上偏移
            needle.setTranslateY(-250);

            
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

    // 更新专辑封面的方法
    public void updateAlbumCover(String imageUrl) {
        // 这里需要重新创建专辑封面组件并替换原有的
        // 具体实现可以根据需要添加
    }

    private void setSharedStateModel(MusicState paramState) {
        musicState = paramState;
        // 创建监听器
        RotateTransition recordRotateTransition = new RotateTransition();
        recordRotateTransition.setNode(recordPane);
        // 25秒一圈
        recordRotateTransition.setDuration(Duration.seconds(25));
        // 旋转360度
        recordRotateTransition.setByAngle(360);
        // 无限循环
        recordRotateTransition.setCycleCount(Animation.INDEFINITE);
        // 取消自动倒转
        recordRotateTransition.setAutoReverse(false);

        // 创建一个 Rotate 变换，指定旋转中心点
        // 初始角度 -30°，pivot=(20,20)
        Rotate needleRotate = new Rotate(60, 45, 45);
        needleImageView.getTransforms().add(needleRotate);

        log.info("旋转角度: " + needleRotate.getAngle());
        Timeline needleUp = new Timeline(new KeyFrame(Duration.seconds(0.6), new KeyValue(needleRotate.angleProperty(), 60)));
        Timeline downUp = new Timeline(new KeyFrame(Duration.seconds(0.6), new KeyValue(needleRotate.angleProperty(), 80)));
        playingStateListener = (obs, oldVal, newVal) -> {
            if (MediaPlayer.Status.PLAYING.equals(newVal)) {
                Platform.runLater(()->{
                    recordRotateTransition.play();
                    downUp.playFromStart();
                    log.info("当前旋转角度: 暂停-" + needleImageView.getRotate());
                });
            } else if (MediaPlayer.Status.PAUSED.equals(newVal) || MediaPlayer.Status.STOPPED.equals(newVal)){
                //pauseRotation();
                log.info("暂停旋转");
                log.info("当前播放状态: " + newVal);
                Platform.runLater(()->{
                    recordRotateTransition.stop();
                    needleUp.playFromStart(); // 抬针
                    log.info("当前旋转角度: 播放-" + needleImageView.getRotate());
                });
            }
        };
        // 注册弱监听器
        musicState.addWeakPlayingListener(playingStateListener);

        // 监听歌曲标题变化
        musicState.songTitlePropertyProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> setSongTitle(newVal));
        });
        // 监听艺术家信息变化
        musicState.singerPropertyProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> setArtistInfo(newVal));
        });
        // 监听专辑信息变化
        musicState.albumPropertyProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> setAlbumInfo(newVal));
        });
        // 监听来源信息变化
        musicState.sourcePropertyProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> setSourceInfo(newVal));
        });
        // 监听歌词URL变化
        lrcUrlListener = (obs, oldVal, newVal) -> {
            Platform.runLater(() -> loadAndSetLrc(newVal));
        };
        // 监听歌词进度 - 创建独立的监听器
        ChangeListener<Duration> currentTimeListener = (obs, oldVal, newVal) -> {
            if (lrcView != null && newVal != null) {
                lrcView.setCurrentTime(newVal);
            }
        };
        musicState.currentTimePropertyProperty().addListener(currentTimeListener);
        musicState.lrcTextPropertyProperty().addListener(lrcUrlListener);
        // 初始化唱片状态 - 移到方法末尾确保所有组件都已初始化
    }

    // 添加加载和设置歌词的方法
    private void loadAndSetLrc(String lrcUrl) {
        if (lrcUrl == null || lrcUrl.isEmpty()) {
            return;
        }

        // 在后台线程中加载歌词
        new Thread(() -> {
            try {
                // 从URL读取字节数据
                java.net.URL url = new java.net.URL(lrcUrl);
                byte[] bytes;
                try (java.io.InputStream inputStream = url.openStream();
                     java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream()) {
                    int nRead;
                    byte[] data = new byte[1024];
                    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    bytes = buffer.toByteArray();
                }

                // 解析歌词
                LrcDoc lrcDoc = LrcDoc.parseLrcDoc(
                        new String(bytes, EncodingDetectUtil.detect(bytes))
                );

                // 在JavaFX线程中设置歌词
                Platform.runLater(() -> {
                    if (lrcView != null && lrcDoc != null) {
                        lrcView.setLrcDoc(lrcDoc);
                    }
                });
            } catch (Exception e) {
                System.err.println("歌词加载失败: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}