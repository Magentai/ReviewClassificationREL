import numpy as np
import pandas as pd
import nltk
import re
import os
import codecs
from sklearn import feature_extraction
import mpld3
import matplotlib.pyplot as plt
import matplotlib as mpl

import re
tx=[]      
def preprocess_text(text1):
    
    l = 1
    for line in text1:
        text = re.sub('((www\.[^\s]+)|(https?://[^\s]+))','URL', line)
        text = re.sub(r"\d+", "",line, flags=re.UNICODE)
        text = re.sub('@[^\s]+','USER', line)
        text = re.sub('[^а-яА-Я]+', ' ', text)
        text = re.sub(' +',' ', text)
        l = 1+l
        print(l)
        tx.append(text)
        f2 = open( 'MyJson(1).txt', 'w', encoding='utf-8', errors='ignore' )
        for element in tx:
            f2.write(element)
            f2.write('\n')
    f2.close()

from nltk.stem.snowball import SnowballStemmer
stemmer = SnowballStemmer("russian")

def token_and_stem(text):
    tokens = [word for sent in nltk.sent_tokenize(text) for word in nltk.word_tokenize(sent)] # это дает нам список предложений
# теперь перебираем каждое предложение и токенизируем его отдельно
    filtered_tokens = []
    for token in tokens:
        if re.search('[а-яА-Я]', token):
            filtered_tokens.append(token)
    stems = [stemmer.stem(t) for t in filtered_tokens]
    return stems

def token_only(text):
    tokens = [word.lower() for sent in nltk.sent_tokenize(text) for word in nltk.word_tokenize(sent)]
    filtered_tokens = []
    for token in tokens:
        if re.search('[а-яА-Я]', token):
            filtered_tokens.append(token)
    return filtered_tokens

titles = open('MyJson(1).txt', 'r', encoding='utf-8', errors='ignore').read().split('\n')
#preprocess_text(titles)

#Создаем словари из полученных основ
totalvocab_stem = []
totalvocab_token = []
for i in titles:
    allwords_stemmed = token_and_stem(i)
    #print(allwords_stemmed)
    totalvocab_stem.extend(allwords_stemmed)
    
    allwords_tokenized = token_only(i)
    totalvocab_token.extend(allwords_tokenized)

from pymystem3 import Mystem
m = Mystem()
A = []

for i in titles:
    print(i)
    lemmas = m.lemmatize(i)
    A.append(lemmas)

#Этот массив можно сохранить в файл либо "забэкапить"
import pickle
with open("mystem.pkl", 'wb') as handle:
                    pickle.dump(A, handle)
stopwords = nltk.corpus.stopwords.words('russian')
#можно расширить список стоп-слов
stopwords.extend(['что', 'это', 'так', 'вот', 'быть', 'как', 'в', 'к', 'на'])

from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

n_featur=200000
tfidf_vectorizer = TfidfVectorizer(max_df=0.85, max_features=10000,
                                 min_df=0.02, stop_words=stopwords,
                                 use_idf=True, tokenizer=token_and_stem, ngram_range=(1,3))

#get_ipython().magic(u'time tfidf_matrix = tfidf_vectorizer.fit_transform(titles)')
#get_ipython().magic(u'time words_matrix = words_vectorizer.fit_transform(sents) #fit the vectorizer to synopses')
#print(tfidf_matrix.shape)

%time tfidf_matrix = tfidf_vectorizer.fit_transform(titles) 

from sklearn.metrics.pairwise import cosine_similarity
dist = 1 - cosine_similarity(tfidf_matrix)
dist.shape

num_clusters = 2

# Метод к-средних - KMeans
from sklearn.cluster import KMeans

km = KMeans(n_clusters=num_clusters)
%time km.fit(tfidf_matrix)
idx = km.fit(tfidf_matrix)
clusters = km.labels_.tolist()

print(clusters)
print (km.labels_)

clusterkm = km.labels_.tolist()
frame = pd.DataFrame(titles, index = [clusterkm])

#k-means
out = { 'title': titles, 'cluster': clusterkm }
frame1 = pd.DataFrame(out, index = [clusterkm], columns = ['title', 'cluster'])
print(frame1)

#чтобы не было отрицательных значений и находилось в пределах от 0 до 1
from sklearn.metrics.pairwise import cosine_similarity
dist = 1 - cosine_similarity(tfidf_matrix)
dist.shape