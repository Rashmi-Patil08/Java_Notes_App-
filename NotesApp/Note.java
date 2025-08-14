import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single note with title, content, and timestamp
 */
public class Note {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    
    // Constructor for new note
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
    
    // Constructor for loading existing note
    public Note(String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
    
    // Getters
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
    
    // Setters
    public void setTitle(String title) {
        this.title = title;
        this.modifiedAt = LocalDateTime.now();
    }
    
    public void setContent(String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }
    
    // Method to get formatted creation date
    public String getFormattedCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return createdAt.format(formatter);
    }
    
    // Method to get formatted modification date
    public String getFormattedModifiedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return modifiedAt.format(formatter);
    }
    
    // Method to convert note to file format
    public String toFileFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return "TITLE:" + title + "\n" +
               "CREATED:" + createdAt.format(formatter) + "\n" +
               "MODIFIED:" + modifiedAt.format(formatter) + "\n" +
               "CONTENT:\n" + content + "\n" +
               "---END_NOTE---\n";
    }
    
    @Override
    public String toString() {
        return "Title: " + title + "\n" +
               "Created: " + getFormattedCreatedDate() + "\n" +
               "Modified: " + getFormattedModifiedDate() + "\n" +
               "Content: " + content;
    }
}
