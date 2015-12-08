'''
Created on 2015年12月8日

@author: john.wq
'''
from ctypes.wintypes import PINT

gen = (x*x for x in range(1,11))
print(next(gen))
print(next(gen))
print(next(gen))


def fibo(max):
    a,b,count = 0,1,0;
    while count < max:
        yield(b)
        a,b = b,a+b
        count = count+1
    return 'done'

gen = fibo(9)
while True:
    try:
        print(next(gen))
    except StopIteration as si:
        print(si.value)
        break