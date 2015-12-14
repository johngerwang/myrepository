'''
Created on 2015年12月11日

@author: johnwang
'''

def fun(self,name="wang"):
    print("hello",name)
        
Hello = type('Hello',(object,),dict(hello=fun))
h = Hello()
h.hello("qiang")
print(type(h))
print(type(Hello))
