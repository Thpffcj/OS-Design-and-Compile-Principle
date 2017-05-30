cmake_minimum_required(VERSION 3.7)
project(MyWorld)

set(CMAKE_C_STANDARD 99)

set(SOURCE_FILES main.c call.c allocated.c allocated.h)
add_executable(MyWorld ${SOURCE_FILES})