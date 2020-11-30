// import sys_lib.FileLib;
import java.util.StringJoiner;
import java.util.InputMismatchException;
import java.util.Scanner;

class FileMenu {
  FileLib lib;

  public FileMenu(FileLib lib) {
    this.lib = lib;
  }

  public void print() {
    String menu = (
        "\nFILE MANAGEMENT PROGRAM\n" +
        "-----------------------\n" +
        "1.  Create a new file\n" +
        "2.  Open and read from a file\n" +
        "3.  Create and write to a new file\n" +
        "4.  Write to the end of a file\n" +
        "5.  Overwrite the content of a file\n" +
        "6.  Delete a file\n" +
        "7.  Create a directory\n" +
        "8.  Delete a directory\n" +
        "9.  Move a file or directory\n" +
        "10. Rename a file or directory\n" +
        "11. Show all files and directories\n" +
        "-----------------------\n" +
        "99. View kernel log\n" +
        "0.  Exit\n"
        );
    System.out.print(menu);
  }

  public void start() {
    boolean exit = false;
    int choice = -1;
    Scanner sc = new Scanner(System.in);

    do {
      this.print();

      choice = -1;
      System.out.print("\nEnter a number: ");
      do {
        try {
          choice = sc.nextInt();
        }
        catch (InputMismatchException ex) {
          System.out.println("Invalid input! Please enter a number in the menu: ");
          sc.next();
        }
        catch (Exception ex) {
          System.out.println("There was an error, try again: ");
          sc.next();
        }
      }
      while (choice == -1);

      try {
        switch(choice) {
          case 0:
            exit = true;
            System.out.println("Goodbye!");
            break;
          case 1:
            System.out.println("You chose: [1. Create a new file]");
            this.makeFile();
            this.delay();
            break;
          case 2:
            System.out.println("You chose: [2. Open and read from a file]");
            this.readFile();
            this.delay();
            break;
          case 3:
            System.out.println("You chose: [3. Create and write to a new file]");
            this.writeNewFile();
            this.delay();
            break;
          case 4:
            System.out.println("You chose: [4. Write to the end of a file]");
            this.appendFile();
            this.delay();
            break;
          case 5:
            System.out.println("You chose: [5. Overwrite the content of a file]");
            this.overwriteFile();
            this.delay();
            break;
          case 6:
            System.out.println("You chose: [6. Delete a file]");
            this.deleteFile();
            this.delay();
            break;
          case 7:
            System.out.println("You chose: [7. Create a directory]");
            this.makeDirectory();
            this.delay();
            break;
          case 8:
            System.out.println("You chose: [8. Delete a directory]");
            this.deleteDirectory();
            this.delay();
            break;
          case 9:
            System.out.println("You chose: [9. Move a file or directory]");
            this.moveFileOrDir();
            this.delay();
            break;
          case 10:
            System.out.println("You chose: [10. Rename a file or directory]");
            this.renameFileOrDir();
            this.delay();
            break;
          case 11:
            System.out.println("You chose: [11. Show all files and directories]");
            this.showFileTree();
            this.delay();
            break;
          case 99:
            System.out.println("You chose: [99. View kernel log]");
            this.showKernelLog();
            this.delay();
            break;
          default: 
            System.out.println("The number you entered is not in the menu!");
            this.delay(10);
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("There was an error.");
      }
    }
    while (!exit);
  }
  
  private void makeFile() throws Exception {
    String fileName = this.inputItemName("file");
    if (fileName.equals("!END")) return;
    
    if (lib.fileExists(fileName)) {
      System.err.println("Error creating file. The file already exists.");
    }
    else {
      long fd = lib.openFile(fileName);
      if (fd < 0) {
        System.err.println("Error opening file.");
      }
      else {
        lib.closeFile(fd);
      }
    }
  }

  private void readFile() throws Exception {
    String fileName = this.inputItemName("file");
    if (fileName.equals("!END")) return;
    
    if (!lib.fileExists(fileName)) {
      System.err.println("Error opening file. Make sure the file exists.");
    }
    else {
      long fd = lib.openFile(fileName);
      if (fd < 0) {
        System.err.println("Error opening file.");
      }
      else {
        String content = lib.readFile(fd);
        System.out.println(content);
        lib.closeFile(fd);
      }
    }
  }

  private void writeNewFile() throws Exception {
    String fileName = this.inputItemName("file");
    if (fileName.equals("!END")) return;
    
    if (lib.fileExists(fileName)) {
      System.err.println("Error creating file. The file already exists.");
    }
    else {
      long fd = lib.openFile(fileName);
      if (fd < 0) {
        System.err.println("Error opening file.");
      }
      else {
        String content = this.readFromTermnl();
        lib.writeFile(fd, content);
        lib.closeFile(fd);
      }
    }
  }

  private void appendFile() throws Exception {
    String fileName = this.inputItemName("file");
    if (fileName.equals("!END")) return;
    
    if (!lib.fileExists(fileName)) {
      System.err.println("Error opening file. Make sure the file exists.");
    }
    else {
      long fd = lib.openFile(fileName);
      if (fd < 0) {
        System.err.println("Error opening file.");
      }
      else {
        String content = "\n" + this.readFromTermnl();
        lib.appendFile(fd, content);
        lib.closeFile(fd);
      }
    }
  }

  private void overwriteFile() throws Exception {
    String fileName = this.inputItemName("file");
    if (fileName.equals("!END")) return;
    
    if (!lib.fileExists(fileName)) {
      System.err.println("Error opening file. Make sure the file exists.");
    }
    else {
      lib.deleteFile(fileName);
      long fd = lib.openFile(fileName);
      if (fd < 0) {
        System.err.println("Error opening file.");
      }
      else {
        String content = this.readFromTermnl();
        lib.writeFile(fd, content);
        lib.closeFile(fd);
      }
    }
  }

  private void deleteFile() throws Exception {
    String fileName = this.inputItemName("file");
    if (fileName.equals("!END")) return;
    
    if (!lib.fileExists(fileName)) {
      System.err.println("Error deleting file. Make sure the file exists.");
    }
    else {
      lib.deleteFile(fileName);
    }
  }

  private void makeDirectory() throws Exception {
    String dirName = this.inputItemName("directory");
    if (dirName.equals("!END")) return;
    
    if (lib.dirExists(dirName)) {
      System.err.println("Error making directory. The directory already exists.");
    }
    else {
      lib.createDir(dirName);
    }
  }

  private void deleteDirectory() throws Exception {
    String dirName = this.inputItemName("directory");
    if (dirName.equals("!END")) return;
    
    if (!lib.dirExists(dirName)) {
      System.err.println("Error deleting directory. Make sure the directory exists.");
    }
    else {
      lib.deleteDirUnsafe(dirName);
    }
  }

  private void moveFileOrDir() throws Exception {
    String fullpath = this.inputItemName("full-path file or directory");
    if (fullpath.equals("!END")) return;
    
    if (!lib.dirExists(fullpath) && !lib.fileExists(fullpath)) {
      System.err.println("Error moving file/directory. Make sure the file or directory exists.");
    }
    else {
      String pr = lib.getParentName(fullpath);
      String name = lib.getItemName(fullpath);

      System.out.print("Enter new location (directory name): ");
      String newParent = new Scanner(System.in).nextLine().trim();
      String newPath = newParent + "/" + name;

      lib.moveFile(fullpath, newPath);
    }
  }

  private void renameFileOrDir() throws Exception {
    String fullpath = this.inputItemName("full-path file or directory");
    if (fullpath.equals("!END")) return;
    
    if (!lib.dirExists(fullpath) && !lib.fileExists(fullpath)) {
      System.err.println("Error renaming file/directory. Make sure the file or directory exists.");
    }
    else {
      String pr = lib.getParentName(fullpath);
      String name = lib.getItemName(fullpath);

      System.out.print("Enter new file or directory name: ");
      String newName = new Scanner(System.in).nextLine().trim();
      String newPath = pr + newName;

      lib.renameFile(fullpath, newPath);
    }
  }

  private void showFileTree() throws Exception {
    System.out.println("Showing file tree:");
    lib.showTree();
  }

  private void showKernelLog() throws Exception {
    System.out.println("Showing kernel log (to view system calls invoked):");
    lib.printKernelLog(8);
  }

  private String inputItemName(String type) {
    System.out.print("Enter " + type + " name (type !END to cancel): ");
    String itemName = new Scanner(System.in).nextLine();
    return itemName.trim();
  }

  private String readFromTermnl() {
    var sc = new Scanner(System.in);
    String iline = "";
    var input = new StringJoiner("\n");
    System.out.println("Enter the content of the file (type !END to stop inputting)");

    do {
      iline = sc.nextLine();
      if (!iline.equals("!END")) {
        input.add(iline);
      }
    }
    while (!iline.equals("!END"));

    return input.toString();
  }

  private void delay() {
    System.out.print("\nPress [Enter] to continue...");
    new Scanner(System.in).nextLine();
  }

  private void delay(int dots) {
    for (int i = 0; i < dots; i++) {
      System.out.print(". ");
      try {
        Thread.sleep(200);
      }
      catch (InterruptedException ex) {
        System.out.println(ex.getMessage());
      }
    }

    try {
      Thread.sleep(500);
    }
    catch (InterruptedException ex) {
      System.out.println(ex.getMessage());
    }
    System.out.println();
  }
}

