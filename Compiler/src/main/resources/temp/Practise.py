import sys
# total arguments
n = len(sys.argv)
# m = []
m = sys.argv
# print(m)


for i in range(1,n):
    m[i]= (int)(sys.argv[i])
 
    
# arr = [5, 2, 8, 7, 1];     
temp = 0;    
       
# print("Elements of original array: ");    
# for i in range(0, len(arr)):    
#     print(arr[i], end=" ");    
     
#Sort the array in ascending order    
for i in range(1, n):    
    for j in range(i+1, n):    
        if(m[i] > m[j]):    
            temp = m[i];    
            m[i] = m[j];    
            m[j] = temp;    
# print(m)
      
# for i in range(1,n):
#      print(m[i])
     
s = ""
for i in range(1,n):
    if(i<n-1):
        s = s+str(m[i])+" "
    else:
        s = s+str(m[i])
    
print(s)