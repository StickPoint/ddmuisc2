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
                "https://qnm.hunliji.com/o_1j3te8vlh16va1hqh81515hvsgd.jpg",
                "https://qnm.hunliji.com/o_1j3te8vlhd781kpc7b8obf15sae.jpg",
                "https://qnm.hunliji.com/o_1j3te8vlh3f0e5c1ao51oaq1eo7f.jpg",
                "https://qnm.hunliji.com/o_1j3te8vlhavd1e8i1av91ujt1j7ig.jpg",
                "https://qnm.hunliji.com/o_1j3te8vlh1p9p1uqbfdj1kqi1ag6h.jpg"
        );
        sceneryCarousel = new RXCarousel();
        // 设置轮播图最小宽度
        sceneryCarousel.setPrefSize(378,138);
        initCarousel(bannerList);

        recommendContainer = new HomePageContentRecommendContainer();
        // 设置推荐容器最小宽度
        recommendContainer.setMinWidth(138);
        // 将组件添加到HBox中
        topContainer.getChildren().addAll(sceneryCarousel, recommendContainer);

        // 设置水平布局优先级，让两个组件都能撑满高度
        HBox.setHgrow(sceneryCarousel, Priority.ALWAYS);
        HBox.setHgrow(recommendContainer, Priority.ALWAYS);

        // 设置轮播图尺寸
        sceneryCarousel.setPrefHeight(138);

        // 将顶部容器添加到VBox中
        getChildren().add(topContainer);

        // 设置垂直布局优先级
        setVgrow(new Region(), Priority.ALWAYS);
    }

    /**
     * 初始化轮播图
     */
    private void initCarousel(List<String> bannerList) {
        // 创建轮播图页面
        for (String url : bannerList) {
            RXCarouselPane pane = new RXCarouselPane();
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);
            // 设置合适的宽度
            imageView.setFitWidth(378);
            // 设置合适的高度
            imageView.setFitHeight(138);
            // 使用缓存加载图片
            Image image = loadImageWithCache(url, 378, 138);
            imageView.setImage(image);
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
