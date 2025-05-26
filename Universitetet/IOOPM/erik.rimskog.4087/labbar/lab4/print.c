#include <stdio.h>

void print(char *str){
   while(*str != '\0'){
      putchar(*str);
      str += 1;
   }
}

int main(void){
   print("hej\n");
   return 0;
}
