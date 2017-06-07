//
// Created by Thpffcj on 2017/5/30.
//
#include "type.h"
#ifndef MYWORLD_ALLOCATED_H
#define MYWORLD_ALLOCATED_H

int allocate_table(int pid, int size);

int is_legal(int pid, int address1);

void restoration(v_address address1, m_pid_t pid, int actual);

void int_to_str(int input, unsigned char *str, int location);

int str_to_int(unsigned char *str, int location);

#endif //MYWORLD_ALLOCATED_H
