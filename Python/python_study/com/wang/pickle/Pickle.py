#encoding:utf-8
'''
Created on 2015年12月14日

@author: johnwang
'''

import pickle
from email.policy import default

d = dict(name='Bob', age=20, score=88)

pickle.dumps(d)

f = open('3.txt','wb')
pickle.dump(d,f)
f.close()

f = open('3.txt','rb')
d = pickle.load(f)
f.close()
print(d)


import json
d = dict(name='Bob', age=20, score=88) #内置数据类型dict
json_string = json.dumps(d)
print(json_string)

d = json.loads(json_string)
print(d)

#由于JSON标准规定JSON编码是UTF-8，所以我们总是能正确地在Python的str与JSON的字符串之间转换。


class Student:
    
    def __init__(self,name,age,sex):
        self.name = name
        self.sex = sex
        self.age = age
        
def s2json(std):
    return {
            'name':std.name,
            'age':std.age,
            'sex':std.sex
            }

def json2std(json):
    return Student(json['name'],json['age'],json['sex'])

print(Student)
s = Student("wang",10,"M")
json_string = json.dumps(s,default=s2json)
json_string = json.dumps(s,default=lambda x:x.__dict__)
print(json_string)
print("======", s.__dict__)

s = json.loads(json_string,object_hook=json2std)

print(json.dumps(s, default=lambda obj: obj.__dict__))


