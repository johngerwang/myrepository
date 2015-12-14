#Python面向对象
##类定义
1.关键字.Class

2.构造函数.\_\_init__(self)

3.self.表示创建的对象本身，可以把成员变量绑定到self上。方法的第一参数一定是self。

4.父类.超级父类为object。默认可以不写，写在括号中表示继承.

5.例子
	
	class Student(object):
    def __init__(self,name,score):
        self.name = name
        self.score = score
    def print(self):
        print(self.name,self.score)   
    wang = Student('wang',100)
    wang.print()   
    
6.访问成员变量的值.可以直接通过对象名.变量名的形式。如wang.name.注意，该变量是成员变量，成员变量不需要声明，而类变量需要。当需要限制变量被访问时，需要加上双下划线\_\_来限定.但是实际上并不是不能访问，而是Python把\_\_name改成了\_Student\_\_name，所以通过wang.\_Student\_\_name依然可以访问到。你会看到以一个下划线开头的实例变量名，比如_name，这样的实例变量外部是可以访问的，但是，按照约定俗成的规定，当你看到这样的变量时，意思就是，“虽然我可以被访问，但是，请把我视为私有变量，不要随意访问”。
		
		class Student(object):
		  def __init__(self,name,score):
        self.__name = name
        self.__score = score
7.总的来说就是，Python本身没有任何机制阻止你干坏事，一切全靠自觉。

8.继承。定义方式为class SubClass(SuperClass):。可以使用isinstance(x,(classtype1,[classtype2]))来判断是否是classtype类型，或者判断是否是某一种类型。利用继承可以实现多态。

9.鸭子类型(XXX-like duck)。test_run方法可以接受任何实现了run方法的类型。某种意义上说是因为形参不对类型做任何检查。这个是动态语言的特性。

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

10.使用type()返回对象的class类型，如果是对类执行type函数，则返回type，class的类型是type。
注意，一切都是对象。
	
	import types
	def fn():
   	 pass

	print(type(fn)) #<class 'function'>
	print(type(abs)) #<class 'builtin_function_or_method'>
	print(type(lambda x: x)) #<class 'function'>,可以理解为匿名函数。
	print(type((x for x in range(10)))) #<class 'generator'>
	print(type(fn)==types.FunctionType) #True
	print(type(abs)==types.BuiltinFunctionType) #True
	print(type(lambda x: x)==types.LambdaType) #True
	print(type((x for x in range(10)))==types.GeneratorType) # True
    print(type(wang)) #<class '__main__.Student'>

    type(123) #<class 'int'>
	type('abc') #<class 'str'>
	type(None) #<'NoneType'>
	
11.dir()。该函数获取该模块/类的成员变量与方法。

12.hasattr：判断对象是否具有该属性（包括方法和成员变量）。setattr：设置对象的属性（包括方法和成员变量），getattr：获取对象的属性（包括方法和成员变量）。
	hasattr(obj,'fun')
	getattr(obj,'fun',404) ->404是指获取不到该属性时得到的默认值。
	setattr(wang, 'print', test_run)
	wang.print(Animal()) #wang(Student)对象的print方法变成了test_run()函数。
   
13.类变量
		
	class Teacher():
    	name = "Xujj"  #类变量name，可以被所有的对象共享，对象也可以使用该变量。

	print(Teacher.name)
类变量的优先级没有成员变量高，所以当对象有该属性时，会覆盖该概述的值。
	
	tt = Teacher()
	print(tt.name) # Xujj
	tt.name = 'wang' #此处动态绑定了一个属性。
	print(tt.name)  #wang
	print(Student) # Xujj
	del s.name
	print(tt.name) #Xujj
注意，尽量避免不要要对象的属性和类的属性相同。
        
14.动态语言特性之动态绑定方法	
	
	class Teacher():
    	name = "XUjj"

	print(Teacher.name)

	def teach(self): #注意，此处必须得有self参数，不然无法绑定给对象
    	print("set down plz")
	from types import MethodType
	t = Teacher()
	t.teach = MethodType(teach,t) #动态绑定一个方法给t对象,其他对象无法使用该方法
	t.teach(
	Teacher.teach = MethodType(teach,Teacher) #对类绑定一个方法，所有对象都可以使用
	tt = Teacher()
	tt.teach()  #所有的对象都可以使用该方法
15.限制成员变量的添加。
	
	class Teacher()
		__slots__= ('name','age') # 用tuple定义允许绑定的成员变量的名称
		
通过该语句可以使Teacher类/对象，禁止添加属性成员变量，否则报错AttributeError

16.使方法变成属性@property

	class Teacher():
   	 	name = "XUjj"
    	__slots__= ('__sex','age')
    
    	@property
    	def sex(self):
      	  return self.__sex
    	@sex.setter #因为上面用了property注释，此处sex.setter注释就变得可用了。
    	def sex(self,value):
        	self.__sex = value
        
	ta = Teacher()
	ta.sex = 6  #相当于调用了sex(self,value）方法
	print(ta.sex) #相当于调用了sex(self)方法
	
17.多重继承。定义形式如下。所谓的MIXIN设计就是让一个类拥有多个其他类的功能，可以通过多重继承来实现。
		
		class subclass(superclass1[,superclass2])
		
18.Python的类似Java的toString()方法为\_\_str\_\_()(配合print函数使用)和\_\_repr\_\_()(在终端中直接输入对象会打印该对象的信息，此时调用的是该方法）

	class Student(object):
    	def __init__(self, name):
        	self.name = name
    	def __str__(self):
        	return 'Student object (name=%s)' % self.name
   	 	\_\_repr\_\_ = \_\_str\_\_
   	 
   	 print(Student('Michael'))  #Student object (name: Michael)
   	 #终端模式下，如果不实现\_\_repr\_\_ = \_\_str\_\_，则会出现下面的状况。如果实现了，name就跟上面的输出一样了。
	s = Student('Michael')
	s  #<__main__.Student object at 0x109afb310>

19.在类中实现了\_\_iter\_\_()和\_\_next\_\_()，\_\_getitem\_\_()等方法，那么就可以对该类的对象进行for x in obj操作了，还可以像list一样操作(读取)该类的对象，要对该类对象进行"写"，还得实现\_\_setitem\_\_(),删除操作的话，害的实现\_\_delitem\_\_()等等。总之，通过上面的方法，我们自己定义的类表现得和Python自带的list、tuple、dict没什么区别，这完全归功于动态语言的“鸭子类型”，不需要强制继承某个接口。
还可以定义\_\_getattr\_\_()方法，当调用该类中未定义的成员变量时就会调用该方法（否则不调用），如果不实现该方法，默认返回None，可以利用该方面的实现，限定一些可以动态绑定的成员变量。

	class Fib(object):
    	def __init__(self):
        	self.a, self.b = 0, 1 # 初始化两个计数器a，b

    	def __iter__(self):
        	return self # 实例本身就是迭代对象，故返回自己

    	def __next__(self):
        	self.a, self.b = self.b, self.a + self.b # 计算下一个值
        	if self.a > 100000: # 退出循环的条件
            		raise StopIteration();
        	return self.a # 返回下一个值	
       
       def __getitem__(self, n):
        a, b = 1, 1
        for x in range(n):
            a, b = b, a + b
        return a
        if isinstance(n, slice): # n是切片,注意，切片[x:x]的类型是slice
            start = n.start
            stop = n.stop
            if start is None:
                start = 0
            a, b = 1, 1
            L = []
            for x in range(stop):
                if x >= start:
                    L.append(a)
                a, b = b, a + b
            return L
        
       for n in Fib(): 
			print(n)  #此处的n代表第n次的返回值。
		
		1
		1
		2
		3
		5
		\.\.\.
		46368
		75025
		
		Fib()[5] #以5为上限，计算斐波那契值。像list一样操作该类的对象。
		f = Fib()
		f[0:5]   #以切片的形式[1, 1, 2, 3, 5]

20.调用实例对象。在类中实现\_\_call\_\_()方法后，直接调用对象就是调用\_\_call\_\_()方法。

	class Student(object):
    	def __init__(self, name):
        	self.name = name

    	def __call__(self):
       		print('My name is %s.' % self.name)
  
	 	s = Student('Michael')
		s() # self参数不要传入。->My name is Michael.

如果你把对象看成函数，那么函数本身其实也可以在运行期动态创建出来，因为类的实例都是运行期创建出来的，这么一来，我们就模糊了对象和函数的界限。
那么，怎么判断一个变量是对象还是函数呢？其实，更多的时候，我们需要判断一个对象是否能被调用，能被调用的对象就是一个Callable对象，比如函数和我们上面定义的带有\_\_call\_\_()的类实例。通过callable()函数，我们就可以判断一个对象是否是“可调用”对象。
	
	>>> callable(Student())
	True
	>>> callable(max)
	True
	>>> callable([1, 2, 3])
	False
	>>> callable(None)
	False
	>>> callable('str')
	False

21.枚举.使用Enum。
	
	form enum import Enum
	
	Month  = Enum('month',('jan','feb','mar','apr','may','jun'))
	print(Month.jan.value) ->1。使用value属性获得值。默认可以从1开始计数。如果需要自定义，需要集成Enum类。
	for name, member in Month.__members__.items():
    	print(name, '=>', member, ',', member.value)

	jan => month.jan , 1
	feb => month.feb , 2
	。。。
	jun => month.jun , 6

22.动态语言与静态语言最大的区别是，动态语言的函数/类/方法的定义是在运行时创建的，而不是编译时期确定的。
class的创建是通过type()函数来完成的，相当于在解释执行时，python解释器扫描class的定义，然后调用type()函数来完成类型的创建。以下是从另一个侧面来证明该说明。
	
	def fun(self,name="wang"):
		print("hello",name)

	Hello = type('Hello',(object,),dict(hello=fun))
	h = Hello()
	h.hello("qiang")  #hello qiang
	print(type(h))  #<class '__main__.Hello'>
	print(type(Hello)) #<class 'type'>
	
type()函数定义。第一个参数：类名。第二个参数：必须得是一个tuple，里面是类的父类。第三个参数，必须是一个dict，定义方法。
通过type()函数创建的类和直接写class是完全一样的，因为Python解释器遇到class定义时，仅仅是扫描一下class定义的语法，然后调用type()函数创建出class。

23.try...except...finally。当需要捕获异常时可以使用该语法。finally中的语句一定会被执行，不管是否有异常发生。如下列子。注意：所以异常的超级父类是BaseExpcetion。

	import logging
	
	try:
    	print('try...')
    	r = 10 / int('2')
    	print('result:', r)
	except ValueError as e:  #注意，有一个as关键字
    	print('ValueError:', e)
    	raise XXXError #也可以在此处再次向上抛出异常，由上层处理。如果不明确定异常类型，则还是抛出ValueError。
	except ZeroDivisionError as e:
    	print('ZeroDivisionError:', e)
    	logging.exception(e)	
	else:  #如果没有错误发生，可以在except语句块后面加一个else，当没有错误发生时，会自动执行else语句：
    	print('no error!')
	finally:
    	print('finally...')
	print('END')
当发生异常时，执行完finally后就不再往下执行了，但是如果使用logging，则可以将异常信息收集到，并继续往下执行。比如上例中，当发生除0异常时，最后面的print('END')依然可以被执行。

注意：我们明明已经捕获了错误，但是，打印一个ValueError!后，又把错误通过raise语句抛出去了，这不有病么？
其实这种错误处理方式不但没病，而且相当常见。捕获错误目的只是记录一下，便于后续追踪。但是，由于当前函数不知道应该怎么处理该错误，所以，最恰当的方式是继续往上抛，让顶层调用者去处理。好比一个员工处理不了一个问题时，就把问题抛给他的老板，如果他的老板也处理不了，就一直往上抛，最终会抛给CEO去处理。

24.断言(assert)。如果assert判断结果是false，则抛出AssertionError。可以通过这种方式调试代码，在执行的时候，通过-O选项，使之不执行。

25.logging。可以将信息log到console。也可以设置级别，分别为info,debug,warnning,error

	import logging
	logging.basicConfig(level=logging.INFO)

	
26.单元测试（unittet）

	class Dict(dict):

    def __init__(self, **kw):
        super().__init__(**kw)

    def __getattr__(self, key):#以属性的形式取值
        try:
            return self[key]
        except KeyError:
            raise AttributeError(r"'Dict' object has no attribute '%s'" % key)

    def __setattr__(self, key, value):  #以属性的形式赋值
        self[key] = value

	d = Dict(**{"a":1,"b":2})
	d.a = 3
	print(d.a)

import unittest  #需要导入unitest模块

class TestDict(unittest.TestCase): #需要继承unittest.TestCase

    def setUp(self): #在所有的test执行开始之前执行
        print('setUp...')

    def tearDown(self): #在所有的test执行后执行
        print('tearDown...')
        
    def test_init(self): #必须以test_开头，否则不被执行
        d = Dict(**{"a":1,"b":2})
        self.assertEqual(d['a'],1)  #使用self.assertXXXX在判断
        self.assertEqual(d['b'],2)
        self.assertEqual(d.a,1)
        self.assertEqual(d.b,2)
        self.assertTrue(isinstance(d, dict))
        
    def test_key(self):
        d = Dict()
        d['key']= "1"
        self.assertEqual(d['key'],"1")
        
    def test_attr(self):
        d = Dict()
        d.key= "1"
        self.assertEqual(d['key'],"1")
        self.assertTrue('key' in d)
        
    def test_keyerror(self):
        d = Dict()
        with self.assertRaises(KeyError): #对异常抛出的判断，需要使用with self.assertRaises
            value = d['empty']

    def test_attrerror(self):
        d = Dict()
        with self.assertRaises(AttributeError):
            value = d.empty
            
	if __name__ == '__main__':  
   	 	unittest.main()#只有加了这一句，才会执行
   	 	#也可以不用这一句，在命令行中python -m unittest 文件名(如mytest.py)

27.基于注释的测试。python会按照命令的输入输出形式来执行对代码的测试。

class Dict(dict):
    '''
    Simple dict but also support access as x.y style.

    >>> d1 = Dict()
    >>> d1['x'] = 100
    >>> d1.x
    100
    >>> d1.y = 200
    >>> d1['y']
    200
    >>> d2 = Dict(a=1, b=2, c='3')
    >>> d2.c
    '3'
    >>> d2['empty']
    Traceback (most recent call last):
        ...
    KeyError: 'empty'
    >>> d2.empty
    Traceback (most recent call last):
        ...
    AttributeError: 'Dict' object has no attribute 'empty'
    '''
    def __init__(self, **kw):
        super(Dict, self).__init__(**kw)

    def __getattr__(self, key):
        try:
            return self[key]
        except KeyError:
            raise AttributeError(r"'Dict' object has no attribute '%s'" % key)

    def __setattr__(self, key, value):
        self[key] = value

	if __name__=='__main__':
    	import doctest
   	 	doctest.testmod()
   
28.python编码。python3对字符串默认使用unicode编码，即在内存中是unicode编码，此内部编码方式是一个桥梁。假设从文件中读取utf-8编码的内容到前台以gb2312来正常显示的话，必须经历utf-8->unicode->gb2312转换的过程。
	
	ff = open('1.txt,'rb') #假设1.txt是以utf-8编码的文件，内容为“中文”
	bytes = ff.read()
	print(bytes)  #此处时显示是utf-8编码形式的字节码，b'\xe4\xb8\xad\xe6\x96\x87'
	print("string: " + chardet.detect(bytes)["encoding"] + " with confidence: " + 	str(chardet.detect(bytes)["confidence"]))
	#string: utf-8 with confidence: 0.7525

	a = bytes.decode('utf-8') #将utf-8解码成unicode类型的字节码
	print(type(a)) #<class 'str'>
	print(a)  #中文
	b = a.encode('gb2312') #将unicode的字节码编码成gb2312的字符串
	print(type(b) ) #<class 'bytes'>
	print(b) #b'\xd6\xd0\xce\xc4'
	
29.json化与反json化
	import json
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

	s = Student("wang",10,"M")
	json_string = json.dumps(s,default=s2json) #需要一个转换函数
	json_string = json.dumps(s,default=lambda x:x.__dict__) #使用__dict__获取所有的成员变量的名称与值，行成dict
	print(json_string) #{"name": "wang", "sex": "M", "age": 10}
	print("======", s.__dict__)
    
	s = json.loads(json_string,object_hook=json2std) #需要一个转换函数
	#{"name": "wang", "sex": "M", "age": 10}

30.序列化
	import pickle
	d = dict(name='Bob', age=20, score=88)

	pickle.dumps(d)

	f = open('3.txt','wb')
	pickle.dump(d,f)
	f.close()

	f = open('3.txt','rb')
	d = pickle.load(f)
	f.close()
	print(d)


