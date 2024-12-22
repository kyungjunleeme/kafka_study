# m1 
dd if=/dev/urandom of=test.txt bs=1G count=1

cc read-write.c -o read-write
cc mmap.c -o mmap

chmod 755 read-write
chmod 755 mmap

time ./read-write
time ./mmap

rm test.txt
