# Data Storage

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"medium"}

Data models are perhaps the most important part of developing software, because they have such a profound effect: not only on how the software is written, but also on how we think about the problem that we are solving. Most applications are built by layering one data model on top of another. For each layer, the key question is: how is it represented in terms of the next-lower layer? For example:



1. As an application developer, you look at the real world (in which there are people, organizations, goods, actions, money flows, sensors, etc.) and model it in terms of objects or data structures, and APIs that manipulate those data structures. Those structures are often specific to your application.
2. When you want to store those data structures, you express them in terms of a general-purpose data model, such as JSON or XML documents, tables in a relational database, or a graph model.
3. The engineers who built your database software decided on a way of representing that JSON/XML/relational/graph data in terms of bytes in memory, on disk, or on a network. The representation may allow the data to be queried, searched, manipulated, and processed in various ways.
4. On yet lower levels, hardware engineers have figured out how to represent bytes in terms of electrical currents, pulses of light, magnetic fields, and more.

In a complex application there may be more intermediary levels, such as APIs built upon APIs, but the basic idea is still the same: each layer hides the complexity of the layers below it by providing a clean data model. These abstractions allow different groups of people—for example, the engineers at the database vendor and the application developers using their database—to work together effectively.

It can take a lot of effort to master just one data model (think how many books there are on relational data modeling). Building software is hard enough, even when working with just one data model and without worrying about its inner workings. But since the data model has such a profound effect on what the software above it can and can’t do, it’s important to choose one that is appropriate to the application.

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"advanced"}

Relational model is most used model. Relation is unordered collection of tuples (rows in a table). Over the years, there have been many competing approaches to data storage and querying. In the 1970s and early 1980s, the network model and the hierarchical model were the main alternatives, but the relational model came to dominate them. Object databases came and went again in the late 1980s and early 1990s. XML databases appeared in the early 2000s, but have only seen niche adoption. Each competitor to the relational model generated a lot of hype in its time, but it never lasted.

In the 2010s, NoSQL is the latest attempt to overthrow the relational model’s dominance. The name “NoSQL” is unfortunate, since it doesn’t actually refer to any particular technology—it was originally intended simply as a catchy Twitter hashtag for a meetup on open source, distributed, nonrelational databases. It has been retroactively reinterpreted as Not Only SQL.

There are several driving forces behind the adoption of NoSQL databases, including:



*   A need for greater scalability than relational databases can easily achieve, including very large datasets or very high write throughput.
*   A widespread preference for free and open source software over commercial database products
*   desire for a more dynamic and expressive data model

###### {"semantic":"tip", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"advanced"}

It is not rare that relational database where data are modeled in denormalized form has similar performance as noSQL database. Denormalization has different forms but they are not of significance - it is important that denormalization is any kind of data duplication in storage. Denormalization requires joins in relational database which is pretty efficient in it. NoSQL databases are not so efficient since data are usually already denormalized.  

###### {"semantic":"warning", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"advanced"}

Document databases traditionally have difficulty in modeling many-to-many relationships and because of that, beside relational, network model is also used.

The links between records in the network model were not foreign keys, but more like pointers in a programming language (while still being stored on disk). The only way of accessing a record was to follow a path from a root record along these chains of links. This was called an access path.

###### {"semantic":"comparison", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"advanced"}

The main arguments in favor of the document data model are schema flexibility, better performance due to locality, and that for some applications it is closer to the data structures used by the application. The relational model counters by providing better support for joins, and many-to-one and many-to-many relationships.If the data in your application has a document-like structure (i.e., a tree of one-tomany relationships, where typically the entire tree is loaded at once), then it’s probably a good idea to use a document model. 

For highly interconnected data, the document model is awkward, the relational model is acceptable, and graph models (see “Graph-Like Data Models” on page 49) are the most natural.

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"medium"}

No schema means that arbitrary keys and values can be added to a document, and when reading, clients have no guarantees as to what fields the documents may contain. Document databases are sometimes called schemaless, but that’s misleading, as the code that reads the data usually assumes some kind of structure—i.e., there is an implicit schema, but it is not enforced by the database [20]. A more accurate term is **schema-on-read** (the structure of the data is implicit, and only interpreted when the data is read), in contrast with **schema-on-write** (the traditional approach of relational databases, where the schema is explicit and the database ensures all written data conforms to it)

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"medium"}

Schema-on-read is similar to dynamic (runtime) type checking in programming languages, whereas schema-on-write is similar to static (compile-time) type checking.

The difference between the approaches is particularly noticeable in situations where an application wants to change the format of its data. For example, say you are currently storing each user’s full name in one field, and you instead want to store the first name and last name separately. In a document database, you would just start writing new documents with the new fields and have code in the application that handles the case when old documents are read. 

For example:


```
if (user && user.name && !user.first_name) {
// Documents written before Dec 8, 2013 don't have first_name
user.first_name = user.name.split(" ")[0];
}
```


On the other hand, in a “statically typed” database schema, you would typically perform

a migration along the lines of:


```
ALTER TABLE users ADD COLUMN first_name text;
UPDATE users SET first_name = split_part(name, ' ', 1);
```


###### {"semantic":"warning", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"medium"}

Schema changes have a bad reputation of being slow and requiring downtime. This reputation is not entirely deserved: most relational database systems execute the ALTER TABLE statement in a few milliseconds.

###### {"semantic":"tip", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"medium"}

The schema-on-read approach is advantageous if the items in the collection don’t all

have the same structure for some reason (i.e., the data is heterogeneous)—for example,

because:



*   There are many different types of objects, and it is not practical to put each type of object in its own table.
*   The structure of the data is determined by external systems over which you have no control and which may change at any time.

But in cases where all records are expected to have the same structure, schemas are a useful mechanism for documenting and enforcing that structure.

###### {"semantic":"tip", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann", "mastery":"advanced"}

Real power of relational databases is query optimizer. Creator is not expected to do all optimization on her own. SQL is a declarative language with is not so powerful as imperative languages but that is exactly the reason why query optimizer can do its magic on it. Imperative code is hard to parallelize. 

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

All three storage models (document, relational, and graph) are widely used today, and each is good in its respective domain. One model can be emulated in terms of another model - for example, graph data can be represented in a relational database—but the result is often awkward. That’s why we have different systems for different purposes, not a single one-size-fits-all solution.

###### {"semantic":"comparison"}

RDBMS when we need transaction isolation, data has clear schema and it is not big data.

Else NoSQL.

###### {"semantic":"tool", "source-link":"https://db-engines.com/en/ranking_trend"}

Data storage popularity

![alt_text](/images/Data-Storage0.png "image_tooltip")

![alt_text](/images/Data-Storage1.png "image_tooltip")



## RDBMS

    *   Denormalization

###### {"semantic":"explanation", "source-link":"https://www.youtube.com/watch?v=5ZjhNTM8XU8", "time-period":"00:09:46 - 00:27:43", "source-author": "Martin Kleppmann", "mastery":"advanced"}

Transaction isolation levels explained


## NoSQL

###### {"semantic":"use-case"}

NoSql use cases:



*   Big data, billion of rows TB in size 
*   Not fully structured data, each record/row can has different shape and can be changed adding columns when needed
*   Not required transactions, sacrifice ACID compliance for performance and scalability.
*   High ready/write performance where horizontal scalability is easy


### Graph

Graph databases - store information about various types of networks. It uses edges and nodes to represent and store data



*   Popular implementations: Neo4J, Microsoft Cosmos DB
*   Use cases: social network, fraud detection


### Key-Value

###### {"semantic":"intro-definition"}

Key-value stores - a pair of key(attribute) and its value; 



*   Popular implementations: Redis, Riak, Amazon DynamoDB, Memcached, Microsoft Cosmos DB
*   Use cases: store user preferences or session data, billion of rows TB in size 
*   Document
*   Wide-column


### Wide-Column stores

###### {"semantic":"intro-definition"}

Wide-Column stores - Instead of ‘tables,’ in columnar databases we have column families, which are containers for rows. Unlike relational databases, we don’t need to know all the columns up front and each row doesn’t have to have the same number of columns.



*   Popular implementations: Cassandra, Microsoft Cosmos DB, HBase
*   Use cases: heavy write requests like blogging platforms, billion of rows TB in size 
*   Warning: Don’t mix wide-column stores with [column oriented databases](https://en.wikipedia.org/wiki/Column-oriented_DBMS)

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Two types of applications - transaction processing and analytics. Usually an analytic query needs to scan over a huge number of records, only reading a few columns per record, and calculates aggregate statistics (such as count, sum, or average) rather than returning the raw data to the user. These queries are often written by business analysts, and feed into reports that help the management of a company make better decisions (business intelligence). In order
to differentiate this pattern of using databases from transaction processing, it has
been called online analytical processing (OLAP).


![alt_text](/images/Data-Storage2.png "image_tooltip")


On the surface, a data warehouse and a relational OLTP database look similar, because they both have a SQL query interface. However, the internals of the systems can look quite different, because they are optimized for very different query patterns.

###### {"semantic":example, "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Analytic queries might be:



*   What was the total revenue of each of our stores in January?
*   How many more bananas than usual did we sell during our latest promotion?
*   Which brand of baby food is most often purchased together with brand X diapers?

###### {"semantic":"explanation", "source-link":"https://learning.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/", "source-author":"Martin Kleppmann"}

Although fact tables are often over 100 columns wide, a typical data warehouse query only accesses 4 or 5 of them at one time ("SELECT *" queries are rarely needed for analytics) 

In most OLTP databases, storage is laid out in a row-oriented fashion: all the values from one row of a table are stored next to each other. Document databases are similar: an entire document is typically stored as one contiguous sequence of bytes.

Row-oriented storage engine still needs to load all of those rows (each consisting of over 100 attributes) from disk into memory, parse them, and filter out those that don’t meet the required conditions. That can take a long time. The idea behind column-oriented storage is simple: don’t store all the values from one row together, but store all the values from each column together instead. If each column is stored in a separate file, a query only needs to read and parse those columns that are used in that query, which can save a lot of work. 


### Document-based databases

Document-based databases - pair each key with a complex data structure known as a document. Documents can contain many different key-value pairs, or key-array pairs, or even nested documents; different records may have different columns; Document stores often use internal notations, which can be processed directly in applications, mostly JSON - this require client-side processing of the structures, which has the disadvantage that the features offered by document stores (such as secondary indexes) 



*   Popular implementations: MongoDB, Amazon DynamoDB, Microsoft Azure Cosmos DB
*   Use cases: e-commerce and blogging platforms


### Search engines



*   Popular implementations: Elasticsearch
*   Use cases: complex search expressions, full text search, stemming


## 


## Caching

###### {"semantic":why}

Caching can be performed for two reasons:



*   Performance
*   Resilience

**_Caching for performance _**is the common technique for performance optimization. We store the result of an operation, in order to improve the response time for subsequent requests for the same operation.

**_Caching for resilience_** is used to provide a safety net in case of a downstream service failure. The client can decide to serve data from its cache, in such situation. This data is potentially stale. We could also use a reverse proxy, to serve stale data. For some cases is better to be available, and with stale data, than to be unavailable and not return any result at all.

###### {"semantic":"explanation"}

Caching can be implemented on:



*   Client side
*   Server side
*   Proxy

In **_client side caching, _**the client stores the cached result. The client gets to decide when it should retrieve a fresh copy. The downstream service should provide hints to help the client understand what to do with the response, so it knows when and if to make a new request. Client-side caching can help reduce network calls drastically, and can be a fast way of reducing load on a downstream service. The client is in charge of the caching behavior, and if you want to make changes to how caching is done, rolling out changes to a number of consumers could be difficult. Invalidation of stale data can also be tricky. HTTP’s `cache-control` can be used very efficiently to control the state of cache on clients.

With **_server-side caching_**, the server handles caching responsibility, making use of a system like _Redis_ or _Memcached. _For simple cases, an in-memory cache can be used as well. With a cache near or inside a service boundary, it can be easier to reason about things like invalidation of data, or track and optimize cache hits. In a situation where there are multiple types of clients, a server-side cache could be the fastest way to improve performance.

With **_proxy caching_**, a proxy is placed between the client and the server. A great example of this is using a reverse proxy or content delivery network (CDN). If the proxy is designed to cache generic traffic, it can also cache more than one service; a common example is a reverse proxy like _Squid_ or _Varnish_, which can cache any HTTP traffic. Having a proxy between the client and server does introduce additional network hops, but, most of the time, the performance optimizations resulting from the caching itself outweigh any additional network costs.

**_Caching writes_** is useful when there are bursts of writes, or when there is a good chance that the same data will be written multiple times. With write-behind cache, data can be written to a local cache, and at some later point it will be flushed to a downstream source, probably the canonical source of data. When used to buffer and potentially batch writes, write-behind caches can be a useful further performance optimization. With a write-behind cache, if the buffered writes are suitably persistent, even if the downstream service is unavailable we could queue up the writes and send them through when it is available again.

###### {"semantic":"composed-of"}

By default try to use out-of-box caching in DB and browser. First cache to include is CDN if you have a significant amount of static content.

![alt_text](/images/Data-Storage3.png "image_tooltip")

## BLOB handling

###### {"semantic":"explanation"}

There are several different ways where BLOB can be stored:



*   [File server](https://en.wikipedia.org/wiki/File_server)
*   [NAS](https://en.wikipedia.org/wiki/Network-attached_storage)
*   [SAN](https://en.wikipedia.org/wiki/Storage_area_network#SAN-NAS_hybrid)
*   [DAS](https://en.wikipedia.org/wiki/Direct-attached_storage)


![alt_text](/images/Data-Storage4.png "image_tooltip")


BLOB storage types:



*   **Local machine file storage**, fast
*   **File servers (NAS, AWS EFS, Azure File Storage), **fast 5k-10k file system operations, individual files up to 52TB
*   **block storage (SAN, AWS EBS, Azure Storage)**, medium speed 2k-5k IOPS/s, individual files up to 16TB
*   **Object storage (AWS S3, Azure Blob Storage)**, slowest with 100s requests (PUT, LIST, DELETE) per second, cheapest (0.02$/GB), individual files up to 5TB


![alt_text](/images/Data-Storage5.png "image_tooltip")


 

###### {"semantic":"explanation", "source-link":"https://dzone.com/articles/which-is-better-saving-files-in-database-or-in-fil", "source-author":"Abuthahir Sulaiman"}

**What is the best strategy for file storage?**

**Storing Files in a File System**

**_Pros:_**



*   Cheaper
*   Performance (no additional processing of files to store in DB, files can be sent directly to the network interface with _sendfile()_)
*   Can be used in combination with CDN to improve application performance

**_Cons:_**



*   More difficult to setup backup
*   Implementation can become complex (e.g., to overcome limitation of number of files in a directory)
*   Low security 

**_Note: _**Scaling is easily supported either in the cloud (S3, Azure Storage), or on premises with HDFS.

**Storing Files in a Database**

**_Pros:_**



*   More secure (additional layer of security)
*   Easy backup and replication
*   Support for partial reading of a file (GridFS), without having to load the whole file
*   Support for automated replication on geographically distributed instances
*   Support for file versioning
*   Easier querying

**_Cons:_**



*   Storing larger files can be less effective (e.g., MongoDB has GridFS for files larger than 16MB which splits those files into several chunks)
*   Database backup grows large
*   Price is higher than plain storage (both on premise and cloud)
*   No atomic updates for large files

**_Note:_** there are tools/add ons for relational databases which provide support for large number of files to be stored directly in the file system, yet access them through the database (e.g., [FileStream](https://www.sqlshack.com/filestream-in-sql-server/)).


#### Hybrid approach

Put metadata in DB, and store files on file system. 

**_Pros:_**



*   Give additional semantics to stored files 

**_Cons:_**



*   Increased complexity
*   Integrity between data in the file system and metadata in the database can be compromised (e.g., moving the file to a different location)
