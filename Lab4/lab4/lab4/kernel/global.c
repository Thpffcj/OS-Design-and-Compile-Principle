
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                            global.c
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                                    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#define GLOBAL_VARIABLES_HERE

#include "type.h"
#include "const.h"
#include "protect.h"
#include "tty.h"
#include "console.h"
#include "proc.h"
#include "global.h"
#include "proto.h"




PUBLIC	PROCESS	proc_table[NR_TASKS + NR_PROCS];

//在task_table中添加一项进程
PUBLIC	TASK	task_table[NR_TASKS] = {
	{task_tty, STACK_SIZE_TTY, "tty"}};

PUBLIC  TASK    user_proc_table[NR_PROCS] = {
	{TestA, STACK_SIZE_TESTA, "TestA"},
	{TestB, STACK_SIZE_TESTB, "TestB"},
	{TestC, STACK_SIZE_TESTC, "TestC"},
	{TestD, STACK_SIZE_TESTD, "TestD"},
	{TestE, STACK_SIZE_TESTE, "TestE"}};

PUBLIC	char		task_stack[STACK_SIZE_TOTAL];

PUBLIC	TTY		tty_table[NR_CONSOLES];
PUBLIC	CONSOLE		console_table[NR_CONSOLES];

PUBLIC	irq_handler	irq_table[NR_IRQ];

PUBLIC	system_call		sys_call_table[NR_SYS_CALL] = {	sys_get_ticks,
													  	sys_process_sleep,
													  	sys_disp_str,
													  	sys_sem_p,
													  	sys_sem_v,
													  	sys_write};
PUBLIC SEMAPHORE customers;
PUBLIC SEMAPHORE barber;
PUBLIC SEMAPHORE mutex;
PUBLIC SEMAPHORE number;

//指定椅子数目和等待的人
PUBLIC int chairs = 3;
PUBLIC int waiting = 0;
PUBLIC int numbers = 0;

