#include "utils.h"
#include <stdlib.h>
#include <stdio.h>
#include <time.h>

int main(void){

   char *name = ask_question_string("Vad är ditt namn?");

   printf("gissa talet :) %s\n", name);

   srand(time(NULL));

   int r = random() % 100 +1;

   int won = 0;
   int guess = 0;
   int i;
   for(i = 0; i < 15; i++){
      guess = ask_question_int("En gissning ty:");
      if(r == guess){
         won = 1;
         break;
      }else if (r < guess){
         printf("gissa mindre!\n");
      }else{
         printf("gissa större!\n");
      }
   }

   if(won == 0){
      printf("du suger! Talet var %d!\n", r);
   }else{
      printf("YAY! du vann! grattis %s! %d behövde du bara!\n", name, i+1);
   }

   return 0;
}
