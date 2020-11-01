#include <linux/kernel.h>
#include <linux/errno.h>	      // for E* (error codes)
#include <uapi/linux/stat.h>    // for S_I* (file modes)
#include <asm/fcntl.h>	      	// for O_* (file open flags)

static long cpy_usrstr(char *buf, const char __user * fname, long bufsize, const char *scall_name) {
  long copied = strncpy_from_user(buf, fname, bufsize);
  if (copied < 0 || copied == bufsize) {
    long ret = -EFAULT;
    printk(KERN_ERR "-- custom-built -- #%s# syscall called unsuccessfully:", scall_name);
    printk(KERN_ERR "Error copying string from user space with error code %ld\n", ret);
    return ret;
  }
  return 0;
}

/*
 * custom-built system call, similar to sys_mkdir
 * author: Khoa D.Luu
 */
SYSCALL_DEFINE1(create_dir, const char __user *, dirname)
{
  char buf[256];
  long ret = cpy_usrstr(buf, dirname, sizeof(buf), "create_dir");
  if (ret < 0) return ret;

  printk(KERN_INFO "-- custom-built -- #create_dir# syscall called ");
  printk(KERN_INFO "with directory name \"%s\"\n", buf);

  return do_mkdirat(AT_FDCWD, dirname, S_IRWXU | S_IRGRP | S_IROTH);
}

/*
 * custom-built system call, similar to sys_rmdir
 * author: Khoa D.Luu
 */
SYSCALL_DEFINE1(delete_dir, const char __user *, dirname)
{
  char buf[256];
  long ret = cpy_usrstr(buf, dirname, sizeof(buf), "delete_dir");
  if (ret < 0) return ret;

  printk(KERN_INFO "-- custom-built -- #delete_dir# syscall called ");
  printk(KERN_INFO "with directory name \"%s\"\n", buf);

  return do_rmdir(AT_FDCWD, dirname);
}

/*
 * custom-built system call, similar to sys_unlink
 * author: Khoa D.Luu
 */
SYSCALL_DEFINE1(delete_file, const char __user *, filename)
{
  char buf[256];
  long ret = cpy_usrstr(buf, filename, sizeof(buf), "delete_file");
  if (ret < 0) return ret;

  printk(KERN_INFO "-- custom-built -- #delete_file# syscall called ");
  printk(KERN_INFO "with filename \"%s\"\n", buf);

  return do_unlinkat(AT_FDCWD, getname(filename));
}

/*
 * custom-built system call, similar to sys_rename
 * author: Khoa D.Luu
 */
SYSCALL_DEFINE2(reloc_file, const char __user *, oldname, const char __user *, newname)
{
  char buf_old[256];
  long ret = cpy_usrstr(buf_old, oldname, sizeof(buf_old), "reloc_file");
  if (ret < 0) return ret;

  char buf_new[256];
  ret = cpy_usrstr(buf_new, newname, sizeof(buf_new), "reloc_file");
  if (ret < 0) return ret;

  printk(KERN_INFO "-- custom-built -- #reloc_file# syscall called ");
  printk(KERN_INFO "with old filename \"%s\" ", buf_old);
  printk(KERN_INFO "and new filename \"%s\"\n", buf_new);

	return do_renameat2(AT_FDCWD, oldname, AT_FDCWD, newname, 0);
}

