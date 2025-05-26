#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void print_number(int a){
   char out[18] = "";
   if(a % 3 == 0) strcat(out, "fizz ");
   if(a % 5 == 0) strcat(out, "buzz ");

   int len = strlen(out);
   if(len == 0){
      sprintf(out, "%d ", a);
   }

   len = strlen(out);
   out[len-1] = '\0'; //ta bort sista space
   printf("%s", out);

}

int main(int argc, char *argv[])
{
   if(argc != 2){
      puts("gör rätt!");
   }else{
      int times = atoi(argv[1]);
      for(int i = 1; i <= times; i++){
         print_number(i);
         if(i < times) printf(", ");
      }
      printf("\n");
   }
   return 0;
}
