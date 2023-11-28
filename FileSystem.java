import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileSystem {

    private String baseDirectory;
    private Map<String, List<FileVersion>> fileSystem;
    private Map<String, Integer> currentVersion;
    private Map<String, Map<Integer, String>> versions;

    // Constructor: Initializes the file system with a base directory and creates necessary data structures
    public FileSystem(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.fileSystem = new HashMap<>();
        this.currentVersion = new HashMap<>();
        this.versions = new HashMap<>();

        File directory = new File(baseDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Get details of a file: Shows all versions with content and creation dates
    public void getDetails(String fileName) {
        if (fileSystem.containsKey(fileName)) {
            List<FileVersion> versions = fileSystem.get(fileName);
            for (FileVersion version : versions) {
                System.out.println("Version: " + version.getVersionNumber() +
                        ", Content: " + version.getContent() +
                        ", Created at: " + version.getCreationDate());
            }
        } else {
            System.out.println("File not found: " + fileName);
        }
    }

    // Create a file with user-defined content in a specified or present directory
    public void createFile(String fileName, String content) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to create the file in the present directory? (yes/no)");
        String choice = scanner.nextLine();

        File file;
        if ("yes".equalsIgnoreCase(choice)) {
            file = new File(baseDirectory, fileName);
        } else if ("no".equalsIgnoreCase(choice)) {
            System.out.println("Enter the path for the new directory:");
            String newDirectoryPath = scanner.nextLine();
            File newDirectory = new File(newDirectoryPath);
            newDirectory.mkdirs();
            file = new File(newDirectory, fileName);
        } else {
            System.out.println("Invalid choice. File will be created in the present directory.");
            file = new File(baseDirectory, fileName);
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileVersion fileVersion = new FileVersion(content, 1);
        fileSystem.put(fileName, new ArrayList<>(Collections.singletonList(fileVersion)));
        currentVersion.put(fileName, 2);
        versions.put(fileName, new HashMap<>(Map.of(1, content)));

        // Format the date for display
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        String formattedDate = dateFormat.format(fileVersion.getCreationDate());

        // Print statements for file creation without fileSystem and versions maps
        System.out.println("File '" + fileName + "' created as '" + file.getAbsolutePath() + "'.");
        System.out.println("Version: " + fileVersion.getVersionNumber() +
                ", Content: "    + fileVersion.getContent() +
                ", Created at: " + formattedDate);
    }

    // Edit the content of a file and create a new version with updated content and date
    public void editFile(String fileName, String newContent) {
        if (fileSystem.containsKey(fileName)) {
            int versionNumber = currentVersion.get(fileName);
            FileVersion fileVersion = new FileVersion(newContent, versionNumber);
            fileSystem.get(fileName).add(fileVersion);
            currentVersion.put(fileName, versionNumber + 1);
            versions.get(fileName).put(versionNumber, newContent);
    
            File file = new File(baseDirectory, fileName);
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                writer.write("\n" + newContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            // Format the date for display
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            String formattedDate = dateFormat.format(fileVersion.getCreationDate());
    
            // Print statements for the file update without fileSystem and versions maps
            System.out.println("File '" + fileName + "' content has been updated to version " + currentVersion.get(fileName) + ".");
            System.out.println("Version: " + fileVersion.getVersionNumber() +
                    ", Content: " + fileVersion.getContent() +
                    ", Created at: " + formattedDate);
        } else {
            System.out.println("File not found: " + fileName);
        }
    }

    // Delete a file and all its versions from the file system
    public void deleteFile(String fileName) {
        if (fileSystem.containsKey(fileName)) {
            File file = new File(baseDirectory, fileName);
            if (file.delete()) {
                fileSystem.remove(fileName);
                currentVersion.remove(fileName);
                versions.remove(fileName);

                // Format the date for display
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                Date deletionDate = new Date(); // Use the current date as the deletion date
                String formattedDate = dateFormat.format(deletionDate);

                // Print statements for the file deletion
                System.out.println("File deleted: " + fileName);
                System.out.println("Version: " + currentVersion.get(fileName) +
                        ", Deleted at: " + formattedDate);
            } else {
                System.out.println("Failed to delete file: " + fileName);
            }
        } else {
            System.out.println("File not found: " + fileName);
        }
    }

      // Revert a file to a specific version by replacing the current version with the selected version's content
      public void revertToVersion(String fileName, int versionNumber) {
        if (fileSystem.containsKey(fileName)) {
            if (versions.get(fileName).containsKey(versionNumber)) {
                String content = versions.get(fileName).get(versionNumber);
                FileVersion fileVersion = new FileVersion(content, versionNumber);
                fileSystem.get(fileName).add(fileVersion);
                currentVersion.put(fileName, versionNumber + 1);

                File file = new File(baseDirectory, fileName);
                try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                    writer.write("\n" + content); // Append content to the file
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Update the creation date in the versions map
                versions.get(fileName).put(versionNumber, content);
                
                // Print statements for the reverted version
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                String formattedDate = dateFormat.format(fileVersion.getCreationDate());

                System.out.println("File reverted to version " + versionNumber + ": " + fileName);
                System.out.println("Version: " + fileVersion.getVersionNumber() +
                        ", Content: " + fileVersion.getContent() +
                        ", Created at: " + formattedDate);
            } else {
                System.out.println("Version not found: " + versionNumber);
            }
        } else {
            System.out.println("File not found: " + fileName);
        }
    }

    // Copy a file to a specified destination directory
    public void copyFile(String fileName, String newPath) {
        if (fileSystem.containsKey(fileName)) {
            Path sourcePath = new File(baseDirectory, fileName).toPath();
            Path destinationDir = new File(newPath).toPath();

            if (!Files.exists(destinationDir)) {
                try {
                    Files.createDirectories(destinationDir);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to create destination directory: " + newPath);
                    return;
                }
            }

            Path destinationPath = destinationDir.resolve(fileName);

            try {
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied to: " + newPath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to copy file: " + fileName);
            }
        } else {
            System.out.println("File not found: " + fileName);
        }
    }

    // Move a file to a specified destination directory
    public void moveFile(String fileName, String newPath) {
        if (fileSystem.containsKey(fileName)) {
            File sourceFile = new File(baseDirectory, fileName);
            File destinationDir = new File(newPath);

            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }

            File destinationFile = new File(destinationDir, fileName);

            if (sourceFile.renameTo(destinationFile)) {
                fileSystem.remove(fileName);
                currentVersion.remove(fileName);
                versions.remove(fileName);
                System.out.println("File moved to: " + newPath);
            } else {
                System.out.println("Failed to move file: " + fileName);
            }
        } else {
            System.out.println("File not found: " + fileName);
        }
    }

    // Inner class representing a version of a file with content, version number, and creation date
    private static class FileVersion {
        private String content;
        private int versionNumber;
        private Date creationDate;

        public FileVersion(String content, int versionNumber) {
            this.content = content;
            this.versionNumber = versionNumber;
            this.creationDate = new Date();
        }

        public String getContent() {
            return content;
        }

        public int getVersionNumber() {
            return versionNumber;
        }

        public Date getCreationDate() {
            return creationDate;
        }
    }
}

