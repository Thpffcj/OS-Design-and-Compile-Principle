//
// Created by SisselWu on 2017/5/2.
//

#include "call.h"
#include "bottom.h"
#include <stdio.h>

#define Fail(tips) \
        printf(tips); \
        printf("\n");\
        return -1;

#define Success(tips) \
        printf(tips); \
        printf("\n");\
        return 0;

// memory protection
int test1()
{
    init();

    v_address va, vb;
    if (allocate(&va, 1024, 1) != 0 || allocate(&vb, 1024, 2) != 0)
    {
        Fail("test1: fail, allocation");
    }

    data_unit d;
    if (read(&d, va, 1) != 0 || read(&d, vb, 2) != 0)
    {
        Fail("test1: fail, normal access");
    }

    if (read(&d, va + 1024, 1) != -1 || read(&d, vb - 10233, 2) != -1)
    {
        Fail("test1: fail, illegal access");
    }

    Success("test1: pass");
}

// basic read/write
int test2()
{
    init();

    v_address va;
    if (allocate(&va, 1024, 1) != 0)
    {
        Fail("test2: fail, allocation");
    }

    data_unit d = 'w';
    if (write(d, va, 1) != 0)
    {
        Fail("test2: fail, write");
    }

    data_unit r;
    if (read(&r, va, 1) != 0 || r != d)
    {
        Fail("test2: fail, read");
    }

    Success("test2: pass");
}

// locality
int test3()
{
    init();

    v_address va;
    if (allocate(&va, 1024, 1) != 0)
    {
        Fail("test3: fail, allocation");
    }

    data_unit d = 'w';
    if (write(d, va, 1) != 0)
    {
        Fail("test3: fail, write");
    }

    count_t mr, mw, dr, dw;
    evaluate(&mr, &mw, &dr, &dw);

    for (int i = 0; i < 233; ++i)
    {
        if (write(d, va + i, 1) != 0)
        {
            Fail("test3: fail, loop");
        }
    }

    count_t mr2, mw2, dr2, dw2;
    evaluate(&mr2, &mw2, &dr2, &dw2);
    if (dr2 - dr > 0 || dw2 - dw > 0)
    {
        Fail("test3: fail, disk access");
    }
    else
    {
        Success("test3: pass");
    }
}

// locality, multiple processes
int test4()
{
    init();

    v_address va, vb;
    if (allocate(&va, 1024 * 1024 * 70, 1) != 0 || allocate(&vb, 1024 * 1024 * 70, 2) != 0)
    {
        Fail("test4: fail, allocation");
    }

    count_t mr, mw, dr, dw;
    evaluate(&mr, &mw, &dr, &dw);

    data_unit d = 'k';
    data_unit v;
    for (int i = 0; i < 23333; ++i)
    {
        if (write(d, va, 1) != 0
            || write(d, va + 1024 * 1024 * 60, 1) != 0
            || write(d, vb, 2) != 0
            || write(d, vb + 1024 * 1024 * 60, 2) != 0)
        {
            Fail("test4: fail, loop");
        }

        if (read(&v, va, 1) != 0
            || read(&v, va + 1024 * 1024 * 60, 1) != 0
            || read(&v, vb, 2) != 0
            || read(&v, vb + 1024 * 1024 * 60, 2) != 0)
        {
            Fail("test4: fail, loop");
        }
    }

    count_t mr2, mw2, dr2, dw2;
    evaluate(&mr2, &mw2, &dr2, &dw2);
    if (dr2 - dr > 32000000 || dw2 - dw > 64000000)
    {
        Fail("test4: fail, disk access");
    }
    else
    {
        Success("test4: pass");
    }
}

// basic allocation
int test5()
{
    init();
    v_address va, vb, vc, vd;
    if (allocate(&va, 1024 * 1024 * 8, 1) != 0 || allocate(&vb, 1024 * 1024 * 8, 2) != 0
        || allocate(&vc, 1024 * 1024 * 8, 3) != 0 || allocate(&vd, 1024 * 1024 * 8, 4) != 0)
    {
        Fail("test5: fail, allocation");
    }

    Success("test5: pass");

}

// basic allocation/free
int test6()
{
    init();

    v_address vs[91];
    m_size_t size = 1024 * 1024;

    count_t mr1, mw1, dr1, dw1;
    evaluate(&mr1, &mw1, &dr1, &dw1);

    // pid: 1 - 90
    for (m_size_t i = 0; i < 90; ++i)
    {
        if (allocate(vs + i, size, i+1) != 0)
        {
            Fail("test6: fail, allocation");
        }
    }

    // pid: 1 - 80
    for (m_size_t j = 0; j < 80; ++j)
    {
        if (free(vs[j], j+1) != 0)
        {
            Fail("test6: fail, free");
        }
    }

    // pid: 1 - 80
    for (m_size_t i = 0; i < 80; ++i)
    {
        if (allocate(vs + i, size, i+1) != 0)
        {
            Fail("test6: fail, allocation2");
        }
    }

    count_t mr2, mw2, dr2, dw2;
    evaluate(&mr2, &mw2, &dr2, &dw2);
    if (dr2 - dr1 > 0 || dw2 - dw1 > 0)
    {
        Fail("test6: fail, disk access");
    }
    else
    {
        Success("test6: pass");
    }

    Success("test6: pass");
}

// fragment handle
int test7()
{
    init();

    v_address vs[91];
    m_size_t size = 1024 * 1024;

    count_t mr1, mw1, dr1, dw1;
    evaluate(&mr1, &mw1, &dr1, &dw1);

    // pid : 1 - 90
    for (m_size_t i = 0; i < 90; ++i)
    {
        if (allocate(vs + i, size, i+1) != 0)
        {
            Fail("test7: fail, allocation");
        }
    }

    // pid : 1, 3, ..., 89
    for (m_size_t j = 0; j < 45; ++j)
    {
        if (free(vs[j * 2], j*2+1) != 0)
        {
            Fail("test7: fail, free");
        }
    }

    if (allocate(vs + 90, size * 45, 91) != 0)
    {
        Fail("test7: fail, fragments");
    }

    count_t mr2, mw2, dr2, dw2;
    evaluate(&mr2, &mw2, &dr2, &dw2);
    if (dr2 - dr1 > 0 || dw2 - dw1 > 0)
    {
        Fail("test7: fail, disk access");
    }
    else
    {
        Success("test7: pass");
    }
}

// big capacity allocation
int test8()
{
    init();
    v_address va;
    m_size_t size = 1024 * 1024 * 233;
    if (allocate(&va, size, 1) != 0)
    {
        Fail("test8: fail, big allocation");
    }

    data_unit d1 = 'o';
    data_unit d2 = 'v';
    data_unit v1, v2;

    v_address vb = va + size - 2;

    if (write(d1, va, 1) != 0 || write(d2, vb, 1) != 0)
    {
        Fail("test8: fail, write");
    }

    if (read(&v1, va, 1) != 0 || read(&v2, vb, 1) != 0 || v1 != d1 || v2 != d2)
    {
        Fail("test8: fail, read");
    }

    Success("test8: pass");
}

