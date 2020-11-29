import java.io.File;

import java.util.Scanner;

// import sys_lib.FileLib;

class FileMenu {
  FileLib lib;

  public FileMenu(FileLib lib) {
    this.lib = lib;
  }

  public void print() {
    String menu = (
        "\nFILE MANAGEMENT PROGRAM\n" +
        "-----------------------\n" +
        "1.\tCreate a new file\n" +
        "2.\tOpen/Read a file\n" +
        "3.\tWrite to the end of a file\n" +
        "4.\tOverwrite the content of a file\n" +
        "5.\tMove a file\n" +
        "6.\tRename a file\n" +
        "7.\tDelete a file\n" +
        "8.\tCreate a directory\n" +
        "9.\tDelete a directory\n" +
        "10.\tMove a directory\n" +
        "11.\tRename a directory\n" +
        "0.\tExit\n"
        );
    System.out.println(menu);
  }

  public void start() {
    boolean exit = false;
    int choice;
    Scanner sc = new Scanner(System.in);

    do {
      this.print();
      System.out.println("Enter a number: ");
      choice = sc.nextInt();

      try {
        switch(choice) {
          case 0:
            exit = true;
            System.out.println("Goodbye!");
            break;
          case 2:
            this.readFile();
            break;
          default: 
            System.out.println("The number you entered is not in the menu!");
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("There was an error.");
      }
    }
    while (!exit);
  }
  
  private void readFile() throws Exception {
    System.out.println("Enter file name: ");
    String fileName = new Scanner(System.in).nextLine();
    
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
        lib.closeFile(fd);

        System.out.println(content);
      }
    }
  }
}

