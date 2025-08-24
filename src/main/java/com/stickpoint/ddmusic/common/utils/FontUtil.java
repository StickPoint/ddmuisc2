package com.stickpoint.ddmusic.common.utils;

/**
 * @author fntp
 * @date 2025/8/24
 */
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontUtil {
    private static final Logger log = LoggerFactory.getLogger(FontUtil.class);
    private static final Map<String, Font> LOADED_FONTS = new HashMap<>();

    /**
     * 加载自定义字体
     * @param fontPath 字体文件路径（相对于resources目录）
     * @param size 字体大小
     * @return Font对象
     */
    public static Font loadFont(String fontPath, double size) {
        String key = fontPath + "_" + size;
        // 检查是否已经加载过
        if (LOADED_FONTS.containsKey(key)) {
            return LOADED_FONTS.get(key);
        }
        try {
            InputStream fontStream = FontUtil.class.getResourceAsStream(fontPath);
            if (fontStream != null) {
                Font font = Font.loadFont(fontStream, size);
                if (font != null) {
                    LOADED_FONTS.put(key, font);
                    fontStream.close();
                    return font;
                }
                fontStream.close();
            }
        } catch (Exception e) {
            log.warn("Failed to load custom font: {}", fontPath, e);
        }

        // 回退到系统默认字体
        return Font.font("Microsoft YaHei", size);
    }
}