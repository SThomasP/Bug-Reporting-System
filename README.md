A distributed notifications framework, developed for a university coursework.

There are two main executables, the BugLogger and the BugReporter.

The BugLogger is the server side application with a NotificationSink
It takes two arguments
 The First being the port you want to host the rmi on.
 The Second being the folder you want to save the csvs to.

The BugReporter is the client side applicaiton with a NotificationSource
It needs at least one argument, but will take any number.
each argument being the portnumber of a BugLogger hosted on the local machine
