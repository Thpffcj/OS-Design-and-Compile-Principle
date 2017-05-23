global printDir
global printFile
global myprint

section .data
	head1 : db `\e[1;34m`
	head2 : db `\e[1;35m`
	tail:   db `\e[0m`
	output: dw 0

section .text
printDir:
	mov eax, 4
	mov ebx, 1
	mov ecx, head1
	mov edx, 7
	int 80h
	mov eax, 4
	mov ebx, 1
	mov ecx, [esp+4]  ;后面的参数先入栈
	mov edx, [esp+8]
	int 80h
	mov eax, 4
	mov ebx, 1
	mov ecx, tail
	mov edx, 4
	int 80h
	ret

printFile:
	mov eax, 4
	mov ebx, 1
	mov ecx, head2
	mov edx, 7
	int 80h
	mov eax,4
	mov ebx,1
	mov ecx, [esp+4]
	mov edx, [esp+8]
	int 80h
	mov eax, 4
	mov ebx, 1
	mov ecx, tail
	mov edx, 4
	int 80h
	ret


myprint:
	mov eax,4
	mov ebx,1
	mov ecx,[esp+4]
	mov edx,[esp+8]
	int 80h

	mov eax, 4
	mov ebx, 1
	mov ecx, tail
	mov edx, 4
	ret

