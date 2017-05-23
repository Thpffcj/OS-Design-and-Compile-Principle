lab2 : print.o main.c
	gcc -o lab2 main.c print.o -lm

print.o : my_print.asm
	nasm -f elf -o print.o my_print.asm 
