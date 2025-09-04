package com.stickpoint.ddmusic.page.state;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.media.MediaPlayer;

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

    public MusicState() {
        songTitleProperty = new SimpleStringProperty();
        singerProperty = new SimpleStringProperty();
        albumProperty = new SimpleStringProperty();
        albumCoverProperty = new SimpleStringProperty();
        sourceProperty = new SimpleStringProperty();
        playUrlProperty = new SimpleStringProperty();
        playerStatusProperty = new SimpleObjectProperty<>();
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
        this.songTitleProperty.set(songTitleProperty);
    }

    public String getSingerProperty() {
        return singerProperty.get();
    }

    public StringProperty singerPropertyProperty() {
        return singerProperty;
    }

    public void setSingerProperty(String singerProperty) {
        this.singerProperty.set(singerProperty);
    }

    public String getAlbumProperty() {
        return albumProperty.get();
    }

    public StringProperty albumPropertyProperty() {
        return albumProperty;
    }

    public void setAlbumProperty(String albumProperty) {
        this.albumProperty.set(albumProperty);
    }

    public String getAlbumCoverProperty() {
        return albumCoverProperty.get();
    }

    public StringProperty albumCoverPropertyProperty() {
        return albumCoverProperty;
    }

    public void setAlbumCoverProperty(String albumCoverProperty) {
        this.albumCoverProperty.set(albumCoverProperty);
    }

    public String getSourceProperty() {
        return sourceProperty.get();
    }

    public StringProperty sourcePropertyProperty() {
        return sourceProperty;
    }

    public void setSourceProperty(String sourceProperty) {
        this.sourceProperty.set(sourceProperty);
    }

    public String getPlayUrlProperty() {
        return playUrlProperty.get();
    }

    public StringProperty playUrlPropertyProperty() {
        return playUrlProperty;
    }

    public void setPlayUrlProperty(String playUrlProperty) {
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
