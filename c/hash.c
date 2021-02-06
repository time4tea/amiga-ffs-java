

#include<strings.h>
#include <stdio.h>

#define BSIZE 512

int HashName(const char *name)
{
	unsigned long hash;
	size_t l;				/* sizeof(int)>=2 */
	int i;

	l=strlen(name);
	hash = l;
	for(i=0; i<l; i++) {
		hash=hash*13;
		hash=hash + toupper(name[i]);	/* not case sensitive */
		hash=hash & 0x7ff;
        }
	hash=hash % ((BSIZE/4)-56);		/* 0 < hash < 71 in the case of 512 byte blocks */

	return(hash);
}

int main(int argc, char*argv[]) {

	char* name = argv[1];

	printf("Hash of %s is %d\n", name, HashName(name) );
}
