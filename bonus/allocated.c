//
// Created by Thpffcj on 2017/5/29.
//
#include <stdio.h>
#include "allocated.h"
#include "type.h"
#include "bottom.h"

int str_length(unsigned char *str);
int str_to_int(unsigned char *str, int location);
void int_to_str(int input, unsigned char *str, int location);

int unallocate_table(int size)
{
//    printf("%s", "size ");
//    printf("%d\n", size);
    int begin = 0;
    int remaining = 0;
    unsigned char address[5];
    unsigned char length[5];
    for(int i = 1024*15; i<1024*30; i+=15)
    {
        for(int j=0; j<5; j++)
        {
            length[j] = mem_read(i+10+j);
        }
        int len = str_to_int(length, 0);
//        printf("%s", "len ");
//        printf("%d\n", len);
        if( len > size) {
            for(int j=0; j<5; j++)
            {
                address[j] = mem_read(i+5+j);
            }
            begin = str_to_int(address, 0) + size;
//            printf("%s", "begin ");
//            printf("%d\n", begin);
            int_to_str(begin, address, 0);
            for(int j=0; j<5; j++)
            {
                mem_write(address[j], i+5+j);
            }

            for(int j=0; j<5; j++)
            {
                length[j] = mem_read(i+10+j);
            }
            remaining = str_to_int(length, 0) - size;
            int_to_str(remaining, length, 0);
            for(int j=0; j<5; j++)
            {
                mem_write(length[j], i+10+j);
            }

            return begin - size;
        }
    }
    return -1;
}

int allocate_table(int pid, int size)
{
    unsigned char mid[5];
    unsigned char address[5];
    unsigned char length[5];
    unsigned char id;

    int location;
    location = unallocate_table(size);
//    printf("%s", "location ");
//    printf("%d\n", location);
    if(location == -1)
    {
        return -1;
    }
    for(int i=0; i<1024*15; i+=15)
    {
        id = mem_read(i);
        if(id == '0')
        {
            int_to_str(pid, mid, 0);
            int_to_str(location, address, 0);
            int_to_str(size, length, 0);
            for(int j=0; j<5; j++)
            {
                mem_write(mid[j], i+j);
                mem_write(address[j], i+5+j);
                mem_write(length[j], i+10+j);
            }
            return location;
        }
    }
    return -1;
}

void restoration(v_address address1, m_pid_t pid, int actual)
{
    unsigned char mid[5];
    unsigned char address[5];
    unsigned char length[5];
    unsigned char length1[5];
    int len;

    for(int i=0; i<5; i++)
    {
        mem_write('0', actual+i);
        address[i] = mem_read(actual+i+5);
        length[i] = mem_read(actual+i+10);
    }

    for(int i = 1024*15; i<1024*30; i+=15)
    {
        for(int j=0; j<5; j++)
        {
            length1[j] = mem_read(i+j+10);
        }
        len = str_to_int(length1, 0);
        if(len == 0)
        {
            for(int j=0; j<5; j++) {
                mem_write(address[j], i+j+5);
                mem_write(length[j], i+j+10);
            }
            break;
        }
    }
}

int is_legal(int pid, int address1)
{
    unsigned char mid[5];
    unsigned char address[5];

//    printf("%s", "address ");
//    printf("%d\n", address1);
    int ppid;
    int padd;
    for(int i=0; i<1024*15; i+=15)
    {
        for(int j=0; j<5; j++)
        {
            address[j] = mem_read(i+5+j);
        }
        padd = str_to_int(address, 0);
        if(padd == address1)
        {
            for(int j=0; j<5; j++)
            {
                mid[j] = mem_read(i+j);
            }
            ppid = str_to_int(mid, 0);
            if(ppid == pid)
            {
                // 返回实际位置
                return i;
            }
        }
    }
    return -1;
}

//数字转字符串
void int_to_str(int input, unsigned char *str, int location)
{
    int num = input;
    int n = num % 100;

    for(int i=4; i>=0; i--)
    {
        str[location + i] = (unsigned char) (n);
        num = num / 100;
        n = num % 100;
    }
}

int str_to_int(unsigned char *str, int location)
{
    int result = 0;
    for(int i=0; i<4; i++){
        result = (result + str[location + i]) * 100;
    }
    result = result + str[location + 4];
    return result;
}