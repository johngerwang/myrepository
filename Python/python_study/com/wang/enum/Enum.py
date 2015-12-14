'''
Created on 2015年12月11日

@author: johnwang
'''

from enum import Enum


Month  = Enum('month',('jan','feb','mar','apr','may','jun'))

print(Month.jan.value)
for name, member in Month.__members__.items():
    print(name, '=>', member, ',', member.value)