'''
Created on 2015年12月11日

@author: johnwang
'''


class Dict(dict):

    def __init__(self, **kw):
        super().__init__(**kw)

    def __getattr__(self, key):
        try:
            return self[key]
        except KeyError:
            raise AttributeError(r"'Dict' object has no attribute '%s'" % key)

    def __setattr__(self, key, value):
        self[key] = value
        
import unittest

class TestDict(unittest.TestCase):

    def test_init(self):
        d = Dict(**{"a":1,"b":2})
        self.assertTrue(d['a'],1)
        self.assertTrue(d['b'],2)
        self.assertTrue(d.a,1)
        self.assertTrue(d.b,2)
        self.assertTrue(isinstance(d, dict))
        
    def test_key(self):
        d = Dict()
        d['key']= "1"
        self.assertTrue(d['key'],"1")
        
    def test_attr(self):
        d = Dict()
        d.key= "1"
        self.assertTrue(d['key'],"1")
        self.assertTrue('key' in d)
        
    def test_keyerror(self):
        d = Dict()
        with self.assertRaises(KeyError):
            value = d['empty']

    def test_attrerror(self):
        d = Dict()
        with self.assertRaises(AttributeError):
            value = d.empty
            
    def setUp(self):
        print('setUp...')

    def tearDown(self):
        print('tearDown...')
if __name__ == '__main__':
    unittest.main()
    
        
