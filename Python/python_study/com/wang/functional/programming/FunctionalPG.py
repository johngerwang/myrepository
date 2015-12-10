#encoding:utf-8
'''
Created on 2015年12月8日

@author: john.wq
'''
from _functools import reduce

def add(x,y,f):
    return f(x)+f(y)

result = add(-5,-6,abs)

print(result)


def square(x):
    return x*x
r = map(square,[1,2,3,4])
print(r)
print(list(r))

l = list(map(str, [1, 2, 3, 4, 5, 6, 7, 8, 9]))
print(l)

def fun(x,y):
    return x*10+y

print(reduce(fun,[1,2,3]))

def char2num(s):
    return {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}[s]
print(reduce(fun,map(char2num,"12345")))

#整理成一个方法
def str2int(s):
    def fn(x, y):
        return x * 10 + y
    def char2num(s):
        return {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}[s]
    return reduce(fn, map(char2num, s))

def strtoint(s):
    return reduce(lambda x, y: x * 10 + y, map(lambda z:{'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}[z], s))

print(strtoint('12345'))

def lowerUpper(string):
    if len(string) >2:
        string = string.upper()
        firstChar = string[0]
        leftChar = string[1:].lower()
        return firstChar+leftChar

print(list(map(lowerUpper,['adam', 'LISA', 'barT'])))  

def multipy(x,y):
    return x*y
print(reduce(multipy,[1,2,3,4,5]))


def intgen(): #3开始的奇数序列
    start = 1
    while True:
        start = start+2
        yield start
        
def disable(n):
    return lambda x:x%n>0

def primes():
    yield 2
    it = intgen()
    while True:
        n = next(it)
        yield n
        it = filter(disable(n),it)

for n in primes():
    if n < 100:
        print(n)
    else:
        break
    
print(sorted([-5,-10,23,1,2,3],key=abs))

def lazy_sum(*args):
    def sum():
        result = 0
        for i in args:
            result = result+i
        return result
    return sum
f1=lazy_sum(*[1,2,3])
f2=lazy_sum(*[1,2,3])
print(f1())
print(f2())

def count1():
    fs = []
    for i in range(1, 4):
        def f():
            return i*i
        fs.append(f)
    return fs

f1, f2, f3 = count1()
print(f1(),f2(),f3())

def count(*args):
    def f(i):
        def g():
            return i*i
        return g 
    fs = []
    for i in args:
        fs.append(f(i))
    return fs
args = [1,2,3]
f1, f2, f3 = count(*args)
print(f1(),f2(),f3())
