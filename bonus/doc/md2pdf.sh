#!/bin/sh

pandoc -N -s --toc --smart --latex-engine=xelatex -V CJKmainfont='PingFang SC' -V mainfont='Monaco' -V geometry:margin=1in 实验指南.md -o 实验指南.pdf
