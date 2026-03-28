package com.stickpoint.ddmusic.page.node;

import com.stickpoint.ddmusic.page.state.MusicState;
import java.util.Objects;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * \u672c\u5730\u4e0b\u8f7d\u9875\u9762
 * @author fntp
 * @date 2025/12/30
 */
public class LocalDownloadPage extends BorderPane {

    private static final String PAGE_TITLE = "\u672c\u5730\u4e0e\u4e0b\u8f7d";
    private static final String LOCAL_MODE_TEXT = "\u672c\u5730\u97f3\u4e50";
    private static final String DOWNLOAD_MODE_TEXT = "\u4e0b\u8f7d\u7ba1\u7406";
    private static final String LOCAL_SUBTITLE = "\u7ba1\u7406\u4f60\u7684\u672c\u5730\u66f2\u5e93\u4e0e\u4e0b\u8f7d\u4efb\u52a1";
    private static final String DOWNLOAD_SUBTITLE = "\u67e5\u770b\u4e0b\u8f7d\u8fdb\u5ea6\u3001\u72b6\u6001\u4e0e\u64cd\u4f5c";

    private final LocalSongPanel localSongPanel;
    private final DownloadSongPanel downloadSongPanel;
    private final Button modeSwitchButton;
    private final StackPane contentContainer;
    private final Label pageSubtitle;

    private boolean showingLocal = true;
    private SequentialTransition buttonFadeTransition;

    public LocalDownloadPage(MusicState musicState, BottomMusicContainer bottomMusicContainer) {
        localSongPanel = new LocalSongPanel(musicState, bottomMusicContainer);
        downloadSongPanel = new DownloadSongPanel();
        modeSwitchButton = createSwitchButton();
        contentContainer = new StackPane();
        pageSubtitle = new Label();

        initialize();
        setupLayout();
    }

    private void initialize() {
        modeSwitchButton.setOnAction(event -> toggleMode());
        applyCurrentState(false);
    }

    private void setupLayout() {
        String stylesheet = Objects.requireNonNull(getClass().getResource("/css/LocalDownloadPage.css")).toExternalForm();
        if (!getStylesheets().contains(stylesheet)) {
            getStylesheets().add(stylesheet);
        }

        Label pageTitle = new Label(PAGE_TITLE);
        pageTitle.getStyleClass().add("download-page-title");

        pageSubtitle.getStyleClass().add("download-page-subtitle");

        VBox titleBlock = new VBox(6, pageTitle, pageSubtitle);
        titleBlock.getStyleClass().add("download-title-block");

        HBox header = new HBox(24, titleBlock, modeSwitchButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("download-page-header");
        HBox.setHgrow(titleBlock, Priority.ALWAYS);

        contentContainer.getStyleClass().add("download-content-container");
        contentContainer.setMinHeight(0);
        contentContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        localSongPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        downloadSongPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox mainContainer = new VBox(18, header, contentContainer);
        mainContainer.setPadding(new Insets(18, 18, 16, 18));
        mainContainer.getStyleClass().add("download-main-container");
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        setCenter(mainContainer);
        setStyle("-fx-background-color: transparent;");
    }

    private Button createSwitchButton() {
        Button button = new Button(LOCAL_MODE_TEXT);
        button.getStyleClass().add("download-switch-button");
        button.setMinWidth(156);
        button.setPrefWidth(156);
        button.setMinHeight(44);
        button.setPrefHeight(44);
        button.setFocusTraversable(false);
        return button;
    }

    private void toggleMode() {
        if (buttonFadeTransition != null
            && buttonFadeTransition.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            return;
        }

        boolean nextShowingLocal = !showingLocal;
        FadeTransition fadeOut = createButtonFade(1.0, 0.0);
        fadeOut.setOnFinished(event -> {
            showingLocal = nextShowingLocal;
            applyCurrentState(true);
            modeSwitchButton.setOpacity(0.0);
        });

        FadeTransition fadeIn = createButtonFade(0.0, 1.0);

        buttonFadeTransition = new SequentialTransition(fadeOut, fadeIn);
        modeSwitchButton.setDisable(true);
        buttonFadeTransition.setOnFinished(event -> {
            modeSwitchButton.setOpacity(1.0);
            modeSwitchButton.setDisable(false);
        });
        buttonFadeTransition.playFromStart();
    }

    private FadeTransition createButtonFade(double fromValue, double toValue) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(170), modeSwitchButton);
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        return fadeTransition;
    }

    private void applyCurrentState(boolean animateContent) {
        modeSwitchButton.setText(showingLocal ? DOWNLOAD_MODE_TEXT : LOCAL_MODE_TEXT);
        switchContent(showingLocal, animateContent);
    }

    private void switchContent(boolean showLocal, boolean animate) {
        Node target = showLocal ? localSongPanel : downloadSongPanel;
        String subtitleText = showLocal ? LOCAL_SUBTITLE : DOWNLOAD_SUBTITLE;

        target.setOpacity(1.0);
        StackPane.setAlignment(target, Pos.TOP_LEFT);

        if (!animate || contentContainer.getChildren().isEmpty()) {
            contentContainer.getChildren().setAll(target);
        } else {
            target.setOpacity(0.0);
            contentContainer.getChildren().setAll(target);

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(220), target);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeTransition.play();
        }

        pageSubtitle.setText(subtitleText);
    }
}
