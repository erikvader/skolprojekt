#include <stdio.h>

typedef int(*int_fold_func)(int, int);

int fib(int i){
   if(i <= 1){
      return 1;
   }else{
      return fib(i-1) + fib(i-2);
   }
}

/// En funktion som tar en array av heltal, arrayens längd och
/// en pekare till en funktion f av typen Int -> Int -> Int
int foldl_int_int(int numbers[], int numbers_siz, int_fold_func f){
  int result = 0;

  // Loopa över arrayen och för varje element e utför result = f(result, e)
  for (int i = 0; i < numbers_siz; ++i){
    result = f(result, numbers[i]);
  }

  return result;
}

int add(int a, int b){
  return a + b;
}

int sum(int tal[], int tal_size){
   return foldl_int_int(tal, tal_size, add);
}

int main(void){
   int tal[] = {1, 2, 3, 4, 5};
   printf("%d\n", sum(tal, 5));
   return 0;
}
