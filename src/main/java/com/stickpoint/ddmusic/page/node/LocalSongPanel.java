package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.page.state.MusicState;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

/**
 * Local music page shown on the right side of the home layout.
 */
public class LocalSongPanel extends BorderPane {

    private static final String PAGE_TITLE = "\u672c\u5730\u97f3\u4e50";
    private static final String PAGE_SUBTITLE = "\u7ba1\u7406\u672c\u5730\u66f2\u5e93\uff0c\u626b\u63cf\u540e\u53ef\u4ee5\u76f4\u63a5\u64ad\u653e";
    private static final String PICK_FOLDER_TEXT = "\u9009\u62e9\u6587\u4ef6\u5939";
    private static final String SCAN_BUTTON_TEXT = "\u626b\u63cf\u97f3\u4e50";
    private static final String PICK_FOLDER_TITLE = "\u9009\u62e9\u672c\u5730\u97f3\u4e50\u6587\u4ef6\u5939";
    private static final String FOLDER_PROMPT_TEXT = "\u8bf7\u9009\u62e9\u672c\u5730\u97f3\u4e50\u6587\u4ef6\u5939";
    private static final String INITIAL_STATUS_TEXT = "\u672a\u9009\u62e9\u6587\u4ef6\u5939";
    private static final String INVALID_FOLDER_TEXT = "\u8bf7\u5148\u9009\u62e9\u6709\u6548\u7684\u97f3\u4e50\u6587\u4ef6\u5939";
    private static final String LOCAL_SOURCE_TEXT = "\u672c\u5730";
    private static final String LOCAL_ALBUM_TEXT = "\u672c\u5730\u4e13\u8f91";
    private static final String UNKNOWN_ARTIST_TEXT = "\u672a\u77e5\u6b4c\u624b";

    private final UnifiedMusicTable songTable;
    private final ComboBox<String> folderComboBox;
    private final Button pickFolderButton;
    private final Button scanButton;
    private final Label statusLabel;

    public LocalSongPanel(MusicState musicState, BottomMusicContainer bottomMusicContainer) {
        songTable = new UnifiedMusicTable(musicState, bottomMusicContainer);
        folderComboBox = new ComboBox<>();
        pickFolderButton = new Button(PICK_FOLDER_TEXT);
        scanButton = new Button(SCAN_BUTTON_TEXT);
        statusLabel = new Label(INITIAL_STATUS_TEXT);

        initialize();
        setupLayout();
    }

    private void initialize() {
        loadStylesheet();

        songTable.setData(List.of(), true);

        folderComboBox.getStyleClass().add("library-folder-combo");
        folderComboBox.setPromptText(FOLDER_PROMPT_TEXT);
        folderComboBox.setFocusTraversable(false);
        folderComboBox.setPrefWidth(360);
        folderComboBox.setMaxWidth(Double.MAX_VALUE);

        pickFolderButton.getStyleClass().addAll("library-button", "library-secondary-button");
        pickFolderButton.setFocusTraversable(false);
        pickFolderButton.setOnAction(event -> chooseFolder());

        scanButton.getStyleClass().addAll("library-button", "library-primary-button");
        scanButton.setFocusTraversable(false);
        scanButton.setOnAction(event -> scanFolder());

        statusLabel.getStyleClass().add("library-status-label");
    }

    private void setupLayout() {
        Label titleLabel = new Label(PAGE_TITLE);
        titleLabel.getStyleClass().add("library-page-title");

        Label subtitleLabel = new Label(PAGE_SUBTITLE);
        subtitleLabel.getStyleClass().add("library-page-subtitle");

        VBox headerBox = new VBox(6, titleLabel, subtitleLabel);
        headerBox.getStyleClass().add("library-page-header");

        Region toolbarSpacer = new Region();
        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);
        HBox.setHgrow(folderComboBox, Priority.ALWAYS);

        HBox toolbar = new HBox(12, folderComboBox, pickFolderButton, scanButton, toolbarSpacer, statusLabel);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("library-toolbar");

        VBox tableCard = new VBox(songTable);
        tableCard.getStyleClass().add("library-table-card");
        VBox.setVgrow(songTable, Priority.ALWAYS);

        VBox pageContent = new VBox(18, headerBox, toolbar, tableCard);
        pageContent.setPadding(new Insets(18, 18, 16, 18));
        pageContent.setFillWidth(true);
        pageContent.getStyleClass().addAll("music-library-page", "local-music-page");
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        getStyleClass().add("local-song-panel");
        setCenter(pageContent);
        setStyle("-fx-background-color: transparent;");
    }

    private void loadStylesheet() {
        String stylesheet = Objects.requireNonNull(getClass().getResource("/css/LocalDownloadPage.css")).toExternalForm();
        if (!getStylesheets().contains(stylesheet)) {
            getStylesheets().add(stylesheet);
        }
    }

    private void chooseFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(PICK_FOLDER_TITLE);

        String currentSelection = folderComboBox.getValue();
        if (currentSelection != null && !currentSelection.isBlank()) {
            File currentDirectory = new File(currentSelection);
            if (currentDirectory.exists() && currentDirectory.isDirectory()) {
                directoryChooser.setInitialDirectory(currentDirectory);
            }
        } else {
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        Window window = getScene() == null ? null : getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(window);
        if (selectedDirectory == null) {
            return;
        }

        String directoryPath = selectedDirectory.getAbsolutePath();
        ObservableList<String> items = folderComboBox.getItems();
        if (!items.contains(directoryPath)) {
            items.add(directoryPath);
        }

        folderComboBox.setValue(directoryPath);
        statusLabel.setText("\u5df2\u9009\u62e9: " + directoryPath);
    }

    private void scanFolder() {
        String selectedFolder = folderComboBox.getValue();
        if (selectedFolder == null || selectedFolder.isBlank()) {
            statusLabel.setText(INVALID_FOLDER_TEXT);
            return;
        }

        File folder = new File(selectedFolder);
        if (!folder.exists() || !folder.isDirectory()) {
            statusLabel.setText(INVALID_FOLDER_TEXT);
            return;
        }

        statusLabel.setText("\u6b63\u5728\u626b\u63cf: " + selectedFolder);

        List<UnifiedMusicTable.MusicItem> musicItems = new ArrayList<>();
        scanMusicFiles(folder, musicItems);

        songTable.setData(musicItems, true);
        if (musicItems.isEmpty()) {
            statusLabel.setText("\u5f53\u524d\u76ee\u5f55\u672a\u53d1\u73b0\u53ef\u7528\u97f3\u9891\u6587\u4ef6");
        } else {
            statusLabel.setText("\u626b\u63cf\u5b8c\u6210\uff0c\u5171\u53d1\u73b0 " + musicItems.size() + " \u9996\u672c\u5730\u97f3\u4e50");
        }
    }

    private void scanMusicFiles(File folder, List<UnifiedMusicTable.MusicItem> data) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scanMusicFiles(file, data);
                continue;
            }

            String displayName = file.getName();
            String lowerCaseName = displayName.toLowerCase(Locale.ROOT);
            if (!isSupportedAudioFile(lowerCaseName)) {
                continue;
            }

            String baseName = displayName.substring(0, displayName.lastIndexOf('.'));
            String artist = UNKNOWN_ARTIST_TEXT;
            String songName = baseName;
            int separatorIndex = baseName.indexOf(" - ");
            if (separatorIndex > 0) {
                artist = baseName.substring(0, separatorIndex).trim();
                songName = baseName.substring(separatorIndex + 3).trim();
            }

            String filePath = file.getAbsolutePath();
            String fileUrl = "file:///" + filePath.replace("\\", "/").replace(" ", "%20");

            data.add(new UnifiedMusicTable.MusicItem(
                "local-" + file.hashCode(),
                songName,
                artist,
                LOCAL_ALBUM_TEXT,
                LOCAL_SOURCE_TEXT,
                fileUrl,
                "",
                "",
                filePath
            ));
        }
    }

    private boolean isSupportedAudioFile(String fileName) {
        return fileName.endsWith(".mp3")
            || fileName.endsWith(".wav")
            || fileName.endsWith(".flac")
            || fileName.endsWith(".aac")
            || fileName.endsWith(".ogg")
            || fileName.endsWith(".wma");
    }
}
