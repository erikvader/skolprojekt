# to find source files in nested directories
# sudo find . -name "*.c" | sed -r "s/.*\/(.*?)$/\1/" | xargs
CC = gcc
exe = cprog
#$(filter-out tree.c, )
sources = $(wildcard *.c)
objects=$(patsubst %.c,%.o,$(sources))
dep=$(patsubst %.o,%.d,$(objects))
flags=-MMD -Wall -g
bflags=
tags=GPATH GRTAGS GSYMS GTAGS

.PHONY: all
all: $(exe)

run: $(exe)
	./$(exe)

$(exe): $(objects)
	$(CC) $(flags) -o $@ $^ $(bflags)

-include $(dep)

.c.o:
	$(CC) $(flags) -o $@ -c $< $(bflags)

.PHONY: clean
clean:
	-rm -fr $(objects) $(exe) $(dep) $(tags)
