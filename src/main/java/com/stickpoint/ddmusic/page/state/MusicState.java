package com.stickpoint.ddmusic.page.state;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.media.MediaPlayer;

/**
 * 音乐播放状态
 * @author fntp
 * @date 2025/9/3
 */
public class MusicState {

    private BooleanProperty isPlayingProperty;

    private StringProperty songTitleProperty;

    private StringProperty singerProperty;
    /**
     * 专辑
     */
    private StringProperty albumProperty;
    /**
     * 专辑封面
     */
    private StringProperty albumCoverProperty;
    /**
     * 歌曲来源
     */
    private StringProperty sourceProperty;
    /**
     * 播放地址
     */
    private StringProperty playUrlProperty;
    /**
     * 播放器状态
     */
    private ObjectProperty<MediaPlayer.Status> playerStatusProperty;

    public void addWeakPlayingListener(ChangeListener<Boolean> listener) {
        // 使用 WeakChangeListener 包装用户传入的监听器
        isPlayingProperty.addListener(new WeakChangeListener<>(listener));
    }

}
