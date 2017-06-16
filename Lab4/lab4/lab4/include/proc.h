
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                               proc.h
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                                    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/


typedef struct s_stackframe {	/* proc_ptr points here				↑ Low			*/
	u32	gs;		/* ┓						│			*/
	u32	fs;		/* ┃						│			*/
	u32	es;		/* ┃						│			*/
	u32	ds;		/* ┃						│			*/
	u32	edi;		/* ┃						│			*/
	u32	esi;		/* ┣ pushed by save()				│			*/
	u32	ebp;		/* ┃						│			*/
	u32	kernel_esp;	/* <- 'popad' will ignore it			│			*/
	u32	ebx;		/* ┃						↑栈从高地址往低地址增长*/		
	u32	edx;		/* ┃						│			*/
	u32	ecx;		/* ┃						│			*/
	u32	eax;		/* ┛						│			*/
	u32	retaddr;	/* return address for assembly code save()	│			*/
	u32	eip;		/*  ┓						│			*/
	u32	cs;		/*  ┃						│			*/
	u32	eflags;		/*  ┣ these are pushed by CPU during interrupt	│			*/
	u32	esp;		/*  ┃						│			*/
	u32	ss;		/*  ┛						┷High			*/
}STACK_FRAME;


typedef struct s_proc {
	STACK_FRAME regs;          /* process registers saved in stack frame */

	u16 ldt_sel;               /* gdt selector giving ldt base and limit */
	DESCRIPTOR ldts[LDT_SIZE]; /* local descriptors for code and data */

        int ticks;                 /* remained ticks */
        int priority;  //设置优先级
        int sleep; //计数剩余睡眠时间片数
        int stop;  //标记进程是否阻塞
		int color;  //进程输出的颜色
		
	u32 pid;                   /* process id passed in from MM */
	// char p_name[16];           /* name of the process */
    char* p_name;
    // char p_source[20];			//resource the process need


	int nr_tty;
}PROCESS;

typedef struct s_task {
	task_f	initial_eip;
	int	stacksize;
	char	name[32];
}TASK;

/*
信号量：定义结构体semahore
int value  计数资源数和等待进程数
PROCESS×[]  等待进程队列（循环队列）
int in int out  维护进程等待队列
char* source  所代表的资源名称
*/
typedef struct semaphore
{
	int value;
	PROCESS* list[10];
	int in;
	int out;
	// char source[16];
	char* source;
	int flag;
	
}SEMAPHORE;


/* Number of tasks & procs */
#define NR_TASKS	1
#define NR_PROCS	5

/* stacks of tasks */
#define STACK_SIZE_TTY		0x8000
#define STACK_SIZE_TESTA	0x8000
#define STACK_SIZE_TESTB	0x8000
#define STACK_SIZE_TESTC	0x8000
#define STACK_SIZE_TESTD	0x8000
#define STACK_SIZE_TESTE	0x8000

#define STACK_SIZE_TOTAL	(STACK_SIZE_TTY + \
				STACK_SIZE_TESTA + \
				STACK_SIZE_TESTB + \
				STACK_SIZE_TESTC + \
				STACK_SIZE_TESTD + \
				STACK_SIZE_TESTE)

