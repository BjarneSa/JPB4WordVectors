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

print('Hello world')
