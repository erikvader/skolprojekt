#include "stack.h"
#include "list.h"

stack_t *stack_new(){
   return list_new();
}

void stack_push(stack_t* stack, L ele){
   list_prepend(stack, ele);
}

bool stack_pop(stack_t *stack, L *ele){
   return list_remove(stack, 0, ele);
}

bool stack_is_empty(stack_t *stack){
   return list_length(stack) <= 0;
}
