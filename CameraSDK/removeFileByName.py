#!/usr/bin/python
# -*- coding: UTF-8 -*- 

import os,sys

removedFileName='.DS_Store';

print '====================os.path.walk �����ļ��������е��ļ�=================================='
def funx(path):
	for root, dirs, files in os.walk(path):
		for fn in files:
			#print root,fn
			if fn==removedFileName:
				os.remove(os.path.join(root, fn))
				print os.path.join(root, fn) + ' has been removed!!!'

#���õݹ麯��
rootpath = os.path.abspath('.') 
print 'rootpath:' + rootpath 
funx(rootpath)
