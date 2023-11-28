import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize the file system in a specific directory
        FileSystem fs = new FileSystem("my_file_system");

        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("\nFile System Menu:");
            System.out.println("1. Create a new file");
            System.out.println("2. Edit a file");
            System.out.println("3. Get file details");
            System.out.println("4. Revert to a previous version");
            System.out.println("5. Copy a file to a different directory");
            System.out.println("6. Move a file to a different directory");
            System.out.println("7. Delete a file");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter file name: ");
                    String createFileName = scanner.nextLine();
                    System.out.print("Enter initial content: ");
                    String createFileContent = scanner.nextLine();
                    fs.createFile(createFileName, createFileContent);
                    break;
                case 2:
                    System.out.print("Enter file name: ");
                    String editFileName = scanner.nextLine();
                    System.out.print("Enter updated content: ");
                    String editFileContent = scanner.nextLine();
                    fs.editFile(editFileName, editFileContent);
                    break;
                case 3:
                    System.out.print("Enter file name: ");
                    String detailsFileName = scanner.nextLine();
                    fs.getDetails(detailsFileName);
                    break;
                case 4:
                    System.out.print("Enter file name: ");
                    String revertFileName = scanner.nextLine();
                    System.out.print("Enter version number to revert to: ");
                    int revertVersionNumber = scanner.nextInt();
                    fs.revertToVersion(revertFileName, revertVersionNumber);
                    break;
                case 5:
                    System.out.print("Enter file name: ");
                    String copyFileName = scanner.nextLine();
                    System.out.print("Enter destination directory: ");
                    String copyDestinationDir = scanner.nextLine();
                    fs.copyFile(copyFileName, copyDestinationDir);
                    break;
                case 6:
                    System.out.print("Enter file name: ");
                    String moveFileName = scanner.nextLine();
                    System.out.print("Enter destination directory: ");
                    String moveDestinationDir = scanner.nextLine();
                    fs.moveFile(moveFileName, moveDestinationDir);
                    break;
                case 7:
                    System.out.print("Enter file name: ");
                    String deleteFileName = scanner.nextLine();
                    fs.deleteFile(deleteFileName);
                    break;
                case 0:
                    System.out.println("Exiting File System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
