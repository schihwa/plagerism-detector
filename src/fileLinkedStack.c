
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <stdio.h>
#include "../common/macros.h"


struct stackNode {
    char fileName[MAX_PATH_SIZE];
    struct stackNode* next;
};

void push(struct stackNode** root, char* fileName)
{
    struct stackNode* stackNode = (struct stackNode*) malloc(sizeof(struct stackNode));
    strcpy(stackNode->fileName, fileName);
    stackNode->next = *root;
    *root = stackNode;
    printf("%s pushed to stack\n", fileName);
}

char* peek_and_pop(struct stackNode** root)
{
    if (!*root){
        return NULL;
    }
    struct stackNode* temp = *root;
    *root = (*root)->next;
    char* popped = strdup(temp->fileName);
    free(temp);
    return popped;
}