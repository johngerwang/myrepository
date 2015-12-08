#encoding:utf-8

def default_para_fun(x,n=2):
    result = 1;
    while n>0:
        n = n-1
        result = result*n
    return result

def user_profile(name,sex,city='nj',age=6):
    print(name,sex,city,age)
    

user_profile('wang', 'M', city='nj',age='33')

def add_end(L=[]):
    L.append('end')
    return L
    
print(add_end([1,2,3]))
print(add_end([4,5,6]))
print(add_end())
print(add_end())

def add_end1(L=None):
    if L is None:
        L = []
    L.append('end')
    return L
print(add_end1())
print(add_end1())

def person(name,sex,**kw):
    print(name,sex,kw)

args = ['wamg','M']
person(*args)
person('wang', 'M', age=33,city="nj")
person('wang', 'M' )
profile ={"age":33,"city":"nj"}
person('wang', 'M', **profile) 

def person_profile(name,sex,*,age=6,city):
    print(name,sex,age,city)
    
person_profile('wang', 'M', city='nj') 


def para_mix(name,sex,city='nj',postcode=123,*args,**kw):
    print(name,sex,city,postcode,args,kw)

arg = [1,2,3]
para_mix('wang','M',234,*arg)
para_mix('wang','M','nj',*arg,address="jn")

args=('wangqiang','M','other','234',2323)
kws={"address":"jn"}
para_mix(*args,**kws)