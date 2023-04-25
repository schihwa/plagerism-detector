#include <stdio.h>
#include <dirent.h>
#include <errno.h>
#include "../common/folderContents.h"


int verifyDirectory(folder* text) {
    DIR *dir = opendir(text->location);
    struct dirent *ent;
    int file_count = 0;
    printf("yes");

    while ((ent = readdir(dir)) != NULL) {
        // Skip the "." and ".." entries, which represent the current and parent directories
        if (strcmp(ent->d_name, ".") == 0 || strcmp(ent->d_name, "..") == 0) {
            continue;
        }
    
        // Construct the file path by appending the file name to the directory path
        char path[100];
        printf(path, sizeof(path), "..\\text\\%s", ent->d_name);
        FILE *fp = fopen(path, "r");

        // If the file couldn't be opened continue to the next file
        if (fp == NULL) {
            printf("Error opening file %s: %s\n", path, strerror(errno));
            continue;
        }
        fclose(fp);
        file_count++;
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
