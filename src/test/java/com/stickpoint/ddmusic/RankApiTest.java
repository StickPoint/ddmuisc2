package com.stickpoint.ddmusic;

import com.stickpoint.ddmusic.common.utils.HttpUtils;

/**
 * 测试音乐榜单API
 * @author fntp
 * @date 2026/1/23
 */
public class RankApiTest {
    public static void main(String[] args) {
        // 调用API获取音乐榜单
        String result = HttpUtils.getMusicRank();
        // 打印结果的前500个字符，查看数据结构
        if (result != null && result.length() > 0) {
            System.out.println("API返回结果：");
            System.out.println(result.substring(0, Math.min(result.length(), 1000)));
            
            // 检查是否包含pic字段
            if (result.contains("pic")) {
                System.out.println("\n✓ 返回结果中包含pic字段");
            } else {
                System.out.println("\n✗ 返回结果中不包含pic字段");
            }
        } else {
            System.out.println("API返回结果为空");
        }
    }
}