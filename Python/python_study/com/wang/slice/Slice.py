#encoding:utf-8
'''
Created on 2015年12月8日

@author: john.wq
'''


L = ["micheal","kate","tom","lilei"]

print(L[0:3])

print(L[-1])
print(L[-2:])
print(L[::2])

dic = {'a': 1, 'b': 2, 'c': 3}

for key in dic:
    print(key)
    
for value in dic.values():
    print(value)
    
for k,v in dic.items():
    print(k,v)
    
for i, value in enumerate(['A', 'B', 'C']):
    print(i,value)
    
for i, value in enumerate(('A', 'B', 'C')):
    print(i,value)
    
for x, y in [(1, 1), (2, 4), (3, 9)]:
    print(x,y)