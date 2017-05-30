/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			      console.c
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

/*
	回车键: 把光标移到第一列
	换行键: 把光标前进到下一行
*/

#include "type.h"
#include "const.h"
#include "protect.h"
#include "string.h"
#include "proc.h"
#include "tty.h"
#include "console.h"
#include "global.h"
#include "keyboard.h"
#include "proto.h"

PRIVATE void set_cursor(unsigned int position);
PRIVATE void set_video_start_addr(u32 addr);
PRIVATE void flush(CONSOLE *p_con);


/*======================================================================*
			   init_screen
 *======================================================================*/

 //每一个init_tty调用,初始化TTY对应的console信息,屏幕位置
 PUBLIC void init_screen(TTY *p_tty) {
    //第几个TTY
    int nr_tty = p_tty - tty_table;
    //对应的console指针
    p_tty->p_console = console_table + nr_tty;
    //显存总大小 (in WORD)
    int v_mem_size = V_MEM_SIZE >> 1;


    //初始化对应的console数据信息
    int con_v_mem_size = v_mem_size / NR_CONSOLES;
    //当前控制台对应显存位置
    p_tty->p_console->original_addr = nr_tty * con_v_mem_size;
    //当前控制台占的显存大小
    p_tty->p_console->v_mem_limit = con_v_mem_size;
    //当前显示到了什么位置 = original_addr
    p_tty->p_console->current_start_addr = p_tty->p_console->original_addr;
    //当前显示到了什么位置:默认光标位置在最开始处 = original_addr
    p_tty->p_console->cursor = p_tty->p_console->original_addr;

    //初始化控制台输出过的所有字符信息
    p_tty->p_console->current_char_pos = 0;
    p_tty->p_console->char_buffer[0] = '\0';

    //查找字符串的信息
    p_tty->p_console->current_target_pos = 0;
    p_tty->p_console->target[0] = '\0';

    //是否在搜索模式中
    p_tty->p_console->in_search = 0;
    //是否只接受ESC输入
    p_tty->p_console->only_esc = 0;

    //设置光标信息
    set_cursor(p_tty->p_console->cursor);
}

/*======================================================================*
			   is_current_console
*======================================================================*/

PUBLIC int is_current_console(CONSOLE *p_con) {
    //判断输入的控制台信息是否是全局的当前控制台
    return (p_con == &console_table[nr_current_console]);
}

/*======================================================================*
			   out_char
 *======================================================================*/

 //向控制台输出字符
 PUBLIC void out_char(CONSOLE *p_con, char ch, int color) {
    
	//搜索模式,加入缓存.
    if (p_con->in_search == 1) {
        //搜索模式下,被输入的关键字以不同于之前所输入文字的另一种颜色显示
        color = 1;
        //记录搜索字符,加入上一次代表'\0'的地方
        p_con->target[p_con->current_target_pos] = ch;
        p_con->current_target_pos++;
        p_con->target[p_con->current_target_pos] = '\0';
    }
	
	//显存的起始地址是0XB8000,大小为32KB,范围是0XB8000~0XBFFFF,内存段中每两个字节代表一个字符,低字节为ASCII码,高字节为属性,一个屏幕25行,一行80个字符,大小为25*80*2 = 4000字节,4KB,可以设置8个屏幕

    //显存起始位置 + 当前光标 * 2
    u8 *p_vmem = (u8 *) (V_MEM_BASE + p_con->cursor * 2);

    //这个字符被输出之前光标的位置
    int char_pos_before = p_con->cursor;

    switch (ch) {

        //换行
        case '\n':
            //当前光标的位置 < 控制台起始地址 + 控制台占的显存大小 - SCREEN_WIDTH(80)宽度, 最后一行
            if (p_con->cursor < p_con->original_addr + p_con->v_mem_limit - SCREEN_WIDTH) {
                //移动光标 = 控制台起始地址+SCREEN_WIDTH* ( 当前的行数 + 1)
                p_con->cursor = p_con->original_addr + SCREEN_WIDTH * ((p_con->cursor - p_con->original_addr) / SCREEN_WIDTH + 1);
            }
            break;

        //TAB:位置模4,输出（4-余数）个空格
        case '\t': {
            //当前光标与起始地址的偏移量
            int current_ch_size = p_con->cursor - p_con->original_addr;
            //模一行能够显示的字符数目(80),也就是当前光标在这一行的偏移量
            int current_pos_in_line = current_ch_size % SCREEN_WIDTH;
            //应该输出的空格的数量(位置0,显示4空格;位置1,显示3空格)
            int num_of_space = 4 - current_pos_in_line % 4;

            //如果位置处在一行的末尾(77,78,79三个位置),还要让光标跳到下一行的开始+4位置
            if(current_pos_in_line >= SCREEN_WIDTH-3){
                num_of_space += 4;
            }

            //显示对应的空格
            while (num_of_space > 0) {
                p_con->cursor++;
                *p_vmem++ =' ';
                //写入颜色
                if (color == 0) {
                    *p_vmem++ = DEFAULT_CHAR_COLOR;
                } else {
                    *p_vmem++ = RED_COLOR;
                }
                num_of_space--;
            }
            break;
        }

        //退格
        case '\b':
            //如果光标在起始位置之前
            if (p_con->cursor > p_con->original_addr) {
                
				p_con->current_char_pos--;

                //移动光标到 输出这个字符之前 光标指向的位置,达到整体删除的效果
                int origin_pos = p_con->char_pos[p_con->current_char_pos];
                while (p_con->cursor > origin_pos) {
                    //光标位置递减
                    p_con->cursor--;
                    //写入一个空格
                    *(p_vmem - 2) = ' ';
                    *(p_vmem - 1) = DEFAULT_CHAR_COLOR;
                }

                //退格之后光标的位置
                int now_cursor = p_con->cursor;
                //清空光标指向的缓存的一个字符:将前一个位置变成结束字符
                p_con->char_buffer[p_con->current_char_pos] = '\0';
                p_con->char_color[p_con->current_char_pos] = 0;
                p_con->char_pos[p_con->current_char_pos] = now_cursor;
            }
            break;

        //一般字符
        default:
            //如果光标位置 < 起始位置 + 当前控制台占的显存大小 - 1
            if (p_con->cursor < p_con->original_addr + p_con->v_mem_limit - 1) {
                //写入字符,p_vmem 当前显存位置
                *p_vmem++ = ch;

                //写入颜色
                if (color == 0) {
                    *p_vmem++ = DEFAULT_CHAR_COLOR;
                } else {
                    *p_vmem++ = RED_COLOR;
                }

                //光标后移
                p_con->cursor++;
            }
            break;
    }

	//输出过的所有字符  char char_buffer[SCREEN_SIZE];
	//这个字符被输出之前光标的位置  int char_pos[SCREEN_SIZE];
	//输出过的所有字符对应的颜色  int cache_color[SCREEN_SIZE];
	//下一个可缓存字符位置 int current_char_pos;
	
    //不在搜索模式中,且这个字符不是退格,正常加入缓存,记录这个字符在显存中的位置
    if (p_con->in_search == 0 && ch != '\b') {
       
		//将输出的字符加入缓存,加入上一次代表'\0'的地方,current_char_pos下一个可缓存字符位置
        p_con->char_buffer[p_con->current_char_pos] = ch;
        p_con->char_color[p_con->current_char_pos] = 0;
        //保存这个字符被输出之前光标的位置,ch_pos_in_mem这个字符被输出之前光标的位置
        p_con->char_pos[p_con->current_char_pos] = char_pos_before;
        
		//指针后移
        p_con->current_char_pos++;
        
		//在新的地方加入结尾字符
        p_con->char_buffer[p_con->current_char_pos] = '\0';
        p_con->char_color[p_con->current_char_pos] = 0;
		
		//cache_char_mem_pos这个字符被输出之前光标的位置
        p_con->char_pos[p_con->current_char_pos] = (char_pos_before + 1);
    }


    //滚动屏幕 : 如果光标位置 >= 起始位置 + 屏幕大小(80 * 25)
    while (p_con->cursor >= p_con->current_start_addr + SCREEN_SIZE) {
        scroll_screen(p_con, SCR_DN);
    }

    flush(p_con);
}

/*======================================================================*
                           flush
*======================================================================*/

PRIVATE void flush(CONSOLE *p_con) {
    set_cursor(p_con->cursor);
    set_video_start_addr(p_con->current_start_addr);
}

/*======================================================================*
			    set_cursor
 *======================================================================*/

 //设置光标位置
 PRIVATE void set_cursor(unsigned int position) {
    disable_int();
    out_byte(CRTC_ADDR_REG, CURSOR_H);
    out_byte(CRTC_DATA_REG, (position >> 8) & 0xFF);
    out_byte(CRTC_ADDR_REG, CURSOR_L);
    out_byte(CRTC_DATA_REG, position & 0xFF);
    enable_int();
}

/*======================================================================*
			  set_video_start_addr
 *======================================================================*/

 //切换控制台
 PRIVATE void set_video_start_addr(u32 addr) {
    disable_int();
    out_byte(CRTC_ADDR_REG, START_ADDR_H);
    out_byte(CRTC_DATA_REG, (addr >> 8) & 0xFF);
    out_byte(CRTC_ADDR_REG, START_ADDR_L);
    out_byte(CRTC_DATA_REG, addr & 0xFF);
    enable_int();
}



/*======================================================================*
			   select_console
 *======================================================================*/

 //切换控制台的函数
 PUBLIC void select_console(int nr_console)    /* 0 ~ (NR_CONSOLES - 1) */
{
    //输入检验
    if ((nr_console < 0) || (nr_console >= NR_CONSOLES)) {
        return;
    }

    //当前控制台编号
    nr_current_console = nr_console;

    //设置光标到当前控制台的光标位置
    set_cursor(console_table[nr_console].cursor);
    set_video_start_addr(console_table[nr_console].current_start_addr);
}

/*======================================================================*
			   scroll_screen
 *----------------------------------------------------------------------*
 滚屏.
 *----------------------------------------------------------------------*
 direction:
	SCR_UP	: 向上滚屏
	SCR_DN	: 向下滚屏
	其它	: 不做处理
 *======================================================================*/

 PUBLIC void scroll_screen(CONSOLE *p_con, int direction) {
    if (direction == SCR_UP) {
        if (p_con->current_start_addr > p_con->original_addr) {
            p_con->current_start_addr -= SCREEN_WIDTH;
        }
    }
    else if (direction == SCR_DN) {
        if (p_con->current_start_addr + SCREEN_SIZE <
            p_con->original_addr + p_con->v_mem_limit) {
            p_con->current_start_addr += SCREEN_WIDTH;
        }
    }
    else {
    }

    set_video_start_addr(p_con->current_start_addr);
    set_cursor(p_con->cursor);
}


//向控制台输出带有颜色的字符串
PUBLIC void out_string(CONSOLE *p_con, char *str, int *color) {
    int size = str_length(str);
    int i;
    //输出每一个字符
    for (i = 0; i < size; i++) {
        out_char(p_con, str[i], color[i]);
    }
}

PUBLIC void out_string_color(CONSOLE *p_con, char *str) {
    int size = str_length(str);
    int i;
    //输出每一个字符
    for (i = 0; i < size; i++) {
        out_char(p_con, str[i], 1);
    }
}

//清空一个窗口
PUBLIC void clear_console(CONSOLE *p_con) {
    
	//显存起始位置 + 当前光标 * 2
	u8 *p_vmem = (u8 *) (V_MEM_BASE + p_con->cursor * 2);

    //如果光标在起始位置之前
    while (p_con->cursor > p_con->original_addr) {
        //光标位置递减
        p_con->cursor--;
        //写入一个空格，低八位字符，高八位颜色
        *(p_vmem - 2) = ' ';
        *(p_vmem - 1) = DEFAULT_CHAR_COLOR;
        p_vmem -= 2;
    }

    //回归光标到起点
    set_cursor(p_con->original_addr);
}


//清空一个窗口内部的字符缓存
PUBLIC void clear_console_cache(CONSOLE *p_con) {
    
	//初始化控制台输出过的所有字符信息
    p_con->current_char_pos = 0;
    p_con->char_buffer[0] = '\0';

    p_con->char_pos[0] = p_con->original_addr;

    //查找字符串的信息
    p_con->current_target_pos = 0;
    p_con->target[0] = '\0';

    //是否在搜索模式中
    p_con->in_search = 0;
    //只接受ESC输入
    p_con->only_esc = 0;
}
