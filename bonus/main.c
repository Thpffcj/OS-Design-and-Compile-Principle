#include <stdio.h>
#include "allocated.h"
#include "test.h"

int main() {
    printf("Hello, World!\n");

    test1();
    test2();
    test1();
    test2();
    test1();
    test2();
    return 0;
}

