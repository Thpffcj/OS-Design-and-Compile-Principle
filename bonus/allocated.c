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
    printf("%s", "size ");
    printf("%d\n", size);
    int begin = 0;
    int remaining = 0;
    unsigned char address[4];
    unsigned char length[4];
    for(int i = 1024; i<2048; i+=12)
    {
        for(int j=0; j<4; j++)
        {
            length[j] = mem_read(i+8+j);
        }
        int len = str_to_int(length, 0);
        printf("%s", "len ");
        printf("%d\n", len);
        if( len > size) {
            for(int j=0; j<4; j++)
            {
                address[j] = mem_read(i+4+j);
            }
            begin = str_to_int(address, 0) + size;
            printf("%s", "begin ");
            printf("%d\n", begin);
            int_to_str(begin, address, 0);
            for(int j=0; j<4; j++)
            {
                mem_write(address[j], i+4+j);
            }

            for(int j=0; j<4; j++)
            {
                length[j] = mem_read(i+8+j);
            }
            remaining = str_to_int(length, 0) - size;
            int_to_str(remaining, length, 0);
            for(int j=0; j<4; j++)
            {
                mem_write(length[j], i+8+j);
            }

            return begin - size;
        }
    }
    return -1;
}

int allocate_table(int pid, int size)
{
    unsigned char mid[4];
    unsigned char address[4];
    unsigned char length[4];
    unsigned char id;

    int location;
    location = unallocate_table(size);
    printf("%s", "location ");
    printf("%d\n", location);
    if(location == -1)
    {
        return -1;
    }
    for(int i=0; i<1024; i+=12)
    {
        id = mem_read(i);
        if(id == '0')
        {
            int_to_str(pid, mid, 0);
            int_to_str(location, address, 0);
            int_to_str(size, length, 0);
            for(int j=0; j<4; j++)
            {
                mem_write(mid[j], i+j);
                mem_write(address[j], i+4+j);
                mem_write(length[j], i+8+j);
            }
            return location;
        }
    }
    return -1;
}

int is_legal(int pid, int address1)
{
    unsigned char mid[4];
    unsigned char address[4];

    printf("%s", "address ");
    printf("%d\n", address1);
    int ppid;
    int padd;
    for(int i=0; i<1024; i+=12)
    {
        for(int j=0; j<4; j++)
        {
            address[j] = mem_read(i+4+j);
        }
        padd = str_to_int(address, 0);
        if(padd == address1)
        {
            for(int j=0; j<4; j++)
            {
                mid[j] = mem_read(i+j);
            }
            ppid = str_to_int(mid, 0);
            if(ppid == pid)
            {
                return 1;
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

    for(int i=3; i>=0; i--)
    {
        str[location + i] = (unsigned char) (n);
        num = num / 100;
        n = num % 100;
    }
}

int str_to_int(unsigned char *str, int location)
{
    int result = 0;
    for(int i=0; i<3; i++){
        result = (result + str[location + i]) * 100;
    }
    result = result + str[location + 3];
    return result;
}