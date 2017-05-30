//
// The interfaces provided for simulating the hardware
//

#ifndef MXM_BOTTOM_H
#define MXM_BOTTOM_H

#include "type.h"

#define MEMORY_SIZE 1024 * 1024 * 128
#define DISK_SIZE 1024 * 1024 * 512

#define ERR_OUT_OF_BOUNDS -10
#define ERR_COUNT_OVERFLOW -20

/*
 * ================= memory interfaces =================
 */

// read 'data' at the 'address' in the memory
data_unit mem_read(p_address address);

// write 'data' to memory at the 'address'
void mem_write(data_unit data, p_address address);

/*
 * ================= disk interfaces =================
 */

// load data from disk ( disk[disk_offset, ..., disk_offset + size - 1] )
// to memory ( memory[mem_offset, ..., mem_offset + size - 1] )
void disk_load(p_address mem_offset, p_address disk_offset, m_size_t size);

// save data from memory ( memory[mem_offset, ..., mem_offset + size - 1] )
// to disk ( disk[disk_offset, ..., disk_offset + size - 1] )
void disk_save(p_address mem_offset, p_address disk_offset, m_size_t size);

/*
 * ================= performance =================
 */
// get the usage statistics and print it out
void evaluate(count_t *m_read, count_t *m_write, count_t *d_read, count_t *d_write);

#endif //MXM_BOTTOM_H
