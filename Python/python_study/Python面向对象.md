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

10.使用type()放回对象的class类型，注意，一切都是对象。
	
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

20.调用对象。在类中实现\_\_call\_\_()方法后，直接调用对象就是调用\_\_call\_\_()方法。

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


