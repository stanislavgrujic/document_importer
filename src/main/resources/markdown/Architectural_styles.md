# Architectural styles 

###### {"semantic":"why"}

There are different architectural styles depending on type of applied architectural structural principles or different ways how to and when to process data.

###### {"semantic":"types"}

Structural styles:
*   Monolith
*   Microservices
*   Modular monolith

###### {"semantic":"types", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Styles that address different place where complexity and bottleneck is:

*   Data-intensive systems \
Data is primary challenge - quantity, complexity, speed, e.g. real time business reporting
*   Compute-intensive systems \
CPU cycles are bottleneck, not so frequent problem, e.g. Neural Networks

###### {"semantic":"types"}

Processing styles:
*   OLTP / service / online system
*   Batch processing system / offline systems
*   Stream processing system / near-real-time systems

###### {"semantic":"comparison", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/ch03.html#sec_storage_dwh", "source-author":"Martin Kleppmann", "source-details":"Read entire section >transaction processing or analytics<"}

![alt_text](/images/Architectural-styles0.png "image_tooltip")



## Processing Styles 


### OLTP / service / online system 

###### {"semantic":"intro-definition", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

A service waits for a request or instruction from a client to arrive. When one is received, the service tries to handle it as quickly as possible and sends a response back. Response time is usually the primary measure of performance of a service, and availability is often very important (if the client can’t reach the service, the user will probably get an error message).

###### {"semantic":"tool"}

Web server popularity


![alt_text](/images/Architectural-styles1.png "image_tooltip")


###### {"semantic":"tool"}

Java app server popularity


![alt_text](/images/Architectural-styles2.png "image_tooltip")


###### {"semantic":"explanation"}

**Is it a good idea to have a web/app server and data storage server on the same machine/OS?**

Yes while app is small or medium size - requests per second up to 100 req/s and data storage up to 5GB.

Positive side of having them together is easier administration and fast inter-communication.

Here are architecture characteristics that can be important enough to go toward separated installation:

**Security**

Your web server lives in a DMZ, accessible to the public internet and taking untrusted input from anonymous users. If your web server gets compromised, and you've followed least privilege rules in connecting to your DB, the maximum exposure is what your app can do through the database API.

Ideally your database server should be sitting behind a firewall with only the ports required to perform data access opened. Your web application should be connecting to the database server with a SQL account that has just enough rights for the application to function and no more. For example you should remove rights that permit dropping of objects and most certainly you shouldn't be connecting using accounts such as 'sa'.

In the event that you lose the web server to a hijack (i.e. a full blown privilege escalation to administrator rights), the worst case scenario is that your application's database may be compromised but not the whole database server (as would be the case if the database server and web server were the same machine). If you've encrypted your database connection strings and the hacker isn't savvy enough to decrypt them then all you've lost is the web server.

**Scalability**

Keeping your web server stateless allows you to scale your web servers horizontally pretty much effortlessly. It is _very_ difficult to horizontally scale a database server.

Usually web servers are on cheaper machines.

**Resource sharing**

If you have an application that is resource intensive, it can easily cause the CPU cycles on the machine to peak, essentially bringing the machine to a halt. 

Usually database servers host more than one schema so expensive query in other app can influence your web server performance. 

Web Servers have different hardware requirements than database servers. Database servers fare better with a lot of memory and a really fast disk array while web servers only require enough memory to cache files and frequent DB requests (depending on your setup). 

**Cost**

Regarding cost effectiveness, the two servers won't necessarily be less expensive, however performance/cost ratio should be higher since you don't have to different applications competing for resources. For this reason, you're probably going to have to spend a lot more for one server which caters to both and offers equivalent performance to 2 specialized ones.

Some databases prices are dependent on the number of CPU cores so having it separate makes price lower.

**Monitoring**

Easier to spot which part you have to optimize if they are separated

**Recovery**

Easier to apply recovery procedures independently

###### {"semantic":"explanation"}

**How many requests can a web server handle?**

Usual load span: from 50 to 1000 RPS; depends if usage is more IO (blocked or not) or processing (how runtime engine is optimized)

Use cases: 

*   web page 
*   REST service

Usual products:

*   Apache
*   Tomcat
*   JBoss
*   IIS
*   Nginx - used frequently also as reverse proxy server (url redirecting, port forwarding, authentication over network)
*   Node.js

Usual numbers from production:

*   WhatsApp 740 RPS (requests per second)
*   Stack Overflow 800 RPS (3 servers)
*   Twitter 6.000 RPS
*   Wikipedia 50.000 RPS on 300 machines (150 RPS per machine)

Too general question like asking: how big is a van, and how many vans you will need to move 10,000 things?

10,000 people watching Netflix is different to 10,000 people sending a tweet.

###### {"semantic":"explanation"}

**How much time is needed for a VM/Docker instance to get up and running?**

Usual time for server VM image to become up-and-running without installation scripts execution is 3-5 minutes. Usual time for docker image to become up-and-running is 5-15 seconds.

Influences elasticity which match the supply of resources (which cost money) and demand

Scalability is a long term design choice. Elasticity is a short term ability to handle load.

Elasticity enables businesses to dynamically mitigate variability in demand, along with the peaks and valleys in the demand for an IT service. 

Scalability enables businesses to meet expected demand for services without the need for large, up-front investments in infrastructure.

Elasticity usually includes scalability but not vice versa. Also elasticity includes automation of resource allowaction.

Think of an elastic band: what makes it "elastic" is partly its ability to stretch under pressure, but also the way it quickly returns to its original size when the pressure is released. 

Scalability describes the way a system is designed to meet changing demand - underlying design itself supports rapid and unpredictable changes.

As an example, software that’s scalable can be easily picked up and dropped onto a new server -– possibly within a new network environment -– and just run without any manual configuration. Similarly, the composition of a scalable infrastructure can be quickly changed in a way that all the old bits and pieces immediately know how to work together with the new ones.


### Batch processing system / offline systems 

###### {"semantic":"intro-definition", "source-link":"[https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/ch10.html#ch_batch](https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/ch10.html#ch_batch", "source-author":"Martin Kleppmann", "source-details":"Read entire section >Batch processing<"}

A batch processing system takes a large amount of input data, runs a job to process

it, and produces some output data. Jobs often take a while (from a few minutes to several days), so there normally isn’t a user waiting for the job to finish. Instead, batch jobs are often scheduled to run periodically (for example, once a day). The primary performance measure of a batch job is usually throughput (the time it takes to crunch through an input dataset of a certain size). We discuss batch processing in this chapter.


### Messaging systems 

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=ITrlLErsqzY", "time-period":"00:05:15 - 00:06:19", "source-author":"Clemens Vasters", "mastery":"medium"}

Why so many different messaging tools?

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=ITrlLErsqzY", "time-period":"00:06:21 - 00:14:20", "source-author":"Clemens Vasters"}

Why so many different messaging tools?

###### {"semantic":"example", "source-link":"https://www.youtube.com/watch?v=ITrlLErsqzY", "time-period":"00:14:25 - 00:21:38", "source-author":"Clemens Vasters"}

Different cases with different architecture

###### {"semantic":"example", "source-link":"https://www.youtube.com/watch?v=-3gOqR_TGEs", "time-period":"00:17:49 - 00:26:43", "source-author":"Clemens Vasters", "mastery":"medium"}

Messaging vs eventing

###### {"semantic":"comparison"}

Messaging & eventing characteristics

*   Event
    *   ‘something happened’ type of info
    *   info is broadcasted via pub-sub, 
    *   it has type, 
    *   Pub-sub pattern
    *   publisher does not know or care who is receiving event, 
    *   example ‘file created", ‘new log entry added’
    *   It is unidirectional (no response path since you don’t expect that someone will do something), no fields ‘to", ‘reply-to’ or ‘topics/queues’ because it is presumed it will be forwarded and routed several times, it has message origin
    *   Enables extension of the application with new functionality without explicit instruction to that functionality, e.g. subscribe to event ‘file created’ and do post processing, add additional subscription to send email about new file
    *   Enables extension driven programming
    *   They don’t model RPC exchanges
*   Message
    *   Carry intent and sender expects something will be performed
    *   Command execution, sent to handler expecting status and complex flow control like Sagas and Workflows
    *   Usually sent to one recipient
    *   Typically lives in queues because sender want that one party pick up that message and process it once 

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "tags":"example"}

**Event ordering**

In cases where there is no causal link between events, the lack of a total order is not a big problem, since concurrent events can be ordered arbitrarily.

There are many cases in real life where event order is important, especially when they are messages and not events. For example in a bank account, it is important in which order following events are executed: "add 100$ in account’ and ‘remove 100% from account’; in one order the entire sequence cannot be valid if there is initially enough money on account.

One more example, consider a social networking service, and two users who were in a relationship but have just broken up. One of the users removes the other as a friend, and then sends a message to their remaining friends complaining about their ex-partner. The user’s intention is that their ex-partner should not see the rude message, since the message was sent after the friend status was revoked. However, in a system that stores friendship status in one place and messages in another place, that ordering dependency between the unfriend event and the message send event may be lost. If the causal dependency is not captured, a service that sends notifications about new messages may process the message-send event before the unfriend event, and thus incorrectly send a notification to the ex-partner.

With systems that are small enough, constructing a totally ordered event log is entirely feasible but not for big systems. In most cases, constructing a totally ordered log requires all events to pass through a single leader node that decides on the ordering. If the throughput of events is greater than a single machine can handle, you need to partition it across multiple machines which further complicates the solution. There is not 100% guarantee that different machines have the same system clock which makes precise time comparison as order tool useless.

There is no ideal solution that provides distributed systems that offer event orders and that’s why for example Kafka offers ordering on partition level only which is hosted on one node/machine.

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

The big advantage of log-based integration is loose coupling between the various components, which manifests itself in two ways:



1. At a system level, asynchronous event streams make the system as a whole more robust to outages or performance degradation of individual components. If a consumer runs slow or fails, the event log can buffer messages, allowing the producer and any other consumers to continue running unaffected. The faulty consumer can catch up when it is fixed, so it doesn’t miss any data, and the fault is contained. By contrast, the synchronous interaction of distributed transactions tends to escalate local faults into large scale failures.
2. At a human level, unbundling data systems allows different software components and services to be developed, improved, and maintained independently from each other by different teams. Specialization allows each team to focus on doing one thing well, with well-defined interfaces to other teams’ systems. Event logs provide an interface that is powerful enough to capture fairly strong consistency properties (due to durability and ordering of events), but also general enough to be applicable to almost any kind of data.


#### Stream processing system / near-real-time systems  

###### {"semantic":"intro-definition", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Stream processing is somewhere between online and offline/batch processing (so it is sometimes called near-real-time or nearline processing). Like a batch processing system, a stream processor consumes inputs and produces outputs (rather than responding to requests). However, a stream job operates on events shortly after they happen, whereas a batch job operates on a fixed set of input data. This difference allows stream processing systems to have lower latency than the equivalent batch systems. As stream processing builds upon batch processing.

###### {"semantic":"why", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

In reality, a lot of data is unbounded because it arrives gradually over time: your users produced data yesterday and today, and they will continue to produce more data tomorrow. Unless you go out of business, this process never ends, and so the dataset is never “complete” in any meaningful way [1]. Thus, batch processors must artificially divide the data into chunks of fixed duration: for example, processing a day’s worth of data at the end of every day, or processing an hour’s worth of data at the end of every hour. The problem with daily batch processes is that changes in the input are only reflected in the output a day later, which is too slow for many impatient users. To reduce the delay, we can run the processing more frequently—say, processing a second’s worth of data at the end of every second—or even continuously, abandoning the fixed time slices entirely and simply processing every event as it happens. That is the idea behind stream processing. A “stream” refers to data that is incrementally made available over time, for example stdin and stdout, TCP connection…

We will look at event streams as a data management mechanism: the unbounded, incrementally processed counterpart to the batch data.

###### {"semantic":"metrics", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Let’s do a back-of-the-envelope calculation. At the time of writing, a typical large hard drive has a capacity of 6 TB and a sequential write throughput of 150 MB/s. If you are writing messages at the fastest possible rate, it takes about 11 hours to fill the drive. Thus, the disk can buffer 11 hours’ worth of messages, after which it will start overwriting old messages. This ratio remains the same, even if you use many hard drives and machines. In practice, deployments rarely use the full write bandwidth of the disk, so the log can typically keep a buffer of several days’ or even weeks’ worth of messages.

###### {"semantic":"types", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Three options of stream processing:



1. take the data in the events and write it to a database, cache, search index, or similar storage system, from where it can then be queried by other clients. As shown in Figure 11-5, this is a good way of keeping a database in sync with changes happening in other parts of the system
2. push the events to users in some way, for example by sending email alerts or push notifications, or by streaming the events to a real-time dashboard where they are visualized. In this case, a human is the ultimate consumer of the stream.
3. process one or more input streams to produce one or more output streams. Streams may go through a pipeline consisting of several such processing stages before they eventually end up at an output (option 1 or 2). This case is usually called “stream analytics” or “complex event processing” (CEP) and includes searching for certain event patterns.

###### {"semantic":"comparison", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

The one crucial difference to batch jobs is that a stream never ends. This difference has many implications: sorting does not make sense with an unbounded dataset, and so sort-merge joins cannot be used. Fault-tolerance mechanisms must also change: with a batch job that has been running for a few minutes, a failed task can simply be restarted from the beginning, but with a stream job that has been running for several years, restarting from the beginning after a crash may not be a viable option.

###### {"semantic":"use-case", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Stream processing has long been used for monitoring purposes, where an organization wants to be alerted if certain things happen. For example:



*   Fraud detection systems need to determine if the usage patterns of a credit card have unexpectedly changed, and block the card if it is likely to have been stolen.
*   Trading systems need to examine price changes in a financial market and execute trades according to specified rules.
*   Manufacturing systems need to monitor the status of machines in a factory, and quickly identify the problem if there is a malfunction.
*   Military and intelligence systems need to track the activities of a potential aggressor, and raise the alarm if there are signs of an attack.

These kinds of applications require quite sophisticated pattern matching and correlations.

###### {"semantic":"tip", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

In these systems, the relationship between queries and data is reversed compared to normal databases. Usually, a database stores data persistently and treats queries as transient: when a query comes in, the database searches for data matching the query, and then forgets about the query when it has finished. CEP engines reverse these roles: queries are stored long-term, and events from the input streams continuously flow past them in search of a query that matches an event pattern.

###### {"semantic":"use-case", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Stream analytics use cases:

*   Measuring the rate of some type of event (how often it occurs per time interval)
*   Calculating the rolling average of a value over some time period
*   Comparing current statistics to previous time intervals (e.g., to detect trends or to alert on metrics that are unusually high or low compared to the same time last week)

Such statistics are usually computed over fixed time intervals—for example, you

might want to know the average number of queries per second to a service over the

last 5 minutes, and their 99th percentile response time during that period. Averaging

over a few minutes smoothes out irrelevant fluctuations from one second to the next,

while still giving you a timely picture of any changes in traffic pattern.

###### {"semantic":"comparison", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

The boundary between CEP and stream analytics is blurry, but as a general rule, analytics tends to be less interested in finding specific event sequences and is more oriented toward aggregations and statistical metrics over a large number of events

###### {"semantic":"tool", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Many open source distributed stream processing frameworks are designed with analytics in mind: for example, Apache Storm, Spark Streaming, Flink, Concord, Samza, and Kafka Streams. Hosted services include Google Cloud Dataflow and Azure Stream Analytics.

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=avi-TZI9t2I", "time-period":"00:01:55 - 00:18:20", "source-author":"Martin Kleppmann", "mastery":"medium", "tags":"LinkedIn, example"}

Stream processing explanation

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=fU9hR3kiOK0", "time-period":"00:29:50 - 00:35:22", "source-author":"Martin Kleppmann", "mastery":"advanced"}

Stream everywhere

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Stream of changes to a database can be used to keep derived data systems, such as caches, search indexes, and data warehouses, up to date with a source database. We can regard these examples as specific cases of maintaining materialized views: deriving an alternative view onto some dataset so that you can query it efficiently.

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

In event sourcing, application state is maintained by applying a log of events; here the application state is also a kind of materialized view. Unlike stream analytics scenarios, it is usually not sufficient to consider only events within some time window: building the materialized view potentially requires all events over an arbitrary time period, apart from any obsolete events that may be discarded by log compaction. In effect, you need a window that stretches all the way back to the beginning of time.

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Conventional search engines first index the documents and then run queries over the index. By contrast, searching a stream turns the processing on its head: the queries are stored, and the documents run past the queries.

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

The batch process needs to look at the timestamp embedded in each event. There is no point in looking at the system clock of the machine running the batch process, because the time at which the process is run has nothing to do with the time at which the events actually occurred. Using the timestamps in the events allows the processing to be deterministic: running the same process again on the same input yields the same result.

On the other hand, many stream processing frameworks use the local system clock on the processing machine (the processing time) to determine windowing. This approach has the advantage of being simple, and it is reasonable if the delay between event creation and event processing is negligibly short. However, it breaks down if there is any significant processing lag—i.e., if the processing may happen noticeably later than the time at which the event actually occurred.

###### {"semantic":"example", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

There are many reasons why processing may be delayed. Message delays can also lead to unpredictable ordering of messages.

Confusing event time and processing time leads to bad data.

For example, consider a mobile app that reports events for usage metrics to a server. The app may be used while the device is offline, in which case it will buffer events locally on the device and send them to a server when an internet connection is next available.

In this context, the timestamp on the events should really be the time at which the

user interaction occurred, according to the mobile device’s local clock. However, the

clock on a user-controlled device often cannot be trusted, as it may be accidentally or

deliberately set to the wrong time.

The time at which the event was received by the server (according to the server’s clock) is more likely to be accurate, since the server is under your control, but less meaningful in terms of describing the user interaction.

To adjust for incorrect device clocks, one approach is to log three timestamps:



*   The time at which the event occurred, according to the device clock
*   The time at which the event was sent to the server, according to the device clock
*   The time at which the event was received by the server, according to the server clock

By subtracting the second timestamp from the third, you can estimate the offset between the device clock and the server clock (assuming the network delay is negligible compared to the required timestamp accuracy). You can then apply that offset to the event timestamp, and thus estimate the true time at which the event actually occurred (assuming the device clock offset did not change between the time the event occurred and the time it was sent to the server).

Several types of windows are in common use:

**Tumbling window**

A tumbling window has a fixed length, and every event belongs to exactly one window.

**Hopping window**

A hopping window also has a fixed length, but allows windows to overlap in order to provide some smoothing.

**Sliding window**

A sliding window contains all the events that occur within some interval of each other.

**Session window**

Unlike the other window types, a session window has no fixed duration. Instead, it is defined by grouping together all events for the same user that occur closely together in time, and the window ends when the user has been inactive for some time.


#### Event driven architecture 

###### {"semantic":"why", "source-link":"https://www.youtube.com/watch?v=tdd8w9d_d-c", "time-period":"00:11:18 - 00:19:59", "source-author":"Allard Buijze", "tags":"example, Maslow Syndrome"}

Event driven architecture intro and event/message types


##### Domain events 


##### Event sourcing 

###### {"semantic":"intro-definition", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Event sourcing involves storing all changes to the application state as a log of change events.

The application logic is explicitly built on the basis of immutable events that are written to an event log. In this case, the event store is append only, and updates or deletes are discouraged or prohibited. Events are designed to reflect things that happened at the application level, rather than low-level state changes.

###### {"semantic":"pros", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Event sourcing is a powerful technique for data modeling: from an application point of view it is more meaningful to record the user’s actions as immutable events, rather than recording the effect of those actions on a mutable database. Event sourcing makes it easier to evolve applications over time, helps with debugging by making it easier to understand after the fact why something happened, and guards against application bugs

###### {"semantic":"tip", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Specialized databases such as Event Store have been developed to support applications using event sourcing, but in general the approach is independent of any particular tool. A conventional database or a log-based message broker can also be used to build applications in this style.

###### {"semantic":"warning", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Applications that use event sourcing need to take the log of events (representing the data written to the system) and transform it into application state that is suitable for showing to a user

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

The event sourcing philosophy is careful to distinguish between **events** and **commands**. When a request from a user first arrives, it is initially a command: at this point it may still fail, for example because some integrity condition is violated. The application must first validate that it can execute the command. If the validation is successful and the command is accepted, it becomes an event, which is durable and immutable.

###### {"semantic":"example", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

If a user tries to register a particular username, or reserve a seat on an airplane or in a theater, then the application needs to check that the username or seat is not already taken. When that check has succeeded, the application can generate an event to indicate that a particular username was registered by a particular user ID, or that a particular seat has been reserved for a particular customer. At the point when the event is generated, it becomes a fact. Even if the customer later decides to change or cancel the reservation, the fact remains true that they formerly held a reservation for a particular seat, and the change or cancellation is a separate event that is added later. A consumer of the event stream is not allowed to reject an event: by the time the consumer sees the event, it is already an immutable part of the log, and it may have already been seen by other consumers. Thus, any validation of a command needs to happen synchronously, before it becomes an event—for example, by using a serializable transaction that atomically validates the command and publishes the event. Alternatively, the user request to reserve a seat could be split into two events: first a tentative reservation, and then a separate confirmation event once the reservation has been validated. This split allows the validation to take place in an asynchronous process.

###### {"semantic":"example", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Immutability in databases is an old idea. For example, accountants have been using immutability for centuries in financial bookkeeping. When a transaction occurs, it is recorded in an append-only ledger, which is essentially a log of events describing money, goods, or services that have changed hands. The accounts, such as profit and loss or the balance sheet, are derived from the transactions in the ledger by adding them up If a mistake is made, accountants don’t erase or change the incorrect transaction in the ledger—instead, they add another transaction that compensates for the mistake, for example refunding an incorrect charge. The incorrect transaction still remains in the ledger forever, because it might be important for auditing reasons. If incorrect figures, derived from the incorrect ledger, have already been published, then the figures for the next accounting period include a correction. This process is entirely normal in accounting

###### {"semantic":"pros", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Immutable events also capture more information than just the current state. For example, on a shopping website, a customer may add an item to their cart and then remove it again. Although the second event cancels out the first event from the point of view of order fulfillment, it may be useful to know for analytics purposes that the customer was considering a particular item but then decided against it. Perhaps they will choose to buy it in the future, or perhaps they found a substitute. This information is recorded in an event log, but would be lost in a database that deletes items when they are removed from the cart.

###### {"semantic":"warning", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Besides the performance reasons, there may also be circumstances in which you need data to be deleted for administrative reasons, in spite of all immutability. For example, privacy regulations may require deleting a user’s personal information after they close their account, data protection legislation may require erroneous information to be removed, or an accidental leak of sensitive information may need to be contained.

Truly deleting data is surprisingly hard, since copies can live in many places: for example, storage engines, filesystems, and SSDs often write to a new location rather than overwriting in place, and backups are often deliberately immutable to prevent accidental deletion or corruption. Deletion is more a matter of “making it harder to retrieve the data” than actually “making it impossible to retrieve the data.”

###### {"semantic":"why", "source-link":"https://www.youtube.com/watch?v=tdd8w9d_d-c", "time-period":"00:20:38 - 00:30:36", "source-author":"Allard Buijze"}

Event sourcing intro


##### CQRS 

###### {"semantic":"why", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Storing data is normally quite straightforward if you don’t have to worry about how it is going to be queried and accessed; many of the complexities of schema design, indexing, and storage engines are the result of wanting to support certain query and access patterns. For this reason, you gain a lot of flexibility by separating the form in which data is written from the form it is read, and by allowing several different read views. This idea is sometimes known as command query

responsibility segregation (CQRS).

The traditional approach to database and schema design is based on the fallacy that data must be written in the same form as it will be queried. Debates about normalization and denormalization become largely irrelevant if you can translate data from a write-optimized

event log to read-optimized application state: it is entirely reasonable to denormalize

data in the read-optimized views, as the translation process gives you a mechanism

for keeping it consistent with the event log.


##### Message broker and message queue (Messaging: Request-Reply, Point-to-Point, Publish-Subscribe, Fire-and-forget) 


##### Enterprise service bus 


##### Actor model 

###### {"semantic":"comparison", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Although actor based systems are also based on messages and events, we normally don’t think of them as stream processors:



*   Actor frameworks are primarily a mechanism for managing concurrency and distributed execution of communicating modules, whereas stream processing is primarily a data management technique.
*   Communication between actors is often ephemeral and one-to-one, whereas event logs are durable and multi-subscriber.
*   Actors can communicate in arbitrary ways (including cyclic request/response patterns), but stream processors are usually set up in acyclic pipelines where every stream is the output of one particular job, and derived from a well-defined set of input streams.

###### {"semantic":"cons", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

It is also possible to process streams using actor frameworks. However, many such frameworks do not guarantee message delivery in the case of crashes, so the processing is not fault-tolerant unless you implement additional retry logic.


### Useful numbers 

###### {"semantic":"metrics"}

**Price of a production server**

**To Buy**

Intel Xeon W-2125 

32GB RAM 

2x 500GB SSD

1700$ + 40$/month power, internet 

Dual Intel Xeon Silver 4114 

64GB RAM 

2x 500GB SSD

3500$ + 60$/month power, internet -> on 3 years <span style="text-decoration:underline;">160 $/month</span>

**To Rent **

Hetzner

Intel® Core™ i7-8700 Hexa-Core Coffee Lake incl. Hyper-Threading Technology

64 GB DDR4 RAM

Sata sdd 2 x 1 TB

55eur/month

Windows server standard edition 23 eur / month

Windows server datacenter edition <span style="text-decoration:underline;">143 eur / month</span>

**Cloud**

AWS EC2 8 cores, 32GB 130$ with discount, <span style="text-decoration:underline;">350 $ / month</span> on demand.

###### {"semantic":"metrics", "source-link":"https://www.nngroup.com/articles/response-times-3-important-limits/", "source-author":"Jakob Nielsen"}

**Acceptable response time in UI**



*   **0.1 second** is about the limit for having the user feel that the system is **reacting instantaneously**, meaning that no special feedback is necessary except to display the result.
*   **1.0 second** is about the limit for the **user's flow of thought** to stay uninterrupted, even though the user will notice the delay. Normally, no special feedback is necessary during delays of more than 0.1 but less than 1.0 second, but the user does lose the feeling of operating directly on the data.
*   **10 seconds** is about the limit for **keeping the user's attention** focused on the dialogue. For longer delays, users will want to perform other tasks while waiting for the computer to finish, so they should be given feedback indicating when the computer expects to be done. Feedback during the delay is especially important if the response time is likely to be highly variable, since users will then not know what to expect.

###### {"semantic":"metrics", "source-link":"https://www.gigaspaces.com/blog/amazon-found-every-100ms-of-latency-cost-them-1-in-sales/", "source-author":"Yoav Einav"}

Amazon has observed that a 100 ms increase in response time reduces

sales by 1%, and others report that a 1-second slowdown reduces a customer satisfaction metric by 16%.

Google found an extra .5 seconds in search page generation time dropped traffic by 20%. 

A 2017 Akamai study shows that every 100-millisecond delay in website load time can hurt conversion rates by 7%.

###### {"semantic":"metrics"}

![alt_text](/images/Architectural-styles3.png "image_tooltip")


![alt_text](/images/Architectural-styles4.png "image_tooltip")


###### {"semantic":"metrics"}

**When to use data engineering tech stack?**

Big data defined by three V:



*   Volume - when there is more data than one regular server can deal with, currently more than about **5TB**
*   Velocity - when new incoming data has to be processed faster than one regular server can deal with, currently more than about **5k messages/second**
*   Variety - when there is **all kinds **of multimedia storage that is semi-structured and which structure **will be discovered in later phase** of its usage


## Structural styles 


### Monolith 


### Microservices 

###### {"semantic":"why"}

Chose microservices in following cases:



*   Product management motivation \
You are developing a sub-system of enterprise application and you want to manage it as independent as possible - having clear integration points with the rest of the system, having the possibility of using different technology, having independent testing and deployment into production,
*   Complexity management \
Split system into subsystems (microservices) to enforce clear integration points and maximise their independency
*   System characteristic (ility) motivation \
You must run multiple instances of the application on multiple machines in order to satisfy scalability and/or availability requirements, you must provide different security functionality then in the rest of the system…

###### {"semantic":"metaphor"}

Google has around 30k engineers and use about 8 different programming languages. That means 1 programming languages for every 4k engineers.

###### {"semantic":"why", "source-link":"https://www.youtube.com/watch?v=tdd8w9d_d-c", "time-period":"00:01:16 - 00:10:31", "source-author":"Allard Buijze"}

Microservices intro through history

###### {"semantic":"why", "source-link":"https://www.youtube.com/watch?v=7uvK4WInq6k", "time-period":"00:02:42 - 00:03:40", "source-author":"Bernd Rucker", "tags":Surway"}

Microservices - assumption that something is always broken

###### {"semantic":"why", "source-link":"https://www.youtube.com/watch?v=tdd8w9d_d-c", "time-period":"00:08:53 - 00:10:30", "source-author":"Allard Buijze"}

Proper path toward microservices

###### {"semantic":"why", "source-link":"https://www.youtube.com/watch?v=tdd8w9d_d-c", "time-period":"00:10:37 - 00:11:14", "source-author":"Allard Buijze"}

Service location transparency

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:40:20 - 00:46:50", "source-author":"Clemens Vasters", "mastery":"medium", "tags":"Example, Azure"}

Service location transparency

###### {"semantic":"cons", "source-link":"https://microservices.io/patterns/microservices.html", "source-author":"Chris Richardson"}

Drawbacks:

*   Developers must deal with the additional complexity of creating a distributed system:
    *   Developers must implement the inter-service communication mechanism and deal with partial failure
    *   Implementing requests that span multiple services is more difficult
    *   Testing the interactions between services is more difficult
    *   Implementing requests that span multiple services requires careful coordination between the teams
    *   Developer tools/IDEs are oriented on building monolithic applications and don’t provide explicit support for developing distributed applications.
*   Deployment complexity. In production, there is also the operational complexity of deploying and managing a system comprised of many different services.
*   Increased memory consumption. The microservice architecture replaces N monolithic application instances with NxM services instances. If each service runs in its own JVM (or equivalent), which is usually necessary to isolate the instances, then there is the overhead of M times as many JVM runtimes. Moreover, if each service runs on its own VM (e.g. EC2 instance), as is the case at Netflix, the overhead is even higher.

###### {"semantic":"example", "source-link":"https://www.youtube.com/watch?v=r3f9nUw5I0g", "time-period":"00:18:01 - 00:18:58", "source-author":"Bernd Rucker", "tags":"Surway"}

Microservices - most frequent problems

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:03:00 - 00:07:40", "source-author":"Clemens Vasters", "mastery":"medium"}

What is service & system - organizational perspektive

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:42:20 - 00:45:16", "source-author":"Clemens Vasters", "mastery":"medium"}

Service - communication context

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:16:00 - 00:20:05", "source-author":"Clemens Vasters", "mastery":"medium"}

Service organizational perspective and how it is similar to old SOA concept

###### {"semantic":"warning", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:07:46 - 00:12:12", "source-author":"Clemens Vasters", "mastery":"advanced"}

API Gateway as single point of failure that resembles old ESB in SOA

###### {"semantic":"example", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:12:18 - 00:15:35", "source-author":"Clemens Vasters", "tags":"Azure"}

Azure microservices

###### {"semantic":"example", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:23:06 - 00:25:19", "source-author":"Clemens Vasters"}

Service - scalability perspective

###### {"semantic":"example", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:33:48 - 00:38:34", "source-author":"Clemens Vasters", "mastery":"advanced"}

Layers, Tiers and Services


#### Distributed transactions 

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=AUrKofVRHV4", "time-period":"00:00:57 - 00:12:45", "source-author":"Jimmy Bogard"}

Distributed transactions intro

###### {"semantic":"cons", "source-link":"https://www.youtube.com/watch?v=5ZjhNTM8XU8","time-period":"00:27:39 - 00:40:00", "source-author":"Martin Kleppmann", "mastery":"advanced"}

Distributed transactions limitation

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=AUrKofVRHV4", "time-period":"00:12:45 - 01:01:05", "source-author":"Jimmy Bogard", "tags":"RabbitMQ,MongoDB, CosmosDB", "mastery":"advanced"}

An Apostate's Implementation

###### {"semantic":"comparison", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"advanced"}

**Distributed transactions vs derived data**

The classic approach for keeping different data systems consistent with each other involves distributed transactions. At an abstract level, distributed transactions and derived data achieve a similar goal by different means. Distributed transactions decide on an ordering of writes by using locks for mutual exclusion while derived data and event sourcing use a log for ordering. Distributed transactions use atomic commit to ensure that changes take effect exactly once, while log-based systems are often based on deterministic retry and idempotence.

Difference is that distributed transactions provide ‘reading your own writes’ while derived data systems are often updated asynchronously, and so they do not by default offer the same timing guarantees.

Distributed transactions proved in practice as hard to achieve because lot of different reasons ( lot of parties have to agree about standards, not so good fault tolerance and performance) so it seems that log-based derived data is the most promising approach for integrating different data systems. Still eventual consistency remains like an obstacle that is huge for a lot of business cases.

If your application can tolerate occasionally corrupting or losing data in unpredictable ways, life is a lot simpler, and you might be able to get away with simply crossing your fingers and hoping for the best. On the other hand, if you need stronger assurances of correctness, then db transaction is an established approach, but it comes at a cost: it typically only work in a single datacenter (ruling out geographically distributed architectures), and they limit the scale and fault-tolerance properties you can achieve.


#### Business Flow Coordination 

Business flow can be implemented by using:



*   Orchestration
*   Choreography

With **_orchestration_**, there is one central service which coordinates other services. The downside to this approach is that the coordinating service can become too much of a central governing authority. It can become a hub in the middle of a web, and a central point where logic starts to live. This approach can lead to a situation where all of the complexity lies in a single service, and all other services are overly simplified. Changes to such a system can be difficult, since most of the logic is in a single place. Monitoring the system, tracking business process, is usually much simpler than with choreography. 

With **_choreography_**, each service knows its job and based on event’s messages it receives, it determines its behavior. In this approach, a service emits an event, and if a service needs to react on it, it subscribes for receiving that event. This approach leads to a decoupled architecture, where each service contains business logic specific for itself. It is easier to change services in such system. The downside of this approach is that it’s business logic is implicit, and it might be difficult to follow the flow. Monitoring the business flow is difficult, since the logic is distributed among other services. Also, instead of monitoring one service, it is needed to monitor many of them, and be able to show the aggregated results, but also have the ability to drill down into each one of them.

###### {"semantic":"tool", "source-link":"https://www.youtube.com/watch?v=7uvK4WInq6k", "time-period":"00:18:29 - 00:20:00", "source-author":"Bernd Rucker"}

Workflow engines on the market

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=7uvK4WInq6k", "time-period":"00:19:46 - 00:24:28", "source-author":"Bernd Rucker"}

Workflow engines - Comunda example

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=7uvK4WInq6k", "time-period":"00:24:08 - 00:26:37","time-period":"00:27:50 - 00:28:40", "source-author":"Bernd Rucker", "tags":"example"}

Idempotency


#### Service coupling  

###### {"semantic":"explanation", "source-link":"https://www.infoq.com/presentations/netflix-play-api/", "time-period":"00:11:10 - 00:15:15", "source-author":"Suudhan Rangarajan"}

Service coupling at Netflix

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=tdd8w9d_d-c", "time-period":"00:30:38 - 00:35:32", "source-author":"Allard Buijze", "tags":"bounded context"}

Event sourcing intro

###### {"semantic":"example", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:20:07 - 00:22:02", "source-author":"Clemens Vasters", "tags":"Azure"}

Why shared data stores are bad

###### {"semantic":"example", "source-link":"https://www.youtube.com/watch?v=rXi5CLjIQ9k", "time-period":"00:27:21 - 00:29:08", "source-author":"Clemens Vasters"}

Emissaries and SDKs


#### Misbehaviour handling 

###### {"semantic":"example", "source-link":"https://www.youtube.com/watch?v=7uvK4WInq6k", "time-period":"00:03:55 - 00:10:25", "source-author":"Bernd Rucker", "mastery":"medium"}

Circuit breaker example

###### {"semantic":"use-case", "source-link":"https://www.youtube.com/watch?v=7uvK4WInq6k", "time-period":"00:10:43 - 00:17:30", "source-author":"Bernd Rucker", "mastery":"medium"}

Misbehaviour handling - real use case

###### {"semantic":"use-case", "source-link":"https://www.youtube.com/watch?v=7uvK4WInq6k", "time-period":"00:29:05 - 00:29:55", "source-author":"Bernd Rucker", "tags":"example"}

Scenario of failures between services


#### Service Scope 

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=tdd8w9d_d-c", "time-period":"00:32:23 - 00:33:45", "source-author":"Allard Buijze", "tags":"bounded context"}

Bounded Context Example


### Modular monolith

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=5OjqD-ow8GE", "time-period":"00:02:46 - 00:08:12", "source-author":"Simon Brown", "mastery":"advanced"}

Model-Code gap

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=5OjqD-ow8GE", "time-period":"00:12:24 - 00:12:25", "source-author":"Simon Brown", "mastery":"advanced"}

Source code structuring style: Package by layer

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=5OjqD-ow8GE", "time-period":"00:12:28 - 00:21:43", "source-author":"Simon Brown", "mastery":"advanced"}

Source code structuring style: Package by feature

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=5OjqD-ow8GE", "time-period":"00:22:14 - 00:24:10", "source-author":"Simon Brown", "mastery":"advanced"}

Well structured code tooling approach: Fitness function

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=5OjqD-ow8GE", "time-period":"00:25:26 - 00:29:13", "source-author":"Simon Brown", "mastery":"advanced"}

Source code structuring style: Package by component

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=5OjqD-ow8GE", "time-period":"00:29:35 - 00:35:50", "source-author":"Simon Brown", "mastery":"advanced"}

Code organization vs encapsulation, structuring tyles comparison

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=5OjqD-ow8GE", "time-period":"00:40:30 - 00:42:40", "source-author":"Simon Brown", "mastery":"advanced"}

Monolith - Modular Monolith - Microservices spectrum

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=5OjqD-ow8GE", "time-period":"00:44:48 - 00:45:21", "source-author":"Simon Brown", "mastery":"advanced"}

In-process and out-of-process characteristics

## IoT 

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=Esg1BYJdZe0", "time-period":"00:01:35 - 00:03:02", "source-author":"Clemens Vasters"}

First IoT system

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=Esg1BYJdZe0", "time-period":"00:04:30 - 00:10:54", "source-author":"Clemens Vasters", "mastery":"advanced"}

Device-Cloud connectivity challenges including scalability
