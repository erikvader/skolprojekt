#ifndef UTILS_H
#define UTILS_H

#include <stdbool.h>
// extern char *strdup(const char*); //vaffö?

typedef union{
   int i;
   float f;
   char* s;
} answer_t;

typedef bool(*check_func)(char*);
typedef answer_t(*convert_func)(char*);

bool is_number(char *str);
bool is_float(char *str);
bool is_str_not_empty(char*);

int read_string(char *buf, int buf_siz);

answer_t ask_question(char *question, check_func check, convert_func convert);
int ask_question_int(char *question);
float ask_question_float(char *question);
char *ask_question_string(char *question);
char ask_question_char(char*);
char* trim(char*);
#endif
