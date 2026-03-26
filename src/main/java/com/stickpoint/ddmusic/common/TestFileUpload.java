package com.stickpoint.ddmusic.common;

import com.stickpoint.ddmusic.common.utils.HttpUtils;

/**
 * @author fntp
 * @date 2025/9/19
 */
public class TestFileUpload {

    public static void main(String[] args) {
        // 调用示例
        try {
            String response = HttpUtils.uploadFileWithDetails(
                    "F:\\musicCenter\\过往和回忆.mp3",
                    "5515264",
                    "1738647703525",
                    "过往和回忆_fb42c0f5d281512081de3af0662f5829"
            );
            System.out.println( response);
        } catch (Exception e) {
        }
    }
}
