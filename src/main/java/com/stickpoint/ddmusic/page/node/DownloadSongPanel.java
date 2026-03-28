package com.stickpoint.ddmusic.page.node;

import java.util.List;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Download management page shown on the right side of the home layout.
 */
public class DownloadSongPanel extends VBox {

    private static final String PAGE_TITLE = "\u4e0b\u8f7d\u7ba1\u7406";
    private static final String PAGE_SUBTITLE = "\u67e5\u770b\u5f53\u524d\u4efb\u52a1\u7684\u8fdb\u5ea6\u3001\u72b6\u6001\u548c\u5b8c\u6210\u7ed3\u679c";
    private static final String FILTER_ALL = "all";
    private static final String FILTER_DOWNLOADING = "downloading";
    private static final String FILTER_PAUSED = "paused";
    private static final String FILTER_COMPLETED = "completed";
    private static final String FILTER_ALL_LABEL = "\u5168\u90e8\u4efb\u52a1";
    private static final String FILTER_DOWNLOADING_LABEL = "\u4e0b\u8f7d\u4e2d";
    private static final String FILTER_PAUSED_LABEL = "\u5df2\u6682\u505c";
    private static final String FILTER_COMPLETED_LABEL = "\u5df2\u5b8c\u6210";
    private static final String STATUS_DOWNLOADING = "\u4e0b\u8f7d\u4e2d";
    private static final String STATUS_PAUSED = "\u5df2\u6682\u505c";
    private static final String STATUS_COMPLETED = "\u5df2\u5b8c\u6210";
    private static final String PLACEHOLDER_TEXT = "\u6682\u65e0\u4e0b\u8f7d\u4efb\u52a1";

    private final TableView<DownloadItem> downloadTable;
    private final ObservableList<DownloadItem> allDownloadItems;
    private final ObservableList<DownloadItem> visibleDownloadItems;
    private final Button allFilterButton;
    private final Button downloadingFilterButton;
    private final Button pausedFilterButton;
    private final Button completedFilterButton;
    private final Label summaryLabel;

    private String activeFilter = FILTER_ALL;

    public DownloadSongPanel() {
        downloadTable = new TableView<>();
        allDownloadItems = FXCollections.observableArrayList();
        visibleDownloadItems = FXCollections.observableArrayList();
        allFilterButton = createFilterButton(FILTER_ALL_LABEL, FILTER_ALL);
        downloadingFilterButton = createFilterButton(FILTER_DOWNLOADING_LABEL, FILTER_DOWNLOADING);
        pausedFilterButton = createFilterButton(FILTER_PAUSED_LABEL, FILTER_PAUSED);
        completedFilterButton = createFilterButton(FILTER_COMPLETED_LABEL, FILTER_COMPLETED);
        summaryLabel = new Label();

        initialize();
        setupLayout();
    }

    private void initialize() {
        loadStylesheet();

        downloadTable.getStyleClass().add("download-table");
        downloadTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        downloadTable.setTableMenuButtonVisible(false);
        downloadTable.setEditable(false);
        downloadTable.setFocusTraversable(false);
        downloadTable.setFixedCellSize(58);
        downloadTable.setPlaceholder(createPlaceholderLabel());
        downloadTable.setItems(visibleDownloadItems);

        TableColumn<DownloadItem, String> nameColumn = new TableColumn<>("\u6b4c\u66f2");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setSortable(false);
        nameColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("download-song-name");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    getStyleClass().add("download-song-name");
                }
            }
        });

        TableColumn<DownloadItem, String> artistColumn = new TableColumn<>("\u6b4c\u624b");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        artistColumn.setSortable(false);

        TableColumn<DownloadItem, String> statusColumn = new TableColumn<>("\u72b6\u6001");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setSortable(false);
        statusColumn.setCellFactory(column -> new StatusTableCell());

        TableColumn<DownloadItem, String> progressColumn = new TableColumn<>("\u8fdb\u5ea6");
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));
        progressColumn.setSortable(false);
        progressColumn.setCellFactory(column -> new ProgressTableCell());

        TableColumn<DownloadItem, String> speedColumn = new TableColumn<>("\u901f\u5ea6");
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        speedColumn.setSortable(false);
        speedColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("download-speed");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    getStyleClass().add("download-speed");
                }
            }
        });

        TableColumn<DownloadItem, String> actionColumn = new TableColumn<>("");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        actionColumn.setSortable(false);
        actionColumn.setReorderable(false);
        actionColumn.setResizable(false);
        actionColumn.setMinWidth(64);
        actionColumn.setPrefWidth(64);
        actionColumn.setMaxWidth(64);
        actionColumn.setCellFactory(column -> new ActionTableCell());

        downloadTable.getColumns().addAll(
            nameColumn,
            artistColumn,
            statusColumn,
            progressColumn,
            speedColumn,
            actionColumn
        );
        downloadTable.setRowFactory(table -> {
            TableRow<DownloadItem> row = new TableRow<>();
            row.setCursor(Cursor.HAND);
            return row;
        });

        summaryLabel.getStyleClass().add("download-summary-label");

        allDownloadItems.setAll(List.of(
            new DownloadItem("1", "\u504f\u7231", "\u5f20\u82b8\u4eac", STATUS_DOWNLOADING, "52%", "1.2 MB/s"),
            new DownloadItem("2", "\u7a3b\u9999", "\u5468\u6770\u4f26", STATUS_PAUSED, "30%", "0 B/s"),
            new DownloadItem("3", "\u591c\u66f2", "\u5468\u6770\u4f26", STATUS_COMPLETED, "100%", "--")
        ));

        applyFilter();
        refreshFilterButtons();
    }

    private void setupLayout() {
        Label titleLabel = new Label(PAGE_TITLE);
        titleLabel.getStyleClass().add("library-page-title");

        Label subtitleLabel = new Label(PAGE_SUBTITLE);
        subtitleLabel.getStyleClass().add("library-page-subtitle");

        VBox headerBox = new VBox(6, titleLabel, subtitleLabel);
        headerBox.getStyleClass().add("library-page-header");

        HBox filterGroup = new HBox(10, allFilterButton, downloadingFilterButton, pausedFilterButton, completedFilterButton);
        filterGroup.setAlignment(Pos.CENTER_LEFT);
        filterGroup.getStyleClass().add("library-filter-group");

        Region toolbarSpacer = new Region();
        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);

        HBox toolbar = new HBox(12, filterGroup, toolbarSpacer, summaryLabel);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("library-toolbar");

        VBox tableCard = new VBox(downloadTable);
        tableCard.getStyleClass().add("library-table-card");
        VBox.setVgrow(downloadTable, Priority.ALWAYS);

        getStyleClass().addAll("music-library-page", "download-panel", "download-music-page");
        setSpacing(18);
        setPadding(new Insets(18, 18, 16, 18));
        setFillWidth(true);
        setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        getChildren().setAll(headerBox, toolbar, tableCard);
    }

    private void loadStylesheet() {
        String stylesheet = Objects.requireNonNull(getClass().getResource("/css/LocalDownloadPage.css")).toExternalForm();
        if (!getStylesheets().contains(stylesheet)) {
            getStylesheets().add(stylesheet);
        }
    }

    private Button createFilterButton(String label, String filterValue) {
        Button button = new Button(label);
        button.getStyleClass().add("library-filter-button");
        button.setFocusTraversable(false);
        button.setOnAction(event -> {
            activeFilter = filterValue;
            applyFilter();
            refreshFilterButtons();
        });
        return button;
    }

    private Label createPlaceholderLabel() {
        Label placeholder = new Label(PLACEHOLDER_TEXT);
        placeholder.getStyleClass().add("table-placeholder");
        return placeholder;
    }

    private void applyFilter() {
        visibleDownloadItems.setAll(
            allDownloadItems.stream()
                .filter(this::matchesActiveFilter)
                .toList()
        );
        summaryLabel.setText(buildSummaryText());
    }

    private boolean matchesActiveFilter(DownloadItem item) {
        return switch (activeFilter) {
            case FILTER_DOWNLOADING -> STATUS_DOWNLOADING.equals(item.getStatus());
            case FILTER_PAUSED -> STATUS_PAUSED.equals(item.getStatus());
            case FILTER_COMPLETED -> STATUS_COMPLETED.equals(item.getStatus());
            default -> true;
        };
    }

    private void refreshFilterButtons() {
        updateFilterButton(allFilterButton, FILTER_ALL.equals(activeFilter));
        updateFilterButton(downloadingFilterButton, FILTER_DOWNLOADING.equals(activeFilter));
        updateFilterButton(pausedFilterButton, FILTER_PAUSED.equals(activeFilter));
        updateFilterButton(completedFilterButton, FILTER_COMPLETED.equals(activeFilter));
    }

    private void updateFilterButton(Button button, boolean active) {
        button.getStyleClass().remove("is-active");
        if (active) {
            button.getStyleClass().add("is-active");
        }
    }

    private String buildSummaryText() {
        long downloadingCount = allDownloadItems.stream().filter(item -> STATUS_DOWNLOADING.equals(item.getStatus())).count();
        long pausedCount = allDownloadItems.stream().filter(item -> STATUS_PAUSED.equals(item.getStatus())).count();
        long completedCount = allDownloadItems.stream().filter(item -> STATUS_COMPLETED.equals(item.getStatus())).count();

        return "\u5171 " + allDownloadItems.size()
            + " \u4e2a\u4efb\u52a1\uff0c"
            + "\u4e0b\u8f7d\u4e2d " + downloadingCount
            + "\uff0c\u5df2\u6682\u505c " + pausedCount
            + "\uff0c\u5df2\u5b8c\u6210 " + completedCount;
    }

    private double parseProgress(String value) {
        try {
            return Math.max(0, Math.min(1, Double.parseDouble(value.replace("%", "").trim()) / 100.0));
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public static class DownloadItem {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty artist;
        private final SimpleStringProperty status;
        private final SimpleStringProperty progress;
        private final SimpleStringProperty speed;

        public DownloadItem(String id, String name, String artist, String status, String progress, String speed) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.artist = new SimpleStringProperty(artist);
            this.status = new SimpleStringProperty(status);
            this.progress = new SimpleStringProperty(progress);
            this.speed = new SimpleStringProperty(speed);
        }

        public String getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getArtist() {
            return artist.get();
        }

        public String getStatus() {
            return status.get();
        }

        public String getProgress() {
            return progress.get();
        }

        public String getSpeed() {
            return speed.get();
        }
    }

    private class StatusTableCell extends TableCell<DownloadItem, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            Label badge = new Label(item);
            badge.getStyleClass().add("download-status-badge");
            if (STATUS_DOWNLOADING.equals(item)) {
                badge.getStyleClass().add("status-downloading");
            } else if (STATUS_PAUSED.equals(item)) {
                badge.getStyleClass().add("status-paused");
            } else {
                badge.getStyleClass().add("status-completed");
            }

            setText(null);
            setGraphic(badge);
            setAlignment(Pos.CENTER_LEFT);
        }
    }

    private class ProgressTableCell extends TableCell<DownloadItem, String> {
        private final HBox progressBox = new HBox(10);
        private final ProgressBar progressBar = new ProgressBar();
        private final Label progressLabel = new Label();

        private ProgressTableCell() {
            progressBar.getStyleClass().add("download-progress-bar");
            progressBar.setPrefHeight(6);
            HBox.setHgrow(progressBar, Priority.ALWAYS);

            progressLabel.getStyleClass().add("download-progress-text");

            progressBox.setAlignment(Pos.CENTER_LEFT);
            progressBox.getChildren().addAll(progressBar, progressLabel);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            progressLabel.setText(item);
            progressBar.setProgress(parseProgress(item));
            setText(null);
            setGraphic(progressBox);
            setAlignment(Pos.CENTER_LEFT);
        }
    }

    private static class ActionTableCell extends TableCell<DownloadItem, String> {
        private final Button actionButton = new Button("\u22ee");

        private ActionTableCell() {
            actionButton.getStyleClass().add("download-action-button");
            actionButton.setFocusTraversable(false);
            actionButton.setCursor(Cursor.HAND);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            setText(null);
            setGraphic(actionButton);
            setAlignment(Pos.CENTER);
        }
    }
}
