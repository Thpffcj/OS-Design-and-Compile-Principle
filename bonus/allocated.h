//
// Created by Thpffcj on 2017/5/30.
//

#ifndef MYWORLD_ALLOCATED_H
#define MYWORLD_ALLOCATED_H

int allocate_table(int pid, int size);

int is_legal(int pid, int address);

void int_to_str(int input, unsigned char *str, int location);

int str_to_int(unsigned char *str, int location);

#endif //MYWORLD_ALLOCATED_H
