section .text

global _start

_start:
	mov byte[buffer], 0  ;初始化buffer为0
	mov byte[num_count], 0  ;初始化输入个数为0
	mov eax, 4 ;输出欢迎语
	mov ebx, 1
	mov ecx, hello
	mov edx, 13
	int 80h
	JMP input

input:
	mov eax, 3  ;读取输入
	mov ebx, 1
	mov ecx, digitl
	mov edx, 1
	int 80h

	cmp byte[digitl], 0dh  ;如果是回车则结束输入
	je adjustment  ;相等就跳转

	cmp byte[digitl], 0ah  ;如果是回车则结束输入
	je adjustment

	cmp byte[digitl], 20h  ;如果是空格则压栈
	je push_data

	sub byte[digitl], 30h
	mov al, byte[buffer]
	mov bl, 10
	mul bl
	add al, byte[digitl]
	mov byte[buffer], al ;将结果存在buffer中

	JMP input

push_data:
    add byte[num_count],1  ;输入个数+1
    mov ah,0
    mov al,byte[buffer] 
    push ax  ;数据压栈
 
    mov byte[buffer], 0  ;buffer清零

	jmp input


adjustment:
	add byte[num_count], 1
	mov ah, 0
	mov al, byte[buffer]
	push ax  ;数据压栈
	
	;比较笨的方法，因压栈导致输出顺序颠倒，文档要求输入九个数，我申请了十个内存来调整顺序
	;初始化申请的内存单元
	mov word[number1], 0  
	mov word[number2], 0
	mov word[number3], 0
	mov word[number4], 0
	mov word[number5], 0
	mov word[number6], 0
    mov word[number7], 0
    mov word[number8], 0
    mov word[number9], 0
    mov word[number10], 0

	mov cl, byte[num_count]  ;根据输入个数调整
	mov ch, 0
	pop dx
	mov word[number1], dx
	sub cx, 1
	cmp cx, 0
	JE repush_data
	pop dx
	mov word[number2], dx
	sub cx, 1
	cmp cx, 0
	JE repush_data
	pop dx
	mov word[number3], dx
	sub cx, 1
	cmp cx, 0
	JE repush_data
	pop dx
    mov word[number4], dx
    sub cx, 1
    cmp cx, 0
    JE repush_data
	pop dx
	mov word[number5], dx
	sub cx, 1
	cmp cx, 0
	JE repush_data
	pop dx
    mov word[number6], dx
    sub cx, 1
    cmp cx, 0
    JE repush_data
    pop dx
    mov word[number7], dx
    sub cx, 1
    cmp cx, 0
    JE repush_data
    pop dx
    mov word[number8], dx
    sub cx, 1
    cmp cx, 0
    JE repush_data
    pop dx
    mov word[number9], dx
    sub cx, 1
    cmp cx, 0
    JE repush_data
    pop dx
    mov word[number10], dx
    sub cx, 1
	cmp cx, 0
	JE repush_data

repush_data:
	mov cl, byte[num_count]
	mov ch, 0
	mov ax, word[number1]
	push ax
	sub cx, 1
	cmp cx, 0
	JE calculate
	mov ax, word[number2]
	push ax
	sub cx, 1
	cmp cx, 0
	JE calculate
	mov ax, word[number3]
    push ax
    sub cx, 1
    cmp cx, 0
    JE calculate
	mov ax, word[number4]
    push ax
    sub cx, 1
    cmp cx, 0
    JE calculate
	mov ax, word[number5]
    push ax 
    sub cx, 1
    cmp cx, 0
    JE calculate
    mov ax, word[number6]
    push ax
    sub cx, 1
    cmp cx, 0
    JE calculate
    mov ax, word[number7]
    push ax
    sub cx, 1
    cmp cx, 0
    JE calculate
    mov ax, word[number8]
    push ax
    sub cx, 1
    cmp cx, 0
    JE calculate
	mov ax, word[number9]
    push ax
    sub cx, 1
    cmp cx, 0
    JE calculate
    mov ax, word[number10]
    push ax
    sub cx, 1
    cmp cx, 0
    JE calculate

calculate:
	pop dx

fabonacci:
;	jmp print
	;初始化num1,num2,num3为1
	mov cx, 31
	set:
		mov ebx, num1
		add bx, cx;
		sub bx, 1
		mov byte[ebx], 0
		mov ebx, num2
		add bx, cx
		sub bx, 1
		mov byte[ebx], 0
		mov ebx, num3
		add bx, cx
		sub bx, 1
		mov byte[ebx], 0
	sub cx, 1
	cmp cx, 0
	JNE set  ;不相等就跳转
	mov byte[num1+31],1
	mov byte[num2+31],1
	mov byte[num3+31],1
	
;	jmp exit
	cmp dx, 0  ;输入为0
	je output
	cmp dx, 1  ;输入为1
	je output

	sub dx, 2  ;输入大于等于2
	mov byte[num3+31], 0

	mov cx, dx ;计算需要的循环次数
	add cx, 1  

adding:
	sub cx, 1
	push cx 

	mov byte[cf], 0  ;初始化进位为0
	mov cx, 32	;
	next:
		;计算num3=num1+num2
		mov ebx, num1
		add bx, cx
		sub bx, 1
		mov al, byte[ebx]
;		jmp not_zero
		mov ebx, num2
		add bx, cx
		sub bx, 1
		add al, byte[ebx]
		mov ebx, num3
		add bx, cx
		sub bx, 1
		add al, byte[cf]
		mov byte[ebx], al
;		jmp not_zero
		mov byte[cf], 0  ;进位后归0
	
		cmp byte[ebx], 100  
		jb no_cf  ;小于就跳转
;		jmp not_zero
		sub byte[ebx], 100  ;八位数最多保存128,满100则进位
		mov byte[cf], 1
	no_cf:
	sub cx, 1
	cmp cx, 0
	JNE next

;	jmp not_zero

	mov cx, 32
	;num2=num3,num1=num2
	copy:
		mov ebx, num2
		add bx, cx
		sub bx, 1
		mov al, byte[ebx]
		mov ebx, num1
		add bx, cx
		sub bx, 1
		mov byte[ebx], al
;		jmp not_zero
		mov ebx, num3
		add bx, cx
		sub bx, 1
		mov al, byte[ebx]
		mov ebx, num2
		add bx, cx
		sub bx, 1
		mov byte[ebx], al
	sub cx, 1
	cmp cx, 0
	JNE copy
	
	pop cx  ;循环的次数

	cmp cx, 0
	JNE adding

;	jmp exit

output:
	mov cx, 32
	divide:
;		jmp exit
		mov ebx, num3
		add bx, cx
		sub bx, 1
		mov al, byte[ebx]
		
;		jmp not_zero
		mov ah, 0
		mov dl, 10
		div dl  ;除数为八位，余数在ah,商在al
		movzx dx, ah
		push dx
		mov ah, 0
		mov dl, 10
		div dl
		movzx dx, ah
		push dx
;		jmp not_zero
	sub cx, 1
	cmp cx, 0
	JNE divide

	mov cx, 64  ;每个单元存放两位,共64个单元
	mov byte[flag_nz],0  ;用于标记到第一个非0的数字

;	jmp not_zero

print:
	mov word[loop_time], cx ;保存循环次数

	pop dx
	
	cmp byte[flag_nz], 1
	je not_zero  ;遇到非0后正常输出所有数字
	cmp dx, 0
	je zero

	mov byte[flag_nz], 1  ;遇到非0改变标志

not_zero:
	mov word[result], dx
	add word[result], 30h
    
	cmp byte[color], 0  ;交替颜色
    JNE color2

color1:
    mov eax, 4
    mov ebx, 1
    mov ecx, red
    mov edx, red.len
    int 80h
    jmp finally

color2:
    mov eax, 4
    mov ebx, 1
    mov ecx, blue
    mov edx, blue.len
    int 80h
    jmp finally

finally:
	mov eax, 4
	mov ebx, 1
	mov ecx, result
	mov edx, 1
	int 80h
	mov cx, word[loop_time]
	sub cx, 1
	cmp cx, 0
	JNE print

	;下一个数字改变颜色
	cmp byte[color], 0
	JE change1
	cmp byte[color], 1
	JE change2
change1:
	mov byte[color], 1
	jmp zero
change2:
	mov byte[color], 0
	jmp zero

zero:
	mov cx, word[loop_time]
	sub cx, 1
	cmp cx, 0
	JNE print
	
	;输出换行
	mov eax, 4
	mov ebx, 1
	mov ecx, space
	mov edx, 1
	int 80h
	 
	sub byte[num_count], 1
	cmp byte[num_count], 0
	jne calculate
		
exit:
	mov eax, 1
	mov ebx, 0
	int 80h

section .data
	hello: db 'Hello master', 0Ah
	space: db 0Ah	
    
	red: db 1Bh, '[31;1m', 0
	.len equ $ - red
	
	blue: db 1Bh, '[34;1m', 0
	.len: equ $ - blue
	
	color: db 0	

section .bss
num1 resb 32
num2 resb 32
num3 resb 32
number1 resw 1
number2 resw 1
number3 resw 1
number4 resw 1
number5 resw 1
number6 resw 1
number7 resw 1
number8 resw 1
number9 resw 1
number10 resw 1
result resw 1
digitl resb 1
cf resb 1
flag_nz resb 1
buffer resb 1
digit resb 1
num_count resb 1
loop_time resw 1
