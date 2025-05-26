#include <stdio.h>
#include "utils.h"
#include <string.h>
#include <ctype.h>
#include <time.h>
#include <stdlib.h>

struct item{
   char *name, *desc;
   int price;
   char *shelf;
};
typedef struct item item;

void print_item(item *i){
   printf("Name: %s\nDesc: %s\nPrice: %d.%d SEK\nShelf: %s\n",
          i->name,
          i->desc,
          i->price/100,
          i->price%100,
          i->shelf);
}

item make_item(char *name, char *desc, int price, char *shelf){
   return (item) {.name=name, .desc=desc, .price=price, .shelf=shelf};
}

bool is_shelf(char *str){
   int i = 0;
   bool has_c = false, has_num = false;
   while(str[i] != '\0'){
      if(isalpha(str[i])){
         if(i == 0){
            has_c = true;
         }else{
            return false;
         }
      }else if(isdigit(str[i])){
         has_num = true;
      }else{
         return false;
      }
      i++;
   }

   return has_c && has_num;
}

char *ask_question_shelf(char *quest){
   return ask_question(quest, is_shelf, (convert_func) strdup).s;
}

item input_item(){
   char* name = ask_question_string("Name:");
   char* desc = ask_question_string("Description:");
   int price = ask_question_int("Price in ören:");
   char* shelf = ask_question_shelf("Shelf:");
   return make_item(name, desc, price, shelf);
}

char *magick(char **ar1, char **ar2, char **ar3, int len){
   char buf[255] = "";
   int r;
   char *append[] = {"-", " ", "\0"};
   char **ars[] = {ar1, ar2, ar3};
   for(int i = 0; i < 3; i++){
      r = random() % len;
      strcat(buf, ars[i][r]);
      strcat(buf, append[i]);
   }
   return strdup(buf);
}

void list_db(item *items, int len){
   for(int i = 0; i < len; i++){
      printf("%d. %s\n", i+1, items[i].name);
   }
}

void edit_db(item *db, int db_size){
   int i;
   do{
      i = ask_question_int("item att ändra:");
   }while(!(i >= 1 && i <= db_size));

   print_item(&db[i-1]);
   item new_item = input_item();
   db[i-1] = new_item;
}

int main(int argc, char *argv[]){
   char *array1[] = {"super", "asd", "asdf"};
   char *array2[] = {"duper", "muper", "gruper"};
  char *array3[] = {"makapär", "mojäng", "grej"};
  srand(time(NULL));

  if (argc < 2)
  {
    printf("Usage: %s number\n", argv[0]);
  }
  else
  {
    item db[16]; // Array med plats för 16 varor
    int db_siz = 0;    // Antalet varor i arrayen just nu

    int items = atoi(argv[1]); // Antalet varor som skall skapas

    if (items > 0 && items <= 16)
    {
      for (int i = 0; i < items; ++i)
      {
        // Läs in en vara, lägg till den i arrayen, öka storleksräknaren
        item item = input_item();
        db[db_siz] = item;
        ++db_siz;
      }
    }
    else
    {
      puts("Sorry, must have [1-16] items in database.");
      return 1; // Avslutar programmet!
    }

    for (int i = db_siz; i < 16; ++i)
      {
        char *name = magick(array1, array2, array3, 3);
        char *desc = magick(array1, array2, array3, 3);
        int price = random() % 200000;
        char shelf[] = { random() % ('Z'-'A') + 'A',
                         random() % 10 + '0',
                         random() % 10 + '0',
                         '\0' };
        item item = make_item(name, desc, price, strdup(shelf));

        db[db_siz] = item;
        ++db_siz;
      }

     // Skriv ut innehållet
     // for (int i = 0; i < db_siz; ++i)
     // {
     //   print_item(&db[i]);
     // }

    list_db(db, db_siz);

    edit_db(db, db_siz);

    list_db(db, db_siz);
  }
  return 0;
}
