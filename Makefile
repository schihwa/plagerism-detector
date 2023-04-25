ifeq ($(OS),Windows_NT)
	RM = del /q
	MKDIR = mkdir
else
	RM = rm -rf
	MKDIR = mkdir -p
endif

CC=gcc
CFLAGS=-Wall -Werror -I include

SRCDIR=src
OBJDIR=build
BINDIR=bin

SOURCES=$(wildcard $(SRCDIR)/*.c)
OBJECTS=$(patsubst $(SRCDIR)/%.c, $(OBJDIR)/%.o, $(SOURCES))

TARGET=$(BINDIR)/plagiarismDetector.exe

.PHONY: all clean run

all: $(TARGET)

$(TARGET): $(OBJECTS) | $(BINDIR)
	$(CC) $(CFLAGS) -o $@ $^

$(OBJDIR)/%.o: $(SRCDIR)/%.c | $(OBJDIR)
	$(CC) $(CFLAGS) -c -o $@ $<

$(OBJDIR):
	$(MKDIR) $(OBJDIR)

$(BINDIR):
	$(MKDIR) $(BINDIR)

run: $(TARGET)
	$(TARGET) text/myfile.txt text/new.txt

clean:
	$(RM) $(OBJDIR) $(BINDIR)
