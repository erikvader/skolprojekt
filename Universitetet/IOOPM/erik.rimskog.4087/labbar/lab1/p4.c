#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
  if (argc != 3)
    {
      puts("Usage: ./p4 rows growth");
      return 1;
    }
  else
    {
      int t = 0;
      int rows = atoi(argv[1]);
      int growth = atoi(argv[2]);
      int num = growth;
      for (int i = 0; i < rows; ++i)
        {
          for (int j = 0; j < num; ++j)
            {
              printf("#");
            }
          t += num;
          num += growth;
          printf("\n");
        }

      printf("Totalt:%d\n", t);
    }
  return 0;
}
