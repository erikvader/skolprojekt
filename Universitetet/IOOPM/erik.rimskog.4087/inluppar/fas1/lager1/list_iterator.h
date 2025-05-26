#ifndef LIST__ITERATOR_H
#define LIST__ITERATOR_H

#include "list.h"

// frees the list
// applies the freeing function f on every element with type L
// in the list. if f is NULL, then don't run any function on
// the elements.
//void list_free(list_t *list, void (*f)(void*));

typedef struct list_iterator list_iterator_t;

// returns an iterator for the list l
list_iterator_t *list_get_iterator(list_t *l);

// return true if the iterator i has something more in it
bool list_iterator_has_next(list_iterator_t *i);

// returns a pointer the next element in the iterator
// list_iterator_has_next(i) must be true.
// the return pointer is a
// pointer to the ACTUAL element in the list
L *list_iterator_next(list_iterator_t *i);

// removes the underlying entry that the last element from
// last_iterator_next has returned from the list i.
// The return value is a copy of the removed element.
L list_iterator_remove(list_iterator_t *i);

#endif
