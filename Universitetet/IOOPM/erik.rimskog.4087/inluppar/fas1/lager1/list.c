#include "list.h"
#include "list_iterator.h"
#include <stdlib.h>

typedef struct node node_t;
struct node {
   L ele;
   node_t *next;
};

struct list {
   node_t *first, *last;
   int len;
};

struct list_iterator{
   node_t **cur;
   node_t **prev;
   node_t *prevprev;
   list_t *list;
};

list_t *list_new(){
   list_t *new = malloc(sizeof(list_t));
   if(new){
      new->first = NULL;
      new->last = NULL;
      new->len = 0;
   }
   return new;
}

node_t *node_new(L ele){
   node_t *new = malloc(sizeof(node_t));
   if(new){
      new->ele = ele;
      new->next = NULL;
   }
   return new;
}

//converts negative index to corresponding positive index
//(the returned value can still be negative if |index|+1 > list_len (might not be exactly this))
//if index is positive, then it is returned back
//  0  1  2
// -3 -2 -1
int actual_index(int list_len, int index){
   if(index < 0){
      index = list_len + index;
   }
   return index;
}

//checks if index is a valid index for list with
//length list_len
//index >= 0
// if list_len == 3 then valid indexes
// 0 1 2
bool valid_index(int list_len, int index){
   return index < list_len && index >= 0;
}


bool list_insert(list_t *list, int index, L elem){
   index = actual_index(list->len+1, index);
   if(!valid_index(list->len+1, index)){
      return false;
   }

   node_t *new = node_new(elem);

   if(list->len == 0){
      list->first = new;
      list->last = new;
   }else if(index == list->len){
      list->last->next = new;
      list->last = new;
   }else{
      node_t **cur = &list->first;
      for(int i = 0; i < index; i++){
         cur = &((*cur)->next);
      }
      new->next = *cur;
      *cur = new;
      if(new->next == NULL){
         list->last = new;
      }
   }
   list->len++;
   return true;
}

void list_append(list_t *list, L elem){
   list_insert(list, -1, elem);
}

void list_prepend(list_t *list, L elem){
   list_insert(list, 0, elem);
}

int list_length(list_t *list){
   return list->len;
}

L *list_get(list_t *list, int index){
   index = actual_index(list->len, index);
   if(!valid_index(list->len, index)){
      return false;
   }
   node_t *cur = list->first;
   for(int i = 0; i < index; i++){
      cur = cur->next;
   }
   return &(cur->ele);
}

L *list_first(list_t *list){
   return list_get(list, 0);
}

L *list_last(list_t *list){
   return list_get(list, -1);
}

// removes a specified node 'cur' from the list 'list'
// cur -> pointer to the pointer that points to the node to be removed
//        suitable value to remove the first node is &list->first (fixes the special case)
//
// prev -> pointer to the previous node (the node that *cur exists in, or NULL if we remove the first node)
//
// list -> the list
//
// ele -> placeholder to store the removed element
void remove_node(node_t **cur, node_t *prev, list_t *list, L *ele){
   *ele = (*cur)->ele;
   node_t *removed = *cur;
   *cur = (*cur)->next;

   if(list->last == removed){
      list->last = prev;
   }
   free(removed);
   list->len--;
   
}

bool list_remove(list_t *list, int index, L *elem){
   index = actual_index(list->len, index);
   if(!valid_index(list->len, index)){
      return false;
   }

   node_t **cur = &list->first;
   node_t *prev = NULL;
   for(int i = 0; i < index; i++){
      prev = *cur;
      cur = &((*cur)->next);
   }

   remove_node(cur, prev, list, elem);
   
   return true;

}

void list_delete(list_t *list, list_action cleanup){
   node_t *cur = list->first;
   node_t *next;
   while(cur != NULL){
      next = cur->next;
      if(cleanup != NULL) cleanup(cur->ele);
      free(cur);
      cur = next;
   }
   free(list);
}

L *list_to_array(list_t *l){
   int len = list_length(l);
   if(len == 0) return NULL;
   L *arr = calloc(len, sizeof(L));
   list_iterator_t *ite = list_get_iterator(l);
   int i = 0;
   while(list_iterator_has_next(ite)){
      arr[i] = *list_iterator_next(ite);
      i++;
   }
   free(ite);
   return arr;
}

// --------------------- iterator ------------------------------

list_iterator_t *list_get_iterator(list_t *l){
   list_iterator_t* new = malloc(sizeof(list_iterator_t));
   new->prev = NULL;
   new->prevprev = NULL;
   new->cur = &l->first;
   new->list = l;
   return new;
}

L *list_iterator_next(list_iterator_t *i){
   L *ele = &((*i->cur)->ele);
   if(i->prev != NULL) i->prevprev = *i->prev;
   i->prev = i->cur;
   i->cur = &((*i->cur)->next);
   return ele;
}

bool list_iterator_has_next(list_iterator_t *i){
   return *i->cur != NULL;
}

L list_iterator_remove(list_iterator_t *i){
   L temp = NULL;
   remove_node(i->prev, i->prevprev, i->list, &temp);
   return temp;
}


