package com.stickpoint.ddmusic.page.node;


import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

/**
 * @author fntp
 * @date 2025/8/24
 */
public class SvgIcon extends Region {

    private SVGPath svgPath;


    public SvgIcon() {
        this("");
    }

    public SvgIcon(String svgContent) {
        svgPath = new SVGPath();
        svgPath.setContent(svgContent);

        // 设置默认样式
        getStyleClass().add("svg-icon");
        setShape(svgPath);

        // 设置默认大小
        setPrefSize(24, 24);
        setMinSize(24, 24);
        setMaxSize(24, 24);
    }



    public void setIconSize(double width, double height) {
        setPrefSize(width, height);
        setMinSize(width, height);
        setMaxSize(width, height);
    }

    public void setFill(String color) {
        setStyle("-fx-background-color: " + color + ";");
    }

    @SuppressWarnings("unused")
    public void modifyContent(String newSvgPath) {
        svgPath.setContent(newSvgPath);
        svgPath.applyCss();
        this.layout();
    }

}
