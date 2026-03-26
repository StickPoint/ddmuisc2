package com.stickpoint.ddmusic.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * TuneHub API工具类
 * 用于对接TuneHub音乐信息解析服务
 */
public class TuneHubApiUtils {
    
    /**
     * 系统日志
     */
    private static final Logger log = LoggerFactory.getLogger(TuneHubApiUtils.class);
    
    /**
     * TuneHub API基础URL
     */
    private static final String TUNE_HUB_API_BASE_URL = "https://music-dl.sayqz.com/api";
    
    /**
     * 支持的平台
     */
    public enum Platform {
        NETEASE("netease"),
        KUWO("kuwo"),
        QQ("qq");
        
        private final String value;
        
        Platform(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    /**
     * 音质参数
     */
    public enum Quality {
        STANDARD("128k"),
        HIGH("320k"),
        LOSSLESS("flac"),
        HI_RES("flac24bit");
        
        private final String value;
        
        Quality(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    /**
     * 获取歌曲基本信息
     * @param platform 平台标识
     * @param id 歌曲ID
     * @return 歌曲信息JSON
     */
    public static String getSongInfo(Platform platform, String id) {
        log.info("获取歌曲基本信息: 平台={}, 歌曲ID={}", platform.getValue(), id);
        Map<String, Object> params = new HashMap<>();
        params.put("source", platform.getValue());
        params.put("id", id);
        params.put("type", "info");
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 获取音乐文件链接
     * @param platform 平台标识
     * @param id 歌曲ID
     * @param quality 音质参数
     * @return 音乐文件链接（302重定向）
     */
    public static String getMusicUrl(Platform platform, String id, Quality quality) {
        log.info("获取音乐文件链接: 平台={}, 歌曲ID={}, 音质={}", platform.getValue(), id, quality.getValue());
        Map<String, Object> params = new HashMap<>();
        params.put("source", platform.getValue());
        params.put("id", id);
        params.put("type", "url");
        params.put("br", quality.getValue());
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 获取音乐文件链接（默认320k音质）
     * @param platform 平台标识
     * @param id 歌曲ID
     * @return 音乐文件链接（302重定向）
     */
    public static String getMusicUrl(Platform platform, String id) {
        return getMusicUrl(platform, id, Quality.HIGH);
    }
    
    /**
     * 获取专辑封面
     * @param platform 平台标识
     * @param id 歌曲ID
     * @return 专辑封面图片链接（302重定向）
     */
    public static String getAlbumCover(Platform platform, String id) {
        log.info("获取专辑封面: 平台={}, 歌曲ID={}", platform.getValue(), id);
        Map<String, Object> params = new HashMap<>();
        params.put("source", platform.getValue());
        params.put("id", id);
        params.put("type", "pic");
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 获取歌词
     * @param platform 平台标识
     * @param id 歌曲ID
     * @return LRC格式歌词
     */
    public static String getLyrics(Platform platform, String id) {
        log.info("获取歌词: 平台={}, 歌曲ID={}", platform.getValue(), id);
        Map<String, Object> params = new HashMap<>();
        params.put("source", platform.getValue());
        params.put("id", id);
        params.put("type", "lrc");
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 搜索歌曲
     * @param platform 平台标识
     * @param keyword 搜索关键词
     * @param limit 结果数量限制
     * @return 搜索结果JSON
     */
    public static String searchSong(Platform platform, String keyword, int limit) {
        log.info("搜索歌曲: 平台={}, 关键词={}, 限制={}", platform.getValue(), keyword, limit);
        Map<String, Object> params = new HashMap<>();
        params.put("source", platform.getValue());
        params.put("type", "search");
        params.put("keyword", keyword);
        params.put("limit", limit);
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 搜索歌曲（默认20条结果）
     * @param platform 平台标识
     * @param keyword 搜索关键词
     * @return 搜索结果JSON
     */
    public static String searchSong(Platform platform, String keyword) {
        return searchSong(platform, keyword, 20);
    }
    
    /**
     * 聚合搜索（搜索所有平台）
     * @param keyword 搜索关键词
     * @return 聚合搜索结果JSON
     */
    public static String aggregateSearch(String keyword) {
        log.info("聚合搜索歌曲: 关键词={}", keyword);
        Map<String, Object> params = new HashMap<>();
        params.put("type", "aggregateSearch");
        params.put("keyword", keyword);
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 获取歌单详情
     * @param platform 平台标识
     * @param id 歌单ID
     * @return 歌单详情JSON
     */
    public static String getPlaylistDetail(Platform platform, String id) {
        log.info("获取歌单详情: 平台={}, 歌单ID={}", platform.getValue(), id);
        Map<String, Object> params = new HashMap<>();
        params.put("source", platform.getValue());
        params.put("id", id);
        params.put("type", "playlist");
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 获取排行榜列表
     * @return 排行榜列表JSON
     */
    public static String getToplists() {
        log.info("获取排行榜列表");
        Map<String, Object> params = new HashMap<>();
        params.put("type", "toplists");
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 获取排行榜歌曲
     * @param platform 平台标识
     * @param id 排行榜ID
     * @return 排行榜歌曲JSON
     */
    public static String getToplistSongs(Platform platform, String id) {
        log.info("获取排行榜歌曲: 平台={}, 排行榜ID={}", platform.getValue(), id);
        Map<String, Object> params = new HashMap<>();
        params.put("source", platform.getValue());
        params.put("id", id);
        params.put("type", "toplist");
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 获取系统状态
     * @return 系统状态JSON
     */
    public static String getSystemStatus() {
        log.info("获取系统状态");
        Map<String, Object> params = new HashMap<>();
        params.put("type", "status");
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
    
    /**
     * 获取统计数据
     * @return 统计数据JSON
     */
    public static String getStats() {
        log.info("获取统计数据");
        Map<String, Object> params = new HashMap<>();
        params.put("type", "stats");
        
        return HttpUtils.doTuneFreeGetWithParams(TUNE_HUB_API_BASE_URL, params);
    }
}