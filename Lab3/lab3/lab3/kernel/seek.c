#include "const.h"

//查找input字符串中的target字符串
//返回结果int *result 第一位代表找到的数目其余各位代表发现target在input中的下标 
PUBLIC void find_string(char *input, char *target, int *result) {

    int result_size = 0;
    result[0] = 0;

    int input_len = str_length(input);
    int target_len = str_length(target);

    int i;
    int j;
    for (i = 0; i < input_len; i++) {

        //发现首字母匹配
        if (input[i] == target[0]) {
            int index = i;
            //检查结果
            int match = 1;

            //检查是否匹配target剩余的字母
            for (j = 0; j < target_len; j++) {
                if (input[index] == target[j]) {
                    index++;
                    //发现有不一致的字母
                } else {
                    match = 0;
                }
            }

            //成功匹配目标字符串
            if (match) {
                result[0]++;
                result_size++;
                result[result_size] = i;
            }
        }
        //首字母都不匹配,继续查找下一位
    }
}

//计算字符串的长度
PUBLIC int str_length(char *str) {
    int i;
    int size = 0;

    for (i = 0; ; i++) {
        if (str[i] != '\0') {
            size++;
        } else {
            break;
        }
    }

    return size;
}
