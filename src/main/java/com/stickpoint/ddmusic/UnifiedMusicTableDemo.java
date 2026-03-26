package com.stickpoint.ddmusic;

import com.stickpoint.ddmusic.page.node.UnifiedMusicTable;
import com.stickpoint.ddmusic.page.node.BottomMusicContainer;
import com.stickpoint.ddmusic.page.state.MusicState;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一音乐表格演示程序
 * 用于测试和展示UnifiedMusicTable组件的功能
 */
public class UnifiedMusicTableDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建音乐状态和底部音乐容器
        MusicState musicState = new MusicState();
        BottomMusicContainer bottomMusicContainer = new BottomMusicContainer(musicState);
        
        // 创建统一音乐表格组件
        UnifiedMusicTable musicTable = new UnifiedMusicTable(musicState, bottomMusicContainer);
        
        // 创建测试数据
        List<UnifiedMusicTable.MusicItem> testItems = createTestMusicItems();
        
        // 设置表格数据
        musicTable.setData(testItems, false);
        
        // 创建根容器
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #f0f0f0;");
        root.getChildren().add(musicTable);
        
        // 创建场景
        Scene scene = new Scene(root, 1000, 600);
        
        // 设置舞台
        primaryStage.setTitle("统一音乐表格演示");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * 创建测试音乐数据
     */
    private List<UnifiedMusicTable.MusicItem> createTestMusicItems() {
        List<UnifiedMusicTable.MusicItem> items = new ArrayList<>();
        
        // 添加测试数据
        items.add(new UnifiedMusicTable.MusicItem(
            "1", "海阔天空", "Beyond", "乐与怒", "网易云音乐",
            "http://example.com/play1.mp3", "http://example.com/pic1.jpg", "http://example.com/lrc1.lrc"
        ));
        
        items.add(new UnifiedMusicTable.MusicItem(
            "2", "晴天", "周杰伦", "叶惠美", "QQ音乐",
            "http://example.com/play2.mp3", "http://example.com/pic2.jpg", "http://example.com/lrc2.lrc"
        ));
        
        items.add(new UnifiedMusicTable.MusicItem(
            "3", "成都", "赵雷", "无法长大", "网易云音乐",
            "http://example.com/play3.mp3", "http://example.com/pic3.jpg", "http://example.com/lrc3.lrc"
        ));
        
        items.add(new UnifiedMusicTable.MusicItem(
            "4", "起风了", "买辣椒也用券", "起风了", "酷狗音乐",
            "http://example.com/play4.mp3", "http://example.com/pic4.jpg", "http://example.com/lrc4.lrc"
        ));
        
        items.add(new UnifiedMusicTable.MusicItem(
            "5", "倒数", "邓紫棋", "另一个童话", "QQ音乐",
            "http://example.com/play5.mp3", "http://example.com/pic5.jpg", "http://example.com/lrc5.lrc"
        ));
        
        // 添加更多测试数据
        for (int i = 6; i <= 20; i++) {
            items.add(new UnifiedMusicTable.MusicItem(
                String.valueOf(i), "歌曲" + i, "歌手" + i, "专辑" + i, "音源" + i,
                "http://example.com/play" + i + ".mp3", 
                "http://example.com/pic" + i + ".jpg", 
                "http://example.com/lrc" + i + ".lrc"
            ));
        }
        
        return items;
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        launch(args);
    }
}