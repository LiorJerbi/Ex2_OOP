# Ex2_OOP
## Part one Ex2_1 

In this assigment we are creating various text files in a random size, and calculating the number of lines in each file.  
We are using three methods:
  - No threads
  - Threads
  - Threadpool 
 
Finally, we are calculating the elapsed time of each method's execution.
#### Creating text files
This function receives an integer n that represents the number of text files to be created. 
Each of the files is built by a random number of lines to be filled. Evantually an array with the names of each file is returned. 

#### No thread method
This is a function that goes through an array of file names, and counts the number of lines using a "hasnextline" method of Scanner.

#### Threads method
First of all, the _LineCountThread_ is an object that extends thread functionality. The run method implements the same methods the previous function has implemented- going through each of the file in the given array and saving it's number of lines. 
The main method creates a new LineCountThread, with each of the file name, activating it's _run_ method by _start()_, and finally joining each of the threads to get a summed counter of lines as one variable. 

#### Threadpool method
First of all, the _LineCountTPool_ is an object that implements a _Callable_ interface.
In Java, a _callable_ interface is an interface that defines a single method--> _call()_, which is used to encapsulate a task that can be executed concurrently by multiple threads. 
The _Callable_ interface is similar to the _Runnable_ interface, but it is designed to allow a thread to return a result when it completes its task.
The _call()_ method implements the same functionality described before, meaning that is calculating the number of lines of each text file in the array. 
Our main method creates a fixed-sized thread pool with a number of threads equal to the number of files to be processed. Also, it initialize an _Array list_ to store in the Future objects returned by the thread pool. It iterates over the list of file names and creates a _LineCountTPool Callable_ object for each file name. Later on submits the Callable object to the thread pool and stores the returned Future object in the list, and finally, iterates over the list of _Future_ objects and retrieves the result of each _Future_ object by calling the _get()_ method. Each result is added to the counter and the thread pool is shut down. 

### Overall structure :classical_building:

<img src="https://user-images.githubusercontent.com/118935583/211159843-93fcb1e8-cb78-4af6-80e4-6294859ccd24.png" alt="drawing" width="600"/>


When running the program, we are creating 10,000 text files to be reviewed by our functions, by using _createTextFiles_ function which creates files with a random number of lines between 1-999999 containing the following string;
> "Hello world from yael and lior" 

Within each call of the methods over our file, we are measuring the elapsed time for each action. 

### Findings :stopwatch:

<img src="https://user-images.githubusercontent.com/118935583/211162212-bcb84e8f-b0f6-489c-a1a5-d8856cbb11c2.png" alt="drawing" width="800"/>

As we can see, they all counted the same amount of lines, which mean there is no differences between their functionality, as desired. 
* The "normal" method took total time of 9 minutes and 45 seconds of execution time. 
* The "Thread" method took total time of 4 minutes and 23 seconds of execution time.
* And finally, the "ThreadPool" method took total time of 2 minutes and 24 seconds of execution time. 

We see clearly that threads are very efficient when it comes to a long tasks such as reading from files. 
But more clearly is that the _ThreadPool_ method is the most efficient one of all for such tasks. However, we have encountered a cases where normal _Thread_ method was more efficient, particularly in cases where we worked on a lower number of files. 

to be continue...	:zombie:

 
