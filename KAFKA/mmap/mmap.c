#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <unistd.h>

#define BUFFER_SIZE 4096

int main(int argc, char *argv[])
{
   char *input_filepath = "test.txt";
   char *output_filepath = "output.txt";
   struct stat fileInfo;
   int input_fd = -1, output_fd = -1;
   
   // 입력 파일 열기
   input_fd = open(input_filepath, O_RDONLY, (mode_t)0600);
   if (input_fd < 0)
   {
       perror("error opening input file");
       exit(EXIT_FAILURE);
   }

   // 출력 파일 열기
   output_fd = open(output_filepath, O_WRONLY | O_CREAT | O_TRUNC, (mode_t)0644);
   if (output_fd < 0)
   {
       perror("error opening output file");
       close(input_fd);
       exit(EXIT_FAILURE);
   }

   fstat(input_fd, &fileInfo);
   printf("file size is %ji\n", (intmax_t)fileInfo.st_size);

   char buffer[BUFFER_SIZE];
   ssize_t bytes_read;

   // Read from input file and write to output file
   while ((bytes_read = read(input_fd, buffer, BUFFER_SIZE)) > 0)
   {
       ssize_t bytes_written = write(output_fd, buffer, bytes_read);
       if (bytes_written < 0)
       {
           perror("error writing to file");
           close(input_fd);
           close(output_fd);
           exit(EXIT_FAILURE);
       }
   }

   if (bytes_read < 0)
   {
       perror("error reading the file");
       close(input_fd);
       close(output_fd);
       exit(EXIT_FAILURE);
   }

   close(input_fd);
   close(output_fd);
   return (0);
}
