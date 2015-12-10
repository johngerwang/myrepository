'''
Created on 2015年12月9日

@author: john.wq
'''

import sys

__author__ ="wang"

def test():
    args = sys.argv
    if len(args)==1:
        print("hello")
    elif len(args)==2:
        print("hello " + args[1])
    else:
        print("too many args")

if __name__ == '__main__':
    test()