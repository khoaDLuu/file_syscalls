import java.util.Scanner;
import java.io.*;

import com.sun.jna.Library;
import com.sun.jna.Native;
// import com.sun.jna.platform.linux.Fcntl.S_IRWXU;
// import com.sun.jna.platform.linux.Fcntl.S_IRGRP;
// import com.sun.jna.platform.linux.Fcntl.S_IROTH;

public class FileLib {
  public interface CStdLib extends Library {
    int syscall(int number, Object... args);
  }

  public static void main(String[] args) throws Exception {
    FileLib fl = new FileLib();
    long fd = -1;
    try {
      System.out.print("Enter your new file's name: ");
      Scanner sc = new Scanner(System.in);
      String fileName = sc.nextLine();

      System.out.println("Opening files named " + fileName);
      fd = fl.openFile(fileName);

      System.out.println("\nListing files ...");
      fl.listFiles();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (fd != -1) fl.closeFile(fd);
      else System.out.println("Error opening file - Error code " + fd);
    }
  }

  private CStdLib clib;
  private Runtime rt;
  private final String psudohome = "/home/keith/Documents/psdhome/";

  public FileLib() throws Exception {
    clib = (CStdLib) Native.loadLibrary("c", CStdLib.class);
    rt = Runtime.getRuntime();
  }

  private void listFiles() throws Exception {
    Process pr = rt.exec("ls -a -l " + psudohome);
    
    BufferedReader rd = new BufferedReader(
        new InputStreamReader(pr.getInputStream()));

    String line = null;
    while ((line = rd.readLine()) != null) {
      System.out.println(line);
    }
    int exitVal = pr.waitFor();
  }

  private void printKernelLog() throws Exception {
    String cmdString = new String("dmesg");
    Process pr = rt.exec(cmdString);
    
    BufferedReader rd = new BufferedReader(
        new InputStreamReader(pr.getInputStream()));

    String line = null;
    String prevLine = null;
    while ((line = rd.readLine()) != null) {
      prevLine = line;
    }
    System.out.println("---\nKernel buffer log:\n...\n" + prevLine);
  
    int exitVal = pr.waitFor();
    System.out.println("\nExited 'dmesg' with error code " + exitVal + "\n---");
  }

  public long openFile(String fileName) throws Exception {
    long fd = clib.syscall(2, psudohome + fileName, 66, 484);
    this.printKernelLog();
    return fd;
  }

  public void closeFile(long fd) throws Exception {
    clib.syscall(3, fd); 
    this.printKernelLog();
  }
}

