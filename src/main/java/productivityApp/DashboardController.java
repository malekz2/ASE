package productivityApp;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Map;

public class DashboardController {

    @FXML private Label lblWelcome;

    // Journal
    @FXML private DatePicker journalDate;
    @FXML private TextArea journalText;
    @FXML private ListView<JournalEntry> journalList;

    // Tasks
    @FXML private TextField taskTitle;
    @FXML private DatePicker taskDue;
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colDue;
    @FXML private TableColumn<Task, Boolean> colDone;

    // Charts
    @FXML private LineChart<String, Number> activityChart;
    @FXML private LineChart<String, Number> sentimentChart;

    private final TaskService taskService = new TaskService();
    private final JournalService journalService = new JournalService();

    @FXML
    public void initialize() {
        if (Session.isLoggedIn()) {
            lblWelcome.setText("Welcome, " + Session.get().getUsername());
        }
        journalDate.setValue(LocalDate.now());
        loadJournal();
        setupTasksTable();
        loadTasks();
        setupCharts();
        refreshCharts();
    }

    // ===== Journal =====
    private void loadJournal() {
        ObservableList<JournalEntry> entries = journalService.listEntries(Session.get().getId());
        journalList.setItems(entries);
    }

    @FXML
    private void saveJournal() {
        String content = journalText.getText();
        LocalDate date = journalDate.getValue() == null ? LocalDate.now() : journalDate.getValue();
        if (content == null || content.isBlank()) {
            alert("Validation", "Write something first.");
            return;
        }
        journalService.addEntry(Session.get().getId(), date, content.trim());
        journalText.clear();
        journalDate.setValue(LocalDate.now());
        loadJournal();
        // (Sentiment chart will be wired later)
    }

    // ===== Tasks =====
    private void setupTasksTable() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDue.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getDueDate() == null ? "" : cd.getValue().getDueDate().toString()
        ));

        colDone.setCellValueFactory(cd -> {
            Task t = cd.getValue();
            BooleanProperty prop = new SimpleBooleanProperty(t.isCompleted());
            prop.addListener((obs, oldV, newV) -> {
                taskService.setCompleted(t.getId(), newV);
                t.setCompleted(newV);
                refreshCharts();
            });
            return prop;
        });
        colDone.setCellFactory(CheckBoxTableCell.forTableColumn(colDone));

        // Optional: delete on DEL key
        taskTable.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DELETE -> {
                    Task sel = taskTable.getSelectionModel().getSelectedItem();
                    if (sel != null) {
                        taskService.deleteTask(sel.getId());
                        loadTasks();
                        refreshCharts();
                    }
                }
            }
        });
    }

    private void loadTasks() {
        taskTable.setItems(taskService.listTasks(Session.get().getId()));
    }

    @FXML
    private void addTask() {
        String title = taskTitle.getText();
        if (title == null || title.isBlank()) {
            alert("Validation", "Task title required.");
            return;
        }
        taskService.addTask(Session.get().getId(), title.trim(), taskDue.getValue());
        taskTitle.clear();
        taskDue.setValue(null);
        loadTasks();
    }

    // ===== Charts =====
    private void setupCharts() {
        activityChart.setCreateSymbols(true); // dots + line
        activityChart.setAnimated(false);
        sentimentChart.setCreateSymbols(true);
        sentimentChart.setAnimated(false);
    }

    private void refreshCharts() {
        activityChart.getData().clear();

        Map<LocalDate,Integer> counts = taskService.tasksCompletedLastNDays(Session.get().getId(), 14);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Completed tasks");

        counts.forEach((date, cnt) -> series.getData().add(new XYChart.Data<>(date.toString(), cnt)));
        activityChart.getData().add(series);

        // Sentiment chart placeholder (no data yet)
        sentimentChart.getData().clear();
    }

    // ===== Misc =====
    private void alert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(content); a.showAndWait();
    }

    @FXML
    private void logout() {
        Session.set(null);
        try {
            GradebookApplication.showLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
