import pickle
import keras
import numpy as np

if __name__ == "__main__":
    with open('./input.txt', 'r', encoding='utf-8') as file:
        text = file.readline()

    with open('../model/REL/tokenizer.pickle', 'rb') as handle:
        tokenizer = pickle.load(handle)

    sequences = np.array(tokenizer.texts_to_sequences(text))

    model = keras.models.load_model("../model/REL/res/weights.h5")
    pred = model.predict(sequences)
    print(pred.tolist())