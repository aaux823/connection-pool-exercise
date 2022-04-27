Introduction
------------

The goal of this exercise is to implement a simple connection pool. Interfaces defining the API and a skeleton
implementation class have been provided. Your task is to implement the methods in the `ConnectionPoolImpl` class,
following the instructions in the Javadoc comments.

This is an 'open book' exercise that you should research and implement to demonstrate your development skills.

We don't want this to be a major time burden for you. If you have limited time focus on implementing the basic API contract 
with working, debuggable code that works when tested, ahead of implementing extra features.

Building
--------

We have provided a simple Gradle project that uses the `java-library` plugin to build and test Java code. You should not
need to make major changes to it.

Use `./gradlew tasks` to see the tasks provided by the plugin.

Testing
-------

We expect you to provide the code you used to test your pool implementation. Quality is crucial for a library like this,
so think carefully about your test coverage, including readability and maintainability of the test code.

Using modern unit testing libraries, you may not need to implement the Connection or ConnectionFactory interfaces to
write and test your connection pool, but if you do then keep the implementations very simple.

Dependencies
------------

You can (and should) use JDK or open source libraries for things like concurrent data structures, logging, testing, etc.
You should not use a third-party connection pool implementation however!

Tips
----

* We want to see that you've read the instructions in the source files and implemented the basic API contract as
  documented.
* We are looking for clean, documented, debuggable, and tested code, that works when we try it.
* Include a README with your submission for anything we need to know in order to run your code and details that you
  think would be helpful for us to understand your implementation, for example: key design decisions, assumptions you
  made, and limitations you're aware of.
