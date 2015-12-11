'''
Created on 2015年12月10日

@author: johnwang
'''
from types import MethodType
class Student():
    
    def __init__(self,name,score):
        self.__name = name
        self.__score = score
        
    def print(self):
        print(self.__name,self.__score)
        

wang = Student('wang',100)
wang.print()

print(wang._Student__name)

#动态语言之鸭子特性 xx-like object
class Animal():
    def run(self):
        print("Animal is running")
        
class Person(object):
    def run(self):
        print("Person is running")

def test_run(object):
    object.run()
    
test_run(Animal())
test_run(Person())
        
import types
def fn():
    pass

print(type(fn))

print(type(abs))

print(type(lambda x: x))

print(type((x for x in range(10))))

print(type(fn)==types.FunctionType)

print(type(abs)==types.BuiltinFunctionType)

print(type(lambda x: x)==types.LambdaType)
    
print(type((x for x in range(10)))==types.GeneratorType)

print(type(wang))

setattr(wang, 'print', test_run)
wang.print(Animal())


class Teacher():
    name = "XUjj"
    __slots__= ('__sex','age')
    
    @property
    def sex(self):
        return self.__sex
    @sex.setter
    def sex(self,value):
        self.__sex = value
        
ta = Teacher()
ta.sex = 6
print(ta.sex)

print(Teacher.name)


def teach(self):
    print("set down plz")
from types import MethodType
t = Teacher()
t.teach = MethodType(teach,t)
t.teach()



