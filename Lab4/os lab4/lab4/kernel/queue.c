#define QUEUE_SIZE 5
typedef struct QUEUE{
	int vals[QUEUE_SIZE];
	int index;
}queue;

typedef struct SEMAPHORE{
	int count;
	queue wait;
}semaphore;

semaphore mutex;
semaphore *p_mutex;
semaphore sput;
semaphore *p_sput;
semaphore sget;
semaphore *p_sget;

void enqueue(queue* q,int val)
{
	if(q->index>=QUEUE_SIZE){
		return;
	}
	int * vals=q->vals;
	vals[q->index]=val;
	q->index++;
}

int dequeue(queue* q)
{
	int result=0;
	int *vals=q->vals;
	int i=0;
	if(q->index==0){
		return 0;
	}
	result=vals[0];
	for(;i<QUEUE_SIZE-1;i++)
	{
		vals[i]=vals[i+1];
	}
	return result;
}

void init_semaphore(semaphore* sem){
	queue *wait=&(sem->wait);
	int* vals=wait->vals;
	int i=0;
	sem->count=1;
	wait->index=0;
	for(i=0;i<QUEUE_SIZE;i++)
	{
		vals[i]=-1;
	}	
}

/**void print_queue(queue* q){
	printf("printqueue:\n");
	int i=0;
	int* vals=q->vals;
	for(i=0;i<QUEUE_SIZE;i++)
	{
		printf("%d ",vals[i]);
		if(vals[i]<0){
			//break;
		}
	}
	printf("\n");
}
queue q0;
queue* pq=&q0;

int main(){
	int* vals=pq->vals;
	int i=0;
	pq->index=0;
	for(;i<QUEUE_SIZE;i++)
	{
		vals[i]=-1;
	}
	enqueue(pq,1);
	print_queue(pq);
	enqueue(pq,2);
	print_queue(pq);
	enqueue(pq,3);
	print_queue(pq);
	enqueue(pq,4);
	print_queue(pq);
	int a=dequeue(pq);
	print_queue(pq);
	dequeue(pq);
	print_queue(pq);
	dequeue(pq);
	print_queue(pq);
	dequeue(pq);
	print_queue(pq);	
	
}
**/
