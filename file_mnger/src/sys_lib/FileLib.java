// package sys_lib;

import java.util.Scanner;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.io.*;
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
    String fileName = null;

    // open/write/close test
    try {
      System.out.print("Enter your new file's name: ");
      Scanner sc = new Scanner(System.in);
      fileName = sc.nextLine();

      System.out.println("Opening file named " + fileName);
      fd = fl.openFile(fileName);
      fl.writeFile(fd, "Hello world!\n");
      System.out.println("Last system error: " + Native.getLastError());

      System.out.println("\nListing files ...");
      fl.listFiles();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (fd >= 0) fl.closeFile(fd);
      else System.out.println("Error opening file - Error code " + fd);
    }

    // open/read/close test
    try {
      System.out.println("Opening file named " + fileName);
      fd = fl.openFile(fileName);
      String content = fl.readFile(fd);
      System.out.println("Last system error: " + Native.getLastError());

      System.out.println("\nFile content:\n" + content);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (fd >= 0) fl.closeFile(fd);
      else System.out.println("Error opening file - Error code " + fd);
    }

    // mkdir/move/rename/unlink/rmdir test
    try {
      System.out.print("Enter your new directory's name: ");
      Scanner sc = new Scanner(System.in);
      String dirName = sc.nextLine();

      System.out.println("Creating directory named " + dirName);
      fl.createDir(dirName);
      System.out.println(" ... Done.");
      System.out.println("Listing files ...");
      fl.listFiles();

      String fileNewLoc = dirName + "/moved" + fileName;
      fl.moveFile(fileName, fileNewLoc);
      System.out.println("Last system error: " + Native.getLastError());
      System.out.println("\nListing files ...");
      fl.listFiles();

      System.out.print("Deleting file named " + fileNewLoc);
      fl.deleteFile(fileNewLoc);
      System.out.println(" ... Done.");

      System.out.println("Deleting directory named " + dirName);
      fl.deleteDir(dirName);
      System.out.println(" ... Done.");
      System.out.println("\nListing files ...");
      fl.listFiles();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private CStdLib clib;
  private Runtime rt;
  private final String psudohome = "/home/keith/Documents/psdhome/";
  private final int __NR_open_file = 436;
  private final int __NR_close_file = 437;
  private final int __NR_read_file = 438;
  private final int __NR_write_file = 439;
  private final int __NR_reloc_file = 440;
  private final int __NR_delete_file = 441;
  private final int __NR_create_dir = 442;
  private final int __NR_delete_dir = 443;

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

  public void showTree() throws Exception {
    Process pr = rt.exec("tree " + psudohome);
    
    BufferedReader rd = new BufferedReader(
        new InputStreamReader(pr.getInputStream()));

    String line = null;
    boolean isFirstLine = true;
    while ((line = rd.readLine()) != null) {
      if (isFirstLine) {
        System.out.println("HOME\t\t\t(HOME = /home/keith/Documents/psdhome/)");
        isFirstLine = false;
      }
      else {
        System.out.println(line);
      }
    }
    int exitVal = pr.waitFor();
  }

  public void printKernelLog() throws Exception {
    this.printKernelLog(10);
  }

  public void printKernelLog(int lineNum) throws Exception {
    String[] cmd = {
      "/bin/sh",
      "-c",
      "dmesg | tail -" + lineNum
    };
    Process pr = rt.exec(cmd); // Coz rt.exec() doesn't work with pipes directly
    
    BufferedReader rd = new BufferedReader(
        new InputStreamReader(pr.getInputStream()));

    System.out.println("---\n...");
    String line = null;
    while ((line = rd.readLine()) != null) {
      System.out.println(line);
    }
  
    int exitVal = pr.waitFor();
    System.out.println("\nExited 'dmesg' with error code " + exitVal + "\n---");
  }

  public long openFile(String fileName) throws Exception {
    long fd = clib.syscall(__NR_open_file, psudohome + fileName);
    // this.printKernelLog();
    return fd;
  }

  public void closeFile(long fd) throws Exception {
    clib.syscall(__NR_close_file, fd); 
    // this.printKernelLog();
  }

  public String readFile(long fd) throws Exception {
    StringBuilder cont = new StringBuilder();
    int chunkSize = 1000;
    Pointer memBlock = new Memory(chunkSize);
    memBlock.clear(chunkSize);

    long readCount = 0;
    while (
        (readCount = clib.syscall(__NR_read_file, fd, memBlock, chunkSize)) > 0
    ) {
      cont.append(memBlock.getString(0));
      memBlock.clear(chunkSize);
      // this.printKernelLog();
    }

    return cont.toString();
  }

  public void writeFile(long fd, String cont) throws Exception {
    clib.syscall(__NR_write_file, fd, cont, cont.length()); 
    // this.printKernelLog();
  }

  public void appendFile(long fd, String cont) throws Exception {
    String existingCont = this.readFile(fd);
    // after readFile(), the file pointer is at the end of the file
    // so there's no need to append new content to existing content
    // String updatedCont = existingCont + "\n" + cont;
    this.writeFile(fd, cont);
  }

  public void moveFile(String currentLoc, String newLoc) throws Exception {
    // additional param `fname`?
    clib.syscall(__NR_reloc_file, psudohome + currentLoc, psudohome + newLoc);
    // this.printKernelLog();
  }

  public void renameFile(String oldName, String newName) throws Exception {
    clib.syscall(__NR_reloc_file, psudohome + oldName, psudohome + newName);
    // this.printKernelLog();
  }

  public void deleteFile(String fileName) throws Exception {
    clib.syscall(__NR_delete_file, psudohome + fileName);
    // this.printKernelLog();
  }

  public void createDir(String dirName) throws Exception {
    clib.syscall(__NR_create_dir, psudohome + dirName);
    // this.printKernelLog();
  }

  public void deleteDir(String dirName) throws Exception {
    clib.syscall(__NR_delete_dir, psudohome + dirName);
    // this.printKernelLog();
  }

  public void deleteDirUnsafe(String dirName) throws Exception {
    this.deleteItemUnsafe(dirName);
  }

  public void deleteItemUnsafe(String itemName) throws Exception {
    if (this.fileExists(itemName)) {
      clib.syscall(__NR_delete_file, psudohome + itemName);
    }
    else if (this.dirEmpty(itemName)) {
      clib.syscall(__NR_delete_dir, psudohome + itemName);
    }
    else {
      for (String childItemName : new File(psudohome + itemName).list()) {
        this.deleteItemUnsafe(itemName + "/" + this.getItemName(childItemName));
      }
      clib.syscall(__NR_delete_dir, psudohome + itemName);
    }
  }

  public boolean fileExists(String fileName) {
    try {
      File f = new File(psudohome + fileName);
      return f.isFile();
    }
    catch (SecurityException ex) {
      return false;
    }
  }

  public boolean dirExists(String dirName) {
    try {
      File f = new File(psudohome + dirName);
      return f.isDirectory();
    }
    catch (SecurityException ex) {
      return false;
    }
  }

  public boolean dirEmpty(String dirName) {
    try {
      File f = new File(psudohome + dirName);
      return f.isDirectory() && f.list().length == 0;
    }
    catch (SecurityException ex) {
      return false;
    }
  }
 
  public String getItemName(String fullpath) {
    File f = new File(fullpath);
    return f.getName();
  }

  public String getParentName(String fullpath) {
    File f = new File(fullpath);
    String prName = f.getParent();
    return prName == null ? "" : (prName + "/");
  }
}

