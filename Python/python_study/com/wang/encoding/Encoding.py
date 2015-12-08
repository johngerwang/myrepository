# encoding: utf-8
'''
Created on 2015年12月7日

@author: john.wq
'''

print('包含中文的str')

print(ord('A'))
print(ord('中'))
print(chr(66))
print(chr(25991))
print('\u4e2d\u6587')
x = b'A'
print(x)

#将数据保存到磁盘或者发送到网络，必须以bytes来发送，所以得需要以下方式编码
x= '中文'.encode(encoding='utf_8', errors='strict')
print(x)
#从网络后者磁盘读取了字节流，那么读取到的数据就是bytes，要把bytes转变为String，则需要用decode()方法。
x = b'\xe4\xb8\xad\xe6\x96\x87'.decode('utf-8')
print(x)