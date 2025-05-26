#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include "utils.h"

void clean_input_buffer(){
   int c;
   do{
      c = getchar();
   }while (c != '\n' && c != EOF);
}

// int ask_question_int(char *question){
//    int result = 0;
//    int conversions = 0;
//    do{
//       printf("%s\n", question);
//       conversions = scanf("%d", &result);
//       clean_input_buffer();
//       //putchar('\n');
//    }while (conversions < 1);
//    return result;
//}

int read_string(char* buf, int buf_size){
   int c;
   int i = 0;
   do{
      c = getchar();
      if(i < buf_size-1 && c != '\n'){
         buf[i] = c;
         i++;
      }
   }while(c != '\n' && c != EOF);
   buf[i] = '\0';
   return i;
}


// char *ask_question_string(char *question, char *buf, int buf_siz){
//    int a_len = 0;
//    do{
//       printf("%s\n", question);
//       a_len = read_string(buf, buf_siz);
//    }while(a_len <= 0);
//    return buf;
// }

// float ask_question_float(char* question){
//    int a_len = 0;
//    char buf[255];
//    do{
//       printf("%s\n", question);
//       a_len = read_string(buf, 255);
//    }while(a_len <= 0 || !is_float(buf)); //behöver egentligen inte kolla längden, is_float returnar redan false ifall buf är tom
//    return atof(buf);
// }

bool is_number(char *str){
   int len = strlen(str);
   bool num = false;
   for(int i = 0; i < len; i++){
      if(str[i] == '-'){
         if(i != 0){
            return false;
         }
      }else if (isdigit(str[i])){
         num = true;
      }else{
         return false;
      }
   }
   return num;
}

bool is_float(char *str){
   int len = strlen(str);
   int dot = false;
   bool num = false;
   for(int i = 0; i < len; i++){
      if(str[i] == '.'){
         if(dot){
            return false;
         }else{
            dot = true;
         }
      }else if(str[i] == '-'){
         if(i != 0){
            return false;
         }
      }else if(isdigit(str[i])){
         num = true;
      }else{
         return false;
      }
   }
   return num;
}

bool is_str_not_empty(char* s){
   return strlen(s) > 0;
}

answer_t ask_question(char *question, check_func check, convert_func convert){

   char buf[255];
   do{
      printf("%s\n", question);
      read_string(buf, 255);
   }while(!check(buf));
   return convert(buf);
}

int ask_question_int(char *question){
   return ask_question(question, is_number, (convert_func) atoi).i;
}

char* ask_question_string(char* question){
   return ask_question(question, is_str_not_empty, (convert_func) strdup).s;
}

float ask_question_float(char * question){
   return ask_question(question, is_float, (convert_func) atof).f;
}

answer_t leading_char(char *s){
   return (answer_t) (int) *s;
}

bool is_one_char(char *s){
   return strlen(s) == 1;
}

char ask_question_char(char *question){
   return (char) ask_question(question, is_one_char, leading_char).i;
}

char* trim(char* str){
   int i = 0;
   int b = -1, e = 0;
   while(str[i] != '\0'){
      if(b == -1 && !isspace(str[i])){
         b = i;
      }
      if(!isspace(str[i])){
         e = i;
      }
      i++;
   }

   int newlen;
   if(b == -1){
      newlen = 1;
   }else{
      newlen = (e-b) + 2;
   }
   char* r = (char*)malloc(sizeof(char) * newlen);
   for(int i = 0; i < newlen-1; i++){
      r[i] = str[b+i];
   }
   r[newlen-1] = '\0';
   return r;
}
