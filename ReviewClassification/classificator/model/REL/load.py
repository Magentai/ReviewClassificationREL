# -*- coding: utf-8 -*-
"""
Created on Tue Nov 19 20:49:38 2019

@author: Ruslan
"""
import json
import numpy as np
from keras.preprocessing.text import Tokenizer
from keras.preprocessing import sequence
from keras.utils import to_categorical

def load_data(name,balanced,cpf):
    (train,labels,mini,minicut)=load_from_json(name,balanced,cpf)
    labels=np.array(labels)
    sequences = tokenize(train)
    if(balanced):
        (train_x,train_y,test_x,test_y)=balance(sequences,labels,minicut)
    else:
        train_x=sequences[:3000]
        test_x=sequences[3000:]
        train_y=labels[:3000]
        test_y=labels[3000:]
    count(train_y, test_y)
    (train_y, test_y) = categoric(train_y, test_y)
    return (train_x,train_y,test_x,test_y)

def load_raw(name, balanced, cpf):
    (train,labels,mini,minicut)=load_from_json(name,balanced,cpf)
    labels=np.array(labels)
    sequences = tokenize(train)
    if(balanced):
        (x_train,y_train,x_test,y_test)=balance(sequences,labels,minicut)
    else:
        x_train=sequences[:3000]
        x_test=sequences[3000:]
        y_train=labels[:3000]
        y_test=labels[3000:]
    count(y_train, y_test)
    (y_train, y_test)=categoric(y_train, y_test)
    return (x_train,y_train,x_test,y_test)

def load_from_json(name,balanced,cpf):
    with open(name, "r", encoding="utf8") as read_file:
        data = json.load(read_file)
    train=list()
    labels=list()
    a,b,c,max=0,0,0,0
    for d in data:
        if(d['score']==0):
            a=a+1
        elif(d['score']==1):
            b=b+1
        elif(d['score']==2):
            c=c+1
        if(len(d['review'])>max):
            max=len(d['review'])
    print('negative:',a,'neutral:',b,'positive:',c)
    print('max char len:',max)
    mini=min(a, b, c)
    if(balanced):
        a,b,c=0,0,0
        minicut=int(round(mini*(1-cpf)))
        for d in data:
            if(d['score']==0 and a<mini):
                a=a+1
                labels.append(d['score'])
                train.append(d['review'])
            if(d['score']==1 and b<mini):
                b=b+1
                labels.append(d['score'])
                train.append(d['review'])
            elif(d['score']==2 and c<mini):
                c=c+1
                labels.append(d['score'])
                train.append(d['review'])
        print('balanced negative:',a,'neutral:',b,'positive:',c)
    else:
        for d in data:
            labels.append(d['score'])
            train.append(d['review'])
    return (train,labels,mini,minicut)

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

def balance(sequences, labels, minicut):
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
        elif(l==0):
            test_x.append(sequences[i])
            test_y.append(l)
            a=a+1
        elif(l==1):
            test_x.append(sequences[i])
            test_y.append(l)
            b=b+1
        elif(l==2):
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

#load_raw("MyJson2.txt",True,0.2)