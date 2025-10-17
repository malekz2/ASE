package productivityApp;

import java.time.LocalDate;

public class JournalEntry {
    private final int id;
    private final int userId;
    private final LocalDate entryDate;
    private final String content;

    public JournalEntry(int id, int userId, LocalDate entryDate, String content) {
        this.id = id; this.userId = userId; this.entryDate = entryDate; this.content = content;
    }
    public int getId(){ return id; }
    public int getUserId(){ return userId; }
    public LocalDate getEntryDate(){ return entryDate; }
    public String getContent(){ return content; }

    @Override public String toString() {
        String snippet = content.replace("\n"," ");
        if (snippet.length() > 60) snippet = snippet.substring(0,57) + "...";
        return entryDate + " — " + snippet;
    }
}
