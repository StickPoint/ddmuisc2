package com.stickpoint.ddmusic.page.node;


import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

/**
 * @author fntp
 * @date 2025/8/24
 */
public class SvgIcon extends Region {

    private SVGPath svgPath;
    /**
     * 如果一个SVG包含多段PATH 则可以通过合并PATH实现
     */
    private SVGPath anotherPath;


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

    public void modifyContent(String newSvgPath, String newAnotherPath) {
        svgPath.setContent(newSvgPath);
        anotherPath = new SVGPath();
        anotherPath.setContent(newAnotherPath);
        Shape newShape = Shape.union(svgPath, anotherPath);
        setShape(newShape);
        this.layout();
    }

}
