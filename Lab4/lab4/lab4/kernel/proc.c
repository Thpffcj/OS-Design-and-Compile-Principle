
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                               proc.c
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                                    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#include "type.h"
#include "const.h"
#include "protect.h"
#include "tty.h"
#include "console.h"
#include "string.h"
#include "proc.h"
#include "global.h"
#include "proto.h"

//添加系统调用的实际方法

/*======================================================================*
                              schedule
 *======================================================================*/
 //原代码中，schedule方法并不是每个时钟周期都会调用，当前进程的ticks没有耗尽时，是不会进行调度的，所以没办法保证每个周期都让sleep的进程的sleep属性-1 将clock_handler方法修改，要求每个时钟周期都遍历一次进程表将所有sleep的进程的sleep属性-1，并跳过该进程的调度.如果调度前的进程ticks没有耗尽，则归还控制权
PUBLIC void schedule()
{

	PROCESS* last = p_proc_ready;
	PROCESS* p;
	int	 greatest_ticks = 0;
	while (!greatest_ticks) {
		for (p = proc_table; p < proc_table+NR_TASKS+NR_PROCS; p++) {//make sure that all the process are tested
			if (p->ticks > greatest_ticks && p->stop == 0) {
				greatest_ticks = p->ticks;
				p_proc_ready = p;
			}
		}

		//每次schedule时，检查每个进程sleep是否为0，不为0，跳过本次时间片分配
		if (!greatest_ticks) {
			for(p=proc_table;p<proc_table+NR_TASKS+NR_PROCS;p++) {
				p->ticks = p->priority;
			}
		}
	}
}

/*======================================================================*
                           sys_get_ticks
 *======================================================================*/
PUBLIC int sys_get_ticks()
{
	return ticks;
}

PUBLIC int sys_process_sleep(int milli_delay) 
{	
	if (p_proc_ready->sleep > 0) {	
		schedule();
		return p_proc_ready->sleep;
	}
	
	p_proc_ready->sleep = milli_delay;
	// p_proc_ready->ticks = p_proc_ready->priority;
	schedule();
	return p_proc_ready->sleep;
}

PUBLIC int sys_sem_p(SEMAPHORE* s)
{
	s->value --;
	if(s->value < 0){
		block_self(s,p_proc_ready);
	}

	return 1;
}

PUBLIC int sys_sem_v(SEMAPHORE* s)
{
	s->value ++;
	if (s->value <= 0){
		wakeup(s);
	}
	return 1;
}

PUBLIC int sys_disp_str(char* str, int length , int color)
{
	int i = 0;
	for (i; i < length; ++i)
	{
		out_char(&console_table[nr_current_console],str[i],color);
	}
	return color;
}

PRIVATE void block_self(SEMAPHORE* s,PROCESS* p){
	s->list[s->in] = p;
	p->stop = 1;
	s->in = (s->in + 1)% 10;
	schedule();
}

PRIVATE void wakeup(SEMAPHORE* s){
	if (s->in == s->out)
	{
		schedule();
	}
	PROCESS* wake = s->list[s->out];
	wake->stop = 0;
	s->out = (s->out + 1)% 10;
	schedule();
}


