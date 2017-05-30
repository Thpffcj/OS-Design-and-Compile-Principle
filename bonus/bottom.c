//
// Created by SisselWu on 2017/4/15.
//

#include <stdlib.h>
#include <stdio.h>
#include "bottom.h"

count_t mem_read_time = 0;
count_t mem_write_time = 0;
count_t disk_read_time = 0;
count_t disk_write_time = 0;

data_unit memory[MEMORY_SIZE];
data_unit disk[DISK_SIZE];

data_unit mem_read(p_address address)
{
    mem_read_time += 1;

    if (address >= MEMORY_SIZE)
    {
        exit(ERR_OUT_OF_BOUNDS);
    }
    else if (mem_read_time == 0)
    {
        exit(ERR_COUNT_OVERFLOW);
    }
    else
    {
        return memory[address];
    }
}

void mem_write(data_unit data, p_address address)
{
    mem_write_time += 1;

    if (address >= MEMORY_SIZE)
    {
        exit(ERR_OUT_OF_BOUNDS);
    }
    else if (mem_write_time == 0)
    {
        exit(ERR_COUNT_OVERFLOW);
    }
    else
    {
        memory[address] = data;
    }
}

void disk_load(p_address mem_offset, p_address disk_offset, m_size_t size)
{
    p_address mem_end = mem_offset + size;
    p_address disk_end = disk_offset + size;

    count_t previous = disk_read_time;

    disk_read_time += 20000;

    if (mem_end >= MEMORY_SIZE || disk_end >= DISK_SIZE || mem_offset >= MEMORY_SIZE || disk_offset >= DISK_SIZE)
    {
        exit(ERR_OUT_OF_BOUNDS);
    }
    else
    {
        disk_read_time += 10 * size;

        if (disk_read_time < previous)
        {
            exit(ERR_COUNT_OVERFLOW);
        }

        for (int i = 0; i < size; ++i)
        {
            memory[mem_offset + i] = disk[disk_offset + i];
        }
    }
}

void disk_save(p_address mem_offset, p_address disk_offset, m_size_t size)
{
    p_address mem_end = mem_offset + size;
    p_address disk_end = disk_offset + size;

    count_t previous = disk_write_time;

    disk_write_time += 20000;

    if (mem_end >= MEMORY_SIZE || disk_end >= DISK_SIZE || mem_offset >= MEMORY_SIZE || disk_offset >= DISK_SIZE)
    {
        exit(ERR_OUT_OF_BOUNDS);
    }
    else
    {
        disk_write_time += 20 * size;

        if (disk_write_time < previous)
        {
            exit(ERR_COUNT_OVERFLOW);
        }

        for (int i = 0; i < size; ++i)
        {
             disk[disk_offset + i] = memory[mem_offset + i];
        }
    }
}

void evaluate(count_t *m_read, count_t *m_write, count_t *d_read, count_t *d_write)
{
    *m_read = mem_read_time;
    *m_write = mem_write_time;
    *d_read = disk_read_time;
    *d_write = disk_write_time;

    printf("memory read  : %llu\n", mem_read_time);
    printf("memory write : %llu\n", mem_write_time);
    printf("disk   read  : %llu\n", disk_read_time);
    printf("disk   write : %llu\n", disk_write_time);
}