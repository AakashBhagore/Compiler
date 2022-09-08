#include <stdio.h>
#include <stdlib.h>
int main (int argc, char *argv[]) {
    int array[10], index=0,i;
  for (int i = 1; i < argc; i++) {
       array[index] = atoi(argv[i]);
       index++;
  }

    int j,k,temp;
    for(j=0; j<=index; j++) 
    {
        for(k=0; k<=j; k++) 
        { 
            if(array[k] > array[j]) {
                temp     = array[k];
                array[k] = array[j];
                array[j] = temp;
            }
        }
    }
    
     for(i=1; i<=index; i++){
      printf("%d",array[i]);
      if(i<index) printf(" ");
  }
}