
# ElasticSearch

- an analytics and full-tex search engine
- often used to enable search functionality for apps
- anything kept in form of JSONs can be searched
- stores data in optimized for searching way (should not be treated as data storage of the app) - denormalized, no transactions, ...
- distributed and highly scalable
- written in Java on top of Apache Lucene


# Kibana

- used for analysis and visualization of data
- supports a number of diagrams and graphs


# LogStash

- a tool that allows to get data into ElasticSearch cluster
- a data processing pipeline which can ingest data from various data sources
- originally built for adding logs to ElasticSearch


# ELK - ElasticSearch + LogStash + Kibana
# Elastic Stack = ELK + more products forming a family


## Beats 
	- a platform that contains a number of data shippers (beats)
	- beats can be installed on servers and send data to LogStash, or add data directly to ElasticSearch cluster
	- Filebeat used for handling log files
	- Metricbeat for collecting CPU or memory
	- Packetbeat for inspecting network traffic
	- Hearbeat for collecting uptime data

## X-Pack
	- a pack of powerful features grouped together to manage ElasticSearch
	- adds security capabilities to secure ElasticSearch cluster
	- offers alerting, monitoring, reporting, etc.


# ElasticSearch Architecture

- API exposed with HTTP REST
- cluster is built of nodes (servers) that hold data
- the collection of nodes keeps all the data
- each node participates in the indexing and search capabilities
- each node can accept REST requests and coordinate the rest of the work (a coordinating node - checks eg. if all replicas are on this node and can perform seach on its own)
- each node knows everything about other nodes
- each node may be assigned as being the master node - a node responsible for coordinating changes to the cluster (adding nodes, creating indencies, ...)
- by default nodes join a cluster named "elasticsearch"

- documents are kept in from of JSONs, each has an id
- an index is a collection of documents that have somewhat similar characteristics

- sharding
	- allows to split and scale volumes of data
	- allows to parallelize operations between multiple nodes
	- a shard contanis a subset of an index data
	- default of 5 shards is used when an index is created
	- if an index was created we CANNOT change number of shards (move data to a new index)

- replication
	- allow to ensure high availability and to improve search query performance
	- shards are copied among replicas 
	- primary shard / replica shard
	- by default we get one replica per shard
	- all operations (adding, removing, updating) are sent to the primary shard, once done, they will be sent to replicas, once the primary shard gets confirmation from all replicas, it can report success to a client

- cluster = nodes (each node can hold many primary and replica shards)
	- be default we get 10 shards per index (5 primary shard + 5 replicas)
	- a replica group = a primary shard and all its replicas


- routing - determining where a document should be stored
	- by default: shard = hash(document) & number_of_shards


## Setup

docker pull elasticsearch
docker images

docker run -d -p 9200:9200 -p 9300:9300 -it -h elasticsearch --name elasticsearch elasticsearch

docker run -d -p 5601:5601 -h kibana --name kibana --link elasticsearch:elasticsearch kibana

http://localhost:9200/
http://localhost:5601/

docker logs -f 864....
docker stop 864....


## Elasticsearch options

- cluster.name
- node.name
- network.host
- network.port
- discovery.zen.ping.unicast.hosts


## Kibana options

- server.port
- server.host
- server.name
- elasticsearch.url
- kibana.index - Kibana owns its own index


## Loading data

```
curl -H "Content-Type: application/json" -XPOST "http://localhost:9200/product/default/_bulk?pretty" --data-binary "@products-bulk.json"
```


## Searching with Dev Tools

```
	REST verb /index/type/API

	GET /myindex/mytype/_search
```





## TODO

docker run -h logstash --name logstash --link elasticsearch:elasticsearch -it --rm -v "$PWD":/config-dir logstash -f /config-dir/logstash.conf

Enter to stdin:
test1
test2
test3


