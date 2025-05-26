#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main(int argc, char *argv[])
{
  if (argc != 2)
    {
      puts("Usage: ./p5 tal");
      return 1;
    }
  else
    {
      //finare att göra detta i en funktion så att man
      //kan göra en return istället för flera breaks.
      int n = atoi(argv[1]);
      //int limit = floor(sqrt(n)) + 1;
      int isPrime = 1;
      for (int i = 2; i <= n; ++i)
        {
          for (int j = 2; j <= n; ++j)
            {
              if (j*i == n)
                {
                  isPrime = 0;
                  break;
                }
            }
          
          if(isPrime == 0)
            {
              break;
            }
        }

      if(isPrime)
        {
          printf("PRIME!\n");
        }
      else
        {
          printf("INTE PRIME :(\n");
        }
      
    }
  return 0;
}
