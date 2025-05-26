/// cat.c
#include <stdio.h>
#include <stdlib.h>

// /home/erik/mapp/fil.c => fil.c
char *get_file_name(char *path){
   int b = 0, i = 0;
   char *cur = path;
   while(*cur != '\0'){
      if(*cur == '/'){
         b = i;
      }
      i++;
      cur++;
   }
   if(i > b+1){
      int newlen = i-b;
      cur = (char*) malloc(sizeof(char) * (newlen+1));
      for(int j = 0; j < newlen; j++){
         cur[j] = path[b+1+j];
      }
      cur[newlen] = '\0';
   }else{
      cur = path; //eller nåt
   }

   return cur;
}

void cat(char *filename){
   FILE *f = fopen(filename, "r");
   int c, i = 1;
   char last = '\n';

   while (1){
      c = fgetc(f);
      if(c == EOF) break;
      if(last == '\n'){
         fprintf(stdout, "%d\t", i);
         i++;
      }
      fputc(c, stdout);
      last = c;
   }
   fclose(f);
}

int main(int argc, char *argv[]){
   if (argc < 2){
      fprintf(stdout, "Usage: %s fil1 ...\n", argv[0]);
   }else{
      for (int i = 1; i < argc; ++i){
         printf("====%s====\n", argv[i]);
         cat(argv[i]);
         putchar('\n');
      }
   }

   return 0;
}
