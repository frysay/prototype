This project rappresent a prototype that contains a bit of over-all knowledge about Java, Spring framework and Web-services.

Basic Eclipse configuration in order to run the project:
	Lombok:
	1 - Download lombok.jar from https://projectlombok.org/download
	2 - Copy the jar into the eclipse folder
	3 - Open eclipe.ini and add after -vmargs the following 2 lines:
		-Xbootclasspath/a:lombok.jar
		-javaagent:lombok.jar
	4 - Restart Eclipse
	5 - Clean project
	
About the project:
	It is a dummy webservice with 2 controllers and a post endpoint in each that gets, validates and stores some data
	assuming it comes from different parts of front-end (from some parts already validated by FE and from some others not).
	The BasicRequestCotroller just process the request without considering possible interaction from an external source (like an hacker)
	The ValidatedRequestController adds a field in order to verify that the request is actually coming from front-end using a key
	that is shared between BE and FE. Note: it is a verification and not an encription. It uses the HMAC algorythm to encode and verify
	the data; it's a technique based on the assumption that the encode is unique and can't be reverted. Actually it has been proven
	that it might have some collisions but it's very rare and it is still used quite a lot.
	
Structure:
	The whole configurations are in the config package and they contain general configurations for the app and the configuration for
	the main database that in this case is just in-memory database. Aspects are used to verify the request for the ValidatedRequestController.
	In the tests there are different configurations that are used for normal integration tests (profile mock) or db query (profile !mock);
	the ApplicationTest loads the real context to verify that the everything is set properly.
	Note: there are 2 different types of ExceptionHandler to show how to override the over-all behaviour of the GenericRestExceptionHandler
	just using the priority with which these two techniques are invoked.
	
JUnit:
	There is just the JUnit for the RequestService just to show how it can be easily used to verify simple class methods.

Integration tests:
	They are loaded with the profile mock that overrides the configuration for the BasicObjectDao with a mock repository; this is done
	because in the integration tests you are not supposed to load and test parts of the real context like the real db functions (for
	that there are better tests like cucumber tests for example). It also uses another in-memory database that in this case is actually
	the same of the one in the main configuration but just because in the main configurations I didn't really want to provide a real db.
	Note: the last test of the BasicRequestController proves how it's possible to forge the request and successfully save some data into
	the db. On the other hand the last test of the ValidatedRequestController, that's actually the same of the BasicRequestController,
	results in the expected exception that avoids forgery.
	The BasicRequestController and the ValidatedRequestController also prove which one of the ExceptionHandler are used (errorCode = 1 
	for the first and errorCode = 3 for the latter).
	
Final note:
	There are many other things that could have been done to show different behaviour and techniques but the scope was just to show-off
	a bit of basic knowledge about Java, Spring framework and Web-services; I'll probably add new ones later on.