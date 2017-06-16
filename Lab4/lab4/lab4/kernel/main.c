
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                            main.c
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                                    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#include "type.h"
#include "const.h"
#include "protect.h"
#include "string.h"
#include "proc.h"
#include "tty.h"
#include "console.h"
#include "global.h"
#include "proto.h"


/*======================================================================*
                            kernel_main
 *======================================================================*/
PUBLIC int kernel_main()
{
	disp_str("-----\"kernel_main\" begins-----\n");

	TASK*		p_task		= task_table;
	PROCESS*	p_proc		= proc_table;
	char*		p_task_stack	= task_stack + STACK_SIZE_TOTAL;
	u16		selector_ldt	= SELECTOR_LDT_FIRST;
	int i;
        u8              privilege;
        u8              rpl;
        int             eflags;
	//NR_TASKS定义了最大进程
	for (i = 0; i < NR_TASKS+NR_PROCS; i++) {
                if (i < NR_TASKS) {     /* 任务 */
                        p_task    = task_table + i;
                        privilege = PRIVILEGE_TASK;
                        rpl       = RPL_TASK;
                        eflags    = 0x1202; /* IF=1, IOPL=1, bit 2 is always 1 */
                }
                else {                  /* 用户进程 */
                        p_task    = user_proc_table + (i - NR_TASKS);
                        privilege = PRIVILEGE_USER;
                        rpl       = RPL_USER;
                        eflags    = 0x202; /* IF=1, bit 2 is always 1 */
                }

		strcpy(p_proc->p_name, p_task->name);	// name of the process
		p_proc->pid = i;			// pid

		//初始化进程表
		p_proc->ldt_sel = selector_ldt;

		memcpy(&p_proc->ldts[0], &gdt[SELECTOR_KERNEL_CS >> 3],
		       sizeof(DESCRIPTOR));
		p_proc->ldts[0].attr1 = DA_C | privilege << 5;
		memcpy(&p_proc->ldts[1], &gdt[SELECTOR_KERNEL_DS >> 3],
		       sizeof(DESCRIPTOR));
		p_proc->ldts[1].attr1 = DA_DRW | privilege << 5;
		p_proc->regs.cs	= (0 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.ds	= (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.es	= (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.fs	= (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.ss	= (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.gs	= (SELECTOR_KERNEL_GS & SA_RPL_MASK) | rpl;

		p_proc->regs.eip = (u32)p_task->initial_eip;
		p_proc->regs.esp = (u32)p_task_stack;
		p_proc->regs.eflags = eflags;

		p_proc->nr_tty = 0;

		p_task_stack -= p_task->stacksize;
		p_proc++;
		p_task++;
		selector_ldt += 1 << 3;
	}

	/*
		进程中添加属性：
		int sleep 计数剩余睡眠时间片数
		int stop 标记进程是否阻塞
		int color 进程输出的颜色
		设置优先级：即规定允许连续运行的时间片数
	*/
	proc_table[0].ticks = proc_table[0].priority = 15;
	proc_table[1].ticks = proc_table[1].priority = 10;
	proc_table[2].ticks = proc_table[2].priority = 10;
	proc_table[3].ticks = proc_table[3].priority = 10;
	proc_table[4].ticks = proc_table[4].priority = 10;
	proc_table[5].ticks = proc_table[5].priority = 10;

        proc_table[1].nr_tty = 0;
        proc_table[2].nr_tty = 0;
        proc_table[3].nr_tty = 0;
        proc_table[4].nr_tty = 0;
        proc_table[5].nr_tty = 0;

        proc_table[0].sleep = 0;
        proc_table[1].sleep = 0;
        proc_table[2].sleep = 0;
        proc_table[3].sleep = 0;
        proc_table[4].sleep = 0;
        proc_table[5].sleep = 0;

        proc_table[0].stop = 0;
        proc_table[1].stop = 0;
        proc_table[2].stop = 0;
        proc_table[3].stop = 0;
        proc_table[4].stop = 0;
        proc_table[5].stop = 0;

        proc_table[2].p_name = "Barber";
        proc_table[3].p_name = "A";
        proc_table[4].p_name = "B";
        proc_table[5].p_name = "C";

        proc_table[2].color = 0x0B;
        proc_table[3].color = 0x0D;
        proc_table[4].color = 0x0E;
        proc_table[5].color = 0x0F;

        customers.in = 0;
        customers.out = 0;
        customers.value = 0;
        customers.source = "customers";
        customers.flag = 1;

        barber.in = 0;
        barber.out = 0;
        barber.value = 0;
        barber.source = "barber";
        barber.flag = 1;

        mutex.in = 0;
        mutex.out = 0;
        mutex.value = 1;
        mutex.flag = 0;
		
  	    number.in = 0;
        number.out = 0;
        number.value = 1;
        number.flag = 0;

        chairs = 1;
        waiting = 0;


	k_reenter = 0;
	ticks = 0;
	numbers = 1;

	p_proc_ready	= proc_table;

	//==================clean screen==============
	disp_pos = 0;
	int m = 0;
	int j = 0;
	for(m = 0 ; m < 80; m++){
		for(j = 0 ; j < 5 ; j++){
			disp_str("     ");
		}
	}
	disp_pos = 0;
	//==================clean screen==============

	init_clock();
    init_keyboard();
	restart();

	while(1){}
}



/*======================================================================*
                               TestA
 *======================================================================*/
void TestA()//normal process
{
	int i = 0;
	while (1) {
		milli_delay(2000);
	}
}

/*======================================================================*
                               TestB
 *======================================================================*/
void TestB()//barber
{
	
	milli_delay(200);
	while(1){
		P_operate(&customers);
		P_operate(&mutex);
		waiting--;
		my_disp("Barber is cutting hair.\n",24,0x0B);
		milli_delay(10000);
		V_operate(&mutex);
		V_operate(&barber);
		milli_delay(2000);
	}	
	
}

/*======================================================================*
                               TestC
 *======================================================================*/
void TestC()//costomer C
{
	char output[4];
	char wait[4];
	while(1){
		sleep(3000);
		
		int numberB = 0;
        P_operate(&number);
        numberB = numbers;
		itoa(output, numberB);
        numbers ++;
        V_operate(&number);
		
		P_operate(&mutex);
		if (waiting < chairs) {
			waiting++;
			itoa(wait, waiting);
			my_disp("C:customer " , 11 , 0x0D);
			my_disp(output, 4, 0x0D);
            my_disp(" come , add waiting num to: ", 28 , 0x0D);
			my_disp(wait, 4, 0x0D);
			my_disp("\n", 1, 0x0F);
			milli_delay(5000);
			V_operate(&customers);
			V_operate(&mutex);
			P_operate(&barber);
			my_disp("C:customer ", 11, 0x0D);
			my_disp(output, 4, 0x0D);
			my_disp(" get hair cut , leave! \n", 24, 0x0D);
			milli_delay(2000);
		}else{
			my_disp("C:customer ", 11, 0x0D);
            my_disp(output, 4, 0x0D);
            my_disp(" come, leave without hair cut!\n", 31, 0x0D);
			milli_delay(2000);
			V_operate(&mutex);
		}
	}
}

/*======================================================================*
                               TestD
 *======================================================================*/
void TestD()//costomer D
{
	char output[4];
	char wait[4];
	while(1){
		sleep(3000);
		
		int numberB = 0;
        P_operate(&number);
        numberB = numbers;
		itoa(output, numberB);
        numbers ++;
        V_operate(&number);
		
		P_operate(&mutex);
		if (waiting < chairs) {
			waiting++;
			itoa(wait, waiting);
			my_disp("D:customer " , 11 , 0x0F);
			my_disp(output, 4, 0x0F);
            my_disp(" come , add waiting num to: ", 28 , 0x0F);
			my_disp(wait, 4, 0x0F);
			my_disp("\n", 1, 0x0F);
			milli_delay(5000);
			V_operate(&customers);
			V_operate(&mutex);
			P_operate(&barber);
			my_disp("D:customer ", 11, 0x0F);
			my_disp(output, 4, 0x0F);
			my_disp(" get hair cut , leave! \n", 24, 0x0F);
			milli_delay(2000);
		}else{
			my_disp("D:customer ", 11, 0x0F);
            my_disp(output, 4, 0x0F);
            my_disp(" come, leave without hair cut!\n", 31, 0x0F);
			milli_delay(2000);
			V_operate(&mutex);
		}
	}
}


/*======================================================================*
                               TestE
 *======================================================================*/
void TestE()//costomer E
{
	char output[4];
	char wait[4];
	while(1){
		sleep(3000);
		
		int numberB = 0;
        P_operate(&number);
        numberB = numbers;
        numbers ++;
		itoa(output, numberB);
        V_operate(&number);
		
		P_operate(&mutex);
		if (waiting < chairs) {
			waiting++;
			itoa(wait, waiting);
			my_disp("E:customer " , 11 , 0x0E);
			my_disp(output, 4, 0x0E);
            my_disp(" come , add waiting num to: ", 28 , 0x0E);
			my_disp(wait, 4, 0x0E);
			my_disp("\n", 1, 0x0E);
			milli_delay(5000);
			V_operate(&customers);
			V_operate(&mutex);
			P_operate(&barber);
			my_disp("E:customer ", 11, 0x0E);
			my_disp(output, 4, 0x0E);
			my_disp(" get hair cut , leave! \n", 24, 0x0E);
			milli_delay(2000);
		}else{
			my_disp("E:customer ", 11, 0x0E);
            my_disp(output, 4, 0x0E);
            my_disp(" come, leave without hair cut!\n", 31, 0x0E);
			milli_delay(2000);
			V_operate(&mutex);
		}
	}
}

