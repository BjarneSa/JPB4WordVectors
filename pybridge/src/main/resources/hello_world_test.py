import io, os, sys, json
import fasttext
import configparser
import socket
import pytest
from configparser import RawConfigParser
from flask import Flask, jsonify, request
from array import array

app = Flask(__name__) #server running on flask device

dir_path = os.path.dirname(os.path.realpath(__file__)) #file path of this file

config = configparser.RawConfigParser()
configFilePath = os.path.join(dir_path, 'config.properties') #configFile should be saved in the same folder and named config.properties
config.read(configFilePath)

# Loading configuration from configuration file for url and port
url_address = config['server']['ipAddress']
port = config.getint('server', 'port')

model = None
wordVectors = None
modelFileName = config['vectors']['modelFileName']

def init_db():
    return


def load_vectors(filename, percentage_to_load):
    """
    This method loads the word vector model. The amount of vectors is dependent on size of model and loading percentage.

    :param filename: word vector model
    :param percentage_to_load: amount of vectors from model (default 10%)
    :return: encoded data
    """
    fin = io.open(filename, 'r', encoding='utf-8', newline='\n', errors='ignore')
    n, d = map(int, fin.readline().split())
    limit = n // 100
    limit_inc = n // 100
    cnt = 0

    data = {}
    print("Found " + str(n * percentage_to_load) + " word vectors of dimension " + str(d))
    for line in fin:
        if cnt == limit:
            limit += limit_inc
            printedOutNumber = cnt / n * 100
            numString = "{0:.2f}".format(printedOutNumber)
            outputString = "    (" + numString + "%)"
            if cnt != 0:
                for c in outputString:
                    print("\b")
            print(outputString)
            # For test purposes.
            if printedOutNumber >= percentage_to_load:
                break

        cnt = cnt + 1
        tokens = line.rstrip().split(' ')
        # data[tokens[0]] = array('f', map(float, tokens[1:]))
        data[tokens[0]] = array('f', [float(x) for x in tokens[1:]])
    print()
    return data


print("To use the pybridge server, a model of word vectors will be loaded now. This can take a moment.")
if len(sys.argv) < 2:
    percentage_to_load = 10
    print("No word vector load percentage given. Using 10% (default)")
else:
    if int(sys.argv[1], 10) <= 100:
        percentage_to_load = int(sys.argv[1], 10)
        print("Loading " + str(percentage_to_load) + "% of all word vectors.")
    else:
        percentage_to_load = 10
        print(sys.argv[1])
        print("Word vector load percentage improper. Using 10% (default)")
wordVectors = load_vectors(modelFileName, percentage_to_load)
print("Loading successful.")

def get_jsonified_vector(word, vector):
    """
    Putting word and vector data in a json object
    :param word: from model's language
    :param vector: values are floats
    :return: json object
    """
    return jsonify({'word': word, 'vector': list(vector)})

def shutdown_server():
    """
    Shutting down the server gracefully. User does not have to use ctrl + c.
    :return:
    """
    func = request.environ.get('werkzeug.server.shutdown')
    if func is None:
        raise RuntimeError('Not running with the Werkzeug Server')
    func()

print('Hello world')
