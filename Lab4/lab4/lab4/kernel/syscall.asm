
; ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;                               syscall.asm
; ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;                                                     Forrest Yu, 2005
; ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

%include "sconst.inc"

;添加系统调用的对外接口
INT_VECTOR_SYS_CALL equ 0x90
_NR_get_ticks       			equ 0 ; 要跟 global.c 中 sys_call_table 的定义相对应！
_NR_sleep						equ 1 ;
_NR_my_disp						equ 2 ;
_NR_P_operate					equ 3 ;
_NR_V_operate					equ 4 ;
_NR_write					equ 5 ;

; 导出符号
global	write
global	get_ticks
global  sleep
global  P_operate
global  V_operate
global 	my_disp

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
	mov ebx,[esp + 4]
	mov eax, _NR_sleep
	int INT_VECTOR_SYS_CALL
	ret

my_disp:
	mov ebx,[esp + 4]
	mov ecx,[esp + 8]
	mov edi,[esp + 12]
	mov eax, _NR_my_disp
	int INT_VECTOR_SYS_CALL
	ret

P_operate:
	mov ebx,[esp + 4]
	mov eax, _NR_P_operate
	int INT_VECTOR_SYS_CALL
	ret

V_operate:
	mov ebx,[esp + 4]
	mov eax, _NR_V_operate
	int INT_VECTOR_SYS_CALL
	ret




; ====================================================================================
;                          void write(char* buf, int len);
; ====================================================================================
write:
        mov     eax, _NR_write
        mov     ebx, [esp + 4]
        mov     ecx, [esp + 8]
        int     INT_VECTOR_SYS_CALL
        ret
