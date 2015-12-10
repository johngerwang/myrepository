'''
Created on 2015年12月9日

@author: john.wq
'''

int("12345")
 

def comparator(x):
    return x[1]
print(max('ah', 'bf', key=comparator))

import functools

int2= functools.partial(int,base=2)
#2进制转换成10进制
print(int2("10010"))