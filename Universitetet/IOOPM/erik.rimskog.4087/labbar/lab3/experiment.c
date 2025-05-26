#include <stdbool.h>
#include <stdio.h>
#include "utils.h"
#include <stdlib.h>

answer_t ask_question(char *question, check_func check, convert_func convert){
   char buf[255];
   do{
      printf("%s\n", question);
      read_string(buf, 255);
   }while(!check(buf));
   return convert(buf);
}

int ask_question_int(char *question){
  answer_t answer = ask_question(question, is_number, (convert_func) atoi);
  return answer.i; // svaret som ett heltal
}

int main(void){
   
   return 0;
}
