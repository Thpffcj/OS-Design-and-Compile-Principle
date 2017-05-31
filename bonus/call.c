//
// Created by Thpffcj on 2017/5/28.
//

#include "type.h"
#include "bottom.h"
#include "allocated.h"

void init()
{
    unsigned char address[4];
    unsigned char legnth[4];
    for(int i = 0; i<2048; i++)
    {
        mem_write('0', i);
    }
    int_to_str(2048, address, 0);
    int_to_str(1024*1024, legnth, 0);
    for(int i=0; i<4; i++)
    {
        mem_write(address[i], 1028+i);
        mem_write(legnth[i], 1032+i);
    }
}

//进程号为 pid 的进程希望访问 address 处的数据。如果访问合法，往 data 指针里写入数据(通过 *data = xxx)，并返回 0；如果访问不合法，返回 -1
//if (allocate(&va, 1024, 1) != 0 || allocate(&vb, 1024, 2) != 0)
//if (read(&d, va, 1) != 0 || read(&d, vb, 2) != 0)
int read(data_unit *data, v_address address, m_pid_t pid)
{
    int legal = -1;
    legal = is_legal(pid, address);
    if(legal == -1)
    {
        return -1;
    }
    *data = mem_read(address);
    return 0;
}

//进程号为 pid 的进程希望往 address 处写入数据。如果访问合法，往 address 处写入 data，并返回 0；如果访问不合法，返回 -1
int write(data_unit data, v_address address, m_pid_t pid)
{
    int legal = -1;
    legal = is_legal(pid, address);
    if(legal == -1)
    {
        return -1;
    }
    mem_write(data, address);
    return 0;
}

// if (allocate(&va, 1024, 1) != 0 || allocate(&vb, 1024, 2) != 0)
// data_unit memory[MEMORY_SIZE];
int allocate(v_address *address, m_size_t size, m_pid_t pid)
{
    int location;
    location = allocate_table(pid, size);
    if(location == -1)
    {
        return -1;
    }
    *address = location;
    return 0;
}

int free(v_address address, m_pid_t pid)
{
    return 1;
}


