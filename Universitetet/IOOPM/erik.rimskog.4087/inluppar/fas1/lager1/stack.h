#ifndef __STACK_H__
#define __STACK_H__

#include <stdbool.h>

typedef struct list stack_t;
typedef void* L;

stack_t *stack_new();

void stack_push(stack_t* stack, L ele);

bool stack_pop(stack_t* stack, L *ele);

bool stack_is_empty(stack_t* stack);

#endif
