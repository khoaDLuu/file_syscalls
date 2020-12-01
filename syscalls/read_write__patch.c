#include <linux/kernel.h>

/*
 * custom-built system call, similar to sys_read
 * author: Khoa D.Luu
 */
SYSCALL_DEFINE3(read_file, unsigned int, fd, char __user *, buf, size_t, count)
{
  printk(KERN_INFO "-- custom-built -- #read_file# syscall called ");
  printk(KERN_INFO "with file descriptor %u\n", fd);

	return ksys_read(fd, buf, count);
}

/*
 * custom-built system call, similar to sys_write
 * author: Khoa D.Luu
 */
SYSCALL_DEFINE3(write_file, unsigned int, fd, const char __user *, buf,
		size_t, count)
{
  printk(KERN_INFO "-- custom-built -- #write_file# syscall called ");
  printk(KERN_INFO "with file descriptor %u\n", fd);

	return ksys_write(fd, buf, count);
}

