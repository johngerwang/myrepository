#encoding:utf-8
'''
Created on 2015年12月8日

@author: john.wq
'''

l = [1,2]
l.insert(3, 3)
print(l)
l.insert(4, 4)
print(l)
l.insert(6, 6)
print(l)
l.pop(3)

l = list(range(1, 11))
print(l)

l = [x*x for x in range(1, 11)]
print(l)

l = [x*x for x in range(1,11) if x%2==0]
print(l)

l = [x*y for x in range(1,4) for y in range(2,5)]
print(l)
import os
dir = [dir for dir in os.listdir('.')]
print(dir)

L = ['Hello', 'World', 'IBM', 'Apple']
l = [s.lower() for s in L]
print(l)

L = ['Hello', 'World', 18, 'Apple', None]
l = [s.lower() for s in L if isinstance(s,str)]
print(l)