[![Build Status](https://travis-ci.org/BjarneSa/JPB4WordVectors.svg?branch=master)](https://travis-ci.org/BjarneSa/JPB4WordVectors)

# JPB4WordVectors
Java-Python bridge for word vector learning model

This is a project dealing with word embedding. Based on fastText, we have pre-trained models of word vectors.
See https://fasttext.cc/docs/en/english-vectors.html for models trained on Wikipedia using fastText.
With 300 double entries, each vector represents the semantic relation within a language.

There a several learning and loading options in python. But many programming environments are written in java.
So we set up a python server using flask. 
The python server is able to load a pre-trained model from a .vec file and provide endpoints to get a vector to a word.
These endpoints can be called from java side using pybridge.
Therefore, we implemented methods for starting the python server from java side and send HTTP requests.
Every returned vector can be converted to a java object and saved in a word vector database.
The setup has been tested with JUnit 5 and Python Unittest.

To use the server by yourself, please configure the environment using the java and python configuration files.
For more information, please read the documentation. [a relative link](DOCUMENTATION.markdown)

Thanks for reading! Enjoy!
