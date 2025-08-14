import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Handles all file I/O operations for the Notes App
 * Demonstrates FileReader, BufferedReader, FileWriter concepts
 */
public class FileManager {
    private static final String NOTES_DIRECTORY = "notes";
    private static final String NOTES_INDEX_FILE = "notes/notes_index.txt";
    private static final String LOG_FILE = "notes/app.log";
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());
    
    static {
        setupLogger();
        createNotesDirectory();
    }
    
    /**
     * Setup logger for exception logging
     */
    private static void setupLogger() {
        try {
            // Create notes directory if it doesn't exist
            File notesDir = new File(NOTES_DIRECTORY);
            if (!notesDir.exists()) {
                notesDir.mkdirs();
            }
            
            FileHandler fileHandler = new FileHandler(LOG_FILE, true); // true for append mode
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Don't log to console
        } catch (IOException e) {
            System.err.println("Failed to setup logger: " + e.getMessage());
        }
    }
    
    /**
     * Create notes directory if it doesn't exist
     */
    private static void createNotesDirectory() {
        File notesDir = new File(NOTES_DIRECTORY);
        if (!notesDir.exists()) {
            boolean created = notesDir.mkdirs();
            if (created) {
                System.out.println("Created notes directory: " + NOTES_DIRECTORY);
            }
        }
    }
    
    /**
     * Save a note to file using FileWriter (demonstrates append vs overwrite)
     * @param note The note to save
     * @param append Whether to append to existing file or overwrite
     * @return true if successful, false otherwise
     */
    public static boolean saveNote(Note note, boolean append) {
        String fileName = NOTES_DIRECTORY + "/" + sanitizeFileName(note.getTitle()) + ".txt";
        
        // Try-with-resources for automatic resource management
        try (FileWriter writer = new FileWriter(fileName, append);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            
            if (append) {
                bufferedWriter.write("\n=== APPENDED CONTENT ===\n");
                bufferedWriter.write("Appended at: " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n");
            }
            
            bufferedWriter.write(note.toFileFormat());
            bufferedWriter.flush(); // Ensure data is written to file
            
            // Update index file
            updateNotesIndex(note.getTitle(), fileName);
            
            logger.info("Note saved successfully: " + fileName + " (append: " + append + ")");
            return true;
            
        } catch (IOException e) {
            // Exception handling and logging
            String errorMsg = "Failed to save note: " + note.getTitle() + " - " + e.getMessage();
            logger.severe(errorMsg);
            System.err.println(errorMsg);
            e.printStackTrace(); // Print stack trace
            return false;
        }
    }
    
    /**
     * Read a note from file using BufferedReader
     * @param fileName The file name to read from
     * @return Note object or null if failed
     */
    public static Note readNote(String fileName) {
        // Try-with-resources for FileReader and BufferedReader
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            
            String title = null;
            LocalDateTime created = null;
            LocalDateTime modified = null;
            StringBuilder content = new StringBuilder();
            String line;
            boolean readingContent = false;
            
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("TITLE:")) {
                    title = line.substring(6); // Remove "TITLE:" prefix
                } else if (line.startsWith("CREATED:")) {
                    created = LocalDateTime.parse(line.substring(8),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                } else if (line.startsWith("MODIFIED:")) {
                    modified = LocalDateTime.parse(line.substring(9),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                } else if (line.equals("CONTENT:")) {
                    readingContent = true;
                } else if (line.equals("---END_NOTE---")) {
                    break;
                } else if (readingContent) {
                    content.append(line).append("\n");
                }
            }
            
            if (title != null && created != null && modified != null) {
                logger.info("Note read successfully: " + fileName);
                return new Note(title, content.toString().trim(), created, modified);
            } else {
                throw new IOException("Invalid note file format");
            }
            
        } catch (FileNotFoundException e) {
            String errorMsg = "Note file not found: " + fileName;
            logger.warning(errorMsg);
            System.err.println(errorMsg);
            return null;
        } catch (IOException e) {
            String errorMsg = "Failed to read note: " + fileName + " - " + e.getMessage();
            logger.severe(errorMsg);
            System.err.println(errorMsg);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Read all notes using different reading approaches
     * @return List of all notes
     */
    public static List<Note> readAllNotes() {
        List<Note> notes = new ArrayList<>();
        
        try (FileReader indexReader = new FileReader(NOTES_INDEX_FILE);
             BufferedReader bufferedReader = new BufferedReader(indexReader)) {
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String fileName = parts[1].trim();
                        Note note = readNote(fileName);
                        if (note != null) {
                            notes.add(note);
                        }
                    }
                }
            }
            
        } catch (FileNotFoundException e) {
            logger.info("Notes index file not found. No notes exist yet.");
        } catch (IOException e) {
            String errorMsg = "Failed to read notes index: " + e.getMessage();
            logger.severe(errorMsg);
            System.err.println(errorMsg);
        }
        
        return notes;
    }
    
    /**
     * Delete a note file
     * @param title The title of the note to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteNote(String title) {
        String fileName = NOTES_DIRECTORY + "/" + sanitizeFileName(title) + ".txt";
        File file = new File(fileName);
        
        try {
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    removeFromNotesIndex(title);
                    logger.info("Note deleted successfully: " + fileName);
                    return true;
                } else {
                    throw new IOException("Failed to delete file");
                }
            } else {
                System.out.println("Note file does not exist: " + title);
                return false;
            }
        } catch (Exception e) {
            String errorMsg = "Failed to delete note: " + title + " - " + e.getMessage();
            logger.severe(errorMsg);
            System.err.println(errorMsg);
            return false;
        }
    }
    
    /**
     * Update the notes index file
     * @param title Note title
     * @param fileName File path
     */
    private static void updateNotesIndex(String title, String fileName) {
        try (FileWriter writer = new FileWriter(NOTES_INDEX_FILE, true)) {
            writer.write(title + ":" + fileName + "\n");
        } catch (IOException e) {
            logger.warning("Failed to update notes index: " + e.getMessage());
        }
    }
    
    /**
     * Remove note from index file
     * @param title Note title to remove
     */
    private static void removeFromNotesIndex(String title) {
        List<String> lines = new ArrayList<>();
        
        // Read all lines except the one to remove
        try (BufferedReader reader = new BufferedReader(new FileReader(NOTES_INDEX_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(title + ":")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            logger.warning("Failed to read index for removal: " + e.getMessage());
            return;
        }
        
        // Write back the remaining lines
        try (FileWriter writer = new FileWriter(NOTES_INDEX_FILE, false)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            logger.warning("Failed to update index after removal: " + e.getMessage());
        }
    }
    
    /**
     * Export all notes to a single file
     * @param exportFileName The export file name
     * @return true if successful
     */
    public static boolean exportAllNotes(String exportFileName) {
        List<Note> notes = readAllNotes();
        
        try (FileWriter writer = new FileWriter(NOTES_DIRECTORY + "/" + exportFileName);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            
            bufferedWriter.write("=== NOTES EXPORT ===\n");
            bufferedWriter.write("Export Date: " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n");
            bufferedWriter.write("Total Notes: " + notes.size() + "\n\n");
            
            for (int i = 0; i < notes.size(); i++) {
                bufferedWriter.write("NOTE " + (i + 1) + ":\n");
                bufferedWriter.write(notes.get(i).toFileFormat());
                bufferedWriter.write("\n");
            }
            
            logger.info("Notes exported successfully to: " + exportFileName);
            return true;
            
        } catch (IOException e) {
            String errorMsg = "Failed to export notes: " + e.getMessage();
            logger.severe(errorMsg);
            System.err.println(errorMsg);
            return false;
        }
    }
    
    /**
     * Get application statistics
     * @return Statistics string
     */
    public static String getAppStatistics() {
        List<Note> notes = readAllNotes();
        File notesDir = new File(NOTES_DIRECTORY);
        File[] files = notesDir.listFiles();
        int totalFiles = files != null ? files.length : 0;
        
        long totalSize = 0;
        if (files != null) {
            for (File file : files) {
                totalSize += file.length();
            }
        }
        
        return "=== App Statistics ===\n" +
               "Total Notes: " + notes.size() + "\n" +
               "Total Files: " + totalFiles + "\n" +
               "Total Storage Used: " + totalSize + " bytes\n" +
               "Notes Directory: " + new File(NOTES_DIRECTORY).getAbsolutePath();
    }
    
    /**
     * Sanitize file name to remove invalid characters
     * @param fileName Original file name
     * @return Sanitized file name
     */
    private static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}