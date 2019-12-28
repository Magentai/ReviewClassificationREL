# -*- coding: utf-8 -*-
"""
Created on Tue Nov 19 21:11:47 2019

@author: Ruslan
"""
# датасет Рубцовой
# TODO описание
# TODO описать проблемы
# Линейная регрессия
# разрез
# пословесный анализ
# автоперевод на англ.
from __future__ import print_function
import numpy as np

from keras.preprocessing import sequence
from keras.datasets import imdb
from tensorflow.keras.layers import GlobalAveragePooling1D
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout, Activation
from tensorflow.keras.layers import Embedding
from load import load_data2


def create_ngram_set(input_list, ngram_value=2):
    """
    Extract a set of n-grams from a list of integers.

    >>> create_ngram_set([1, 4, 9, 4, 1, 4], ngram_value=2)
    {(4, 9), (4, 1), (1, 4), (9, 4)}

    >>> create_ngram_set([1, 4, 9, 4, 1, 4], ngram_value=3)
    [(1, 4, 9), (4, 9, 4), (9, 4, 1), (4, 1, 4)]
    """
    return set(zip(*[input_list[i:] for i in range(ngram_value)]))


def add_ngram(sequences, token_indice, ngram_range=2):
    """
    Augment the input list of list (sequences) by appending n-grams values.

    Example: adding bi-gram
    >>> sequences = [[1, 3, 4, 5], [1, 3, 7, 9, 2]]
    >>> token_indice = {(1, 3): 1337, (9, 2): 42, (4, 5): 2017}
    >>> add_ngram(sequences, token_indice, ngram_range=2)
    [[1, 3, 4, 5, 1337, 2017], [1, 3, 7, 9, 2, 1337, 42]]

    Example: adding tri-gram
    >>> sequences = [[1, 3, 4, 5], [1, 3, 7, 9, 2]]
    >>> token_indice = {(1, 3): 1337, (9, 2): 42, (4, 5): 2017, (7, 9, 2): 2018}
    >>> add_ngram(sequences, token_indice, ngram_range=3)
    [[1, 3, 4, 5, 1337, 2017], [1, 3, 7, 9, 2, 1337, 42, 2018]]
    """
    new_sequences = []
    for input_list in sequences:
        new_list = input_list[:]
        for ngram_value in range(2, ngram_range + 1):
            for i in range(len(new_list) - ngram_value + 1):
                ngram = tuple(new_list[i:i + ngram_value])
                if ngram in token_indice:
                    new_list=np.append(new_list,(token_indice[ngram]))
        new_sequences.append(new_list)

    return new_sequences
def learn(savemodel,typef,categorical):
    # Set parameters:
    # ngram_range = 2 will add bi-grams features
    ngram_range = 2
    maxlen = 600
    batch_size = 64
    embedding_dims = 50
    epochs = 30
    max_features = 3
    if(typef=='json2'):
        (train_x,train_y,test_x,test_y)=load_data2("res//p1","res//zero","res//m1",0.2)
    elif(typef=='imdb'):
        print('Loading data...')
        # save np.load
        np_load_old = np.load
        
        # modify the default parameters of np.load
        np.load = lambda *a,**k: np_load_old(*a, allow_pickle=True, **k)
        
        (train_x, train_y), (test_x, test_y) = imdb.load_data(num_words=max_features)
        
        # restore np.load for future normal usage
        np.load = np_load_old
    print(len(train_x), 'train sequences')
    print(len(test_x), 'test sequences')
    print('Average train sequence length: {}'.format(
        np.mean(list(map(len, train_x)), dtype=int)))
    print('Average test sequence length: {}'.format(
        np.mean(list(map(len, test_x)), dtype=int)))
    
    if ngram_range > 1:
        print('Adding {}-gram features'.format(ngram_range))
        # Create set of unique n-gram from the training set.
        ngram_set = set()
        for input_list in train_x:
            for i in range(2, ngram_range + 1):
                set_of_ngram = create_ngram_set(input_list, ngram_value=i)
                ngram_set.update(set_of_ngram)
    
        # Dictionary mapping n-gram token to a unique integer.
        # Integer values are greater than max_features in order
        # to avoid collision with existing features.
        start_index = max_features + 1
        token_indice = {v: k + start_index for k, v in enumerate(ngram_set)}
        indice_token = {token_indice[k]: k for k in token_indice}
    
        # max_features is the highest integer that could be found in the dataset.
        max_features = np.max(list(indice_token.keys())) + 1
    
        # Augmenting x_train and x_test with n-grams features
        train_x = add_ngram(train_x, token_indice, ngram_range)
        test_x = add_ngram(test_x, token_indice, ngram_range)
        print('Average train sequence length: {}'.format(
            np.mean(list(map(len, train_x)), dtype=int)))
        print('Average test sequence length: {}'.format(
            np.mean(list(map(len, test_x)), dtype=int)))
        
    print('Pad sequences (samples x time)')
    x_train = sequence.pad_sequences(train_x, maxlen=maxlen)
    x_test = sequence.pad_sequences(test_x, maxlen=maxlen)
    print('type',type(x_train))
    print('type',type(x_train[0]))
    print('x_train shape:', x_train.shape)
    print('x_test shape:', x_test.shape)
    print(train_x[0])
    print(train_y[0])
    
    print('Build model...')
    model = Sequential()
    
    # we start off with an efficient embedding layer which maps
    # our vocab indices into embedding_dims dimensions
    model.add(Embedding(max_features,
                        embedding_dims,
                        input_length=maxlen,
                        trainable=True))
    model.add(Dropout(0.15))
    model.add(GlobalAveragePooling1D())
    model.add(Dense(256))
    model.add(Activation('relu'))
    # We project onto a single unit output layer, and squash it with a sigmoid:
    if(categorical):
        model.add(Dense(3, activation='softmax'))
        model.compile(loss='categorical_crossentropy',
                      optimizer='adam',
                      metrics=['accuracy'])
    else:
        model.add(Dense(1, activation='softmax'))
        model.compile(loss='binary_crossentropy',
                      optimizer='adam',
                      metrics=['accuracy'])
    
    model.fit(x_train, train_y,
              batch_size=batch_size,
              epochs=epochs,
              validation_data=(x_test, test_y))
    
    if(savemodel):
        model.save('res//weights.h5')
    
learn(True,'json2',True)