# AmigaFFS in Java

This is some code from the early 2000's that I've updated to compile with gradle. It has some (poor) tests.

# What is it?

It's an incomplete, but working, implementation of the Amiga OFS and FFS in Java.
It will allow you to copy files from an amiga ADF disk, or alternatively from an old Amiga Hard Disk.

# Why

I had an Amiga Hard Disk that was partially corrupted, and the RDB was missing, so it was not mountable using Linux or any other
Amiga Emulator

# How can I use it?

For an OFS/FFS Amiga disk, you could write a program to read some files. For an Amiga Hard Disk, if you can calculate the Root Block Offset,
you could do the same.

It needs some work!

# More details on the file format

http://lclevy.free.fr/adflib/adf_info.html

