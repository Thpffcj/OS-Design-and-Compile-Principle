//
// The interfaces required to implement
//

#ifndef MXM_CALL_H
#define MXM_CALL_H

#include "type.h"

void init();

int read(data_unit *data, v_address address, m_pid_t pid);

int write(data_unit data, v_address address, m_pid_t pid);

int allocate(v_address *address, m_size_t size, m_pid_t pid);

int free(v_address address, m_pid_t pid);

#endif //MXM_CALL_H
