package com.stickpoint.ddmusic.page.skin;

import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.skin.SliderSkin;

/**
 * 默认中国红配色
 * @author fntp
 * @date 2025/8/30
 */
public class RedVerticalSliderSkin extends SliderSkin {

    public RedVerticalSliderSkin(Slider slider) {
        super(slider);
        // 查找并修改轨道和滑块
        Node track = slider.lookup(".track");
        Node thumb = slider.lookup(".thumb");
        if (track != null) {
            track.setStyle("-fx-background-color: #D7000F; -fx-pref-width: 3px;");
        }
        if (thumb != null) {
            thumb.setStyle("-fx-background-color: #D7000F; -fx-background-radius: 6px;");
        }
    }
}
