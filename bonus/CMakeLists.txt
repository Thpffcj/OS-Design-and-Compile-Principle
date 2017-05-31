cmake_minimum_required(VERSION 3.7)
project(MyWorld)

set(CMAKE_C_STANDARD 99)

set(SOURCE_FILES main.c allocated.c allocated.h bottom.h bottom.c call.c call.h type.h test.c test.h)
add_executable(MyWorld ${SOURCE_FILES})