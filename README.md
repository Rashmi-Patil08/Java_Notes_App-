Java File I/O Notes App - Task 4
📋 Task Overview
This is Task 4 of the Java Developer Internship program, focused on implementing a text-based Notes Manager using Java File I/O operations and comprehensive exception handling.
🎯 Objective
Create a text-based notes manager with file read/write capabilities, demonstrating File I/O concepts and exception handling techniques.
🛠️ Tools Used

Language: Java
IDE: VS Code
Terminal: Command line interface for compilation and execution
File I/O Classes: FileReader, BufferedReader, FileWriter, BufferedWriter

📁 Project Structure
java-notes-app/
├── Note.java
├── FileManager.java
├── NotesApp.java
├── README.md
└── notes/                  # Auto-created directory
    ├── app.log            # Application logs
    ├── notes_index.txt    # Notes index file
    └── [note_files].txt   # Individual note files
🔧 Classes Implementation
1. Note Class

Purpose: Represents a single note with metadata
Attributes: title, content, createdAt, modifiedAt
Methods:

Date formatting methods
File format conversion
toString() override


Concepts: Encapsulation, Object modeling

2. FileManager Class

Purpose: Handles all file I/O operations
Key Methods:

saveNote() - Demonstrates FileWriter usage
readNote() - Demonstrates BufferedReader usage
readAllNotes() - Multiple file reading
exportAllNotes() - Batch file operations


Concepts: File I/O, Exception handling, Logging

3. NotesApp Class (Main)

Purpose: Interactive menu-driven application
Features: Complete CRUD operations for notes
Concepts: User interface, Exception propagation

🚀 Features Implemented
Core File I/O Features

✅ Create Notes: Write to files using FileWriter
✅ Read Notes: Read from files using FileReader/BufferedReader
✅ Edit Notes: Update existing files
✅ Delete Notes: File deletion operations
✅ Append Mode: Demonstrate append vs overwrite
✅ Export Function: Combine multiple files into one

Advanced Features

Search Functionality: Content-based note searching
File Indexing: Maintain index of all notes
Statistics: File size and count information
Exception Logging: Comprehensive error logging
Auto-directory Creation: Dynamic folder management

📚 File I/O Concepts Demonstrated
1. FileWriter vs FileReader
java// Writing to file
try (FileWriter writer = new FileWriter(fileName, append)) {
    writer.write(content);
}

// Reading from file
try (FileReader reader = new FileReader(fileName)) {
    // File reading logic
}
2. BufferedReader Benefits
java// Efficient reading with BufferedReader
try (FileReader fileReader = new FileReader(fileName);
     BufferedReader bufferedReader = new BufferedReader(fileReader)) {
    
    String line;
    while ((line = bufferedReader.readLine()) != null) {
        // Process line by line
    }
}
3. Try-with-Resources

Automatic resource management
Ensures files are properly closed
Prevents resource leaks
Cleaner exception handling

4. Append vs Overwrite Mode
java// Overwrite mode (default)
new FileWriter(fileName, false)

// Append mode
new FileWriter(fileName, true)
🔥 Exception Handling Demonstrated
1. Checked Exceptions

IOException: File operation failures
FileNotFoundException: Missing files
Proper try-catch blocks with specific handling

2. Unchecked Exceptions

NumberFormatException: Invalid input parsing
IllegalArgumentException: Invalid parameters
Runtime exception handling

3. Exception Propagation

Method throws declarations
Exception bubbling up the call stack
Stack trace analysis

4. Finally Block Usage

Cleanup operations
Resource deallocation
Guaranteed execution scenarios

5. Exception Logging

Using Java Logger for error tracking
Log levels (INFO, WARNING, SEVERE)
File-based logging system

💻 How to Run
Prerequisites

Java Development Kit (JDK) 8 or higher
VS Code or any Java IDE
Command line access

Compilation and Execution
bash# Compile all Java files
javac *.java

# Run the notes application
java NotesApp
Sample Usage Flow

Start Application: Run java NotesApp
Create Notes: Choose option 1 to create new notes
View Notes: Choose option 2 to see all saved notes
Edit/Delete: Modify existing notes (options 4-5)
Test Append Mode: Use option 6 to see append vs overwrite
Export: Use option 7 to combine all notes
Exception Demo: Use option 10 to see exception handling
