package com.stickpoint.ddmusic.page.node;

import com.leewyatt.rxcontrols.animation.carousel.AnimFade;
import com.leewyatt.rxcontrols.controls.RXCarousel;
import com.leewyatt.rxcontrols.pane.RXCarouselPane;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 首页内容区域
 * @author fntp
 * @date 2025/8/27
 */
public class HomePageContentContainer extends VBox {

    /**
     * 首页轮播图
     */
    public RXCarousel sceneryCarousel;

    /**
     * 推荐容器
     */
    public HomePageContentRecommendContainer recommendContainer;

    private final Map<String, SoftReference<Image>> imageCache = new HashMap<>();

    /**
     * 发现音乐容器
     */
    private DiscoverMusicContainer discoverMusicContainer;
    
    /**
     * 获取发现音乐容器实例
     * @return 发现音乐容器
     */
    public DiscoverMusicContainer getDiscoverMusicContainer() {
        return discoverMusicContainer;
    }
    
    public HomePageContentContainer() {
        // 设置整体样式
        // 加载当前页面的对应css文件
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/HomePageContentContainer.css")).toExternalForm());
        getStyleClass().add("root-pane");

        // 创建顶部HBox容器
        HBox topContainer = new HBox();
        // 设置左右间距为10px
        topContainer.setSpacing(10);
        // 设置内边距，左右各10px
        topContainer.setPadding(new Insets(0, 10, 0, 10));

        // 初始化轮播图
        List<String> bannerList = List.of(
                "https://qnm.hunliji.com/o_1jfo2rnqtdijj2l1c8o6441lecd.png",
                "https://qnm.hunliji.com/o_1jfo2rnqt12us1984v4b1e7hrie.png",
                "https://qnm.hunliji.com/o_1jfo2rnqtahe1svm1bpc16b81fojf.png",
                "https://qnm.hunliji.com/o_1jfo2rnqtd9poamgunequ1856g.png",
                "https://qnm.hunliji.com/o_1jfo2rnqt1dbs1r3k1ia21ihc1bsvh.png"
        );
        sceneryCarousel = new RXCarousel();
        // 设置轮播图最小宽度
        sceneryCarousel.setPrefSize(378,138);
        initCarousel(bannerList);
        
        recommendContainer = new HomePageContentRecommendContainer();
        // 设置推荐容器最小宽度
        recommendContainer.setMinWidth(138);
        
        // 创建一个VBox作为轮播图的容器，用于添加裁剪效果
        javafx.scene.layout.VBox carouselContainer = new javafx.scene.layout.VBox();
        carouselContainer.setPrefSize(378, 138);
        // 移除内边距和外边距，确保裁剪效果完整
        carouselContainer.setPadding(new Insets(0));
        carouselContainer.setSpacing(0);
        
        // 将轮播图添加到容器中
        carouselContainer.getChildren().add(sceneryCarousel);
        
        // 确保轮播图完全填充容器，没有额外边距
        sceneryCarousel.setPrefSize(378, 138);
        sceneryCarousel.setMinSize(378, 138);
        sceneryCarousel.setMaxSize(378, 138);
        sceneryCarousel.setPadding(new Insets(0));
        
        // 设置容器的裁剪区域，确保整个轮播图都是圆角，四个角落圆角大小一致
        javafx.scene.shape.Rectangle containerClip = new javafx.scene.shape.Rectangle(378, 138);
        containerClip.setArcWidth(20);
        containerClip.setArcHeight(20);
        carouselContainer.setClip(containerClip);
        
        // 确保容器本身也有圆角样式
        carouselContainer.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;");
        
        // 将带裁剪效果的容器和推荐容器添加到HBox中
        topContainer.getChildren().addAll(carouselContainer, recommendContainer);

        // 设置水平布局优先级，让两个组件都能撑满高度
        HBox.setHgrow(sceneryCarousel, Priority.NEVER);
        HBox.setHgrow(recommendContainer, Priority.ALWAYS);

        // 设置轮播图尺寸
        sceneryCarousel.setPrefHeight(138);

        // 创建发现音乐容器
        discoverMusicContainer = new DiscoverMusicContainer();
        
        // 将顶部容器和发现音乐容器添加到VBox中
        getChildren().addAll(topContainer, discoverMusicContainer);

        // 设置垂直布局优先级
        VBox.setVgrow(discoverMusicContainer, Priority.ALWAYS);
    }

    /**
     * 初始化轮播图
     */
    private void initCarousel(List<String> bannerList) {
        // 设置轮播图容器的圆角
        sceneryCarousel.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;");
        
        // 创建轮播图页面
        for (String url : bannerList) {
            RXCarouselPane pane = new RXCarouselPane();
            pane.setStyle("-fx-background-radius: 20;");
            
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);
            // 设置合适的宽度
            imageView.setFitWidth(378);
            // 设置合适的高度
            imageView.setFitHeight(138);
            // 使用缓存加载图片
            Image image = loadImageWithCache(url, 378, 138);
            imageView.setImage(image);
            
            // 设置ImageView的圆角
            imageView.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;");
            
            // 创建裁剪区域，确保所有角落都是圆角
            Rectangle clip = new Rectangle(378, 138);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            imageView.setClip(clip);
            
            pane.setCenter(imageView);
            sceneryCarousel.getPaneList().add(pane);
        }
        sceneryCarousel.setAutoSwitch(true);
        sceneryCarousel.setShowTime(Duration.seconds(4.5));
        sceneryCarousel.setCarouselAnimation(new AnimFade());
        sceneryCarousel.setAnimationTime(Duration.millis(600));
        sceneryCarousel.getStyleClass().add("rx-carousel");
    }

    /**
     * 加载图片并加入缓存
     * @param url 图片URL
     * @param width 图片宽度
     * @param height 图片高度
     * @return 加载的图片
     */
    private Image loadImageWithCache(String url, double width, double height) {
        SoftReference<Image> ref = imageCache.get(url);
        Image cachedImage = (ref != null) ? ref.get() : null;

        if (cachedImage != null && !cachedImage.isError()) {
            // 返回缓存中的图片
            return cachedImage;
        } else {
            // 缓存中没有或已失效，重新加载
            Image newImage = new Image(url, width, height, true, true, true);
            newImage.errorProperty().addListener((obs, wasError, isError) -> {
                if (isError) {
                    // 可选：处理加载错误，例如移除缓存项或记录日志
                    imageCache.remove(url);
                }
            });
            imageCache.put(url, new SoftReference<>(newImage));
            return newImage;
        }
    }


}
