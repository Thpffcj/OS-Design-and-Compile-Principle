
; ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;                               syscall.asm
; ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;                                                     Forrest Yu, 2005
; ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

%include "sconst.inc"

_NR_get_ticks       equ 0 ; 要跟 global.c 中 sys_call_table 的定义相对应！
_NR_sleep			equ	1;
_NR_disp_str		equ	2;
_NR_sem_p			equ	3;
_NR_sem_v			equ	4;
INT_VECTOR_SYS_CALL equ 0x90

; 导出符号
global	get_ticks
global	sleep
global	my_disp_str
global	sem_p
global	sem_v

bits 32
[section .text]

; ====================================================================
;                              get_ticks
; ====================================================================
get_ticks:
	mov	eax, _NR_get_ticks
	int	INT_VECTOR_SYS_CALL
	ret
	
sleep:
	mov eax,_NR_sleep
	mov	ebx,[esp+4]
	;mov ecx,[esp+8]
	int 0x90
	ret

my_disp_str:
	mov eax,_NR_disp_str
	mov ebx,[esp+4]
	int 0x90
	ret
sem_p:
	mov eax,_NR_sem_p
	mov ebx,[esp+4]
	int 0x90
	ret
sem_v:
	mov eax,_NR_sem_v
	mov ebx,[esp+4]
	int 0x90
	ret

