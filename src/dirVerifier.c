#include <stdio.h>
#include <dirent.h>
#include <errno.h>
#include "../common/fileLinkedStack.h"

int verifyDirectory(struct stackNode** root) {
    DIR *dir = opendir(FILE_PATH);
    struct dirent *ent;
    int file_count = 0;

    if ((ent = (readdir(dir))) == NULL) {
        printf("haha");
    }

    char path[MAX_PATH_SIZE];
    while ((ent = (readdir(dir))) != NULL) {
        // Skip the "." and ".." entries, which represent the current and parent directories
        if (strcmp(ent->d_name, ".") == 0 || strcmp(ent->d_name, "..") == 0) {
            continue;
        }

        strcpy(path, FILE_PATH); // copy text->location to path
        strcat(path, ent->d_name); // append ent->d_name to path

        FILE *fp = fopen(path, "r");
        
        // If the file couldn't be opened continue to the next file
        if (fp == NULL) {
            printf("Error opening file %s: %s\n", path, strerror(errno));
            continue;
        }

        fclose(fp);
        push(root, ent->d_name);
        file_count++;
        printf("%s passed file check\n", ent->d_name);
    } 

    // Check if there is only 1 file in the directory, and if so, end the function
    if (file_count  <= 1) {
        printf("Found less than 2 files, ending the function\n");
        closedir(dir);
        return 0;
    }

    // Close the directory using closedir()
    closedir(dir);

    // Return 0 to indicate success
    return 0;
}
