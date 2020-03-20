import io, os, sys, json
import fasttext
import configparser
import socket
import pytest
from configparser import RawConfigParser
from flask import Flask, jsonify, request
from array import array

#################################################################

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


##################################################################


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


def get_comparison(word1, word2, vector1, vector2):
    """
    Calculating the sum of all absolute differences between every vector entry.
    :param word1: from model's language
    :param word2: another word
    :param vector1: first vector
    :param vector2: another vector
    :return: comparison as json object with tags word1, word2 and difference
    """
    difference = 0
    for i in range(1, 300):
        difference += abs(vector1[i] - vector2[i])

    return jsonify({'word1': word1, 'word2': word2, 'difference': difference})


def shutdown_server():
    """
    Shutting down the server gracefully. User does not have to use ctrl + c.
    :return:
    """
    func = request.environ.get('werkzeug.server.shutdown')
    if func is None:
        raise RuntimeError('Not running with the Werkzeug Server')
    func()

##############################################################################

def test_server_port():
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    result = sock.connect_ex((url_address, port))
    if result == 0:
        return True
    else:
        return False


def test_server_ip():
    timeout = 5
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(timeout)
    try:
        s.connect(url_address)
        s.shutdown(socket.SHUT_RDWR)
        return True
    except:
        return False
    finally:
        s.close()


def test_get_vector(self):
    request_king = get_vector('king')
    data_king = json.loads(request_king.content)
    assert data_king["0"] == 0.10819999873638153


##############################################################################

@app.route('/')
def main():
    """
    Main http route. Welcomes user on server and refers to /getVector/'word' route.
    :return: Welcome message
    """
    return "Welcome to the server. " \
           "To get a word vector, go to localhost/getVector/'word'"


# This method requires a file of type .bin
@app.route('/loadModel/<fname>')
def load_model(fname):
    model = fasttext.load_model(fname)
    # model = FastText.load_fasttext_format(fname) #trying alternatively but still causing error (too long)
    if model is None:
        return "Loading file has failed"
    else:
        return "Loading successful"


@app.route('/getVector/')
def get_vector_without_input():
    """
    No method to get all vectors. Parameter must be specified.
    :return: Hint to getVector/'word'
    """
    return "To get a word vector, please insert an input word behind the last slash"


@app.route('/getVector/<word>', methods={'GET'})
def get_vector(word):
    """
    Main route to receive a word vector to given word
    Vector will be returned as json file
    Method has been tested using: curl -v http://localhost/getVector/<word>
    :param word: for which you want to request a vector
    :return: --
    """
    try:
        return get_jsonified_vector(word, wordVectors[word])
    except TypeError as err:
        raise TypeError('The given input ' + str(err) + 'cannot be converted to String ')
    except KeyError as err:
        raise KeyError(' The given word ' + str(err) + 'was not found in the current model. Please try another, '
                                                       'more common word.')

@app.route('/compare/<word1>_<word2>')
def compare_two_words(word1, word2):
    return get_comparison(word1, word2, wordVectors[word1], wordVectors[word2])


@app.route('/test/')
def mainTest():
    if test_server_port():
        if test_server_ip():
            return "Server is up and port is open"
        else:
            return "Port is open but socket ip test failed"
    else:
        return "Server is not running on the given ip and port"


@app.route('/shutdown', methods=['GET'])
def shutdown():
    """
    Route for shutting down the server gracefully.
    :return: Success message
    """
    shutdown_server()
    return 'Server shutting down'


#####################################################################

# Entry point for the program. Starting the application with url and port.
if __name__ == '__main__':
    app.run(debug=True, host=url_address, port=port, use_reloader=False)
