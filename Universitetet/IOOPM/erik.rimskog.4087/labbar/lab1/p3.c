#include <stdio.h>

int main(void)
{
  int t = 0;
  for (int i = 1; i < 10; ++i)
    {
      for (int j = 0; j < i; ++j)
        {
          printf("#");
          t = t + 1;
        }
      printf("\n");
    }

  printf("Totalt:%d\n", t);
  
  return 0;
}
