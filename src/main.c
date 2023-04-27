#include "dirVerifier.h"
#include <stdio.h>
#include <string.h>

int main() {
    printf("processing\n");
    struct stackNode* root = NULL;
    verifyDirectory(&root);
    printf("this item has been popped: %s" ,peek_and_pop(&root));
    printf("this item has been popped: %s" ,peek_and_pop(&root));
    return 0;
}


