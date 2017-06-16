
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                               proc.c
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                                    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#include "type.h"
#include "const.h"
#include "protect.h"
#include "proto.h"
#include "string.h"
#include "proc.h"
#include "global.h"
//#include "queue.c"
/*======================================================================*
                              schedule
 *======================================================================*/
PUBLIC void schedule()
{	
	PROCESS* p;
	/*sleep_milis--*/
	for(p=proc_table;p<proc_table+NR_TASKS;p++){
		if(p->sleep_milis>0){
			p->sleep_milis--;
		}
	}
GO_LOOP:
		do{
		p_proc_ready++;
		if(p_proc_ready >= proc_table + NR_TASKS) {
	    		p_proc_ready = proc_table;
		}
		if(p_proc_ready->is_ready==0){
			goto GO_LOOP;
			continue;
		}
		}while(p_proc_ready->sleep_milis>0 );
		p_proc_now=p_proc_ready;
	
}
/*------------operation about semaphore---------------------------------*/
void enqueue(queue* q,int val)
{
	if(q->index>=QUEUE_SIZE){
		return;
	}
	int * vals=q->vals;
	vals[q->index]=val;
	q->index++;
}

int dequeue(queue* q)
{
	int result=0;
	int *vals=q->vals;
	int i=0;
	if(q->index==0){
		return 0;
	}
	result=vals[0];
	for(;i<QUEUE_SIZE-1;i++)
	{
		vals[i]=vals[i+1];
	}
        q->index--;
	return result;
}

void init_semaphore(semaphore* sem){
	queue *wait=&(sem->wait);
	int* vals=wait->vals;
	int i=0;
	sem->count=1;
	wait->index=0;
	for(i=0;i<QUEUE_SIZE;i++)
	{
		vals[i]=-1;
	}	
}
/*----------------------------------------------------------------------*/


/*======================================================================*
                           sys_get_ticks
 *======================================================================*/
PUBLIC int sys_get_ticks()
{
	return ticks;
}

PUBLIC int sys_sleep(int milis)
{	
	p_proc_ready->sleep_milis=milis*HZ/1000+1;
	schedule();
	return	0;
}
PUBLIC	int sys_disp_str(char* str)
{
    disp_color_str(str , p_proc_now->p_table_index+1);
	return 0;
}
PUBLIC int sys_sem_p(semaphore* sem)
{	sem->count--;
	if(sem->count<0){
		queue* temp_q=&(sem->wait);
		enqueue(temp_q,p_proc_now->p_table_index);
		p_proc_now->is_ready=0;	//block the process
		schedule();
	}
	return 0;
}
PUBLIC int sys_sem_v(semaphore* sem)
{
	sem->count++;
	if(sem->count<=0){
		queue* temp_q=&(sem->wait);
		int index=dequeue(temp_q);
		PROCESS *prcs=&(proc_table[index]);
		prcs->is_ready=1;//make it state ready
	}
	schedule();
	return 0;
}
