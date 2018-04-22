
## Ethereum

- a platform for creating decentrailized applications 
- enables creation of DAC - Decentrailized Autonomous Corporations

- Etherneum networks are used to transfer money and store data
- there are many different Etherneum networks (testing, private, ...)
- networks are formed by one or many nodes
- each node is a machine running as Etherneum client
- anyone can run a node
- each node can contain a full copy of the blockchain
- the blockchain is a database that stores a record of every transaction that has ever taken place


## Links

https://github.com/StephenGrider/EthereumCasts
http://rinkeby-faucet.com/	- allows to send some coins using testing network Rinkeby


## Technologies

- web3.js - for developers to create applications
- for customers
	- Metamask - browser externsion
	- Mist Browser


## Metamask

- install Chrome extension
- provide password, an account will be created 
- the account provides:
	- acount address
	- public key
	- private key
- one account is used to get to different networks (like Main, Ropsten, Kovan, Rinkeby)


## Transaction - an object which represents an attempt to send some money from one account to another

Properties of transaction:
- nonce - how many times the sender has sent a transaction
- to - address of account this money is going to
- value - amount of ether to send to the target address
- gasPrice 
- startGas/gasLimit
- v, r, s - geenrated from the sender's private key, cryptographic pieces of data that can be used to generate the senders account address


## Blockchain

- Block = Number, Nonce, Data
- we look for a Nounce number that combined with data will get a SHA256 hash to start with four zeros - the proces is called "mining" - the block is "signed"
- blockchain - a list of signed blocks that point to the the previous ones
- demo https://anders.com/blockchain/blockchain.html
- distributed blockchain - the same blockchain kept on different peers can be checked if consistant just by checking the hash of the last block
- coinbase - new money added to the system

- allows to have an agreement on what have happened in the past


## Ether 

- we look for a hash that's smaller then some number (not starting with 0000)
- the block time a time to find a right Nonce that meets condition
- the target Block Time of Ethereum network is 15 seconds
- the network can decide of changing the difficulty target to speed up/down the process
- demo: https://etherscan.io/chart/blocktime
- it tells how much time it can take for the network to finalize a transaction


## Smart Contract

- an account controlled by code (vs. external accounts created by people)
- specific to one network only (eg. only Rinkeby)
- balance, storage, code


## Solidity

- programming language for writing Smart Contracts
- .sol files
- strongly typed
- solidity compiler is used to prepare
	- byte code ready for deployment
	- Application Binary Interface (ABI) - interface for JS to interact with bytecode of the Smart Contract

- https://remix.ethereum.org





















