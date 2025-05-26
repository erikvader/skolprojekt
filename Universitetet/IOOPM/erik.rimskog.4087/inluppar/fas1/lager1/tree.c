#include <stdio.h>
#include "tree.h"
#include <stdlib.h>
#include <string.h>
#include "list.h"

typedef struct tree_node tree_node;
struct tree_node {
   T data;
   K key;
   tree_node* parent;
   union {
      struct{
         tree_node* left;
         tree_node* right;
      };
      tree_node* child[2];
   };
};

struct tree{
   tree_node *root;
};

tree_t* tree_new(){
   tree_t *new = malloc(sizeof(tree_t));

   if (new != NULL)
      new->root = NULL;
   
   return new;
}

tree_node* new_node(K key, T data, tree_node* parent){
   tree_node* new = malloc(sizeof(tree_node));

   if (new != NULL) {
      new->key = key;
      new->data = data;
      new->parent = parent;
      new->left = NULL;
      new->right = NULL;
   }
   
   return new;
}

void tree_node_delete(tree_node *node, tree_action cleanup){
   if(node == NULL) return;

   tree_node_delete(node->left, cleanup);
   tree_node_delete(node->right, cleanup);

   if (cleanup != NULL)
      cleanup(node->key, node->data);

   free(node);
}

void tree_delete(tree_t* t, tree_action cleanup){
   if(t->root != NULL){
      tree_node_delete(t->root, cleanup);
   }
   free(t);
}

bool tree_empty(tree_t* t){
   return t->root == NULL;
}

bool isRoot(tree_node* t){
   return t->parent == NULL;
}

// finds where key should be placed in a tree_t t
// find_node(&t->root, &par)
// saves parent in par and returns pointer to
// a pointer in parent that points on the found item.
// if parent is NULL, then t is empty and &t->root is returned
tree_node **find_node(tree_node **t, tree_node **parent, K key){
   if(*t == NULL){ //empty tree
      return t;
   }

   if(parent != NULL) *parent = *t;

   int comp = strcmp(key, (*t)->key);

   if(comp < 0){
      return find_node(&(*t)->left, parent, key);
   }else if(comp > 0){
      return find_node(&(*t)->right, parent, key);
   }else{
      return t;
   }
}

bool tree_insert(tree_t* t, K key, T elem){
   tree_node *parent = NULL;
   tree_node **spot = find_node(&(t->root), &parent, key);
   if(*spot == NULL){
      *spot = new_node(key, elem, parent);
      return true;
   }else{
      return false;
   }
}

void tree_node_keys(tree_node *n, list_t *l){
   if(n == NULL) return;
   tree_node_keys(n->left, l);
   list_append(l, n->key);
   tree_node_keys(n->right, l);
}

K* tree_keys(tree_t* t){
   list_t *l = list_new();
   tree_node_keys(t->root, l);
   K* arr = (K*) list_to_array(l);
   list_delete(l, NULL);
   return arr;
}

void tree_node_elements(tree_node *n, T *arr, int *i){
   if(n == NULL) return;
   tree_node_elements(n->left, arr, i);
   arr[*i] = n->data;
   (*i)++;
   tree_node_elements(n->right, arr, i);
}


T* tree_elements(tree_t *t){
   int size = tree_size(t);
   if(size == 0) return NULL;
   T *arr = malloc(sizeof(T) * size);
   int i = 0;
   tree_node_elements(t->root, arr, &i);
   return arr;
}

T tree_get(tree_t *tree, K key){
   tree_node **spot = find_node(&(tree->root), NULL, key);
   if(*spot == NULL){
      return NULL;
   }else{
      return (*spot)->data;
   }
}

bool tree_has_key(tree_t *tree, K key){
   return *find_node(&(tree->root), NULL, key) != NULL;
}

int tree_node_size(tree_node *t){
   if(t == NULL){
      return 0;
   }else{
      return tree_node_size(t->left) + tree_node_size(t->right) + 1;
   }
}

int tree_size(tree_t *tree){
   return tree_node_size(tree->root);
}

//╼├└│─
// void printTree(tree_node* t, linkedlist* p, int last){
//    char* pre = strbuilder_toString(p);
//    printf("%s", pre);
//    free(pre);
//    if(last){
//       printf("└──");
//    }else{
//       printf("├──");
//    }

//    if(t == NULL){
//       printf(">\n");
//    }else{
//       printf("%d\n", t->n);
//       if(last){
//          strbuilder_pushString(p, "   ");
//       }else{
//          strbuilder_pushString(p, "│  ");
//       }
//       printTree(t->right, p, 0);
//       printTree(t->left, p, 1);
//       for(int i = 0; i < 3; i++){
//          strbuilder_popChar(p);
//       }
//    }
// }

// void tree_node_printTree(tree_node* t){
//    linkedlist* prefix = linkedlist_new();
//    printTree(t, prefix, 1);
//    linkedlist_free(prefix, NULL);
// }

tree_node* findMax(tree_node* t){
   tree_node* cur = t;
   while(cur->right != NULL){
      cur = cur->right;
   }
   return cur;
}

tree_node* inorderPredecessor(tree_node* t){
   if(t->left != NULL){
      return findMax(t->left);
   }else{
      tree_node* cur = t;
      tree_node* par = t->parent;
      while(par != NULL && par->left == cur){
         cur = par;
         par = cur->parent;
      }
      return par;
   }
}

//probably correct
tree_node* delete(tree_node** t){
   tree_node* node = *t;
   if(node->left != NULL && node->right != NULL){
      tree_node* ip = inorderPredecessor(node);
      ip = delete(&ip->parent->left);
      ip->parent = node->parent;
      ip->left = node->left;
      ip->right = node->right;
      *t = ip;
      ip->left->parent = ip;
      ip->right->parent = ip;
      node->left = NULL;
      node->right = NULL;
   }else if(node->left != NULL){
      *t = node->left;
      (*t)->parent = node->parent;
      node->left = NULL;
   }else if(node->right != NULL){
      *t = node->right;
      (*t)->parent = node->parent;
      node->right = NULL;
   }else{
      *t = NULL;
   }
   node->parent = NULL;
   return node;
}

T tree_remove(tree_t* tree, K key){
   tree_node *par = NULL;
   tree_node **found = find_node(&tree->root, &par, key);
   tree_node* d = delete(found);
   T data = d->data;
   free(d);
   return data;
}
