package com.stickpoint.ddmusic.page.node;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.concurrent.Task;

/**
 * 异步图片加载组件
 * @author fntp
 * @date 2026/1/23
 */
public class AsyncImageView extends ImageView {

    /**
     * 构造函数
     * @param url 图片URL
     * @param width 图片宽度
     * @param height 图片高度
     */
    public AsyncImageView(String url, double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
        setPreserveRatio(true);
        setSmooth(true);
        loadImage(url, width, height);
    }
    
    /**
     * 异步加载图片
     * @param url 图片URL
     * @param width 图片宽度
     * @param height 图片高度
     */
    private void loadImage(String url, double width, double height) {
        Task<Image> task = new Task<>() {
            @Override
            protected Image call() {
                // 在后台线程加载图片
                return new Image(url, width, height, true, true, true);
            }
            
            @Override
            protected void succeeded() {
                // 加载成功后在UI线程更新图片
                setImage(getValue());
            }
            
            @Override
            protected void failed() {
                // 加载失败时可以设置默认图片或处理错误
                System.err.println("Failed to load image: " + url);
            }
        };
        
        // 启动后台线程
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}