# Ex2_OOP
## Part one Ex2_1 

In this assigment we are creating various text files in a random size, and calculating the number of lines in each file.  
We are using three methods:
  - No threads
  - Threads
  - Threadpool 
 
Finally, we are calculating the elapsed time of each method's execution.
### Creating text files
This function receives an integer n that represents the number of text files to be created. 
Each of the files is built by a random number of lines to be filled. Evantually an array with the names of each file is returned. 

### No thread function
This is a function that goes through an array of file names, and counts the number of lines using a "hasnextline" method of Scanner.

### Threads function 
First of all, the _LineCountThread_ is an object that extends thread functionality. The run method implements the same methods the previous function has implemented- going through each of the file in the given array and saving it's number of lines. 
The main method creates a new LineCountThread, with each of the file name, activating it's _run_ method by _start()_, and finally joining each of the threads to get a summed counter of lines as one variable. 

### Threadpool function
First of all, the _LineCountTPool_ is an object that implements a _Callable_ interface.
In Java, a _callable_ interface is an interface that defines a single method--> _call()_, which is used to encapsulate a task that can be executed concurrently by multiple threads. 
The _Callable_ interface is similar to the _Runnable_ interface, but it is designed to allow a thread to return a result when it completes its task.
The _call()_ method implements the same functionality described before, meaning that is calculating the number of lines of each text file in the array. 
In our main program, the function 

to be continue..


 
