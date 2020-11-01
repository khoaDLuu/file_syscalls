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
 * custom-built system call, similar to sys_open
 * author: Khoa D.Luu
 */
SYSCALL_DEFINE1(open_file, const char __user *, filename)
{
  char buf[256];
  long ret = cpy_usrstr(buf, filename, sizeof(buf), "open_file");
  if (ret < 0) return ret;

  printk(KERN_INFO "-- custom-built -- #open_file# syscall called ");
  printk(KERN_INFO "with filename \"%s\"\n", buf);

  return do_sys_open(
    AT_FDCWD, filename,
    O_RDWR | O_CREAT, S_IRWXU | S_IRGRP | S_IROTH
  );
}

/*
 * custom-built system call which  wraps sys_close
 * author: Khoa D.Luu
 */
SYSCALL_DEFINE1(close_file, unsigned int, fd)
{
  printk(KERN_INFO "-- custom-built -- #close_file# syscall called ");
  printk(KERN_INFO "with file descriptor %u\n", fd);

	int retval = __close_fd(current->files, fd);

	/* can't restart close syscall because file table entry was cleared */
	if (unlikely(retval == -ERESTARTSYS ||
		     retval == -ERESTARTNOINTR ||
		     retval == -ERESTARTNOHAND ||
		     retval == -ERESTART_RESTARTBLOCK))
		retval = -EINTR;

	return retval;
}

