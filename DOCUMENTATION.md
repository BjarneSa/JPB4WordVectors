Documentation python-java-bridge for word vector learning model

1. Configure word vector model:
	The project has been developed with pretrained models of word vectors based on FastText.
	You can either use the small extract included in the upload or better download a model from https://fasttext.cc/docs/en/english-vectors.html.
	Save the model in folder 'resources' and change the model filename in the configuration file 'config.properties'.
	Please note that the word vector file must be of type .vec.

2. Configure server settings:
	By default, the server runs on localhost and port 2525.
	You can change the url and port in the configuration file.
	The server has only been tested on localhost yet. The project definitely allows different ports.
	Linux systems only allow the occupancy of ports greater 1024.

3. Python reference:
	Java calls a python file to start a python server using flask.
	Please do not change the folder structure or move 'pybridgeApplication.py' to another directory.
	If you want to use another version or change the python filename, make changes in the configuration file.
	The python side uses a load percentage for better testing and performance.
	By default, the load percentage is set to 10%. For large learning models, this includes all common words of the language as well as symbols and numbers.
	If you want to load more vectors, configure load percentage.

4. Usage:
	All actions can be done on java side. Python provides endpoints which can be addressed with the corresponding URL route.
	You can use the 'main' method in class 'App'.
	It initiliazes the python server and opens an HTTP connection.
	After successfully starting the server you can request word vectors using System.in or shutdown the server by typing 'shutdown!'.
	All requested word vectors will be saved in the word vector database.

5. Testing:
	Java side has been tested with JUnit, Python with unittest.
	Simple tests are included in the Maven build.
	Therefore, AppTest starts the server, request some vectors, which will be compared to exact values from the model, and shuts the server down.
	To only test python side, use file 'test_pybridge.py'.
	Please note that test result can be dependent on the size of the learning model and on load percentage.

6. Logging:
	log4j is included in the project.
	If you want to change the logging configuration (default: debug and logging to console), open 'log4j.properties'.
