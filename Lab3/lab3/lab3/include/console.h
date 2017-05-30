
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			      console.h
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#ifndef _ORANGES_CONSOLE_H_
#define _ORANGES_CONSOLE_H_

#define SCR_UP	1	/* scroll forward */
#define SCR_DN	-1	/* scroll backward */

#define SCREEN_SIZE		(80 * 25)
#define SCREEN_WIDTH		80

#define DEFAULT_CHAR_COLOR	0x07	/* 0000 0111 黑底白字 */
#define RED_COLOR	0x74;	//黑底红字

#include "tty.h"


/* CONSOLE */
typedef struct s_console
{
	unsigned int	current_start_addr;	/* 当前显示到了什么位置   */
	unsigned int	original_addr;		/* 当前控制台对应显存位置 */
	unsigned int	v_mem_limit;		/* 当前控制台占的显存大小 */
	unsigned int	cursor;			/* 当前光标位置 */

    //输出过的所有字符
	char char_buffer[SCREEN_SIZE];
	//这个字符被输出之前光标的位置
	int char_pos[SCREEN_SIZE];
	//输出过的所有字符对应的颜色
    int char_color[SCREEN_SIZE];
	//下一个可缓存字符位置
    int current_char_pos;

    //被查找的目标字符串缓存与当前位置
    char target[100];
    int current_target_pos;

	//是否处于搜索模式
    int in_search;
	//是否处于搜索模式中按下回车后的状态,即只接受ESC,屏蔽其他按键的状态
    int only_esc;

}CONSOLE;

void out_string(CONSOLE* p_con, char* str, int* color);
int is_current_console(CONSOLE* p_con);
void clear_console(CONSOLE* p_con);
void select_console(int nr_console);
void out_char(CONSOLE* p_con, char ch, int color);
void out_string_no_color(CONSOLE* p_con, char* str);
void out_string_color(CONSOLE* p_con, char* str);
void init_screen(TTY* p_tty);
void clear_console_cache(CONSOLE *p_con);

#endif /* _ORANGES_CONSOLE_H_ */

