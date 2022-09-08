#include <iostream>
#include <stdlib.h>
using namespace std;

int main(int argc, char** argv) {
    
    int number = atoi(argv[1]);
    // cout<<number;
    
    for(int row=0; row<number; row++) {
        for(int column=0; column<=row; column++) {
            cout<<"*";
        }
        cout<<"\n";
    }
    return 0;
}