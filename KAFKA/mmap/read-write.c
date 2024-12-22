#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
	char *filepath = "test.txt";
	struct stat fileInfo;
	int fd = -1;

	fd = open(filepath, O_RDWR | O_CREAT, (mode_t)0600);
	if (fd < 0)
	{
		exit(EXIT_FAILURE);
	}
	fstat(fd, &fileInfo);
	printf("file size is %ld\n", (intmax_t)fileInfo.st_size);
	char *map = mmap(0, fileInfo.st_size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
	if (map == MAP_FAILED)
	{
		close(fd);
		perror("error mmapping the file");
		exit(EXIT_FAILURE);
	}

	for (size_t i = 0; i < 1000000 ; i++)
	{
		map[i] = '2';
	}
	if (msync(map, fileInfo.st_size, MS_SYNC) == -1)
		perror("could not sync the file to disk");

	if (munmap(map, fileInfo.st_size) == -1)
	{
		close(fd);
		perror("error un-mmapping the file");
		exit(EXIT_FAILURE);
	}
	close(fd);
	return (0);
}
