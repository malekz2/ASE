package productivityApp;

import java.time.LocalDate;

public class Task {
    private int id;
    private int userId;
    private String title;
    private LocalDate dueDate;
    private boolean completed;

    public Task(int id, int userId, String title, LocalDate dueDate, boolean completed) {
        this.id = id; this.userId = userId; this.title = title; this.dueDate = dueDate; this.completed = completed;
    }
    public int getId(){ return id; }
    public int getUserId(){ return userId; }
    public String getTitle(){ return title; }
    public LocalDate getDueDate(){ return dueDate; }
    public boolean isCompleted(){ return completed; }
    public void setCompleted(boolean v){ this.completed = v; }
}
