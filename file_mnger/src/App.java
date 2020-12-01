// import sys_lib.FileLib;

public class App {
  public static void main(String[] args) {
    try {
      new FileMenu(new FileLib()).start();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      System.out.println("There was an error.");
    }
  }
} 

