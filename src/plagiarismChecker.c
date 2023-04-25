#include "dirVerifier.h"
#include <stdio.h>
#include <string.h>
#include "../common/folderContents.h"

int main() {
    
    folder text;
    strcpy(text.location, "..\\text");
    text.numFiles = 5;

    printf("processing\n");
    verifyDirectory(&text);
    return 0;
}


//