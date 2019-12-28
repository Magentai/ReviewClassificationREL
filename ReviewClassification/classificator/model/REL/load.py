# -*- coding: utf-8 -*-
"""
Created on Tue Nov 19 20:49:38 2019

@author: Ruslan
"""
import numpy as np
from keras.preprocessing.text import Tokenizer
from keras.utils import to_categorical
import pickle
import os

def load_data2(p1,zero,n1,cpf):
    (train,labels,mini,minicut)=load_from_json2(p1,zero,n1,cpf)
    labels=np.array(labels)
    print(train[0])
    sequences = tokenize(train)
    (x_train,y_train,x_test,y_test)=balance(sequences,labels,minicut,mini)
    count(y_train, y_test)
    (y_train, y_test)=categoric(y_train, y_test)
    return (x_train,y_train,x_test,y_test)

def load_from_json2(p1,zero,m1,cpf):
    train=list()
    labels=list()
    a,b,c=0,0,0
    listOfFile = os.listdir(m1)
    for file in listOfFile:
        with open(m1+'//'+file, 'r', encoding='utf-8') as f:
            train.append(f.readline())
            f.close()
        labels.append(0)
        a=a+1
    listOfFile = os.listdir(zero) 
    for file in listOfFile:
        with open(zero+'//'+file, 'r', encoding='utf-8') as f:
            train.append(f.readline())
            f.close()
        labels.append(1)
        b=b+1
    listOfFile = os.listdir(p1) 
    for file in listOfFile:
        with open(p1+'//'+file, 'r', encoding='utf-8') as f:
            train.append(f.readline())
            f.close()
        labels.append(2)
        c=c+1
    mini=min(a, b, c)
    minicut=int(round(mini*(1-cpf)))
    return(train,labels,mini,minicut)
    
def tokenize(train):# prepare data
    train=np.array(train)
    tokenizer = Tokenizer(num_words=None, 
                          filters='!"#$%&()*+,-./:;<=>?@[\\]^_`{|}~\t\n', 
                          lower=True, 
                          split=' ', 
                          char_level=False, 
                          oov_token=None, 
                          document_count=0)
    tokenizer.fit_on_texts(train) 
    with open('tokenizer.pickle', 'wb') as handle:
        pickle.dump(tokenizer, handle, protocol=pickle.HIGHEST_PROTOCOL)
    sequences = np.array(tokenizer.texts_to_sequences(train))
    print(train.shape)
    print(train[0])
    print(sequences[0])
    return sequences

def categoric(y_train, y_test):
    y_train=to_categorical(y_train,3)
    y_test=to_categorical(y_test,3)
    print('train shape:', y_train.shape)
    print('test shape:', y_test.shape)
    return (y_train, y_test)

def balance(sequences, labels, minicut, mini):
    train_x=list()
    test_x=list()
    train_y=list()
    test_y=list()
    a,b,c,i=0,0,0,0
    for l in labels:
        if(l==0 and a<minicut):
            train_x.append(sequences[i])
            train_y.append(l)
            a=a+1
        elif(l==1 and b<minicut):
            train_x.append(sequences[i])
            train_y.append(l)
            b=b+1
        elif(l==2 and c<minicut):
            train_x.append(sequences[i])
            train_y.append(l)
            c=c+1
        elif(l==0 and a<mini):
            test_x.append(sequences[i])
            test_y.append(l)
            a=a+1
        elif(l==1 and b<mini):
            test_x.append(sequences[i])
            test_y.append(l)
            b=b+1
        elif(l==2 and c<mini):
            test_x.append(sequences[i])
            test_y.append(l)
            c=c+1
        i=i+1
    train_x=np.array(train_x)
    test_x=np.array(test_x)
    train_y=np.array(train_y)
    test_y=np.array(test_y)
    return (train_x,train_y,test_x,test_y)

def count(y_train, y_test):
    a,b,c=0,0,0
    for y in y_train:
        if(y==0):
            a=a+1
        elif(y==1):
            b=b+1
        else:
            c=c+1
    print('train: negative:',a,'neutral:',b,'positive:',c)
    a,b,c=0,0,0
    for y in y_test:
        if(y==0):
            a=a+1
        elif(y==1):
            b=b+1
        else:
            c=c+1
    print('test: negative:',a,'neutral:',b,'positive:',c)