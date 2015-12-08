
'''
Created on 2015年12月8日

@author: john.wq
'''

s1 = set([1,2,3,4])
s2 = set([1,2,3,5])
print(s1&s2)
print(s1|s2)
print(s1-s2)
print(s1^s2)

print(s1.remove(4))

l = [1,2,3,4]
s3 = set(l)
print(s3)