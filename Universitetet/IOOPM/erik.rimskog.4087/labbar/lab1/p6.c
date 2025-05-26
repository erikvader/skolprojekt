#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>

bool is_number(char *str)
{
  int len = strlen(str);
  for(int i = 0; i < len; i++)
    {
      if (!isdigit(str[i]))
        {
          if(!(i == 0 && str[i] == '-'))
            {
              return false;
            }
        }
    }
  return true;
}

int main(int argc, char *argv[])
{
  if (!(argc == 3 && is_number(argv[1]) && is_number(argv[2])))
    {
      puts("Usage: p6 a b");
    }
  else
    {
      int a = atoi(argv[1]);
      int b = atoi(argv[2]);
      int aa = a;
      int bb = b;
      while(a != b)
        {
          if (a < b)
            {
              b -= a;
            }
          else
            {
              a -= b;
            }
        }
      printf("gcd(%d, %d) = %d\n", aa, bb, a);
    }
}
