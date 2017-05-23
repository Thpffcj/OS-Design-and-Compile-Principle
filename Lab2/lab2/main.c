#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

struct DirEntry{  //根目录区中的条目格式
	char DIR_Name[8];  //文件名8字节
	char DIR_Type[3];  //扩展名3字节
	char DIR_Attr;  //文件属性
	char ignore[10];  //保留位
	char DIR_WrtTime[2];  //最后一次写入时间
	char DIR_WrtDate[2];  //最后一次写入日期
	short DIR_FstClus;  //此目录对应的开始簇号
	int FileSize;   //文件大小
};

void readBoot(FILE* fat12); //读取boot
int showDir(FILE* fat12, int cluster, char* path, int endOfPath, char* target);  //读取根目录，或指定目录
void showSubDir(FILE* fat12, char* path);
int getPath(struct DirEntry* entry, char* path, int end, int flag);  //在path基础上增加新的文件（夹）名
int isValid(struct DirEntry* entry);  //entry是否有效
long clusterToPhysics(int cluster); //簇号转物理地址
int getNextCluster(FILE* fat12, int cluster); //从fat中获取下一簇号
int arrayToInt(char* c, int length);  //二进制字符串转整数
int findDir(FILE* fat12, char* path, int cluster, int flag);  //返回目标目录的第一簇号
int getDir(char* org, char* tar, int flag);  //从目标文件路径得到当前最底层路(包括文件)
void inputHelp(char* input);
int handleInput(char* input);  //处理输入,0:输出目录 1:输出文件 2:计数
void toUpper(char* s);
void readFile(FILE* fat12, char* input);
void count(FILE* fat12, char* path);
void countDir(FILE* FAT12, char* path, int cluster, int level);  //计数当前目录,递归计数其子目录
void output(char* out, int end, char* target, int flag);  //输出(判断是否是目标目录下的文件)
void printDir(char* address, int length);
void printFile(char* address, int length);
void myprint(char* address, int length);

short bytePerSec;  //扇区字节数
char secPerClus;  //每簇扇区数
short secOfBoot;  //boot扇区数
char numOfFAT;  //FAT数
short fileNumOfRoot;  //根目录可容纳文件数
short secNum;  //扇区总数
short secNumOfFAT;  //每fat扇区数

void main(){
	FILE *fat12;
	fat12 = fopen("a.img", "rb");  //读取二进制文件
	char path[100] = {0};
	readBoot(fat12);  //从引导扇区中读出需要数据
	showDir(fat12, 0, path, 0, NULL);
	int command = 0;
	while(command != -1){
		char input[50] = {0};
		char buffer[] = "please input:\n";
		myprint(buffer, 14);
		inputHelp(input);  //末尾加0
		command = handleInput(input);
		char buffer2[] = "result:\n";
		myprint(buffer2,8);
		switch(command){
			case 0:
				showSubDir(fat12, input);
				break;
			case 1:
				readFile(fat12, input);
				break;
			case 2:
				count(fat12, input);
				break;
			default:
				break;
		}
	}
	fclose(fat12);
}

void readBoot(FILE* fat12){
	fseek(fat12, 11, SEEK_SET); //把fat12指针移到文件开头11字节
	fread(&bytePerSec, sizeof(short), 1, fat12); // 读取bytePerSec
	fread(&secPerClus, sizeof(char),1,fat12);
    fread(&secOfBoot, sizeof(short),1,fat12);
    fread(&numOfFAT, sizeof(char),1,fat12);
    fread(&fileNumOfRoot, sizeof(short),1,fat12);
    fread(&secNum, sizeof(short),1,fat12);
    fseek(fat12,1,SEEK_CUR);
    fread(&secNumOfFAT, sizeof(short),1,fat12);
}

//showDir(fat12, 0, path, 0, NULL);
//showDir(fat12, entry.DIR_FstClus, nextPath, pathPointer, target);
//showDir(fat12, targetCluster, temp, i, NULL);
//FAT32文件系统中，在创建文件系统时就创建了根目录并且分配存储空间之外，其他所有目录只有在使用过程中根据需要建立。新建一个子目录时，在其父目录建立目录项，在空闲空间中为其分配一个簇，并对簇清零操作，同时将这个簇号记录在它的目录项。
//创建子目录时在为其父目录分配的簇中建立目录项，目录项描述了该子目录的起始簇号；在为子目录建立目录项的同时，也在为子目录分配的簇中，使用前两个目录项描述他与父目录的关系。
int showDir(FILE* fat12, int cluster, char* path, int endOfPath, char* target){
	int isEmpty = 1;  //当前目录是否为空,默认为空
	int fileNum = 0;  //当前目录包括文件数(文件夹和文件)
	long base = 0;  //物理地址
	int nextCluster = cluster;  //待访问簇
	struct DirEntry entry;
	if(cluster == 0) fileNum = fileNumOfRoot;  //根目录,14扇区
	else fileNum = 16;  //非根目录,1扇区
	base = clusterToPhysics(cluster);
	int i = 0;
	for(i; i<fileNum; i++){
		fseek(fat12, base, SEEK_SET);  //定位到扇区
		//接受数据的内存地址，要读的每个数据字节数，要读的数据数，输入流,返回读取到的项个数
		fread(&entry, 1, 32, fat12);  //读一个entry
		base += 32;
		if(isValid(&entry) == 0){  //对应的开始簇号为0或文件名字符名不是一般字符
			continue;
		}
		if(entry.DIR_Name[0] == 0xE5){  //则此目录为空（目录项不包含文件和目录）
			continue;
		}else{
			if(entry.DIR_Attr == 0x10){  //目录
				int pathPointer = getPath(&entry, path, endOfPath, 0);  //返回文件名长度,path中为路径
				char nextPath[100];  //拷贝一份当前路径
				int j = 0;
				for(j; j<100; j++){
					nextPath[j] = path[j];
				}
				showDir(fat12, entry.DIR_FstClus, nextPath, pathPointer, target);
				isEmpty = 0;
			}else{
				int pathPointer = getPath(&entry, path, endOfPath, 1);  //path中为路径,返回长度
				output(path, pathPointer, target, 1);
				isEmpty = 0;
			}
		}
	}
//	nextCluster = getNextCluster(fat12, nextCluster);
	if(isEmpty == 1){
		output(path, endOfPath-1, target, 0);  //打印空目录
	}
	return isEmpty;
}

void showSubDir(FILE* fat12, char* path){
	char temp[50];  //默认路径长度不会超过50
	strcpy(temp, path);
	int targetCluster = findDir(fat12, path, 0, 0);  //返回目标目录的第一簇号
	int i = 0;
	int isEmpty = 0;
	for(i; i<50; i++){
		if(temp[i] == 0){
			break;
		}
	}
	if(temp[i-1] != '/'){
		temp[i] = '/';
		i++;
	}
	if(targetCluster != -1){
		isEmpty = showDir(fat12, targetCluster, temp, i, NULL);	 //读取根目录，或指定目录
	}else{
		char buffer[] = "unknown path\n";	
		myprint(buffer, 14);
	}
	if(isEmpty == 1){
		char buffer[] = "empty dir\n";
		myprint(buffer, 11);
	}
}

void inputHelp(char* input){
	int i = 0;
	while((input[i] = getchar()) != '\n'){  //换行
		i += 1;
	}
	input[i] = 0;
}

int handleInput(char* input){
	toUpper(input);
	char count[7] = "COUNT ";
	char exit[5] = "EXIT";
	if(strstr(input, exit) != NULL){  //str2是否是str1子串
		return -1;	//终止程序
	}
	if(strstr(input, count) != NULL){  //包含count,return 2
		int j = 6;
		for(j; j<50; j++){
			input[j-6] = input[j];
		}
		return 2;
	}else{
		int i = 0;
		for(i; i<50; i++){
			if(input[i] == '.'){  //包含'.', return 1
				return 1;
			}
		}
		return 0;  //都没有,return 0
	}
}

void toUpper(char* s){
	int i = 0;
	for(i; i<50; i++){
		if(s[i] >= 'a' && s[i] <= 'z'){
			s[i] = s[i] - ('a' - 'A');
		}
	}
}

void readFile(FILE* fat12, char* input){
	int cluster = findDir(fat12, input, 0, 1);  //返回目标目录的第一簇号,成功时,path变为最里层目录名
	long base = clusterToPhysics(cluster);
	int nextCluster = cluster;
	char buffer[513];
	buffer[512] = '\n';
	buffer[512] = 0;
	do{
		base = clusterToPhysics(nextCluster);
		fseek(fat12, base, SEEK_SET);
		fread(buffer, 512, 1, fat12);
		myprint(buffer, 512);
		nextCluster = getNextCluster(fat12, nextCluster);
	}while(nextCluster != -1);
}

long clusterToPhysics(int cluster){
	long base = 0;
	if(cluster == 0){
		base = (1+18)*bytePerSec; //返回第一簇
	}else{
		base = (1+18+14+cluster-2)*bytePerSec;
	}
	return base;
}

//从fat中获取下一簇号
int getNextCluster(FILE* fat12, int cluster){
	long temp = ftell(fat12);  //文件位置相对于文件首偏移的字节数
	unsigned char high = 0;
	unsigned char low = 0;
	int next = 0;
	char pos[12];
	//fat1的开始扇区号是1,偏移为512字节
	if((cluster%2) == 0){  //偶数
		int h = (3*cluster)/2 + 1;  //4bit
		int l = (3*cluster)/2;      //8bit
		int pointer = 512 + h;
		//设置文件指针的位置
		fseek(fat12, pointer, SEEK_SET); //把fat12指针移动到文件开头pointer位置
		//接受数据的内存地址，要读的每个数据字节数，要读的数据数，返回读取到的项个数
		fread(&high, 1, 1, fat12);
		pointer = 512 + l;
		fseek(fat12, pointer, SEEK_SET); 
		fread(&low, 1, 1, fat12);
		unsigned char t = 0x08; //00001000
		int i = 11;  //倒序保存的12位
		int j = 0;
		for(j; j<4; j++){
			char c = high & t;
			if(c == 0) pos[i] = 0;
			else pos[i] = 1;
			t = t>>1; //右移1的位置
			i--;
		}
		t = 0x80;  //10000000
		j = 0;
		for(j; j<8; j--){
			char c = low & t;
			if(c == 0) pos[i] = 0;
			else pos[i] = 1;
			t = t>>1;
			i--;
		}
	}else{  //奇数
        int h = (3*cluster)/2 + 1;  //8bit
        int l = (3*cluster)/2;      //4bit
        int pointer = 512 + h;
        fseek(fat12,pointer,SEEK_SET);
        fread(&high,1,1,fat12);
        pointer = 512 + l;
        fseek(fat12,pointer,SEEK_SET);
        fread(&low,1,1,fat12);
        unsigned  char t = 0x80;
        int i = 11;
            int j = 0;
            for(j; j < 8 ; j ++){
                char c = high & t;
                if(c == 0)
                    pos[i] = 0;
                else
                    pos[i] = 1;
                t = t>>1;
                i--;
            }
            t = 0x80;
            j = 0 ;
            for(j; j < 4; j++){
                char c = low & t;
                if(c == 0 )
                    pos[i] = 0;
                else
                    pos[i] = 1;
                t = t>>1;
                i--;
            }
        }
	next = arrayToInt(pos, 12);
	if((next >= 0x00000ff7) && (next <= 0x00000fff))  //坏簇或者文件的最后一个簇
		return -1;
	fseek(fat12, temp, SEEK_SET);  //重置指针
	return next;

}

int arrayToInt(char* temp, int length){
	int n = 0;
	int i = length-1;
	for(i; i>=0; i--){
		int t = pow(2,i);
		n += t*temp[i];
	}
	return n;
}

//getPath(&entry, path, endOfPath, 0);
//getPath(&entry, path上级目录, endOfPath, 1);
int getPath(struct DirEntry* entry, char* path, int end, int flag){  //flag=1:文件  flag=0:目录
	int endOfPath = end;
	int i = 0;
	for(i; i<8; i++){
		if(entry->DIR_Name[i] == ' ') break;
		path[endOfPath] = entry->DIR_Name[i];
		endOfPath++;
	}
	if(flag == 0){
		path[endOfPath] = '/';  //文件夹以‘/’结尾
		endOfPath++;
		return endOfPath;
	}else{  //文件在末尾追加.type
		path[endOfPath] = '.';
		endOfPath++;
		i = 0;
		for(i; i<3; i++){
			if(entry->DIR_Type[i] == ' ') break;
			path[endOfPath] = entry->DIR_Type[i];
			endOfPath++;
		}
	}
	return endOfPath;
}

int isValid(struct DirEntry* entry){  //是否为一个有效簇
	int i = 0;
	if(entry->DIR_FstClus == 0)  //此条目对应的开始簇号
		return 0;
	for(i;i < 8;i++){
        char temp = entry->DIR_Name[i];
        if((temp>='a' && temp<='z')||
           (temp>='A' && temp<='Z')||
           (temp>='0' && temp<='9')||
           (temp == ' ') || (temp == '_')){
            continue;
        }else{
            return 0;
        }
    }
    i = 0 ;
    for(i;i < 3;i++){
        char temp = entry->DIR_Type[i];
        if((temp>='a' && temp<='z')||
           (temp>='A' && temp<='Z')||
           (temp>='0' && temp<='9')||
           (temp == ' ')){
            continue;
        }else{
            return 0;
        }
    }
    return 1;
}

//从目标文件路径得到当前最底层路径(包括文件)
int getDir(char* org, char* tar, int flag){  //flag=0:dir  flag=1:file
	//取当前底层目录
	//源目录除去底层目录
	int length = 0;
	int i = 0;
	for(i; i<8; i++){
		char c = org[i];
		if(flag == 0){
			if(c == '/' || c == 0){
				break;
			}
		}else{
			if(c == '/' || c == 0 || c == '.'){
				break;
			}
		}
		tar[i] = c;
	}
	length = i;
	i++;
	int j = 0;
	for(i; i<50; ++i){
		org[j] = org[i];
		j++;
	}
	return length;
}

//返回目标目录的第一簇号
//findDir(fat12, path, 0, 0);
//findDir(fat12, input, 0, 1)
int findDir(FILE* fat12, char* path, int cluster, int flag){
	int fileNum = 0;
	int nextCluster = cluster;
	int length;  //当前目录名长度
	char dir[8] = {' '};
	length = getDir(path, dir, flag);  //从目标文件路径得到当前最底层路(包括文件),path除去最底层路径,dir最底层路径,返回长度
	if(cluster == 0) fileNum = fileNumOfRoot;  //根目录可容纳文件数
	else fileNum = 16;
	struct DirEntry entry;
	do{
		long base = clusterToPhysics(nextCluster);
		int i = 0;
		for(i; i<fileNum; i++){
			fseek(fat12, base, SEEK_SET);
			fread(&entry, 1, 32, fat12);  //每个数据1字节，32个数据
			base += 32;
			if(isValid(&entry) == 0){  //无效簇
				continue;
			}
			int j = 0;
			do{  //目录是否相同
				if(dir[j] != entry.DIR_Name[j]){
					break;
				}
				j++;
			}while(j<8);
			int p = 0;
			for(p; p<8; p++){  //
				if(entry.DIR_Name[p] == ' '){
					break;
				}
			}
			if(j == length && length == p){  //命中
				int k = 0;
				for(k; k<50; k++){  //检查是否还有下一级目录或文件
					if(path[k] == '/' || path[k] == '.')  //通过是否还有'/''.'判断
					break;
				}
				if((k == 50 && path[0] == 0 && flag == 0) || (k == 50 && flag == 1)){  //没有下一级目录,找到,返回结果
					int n = 0;
					for(n; n<length; n++){
						path[n] = dir[n];
					}
					path[n] = 0;
					return entry.DIR_FstClus;
				}else{
					return findDir(fat12, path, entry.DIR_FstClus, flag);  //继续搜索子目录
				}
			}
		}
		if(nextCluster == 0){
			return -1;
		}
		nextCluster = getNextCluster(fat12, nextCluster);  //从fat中获取下一簇号
	}while(nextCluster != -1);
	return -1; //没找到
}

void count(FILE* fat12, char* path){
	int cluster = findDir(fat12, path, 0, 0);  //返回目标目录的第一簇号
	if(cluster != -1){
		countDir(fat12, path, cluster, 0);
	}else{
		char buffer[] = "Not a directory!\n";
		myprint(buffer, 18);
	}
}

void countDir(FILE* fat12, char* path, int cluster, int level){
	long base = clusterToPhysics(cluster);  
	int nextCluster = cluster;
	int fileNum = 16;
	char dir[] = {'0','1','2','3','4','5','6','7','8','9'};
	char* dirs = dir;
	char file[] = {'0','1','2','3','4','5','6','7','8','9'};
	char* files = file;
//	char buffer[100] = {0};
	struct DirEntry entry;
	if(cluster == 0){
		fileNum = fileNumOfRoot;
	}
	do{
		int i = 0;
		for(i; i<fileNum; i++){  //扫描一个簇内的文件
			fseek(fat12, base, SEEK_SET);
			fread(&entry, 1, 32, fat12);
			base += 32;
			if(isValid(&entry) == 1){
				if(entry.DIR_Attr == 0x10){
					dirs++;
				}else{
					files++;
				}
			}
		}
		nextCluster = getNextCluster(fat12, nextCluster);
	}while(nextCluster != -1);  //目录下一个簇号为-1时退出
	int z = 0;
	int e = 0;
	int length = 0;	
	for(z; z<level; z++){  //输出level个"    ",区分层次
//		char space[] = "    ";
//		e += sprintf(buffer, "%s", space);
		char buffer[] = "  ";
		myprint(buffer, 2);
	}
//	e += sprintf(buffer+level*3, "%s: dir", path);
	int pathLength = strlen(path);
	myprint(path, pathLength);
	myprint(": ", 2);
//	e += sprintf(buffer+e,"%d, file, ",dir);
	myprint(files, 1);
	char buffer1[] = " files, ";
	myprint(buffer1, 8);
	myprint(dirs, 1);
	char buffer2[] = " directories\n";
	myprint(buffer2, 14);	
//  e += sprintf(buffer+e , "%d\n",file);
//	myprint(buffer, e);
	base = clusterToPhysics(cluster);
	nextCluster = cluster;
	fileNum = 16;
	if(cluster == 0) fileNum = fileNumOfRoot;
	do{
		int i = 0;
		for(i; i<fileNum; i++){
			fseek(fat12, base, SEEK_SET);
			fread(&entry, 1, 32, fat12);
			base += 32;
			if(isValid(&entry)){
				if(entry.DIR_Attr == 0x10){
					char next[9] = {0};
					strcpy(next, entry.DIR_Name);
					int j = 0;
					for(j; j<9; j++){
						if(next[j] == ' '){
							next[j] = 0;
							break;
						}
					}
					countDir(fat12, next, entry.DIR_FstClus, level+1);  //递归搜索下一目录
				}
			}
			
		}
		nextCluster = getNextCluster(fat12, nextCluster);
	}while(nextCluster != -1);
}

//output(path, pathPointer, target, 1);
void output(char* out, int end, char* target, int flag){
//	char s[100] = {0};
	int dirLength = 0;
	int fileLength = 0;
	if(flag == 0){
		int i = 0;
		out[end] = '\n';
		end += 1;
		printDir(out, end);
	}else{
		char dir[50] = {0};
		char file[15] = {0};
		int index = end;
		for(index; index>=0; index--){
			if(out[index] == '/'){
				break;
			}
		}
		int i = 0;
		for(i; i<=index; i++){
			dir[i] = out[i];
			dirLength++;
		}
		i = 0;
		index += 1;
		for(index; index<end; index++){
			file[i] = out[index];
			i++;
			fileLength++;
		}
		file[i] = '\n';
//		int c = sprintf(s, "%s", dir);
		printDir(dir, dirLength+1);
//		c = sprintf(s, "%s", file);
		printFile(file, fileLength+1);
	}
}
