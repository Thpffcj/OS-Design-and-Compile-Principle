//
// Created by Thpffcj on 2017/5/29.
//
#include "allocated.h"
#include "type.h"
#include "bottom.h"
data_unit memory[MEMORY_SIZE];

int str_length(unsigned char *str);
int str_to_int(unsigned char *str);
void int_to_str(int input, unsigned char *str);

int unallocate_table(int size)
{
    int i = 1024;
    int begin = 0;
    int size1 = 0;
    unsigned char* cbegin = "";
    unsigned char* csize = "";
    for(; i<2048; i+=3)
    {
        if(memory[i+2] > size)
        {
            *cbegin = memory[i+1];
            begin = str_to_int(cbegin) + size;
            int_to_str(begin, cbegin);
            *csize = memory[i+2];
            size1 = str_to_int(csize) - size;
            int_to_str(size1, csize);
        }
        return memory[i+1] - size;
    }
    return -1;
}

int allocate_table(int pid, int size)
{
    int location;
    location = unallocate_table(size);
    if(location == -1)
    {
        return -1;
    }
    for(int i=0; i<1024; i+=3)
    {
        if(memory[i] == 0)
        {
            memory[i] = pid;
            memory[i+1] = location;
            memory[i+2] = size;
            return location;
        }
    }
    return -1;
}

int is_legal(int pid, int address)
{
    for(int i=0; i<1024; i+=3)
    {
        if(memory[i+1] == address)
        {
            if(memory[i] == pid)
            {
                return 1;
            }
        }
    }
    return -1;
}

//数字转字符串
void int_to_str(int input, unsigned char *str) {
    int num = input;
    int n = num % 10;
    unsigned char tmp[20];

    int i = 0;
    while (n > 0) {
        tmp[i] = (unsigned char) (n + '0');
        i++;
        num = num / 10;
        n = num % 10;
    }
    tmp[i] = '\0';

    for (i = 0; i <= str_length(tmp) - 1; i++) {
        str[i] = tmp[str_length(tmp) - i - 1];
    }
    str[i] = '\0';
}

int str_to_int(unsigned char *str)
{
    int length = str_length(str);
    int result = 0;
    for(int i=0; i<length; i++){
        result = (result + str[i]) * 10;
    }
    return result;
}

//计算字符串的长度
int str_length(unsigned char *str) {
    int i;
    int size = 0;

    for (i = 0; ; i++) {
        if (str[i] != '\0') {
            size++;
        } else {
            break;
        }
    }
    return size;
}