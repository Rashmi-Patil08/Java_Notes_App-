import java.util.List;
import java.util.Scanner;

/**
 * Main class for the Notes App - Text-based Notes Manager
 * Demonstrates File I/O, Exception Handling, and Try-with-resources
 */
public class NotesApp {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to Java Notes App ===");
        System.out.println("A File I/O demonstration with exception handling");
        
        while (true) {
            try {
                displayMenu();
                int choice = getChoice();
                
                switch (choice) {
                    case 1:
                        createNewNote();
                        break;
                    case 2:
                        viewAllNotes();
                        break;
                    case 3:
                        readSpecificNote();
                        break;
                    case 4:
                        editNote();
                        break;
                    case 5:
                        deleteNote();
                        break;
                    case 6:
                        appendToNote();
                        break;
                    case 7:
                        exportNotes();
                        break;
                    case 8:
                        searchNotes();
                        break;
                    case 9:
                        showStatistics();
                        break;
                    case 10:
                        demonstrateExceptionHandling();
                        break;
                    case 0:
                        System.out.println("Thank you for using Notes App!");
                        System.out.println("All your notes are safely saved to files.");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
                
            } catch (Exception e) {
                // Top-level exception handling - demonstrates exception propagation
                System.err.println("An unexpected error occurred: " + e.getMessage());
                System.err.println("Stack trace:");
                e.printStackTrace();
                System.out.println("The application will continue running...");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("              NOTES APP - FILE I/O DEMO");
        System.out.println("=".repeat(60));
        System.out.println("1.  Create New Note");
        System.out.println("2.  View All Notes");
        System.out.println("3.  Read Specific Note");
        System.out.println("4.  Edit Note");
        System.out.println("5.  Delete Note");
        System.out.println("6.  Append to Note (Demonstrate Append Mode)");
        System.out.println("7.  Export All Notes");
        System.out.println("8.  Search Notes");
        System.out.println("9.  Show App Statistics");
        System.out.println("10. Demonstrate Exception Handling");
        System.out.println("0.  Exit");
        System.out.println("=".repeat(60));
        System.out.print("Enter your choice: ");
    }
    
    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            // Demonstrates unchecked exception handling
            System.out.println("Invalid input! Please enter a number.");
            return -1;
        }
    }
    
    private static void createNewNote() {
        System.out.println("\n=== Create New Note ===");
        
        try {
            System.out.print("Enter note title: ");
            String title = scanner.nextLine().trim();
            
            if (title.isEmpty()) {
                throw new IllegalArgumentException("Note title cannot be empty!");
            }
            
            System.out.println("Enter note content (type 'END' on a new line to finish):");
            StringBuilder content = new StringBuilder();
            String line;
            
            while (!(line = scanner.nextLine()).equals("END")) {
                content.append(line).append("\n");
            }
            
            Note note = new Note(title, content.toString().trim());
            
            // Save note using FileWriter (overwrite mode)
            if (FileManager.saveNote(note, false)) {
                System.out.println("✓ Note created and saved successfully!");
                System.out.println("File: " + title + ".txt");
            } else {
                System.out.println("✗ Failed to save note. Check the logs for details.");
            }
            
        } catch (IllegalArgumentException e) {
            // Demonstrates custom exception handling
            System.err.println("Input Error: " + e.getMessage());
        } catch (Exception e) {
            // Catch any other exceptions
            System.err.println("Error creating note: " + e.getMessage());
        }
    }
    
    private static void viewAllNotes() {
        System.out.println("\n=== All Notes ===");
        
        try {
            List<Note> notes = FileManager.readAllNotes();
            
            if (notes.isEmpty()) {
                System.out.println("No notes found. Create your first note!");
                return;
            }
            
            System.out.println("Found " + notes.size() + " note(s):\n");
            
            for (int i = 0; i < notes.size(); i++) {
                System.out.println("--- Note " + (i + 1) + " ---");
                System.out.println(notes.get(i));
                System.out.println();
            }
            
        } catch (Exception e) {
            System.err.println("Error reading notes: " + e.getMessage());
        }
    }
    
    private static void readSpecificNote() {
        System.out.println("\n=== Read Specific Note ===");
        
        try {
            System.out.print("Enter note title: ");
            String title = scanner.nextLine().trim();
            
            String fileName = "notes/" + sanitizeFileName(title) + ".txt";
            Note note = FileManager.readNote(fileName);
            
            if (note != null) {
                System.out.println("\n--- Note Found ---");
                System.out.println(note);
            } else {
                System.out.println("Note not found: " + title);
            }
            
        } catch (Exception e) {
            System.err.println("Error reading note: " + e.getMessage());
        }
    }
    
    private static void editNote() {
        System.out.println("\n=== Edit Note ===");
        
        try {
            System.out.print("Enter note title to edit: ");
            String title = scanner.nextLine().trim();
            
            String fileName = "notes/" + sanitizeFileName(title) + ".txt";
            Note note = FileManager.readNote(fileName);
            
            if (note == null) {
                System.out.println("Note not found: " + title);
                return;
            }
            
            System.out.println("\nCurrent note:");
            System.out.println(note);
            
            System.out.println("\nEnter new content (type 'END' on a new line to finish):");
            StringBuilder newContent = new StringBuilder();
            String line;
            
            while (!(line = scanner.nextLine()).equals("END")) {
                newContent.append(line).append("\n");
            }
            
            note.setContent(newContent.toString().trim());
            
            // Save the edited note (overwrite mode)
            if (FileManager.saveNote(note, false)) {
                System.out.println("✓ Note updated successfully!");
            } else {
                System.out.println("✗ Failed to update note.");
            }
            
        } catch (Exception e) {
            System.err.println("Error editing note: " + e.getMessage());
        }
    }
    
    private static void deleteNote() {
        System.out.println("\n=== Delete Note ===");
        
        try {
            System.out.print("Enter note title to delete: ");
            String title = scanner.nextLine().trim();
            
            System.out.print("Are you sure you want to delete '" + title + "'? (y/N): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            
            if (confirmation.equals("y") || confirmation.equals("yes")) {
                if (FileManager.deleteNote(title)) {
                    System.out.println("✓ Note deleted successfully!");
                } else {
                    System.out.println("✗ Failed to delete note or note not found.");
                }
            } else {
                System.out.println("Delete operation cancelled.");
            }
            
        } catch (Exception e) {
            System.err.println("Error deleting note: " + e.getMessage());
        }
    }
    
    private static void appendToNote() {
        System.out.println("\n=== Append to Note (Demonstrates Append Mode) ===");
        
        try {
            System.out.print("Enter note title to append to: ");
            String title = scanner.nextLine().trim();
            
            String fileName = "notes/" + sanitizeFileName(title) + ".txt";
            Note existingNote = FileManager.readNote(fileName);
            
            if (existingNote == null) {
                System.out.println("Note not found: " + title);
                return;
            }
            
            System.out.println("Enter content to append (type 'END' on a new line to finish):");
            StringBuilder appendContent = new StringBuilder();
            String line;
            
            while (!(line = scanner.nextLine()).equals("END")) {
                appendContent.append(line).append("\n");
            }
            
            // Create a new note with appended content
            Note appendNote = new Note(title, appendContent.toString().trim());
            
            // Save using append mode - demonstrates difference between append and overwrite
            if (FileManager.saveNote(appendNote, true)) {
                System.out.println("✓ Content appended successfully!");
                System.out.println("Note: This demonstrates append mode vs overwrite mode.");
            } else {
                System.out.println("✗ Failed to append content.");
            }
            
        } catch (Exception e) {
            System.err.println("Error appending to note: " + e.getMessage());
        }
    }
    
    private static void exportNotes() {
        System.out.println("\n=== Export All Notes ===");
        
        try {
            System.out.print("Enter export file name (without extension): ");
            String fileName = scanner.nextLine().trim();
            
            if (fileName.isEmpty()) {
                fileName = "notes_export_" + System.currentTimeMillis();
            }
            
            fileName += ".txt";
            
            if (FileManager.exportAllNotes(fileName)) {
                System.out.println("✓ Notes exported successfully to: " + fileName);
            } else {
                System.out.println("✗ Failed to export notes.");
            }
            
        } catch (Exception e) {
            System.err.println("Error exporting notes: " + e.getMessage());
        }
    }
    
    private static void searchNotes() {
        System.out.println("\n=== Search Notes ===");
        
        try {
            System.out.print("Enter search term: ");
            String searchTerm = scanner.nextLine().trim().toLowerCase();
            
            List<Note> allNotes = FileManager.readAllNotes();
            List<Note> matchingNotes = allNotes.stream()
                .filter(note -> note.getTitle().toLowerCase().contains(searchTerm) ||
                               note.getContent().toLowerCase().contains(searchTerm))
                .toList();
            
            if (matchingNotes.isEmpty()) {
                System.out.println("No notes found containing: " + searchTerm);
            } else {
                System.out.println("Found " + matchingNotes.size() + " matching note(s):\n");
                for (int i = 0; i < matchingNotes.size(); i++) {
                    System.out.println("--- Match " + (i + 1) + " ---");
                    System.out.println(matchingNotes.get(i));
                    System.out.println();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error searching notes: " + e.getMessage());
        }
    }
    
    private static void showStatistics() {
        System.out.println("\n" + FileManager.getAppStatistics());
    }
    
    private static void demonstrateExceptionHandling() {
        System.out.println("\n=== Exception Handling Demonstration ===");
        System.out.println("This demonstrates various types of exceptions:");
        
        System.out.println("\n1. Checked Exception (IOException):");
        try {
            // Try to read a non-existent file
            Note note = FileManager.readNote("non_existent_file.txt");
            System.out.println("This won't be printed if file doesn't exist");
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
        }
        
        System.out.println("\n2. Unchecked Exception (NumberFormatException):");
        try {
            int number = Integer.parseInt("not_a_number");
        } catch (NumberFormatException e) {
            System.out.println("Caught NumberFormatException: " + e.getMessage());
        }
        
        System.out.println("\n3. Exception Propagation:");
        try {
            methodThatThrowsException();
        } catch (Exception e) {
            System.out.println("Exception propagated to main method: " + e.getMessage());
            System.out.println("Stack trace shows the call chain:");
            e.printStackTrace();
        }
        
        System.out.println("\n4. Finally Block Demonstration:");
        demonstrateFinallyBlock();
        
        System.out.println("\nException handling demonstration completed!");
    }
    
    private static void methodThatThrowsException() throws Exception {
        // This method demonstrates exception propagation
        throw new Exception("This exception was thrown from methodThatThrowsException()");
    }
    
    private static void demonstrateFinallyBlock() {
        System.out.println("Entering finally block demonstration...");
        
        try {
            System.out.println("In try block");
            throw new RuntimeException("Test exception for finally demo");
        } catch (Exception e) {
            System.out.println("In catch block: " + e.getMessage());
        } finally {
            System.out.println("In finally block - this ALWAYS executes!");
            System.out.println("Finally blocks are used for cleanup operations.");
        }
    }
    
    private static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
