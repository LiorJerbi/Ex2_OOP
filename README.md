# Ex2_OOP
## Part one Ex2_1 

In this assignment we are creating various text files in a random size, and calculating the number of lines in each file.  
We are using three methods:
  - No threads
  - Threads
  - Threadpool 
 
#### Creating text files
This function receives an integer n that represents the number of text files to be created. 
Each of the files is built by a random number of lines to be filled. Evantually an array with the names of each file is returned. 

#### No thread method
This is a function that goes through an array of file names, and counts the number of lines using a _hasnextline()_ method of _Scanner_.

#### Threads method
First of all, the _LineCountThread_ is an object that extends thread functionality. The run method implements the same methods the previous function has implemented- going through each of the files in the given array and saving it's number of lines. 
The main method creates a new LineCountThread, with each of the file name, activating it's _run_ method by _start()_, and finally joining each of the threads to get a summed counter of lines as one variable. 

#### Threadpool method
First of all, the _LineCountTPool_ is an object that implements a _Callable_ interface.
In Java, a _callable_ interface is an interface that defines a single method--> _call()_, which is used to encapsulate a task that can be executed concurrently by multiple threads. 
The _Callable_ interface is similar to the _Runnable_ interface, but it is designed to allow a thread to return a result when it completes its task.
The _call()_ method implements the same functionality described before, meaning that is calculating the number of lines of each text file in the array. 
Our main method creates a fixed-sized thread pool with a number of threads equal to the number of files to be processed. Also, it initializes an _Array list_ to store in the Future objects returned by the thread pool. It iterates over the list of file names and creates a _LineCountTPool Callable_ object for each file name. Later on submits the _Callable_ object to the thread pool and stores the returned Future object in the list, and finally, iterates over the list of _Future_ objects and retrieves the result of each _Future_ object by calling the _get()_ method. Each result is added to the counter and the thread pool is shut down. 

### Overall structure :classical_building:

<img src="https://user-images.githubusercontent.com/118935583/211402146-7e2f44f8-8101-47e8-afb2-8862c249f119.jpeg" alt="drawing" width="600"/>

### Findings :stopwatch:

When running the program, we are creating 10,000 text files to be reviewed by our functions, by using _createTextFiles_ function which creates files with a random number of lines between 1-999999 containing the following string;
> "Hello world from yael and lior" 

Within each call of the methods over our file, we are measuring the elapsed time for each action. 

<img src="https://user-images.githubusercontent.com/118935583/211162212-bcb84e8f-b0f6-489c-a1a5-d8856cbb11c2.png" alt="drawing" width="800"/>

As we can see, they all counted the same amount of lines, which mean there is no differences between their functionality, as desired. 
* The "normal" method took a total time of 9 minutes and 45 seconds of execution time. 
* The "Thread" method took a total time of 4 minutes and 23 seconds of execution time.
* And finally, the "ThreadPool" method took a total time of 2 minutes and 24 seconds of execution time. 

We see clearly that threads are very efficient when it comes to a long tasks such as reading from files. 
But more clearly is that the _ThreadPool_ method is the most efficient one of all for such tasks. However, we have encountered some cases where normal _Thread_ method was more efficient, particularly in cases where we worked on a lower number of files. 

## Part two Ex2_2 
Java enables developers to set the priority of a thread, but not the _Runnable_ operation it executes.
Tightly coupling the operation with the execution path that runs it creates major drawback when
using an executor such as a _ThreadPoolExecutor_: the collection of threads in an executor is defined by
a _ThreadFactory_. By default, it creates all threads with the same priority and non-daemon status.
Moreover, if we wish to execute a returning value operation, for example using the _Callable<V>_
interface, there are no constructors in the Thread class that get a _Callable<V>_ as parameter and we
ought to use an Executor of some type, such as a _ThreadPoolExecutor_[^1].

In this part we created a solution for this "issue" by creating two new types that extend the functionality of Java's Concurrency Framework- _Task_ and _CustomExecutor_.  

### Task Class
_Task_ is an asynchronic task with priority that can return a certain value. Task throws _Exception_ if unable to perform a task, simularly as _Callable()_ interface. Implements; _Callable_, which is an interface designed for classes whose instances are potentially executed by another thread, and _Comparable()_, which is an interface that compares two instances. The _Comparable_(and _comparator_) helps us make our instances of Task compatible for prioritize tasks in executors proccess. _Task_ contains an _Enum_ "Class" named _TaskType_ that represents the group of constants which defines our task priority range[^2].

Constructors; Creating a new _Task_ instance can be done only by the _creatTask()_ method (uses a factory design pattern) that receives a _Callable_ object, and _TaskType_ object or only a _Callable_ object (in cases there are no priority it receives the defult (3)). These factory methods uses the private constructor to ensure a proper construct of the new _Task_. 

### CustomExecutor Class
_CustomExecutor_ is a type of _ThreadPool_ that executes instances of _Task_ according to it's priority preferences. 
Concise explanation of some of class methods/fields. 
* _ThreadPoolExecutor_- An executor mechanism that uses a pool of threads to execute calls asynchronicously . 
* _PriorityBlockingQueue_ - An unbounded blocking queue that uses the same ordering rules as class _PriorityQueue_ and supplies blocking retrieval operations[^4]. 
It's _Comparator_ will be used to order our _Tasks_ by priority. 
* _submit()_- Submits either an asynchronic task or a _TaskType_ with an asynchronic task or a built-in _Task_ to the _PrioityBlockingQueue_ and returns a _Future_ representing the pending result[^3] after it's execution. 
* _maxPrio_ - Holds the value of the highest rated _Task_ that has been submitted to the queue. It is being updated in each call for _submit()_. 
* _gracefullyTerminate()_ - Activation will block insertion of new _Task_s to the queue, executing the remaining _Task_s and finishing all the _Task_s that are currently in the threads of the class. 

### Overall structure :classical_building: 
<img src="https://user-images.githubusercontent.com/118935583/211402584-e0486e86-52da-42db-b375-0d1774d88631.jpeg" alt="drawing" width="600"/>

[^1]: Assignment 2 Part 2/Built-in-limitations 
[^2]: Priority range is 1-10, therefore 1 is the highest priority, and respectively 10 is the lowest one.
[^3]: JavaDoc/ConcurrentAbstractExecutorService/submit() 
[^4]: Oracle site 
