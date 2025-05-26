#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

struct point{
   int x;
   int y;
};
typedef struct point point;

// p1-----+
// |      |
// +-----p2
struct rectangle{
   point p1, p2;
};
typedef struct rectangle rectangle;

void translate(point *p1, point *p2){
   p1->x = p2->x;
   p1->y = p2->y;
}

void print_point(point *p){
   printf("(%d, %d)", p->x, p->y);
}

void print_rectangle(rectangle *r){
   printf("rectangle(upper_left=");
   print_point(&r->p1);
   printf(", lower_right=");
   print_point(&r->p2);
   printf(")");
}

point make_point(int x, int y){
   return (point) {.x=x, .y=y};
}

rectangle make_rect(int x1, int y1, int x2, int y2){
   return (rectangle) {.p1=make_point(x1, y1), .p2=make_point(x2, y2)};
}

int area_rect(rectangle *r){
   return abs((r->p1.x - r->p2.x) * (r->p1.y - r->p2.y));
}

bool intersects_rect(rectangle *r1, rectangle *r2){
   return !(r1->p1.x > r2->p2.x || r1->p2.x < r2->p1.x || r1->p1.y > r2->p2.y || r1->p2.y < r2->p1.y);
}

void insertion_sort(int *a, int len){
   int min, min_ind;
   for(int i = 0; i < len; i++){
      min = a[i];
      min_ind = i;
      for(int j = i+1; j < len; j++){
         if(a[j] < min){
            min_ind = j;
            min = a[j];
         }
      }
      int tmp = a[i];
      a[i] = a[min_ind];
      a[min_ind] = tmp;
   }
}

//intersects_rect(r1, r2) MUST be true
rectangle intersection_rect(rectangle *r1, rectangle *r2){
   int xs[] = {r1->p1.x, r1->p2.x, r2->p1.x, r2->p2.x};
   int ys[] = {r1->p1.y, r1->p2.y, r2->p1.y, r2->p2.y};
   insertion_sort(xs, 4);
   insertion_sort(ys, 4);
   return make_rect(xs[1], ys[1], xs[2], ys[2]);
}

int main(void){
   rectangle r = make_rect(0, 0, 5, 5);
   rectangle t = make_rect(-1, -1, 1, 1);
   print_rectangle(&r);
   printf("\n");
   print_rectangle(&t);
   printf("\n");
   printf("area=%d\n", area_rect(&r));
   printf("intersects=%d\n", intersects_rect(&r, &t));
   // int test[] = {1, 5, 7, 0};
   // insertion_sort(test, 4);
   // for(int i = 0; i < 4; i++){
   //    printf("%d", test[i]);
   // }
   // printf("\n");
   rectangle inter = intersection_rect(&r, &t);
   print_rectangle(&inter);
   printf("\n");
   return 0;
}
