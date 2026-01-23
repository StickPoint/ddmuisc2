package com.stickpoint.ddmusic.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件管理工具类
 * 用于读写.dd格式的配置文件
 */
public class ConfigUtils {

    private static final Logger log = LoggerFactory.getLogger(ConfigUtils.class);
    
    /**
     * 配置文件名称
     */
    private static final String CONFIG_FILE_NAME = "ddmusic.dd";
    
    /**
     * 配置文件路径
     */
    private static final String CONFIG_FILE_PATH = System.getProperty("user.home") + File.separator + CONFIG_FILE_NAME;
    
    /**
     * 配置项键名
     */
    public static final String KEY_DEFAULT_QUALITY = "default_quality";
    public static final String KEY_SCAN_FOLDERS = "scan_folders";
    
    /**
     * 默认配置
     */
    private static final Map<String, String> DEFAULT_CONFIG = new HashMap<>();
    
    static {
        // 初始化默认配置
        DEFAULT_CONFIG.put(KEY_DEFAULT_QUALITY, TuneHubApiUtils.Quality.HIGH.getValue());
        DEFAULT_CONFIG.put(KEY_SCAN_FOLDERS, "");
    }
    
    /**
     * 读取配置文件
     * @return 配置映射
     */
    public static Map<String, String> readConfig() {
        Map<String, String> config = new HashMap<>(DEFAULT_CONFIG);
        
        File configFile = new File(CONFIG_FILE_PATH);
        if (!configFile.exists()) {
            // 配置文件不存在，创建默认配置文件
            log.info("配置文件不存在，创建默认配置文件: {}", CONFIG_FILE_PATH);
            writeConfig(config);
            return config;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // 跳过空行和注释行
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // 解析key-value对
                int separatorIndex = line.indexOf("=");
                if (separatorIndex != -1) {
                    String key = line.substring(0, separatorIndex).trim();
                    String value = line.substring(separatorIndex + 1).trim();
                    if (!key.isEmpty()) {
                        config.put(key, value);
                    }
                }
            }
            log.info("配置文件读取成功: {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            log.error("配置文件读取失败: {}", e.getMessage());
            // 读取失败时返回默认配置
            return new HashMap<>(DEFAULT_CONFIG);
        }
        
        return config;
    }
    
    /**
     * 写入配置文件
     * @param config 配置映射
     */
    public static void writeConfig(Map<String, String> config) {
        File configFile = new File(CONFIG_FILE_PATH);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            // 写入注释
            writer.write("# DD音乐播放器配置文件");
            writer.newLine();
            writer.write("# 格式: key=value");
            writer.newLine();
            writer.newLine();
            
            // 写入配置项
            for (Map.Entry<String, String> entry : config.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            
            log.info("配置文件写入成功: {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            log.error("配置文件写入失败: {}", e.getMessage());
        }
    }
    
    /**
     * 获取单个配置项
     * @param key 配置项键名
     * @return 配置项值
     */
    public static String getConfig(String key) {
        Map<String, String> config = readConfig();
        return config.getOrDefault(key, DEFAULT_CONFIG.getOrDefault(key, ""));
    }
    
    /**
     * 设置单个配置项
     * @param key 配置项键名
     * @param value 配置项值
     */
    public static void setConfig(String key, String value) {
        Map<String, String> config = readConfig();
        config.put(key, value);
        writeConfig(config);
    }
    
    /**
     * 获取默认音乐品质
     * @return 默认音乐品质
     */
    public static TuneHubApiUtils.Quality getDefaultQuality() {
        String qualityValue = getConfig(KEY_DEFAULT_QUALITY);
        for (TuneHubApiUtils.Quality quality : TuneHubApiUtils.Quality.values()) {
            if (quality.getValue().equals(qualityValue)) {
                return quality;
            }
        }
        // 如果配置的值无效，返回默认值
        return TuneHubApiUtils.Quality.HIGH;
    }
    
    /**
     * 设置默认音乐品质
     * @param quality 默认音乐品质
     */
    public static void setDefaultQuality(TuneHubApiUtils.Quality quality) {
        setConfig(KEY_DEFAULT_QUALITY, quality.getValue());
    }
    
    /**
     * 获取扫描文件夹历史记录
     * @return 扫描文件夹历史记录，以逗号分隔
     */
    public static String getScanFolders() {
        return getConfig(KEY_SCAN_FOLDERS);
    }
    
    /**
     * 设置扫描文件夹历史记录
     * @param folders 扫描文件夹历史记录，以逗号分隔
     */
    public static void setScanFolders(String folders) {
        setConfig(KEY_SCAN_FOLDERS, folders);
    }
    
    /**
     * 获取配置文件路径
     * @return 配置文件路径
     */
    public static String getConfigFilePath() {
        return CONFIG_FILE_PATH;
    }
}