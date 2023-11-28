# FileSystem Java Program

This Java program implements a simple version control system for files, allowing users to create, edit, delete, copy, move, and revert to previous versions of files. The system maintains version history, including content and creation dates.

## Usage

1. **Create a File:**
   - Users can create a new file, specifying the file name and initial content. They are prompted to choose whether to create the file in the present directory or a new directory.

2. **Edit a File:**
   - Users can edit an existing file by providing a new content. The program appends the new content to the file and updates the version history.

3. **Delete a File:**
   - Users can delete an existing file. The program removes the file and its version history.

4. **Revert to a Previous Version:**
   - Users can revert a file to a previous version by specifying the version number. The program appends the previous content to the file and updates the version history.

5. **Copy a File:**
   - Users can copy an existing file to a specified destination directory. The program creates a copy of the file in the destination directory.

6. **Move a File:**
   - Users can move an existing file to a specified destination directory. The program moves the file and updates the version history.

## Implementation Details

- The program uses a `FileSystem` class with maps to manage files, versions, and current versions.
- File versions are stored as instances of the inner `FileVersion` class, which includes content, version number, and creation date.
- The program handles file creation, editing, deletion, copying, and moving, ensuring proper version tracking.

## How to Run

Compile the program and run the main class (`FileSystem`) from the command line or an integrated development environment (IDE).

```bash
javac FileSystem.java
java FileSystem
```

Follow the on-screen prompts to interact with the file system.

## Note

- Ensure that you have the necessary permissions to create, modify, and delete files and directories in the specified base directory.

Feel free to explore and modify the program to suit your needs!
