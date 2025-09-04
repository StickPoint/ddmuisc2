package com.stickpoint.ddmusic.page.state;

import com.stickpoint.ddmusic.common.utils.BaseUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * 音乐播放状态
 * @author fntp
 * @date 2025/9/3
 */
@SuppressWarnings("unused")
public class MusicState {

    /**
     * 歌曲标题
     */
    private StringProperty songTitleProperty;
    /**
     * 歌手信息
     */
    private StringProperty singerProperty;
    /**
     * 专辑信息
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
    /**
     * 歌词地址
     */
    private StringProperty lrcTextProperty;
    /**
     * 当前播放时间
     */
    private SimpleObjectProperty<Duration> currentTimeProperty;

    public MusicState() {
        songTitleProperty = new SimpleStringProperty();
        singerProperty = new SimpleStringProperty();
        albumProperty = new SimpleStringProperty();
        albumCoverProperty = new SimpleStringProperty();
        sourceProperty = new SimpleStringProperty();
        playUrlProperty = new SimpleStringProperty();
        playerStatusProperty = new SimpleObjectProperty<>();
        lrcTextProperty = new SimpleStringProperty();
        currentTimeProperty = new SimpleObjectProperty<>();
    }

    /**
     * 播放音乐
     * @param songTitle 歌曲标题
     * @param singer 歌手
     * @param album 专辑
     * @param albumCover 专辑封面
     * @param source 来源
     * @param playUrl 播放地址
     * @param lrcTextUrl 歌词地址
     */
    public void playMusic(String songTitle, String singer, String album, String albumCover,
                          String source, String playUrl, String lrcTextUrl, Duration currentTime) {
        setSongTitleProperty(songTitle);
        setSingerProperty(singer);
        setAlbumProperty(album);
        setAlbumCoverProperty(albumCover);
        setSourceProperty(source);
        setPlayUrlProperty(playUrl);
        setPlayerStatusProperty(MediaPlayer.Status.PLAYING);
        setLrcText(lrcTextUrl);
        setCurrentTimeProperty(currentTime);
    }

    public Duration getCurrentTimeProperty() {
        return currentTimeProperty.get();
    }

    public ReadOnlyObjectProperty<Duration> currentTimePropertyProperty() {
        return currentTimeProperty;
    }

    public void setCurrentTimeProperty(Duration currentTimeProperty) {
        this.currentTimeProperty.set(currentTimeProperty);
    }

    public void setLrcTextProperty(String lrcTextProperty) {
        this.lrcTextProperty.set(lrcTextProperty);
    }

    private void setLrcText(String lrcTextUrl) {
        if (BaseUtils.isEmpty(lrcTextUrl)) {
            lrcTextProperty.set("暂无歌词");
        } else {
            lrcTextProperty.set(lrcTextUrl);
        }
    }

    public String getLrcTextProperty() {
        return lrcTextProperty.get();
    }

    public StringProperty lrcTextPropertyProperty() {
        return lrcTextProperty;
    }

    public void addWeakPlayingListener(ChangeListener<Boolean> listener) {

    }

    public String getSongTitleProperty() {
        return songTitleProperty.get();
    }

    public StringProperty songTitlePropertyProperty() {
        return songTitleProperty;
    }

    public void setSongTitleProperty(String songTitleProperty) {
        if (BaseUtils.isEmpty(songTitleProperty)) {
            this.songTitleProperty.set("未知歌曲");
        }
        if (BaseUtils.equals(songTitleProperty, getSongTitleProperty())) {
            return;
        }
        this.songTitleProperty.set(songTitleProperty);
    }

    public String getSingerProperty() {
        return singerProperty.get();
    }

    public StringProperty singerPropertyProperty() {
        return singerProperty;
    }

    public void setSingerProperty(String singerProperty) {
        if (BaseUtils.isEmpty(singerProperty)) {
            this.singerProperty.set("未知歌手");
        }
        this.singerProperty.set(singerProperty);
    }

    public String getAlbumProperty() {
        return albumProperty.get();
    }

    public StringProperty albumPropertyProperty() {
        return albumProperty;
    }

    public void setAlbumProperty(String albumProperty) {
        if (BaseUtils.isEmpty(albumProperty)) {
            this.albumProperty.set("未知专辑");
        }
        this.albumProperty.set(albumProperty);
    }

    public String getAlbumCoverProperty() {
        return albumCoverProperty.get();
    }

    public StringProperty albumCoverPropertyProperty() {
        return albumCoverProperty;
    }

    public void setAlbumCoverProperty(String albumCoverProperty) {
        if (BaseUtils.isEmpty(albumCoverProperty)) {
            this.albumCoverProperty.set("https://qnm.hunliji.com/o_1j4afpm441o4vceo1eilush18pj9.jpeg");
        }
        this.albumCoverProperty.set(albumCoverProperty);
    }

    public String getSourceProperty() {
        return sourceProperty.get();
    }

    public StringProperty sourcePropertyProperty() {
        return sourceProperty;
    }

    public void setSourceProperty(String sourceProperty) {
        if (BaseUtils.isEmpty(sourceProperty)) {
            this.sourceProperty.set("未知来源");
        }
        this.sourceProperty.set(sourceProperty);
    }

    public String getPlayUrlProperty() {
        return playUrlProperty.get();
    }

    public StringProperty playUrlPropertyProperty() {
        return playUrlProperty;
    }

    public void setPlayUrlProperty(String playUrlProperty) {
        if (BaseUtils.isEmpty(playUrlProperty)) {
            this.playUrlProperty.set("https://qnm.hunliji.com/o_1j4afpm441o4vceo1eilush18pj9.jpeg");
        }
        this.playUrlProperty.set(playUrlProperty);
    }

    public MediaPlayer.Status getPlayerStatusProperty() {
        return playerStatusProperty.get();
    }

    public ObjectProperty<MediaPlayer.Status> playerStatusPropertyProperty() {
        return playerStatusProperty;
    }

    public void setPlayerStatusProperty(MediaPlayer.Status playerStatusProperty) {
        this.playerStatusProperty.set(playerStatusProperty);
    }
}
