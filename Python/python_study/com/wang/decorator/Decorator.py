'''
Created on 2015年12月9日

@author: john.wq
'''
import functools


    
def log(fun):
    @functools.wraps(fun)
    def wrapper(*arg,**kw):
        print(fun.__name__ + "  is called")
        return fun(*arg,**kw)
    return wrapper
@log
def now(txt):
    print(txt)

now("hello")
#相当于
now = log(now("hello"))

def log_para(text):
    def decorator(fun):
        def wrapper(*arg,**kw):
            print(fun.__name__ + "  is called and will be " +text)
            return fun(*arg,**kw)
        return wrapper
    return decorator

@log_para("execute")
def now1(txt):
    print(txt)
now1("aab")
now = log_para("execute")(now1("hello"))