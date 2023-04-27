#pragma once
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <stdio.h>
#include "macros.h"

struct stackNode {
    char fileName[MAX_PATH_SIZE];
    struct stackNode* next;
};

void push(struct stackNode** root, char* fileName);

char* peek_and_pop(struct stackNode** root);


