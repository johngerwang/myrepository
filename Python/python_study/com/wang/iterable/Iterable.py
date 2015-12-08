'''
Created on 2015年12月8日

@author: john.wq
'''
#from _collections_abc import Iterable
from collections import Iterable,Iterator


print(isinstance([], Iterable))
print(isinstance({}, Iterable))
print(isinstance("str", Iterable))
print(isinstance((), Iterable))
print(isinstance(set(), Iterable))
print(isinstance((x for x in range(1,10)), Iterable))

print(isinstance((x for x in range(1,10)), Iterator))
print(isinstance(iter([]), Iterator))