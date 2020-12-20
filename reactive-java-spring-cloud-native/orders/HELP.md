# Getting Started

RSocket is added to replace HTTP which does not support reactive approach (eg. back pressure) 

brew install making/tap/rsc

rsc tcp://localhost:8181 --stream -r orders.3
