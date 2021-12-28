package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"sync"
)

var (
	db     = map[string]interface{}{}
	dbLock sync.Mutex
)

type Entry struct {
	Key   string      `json:"key"`
	Value interface{} `json:"value"`
}

func sendResponse(entry *Entry, responseWriter http.ResponseWriter) {
	responseWriter.Header().Set("Content-Type", "application/json")
	enc := json.NewEncoder(responseWriter)

	if err := enc.Encode(entry); err != nil {
		log.Printf("error encoding %+v - %s", entry, err)
	}
}

func kvPostHandler(responseWriter http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	dec := json.NewDecoder(r.Body)
	entry := &Entry{}

	if err := dec.Decode(entry); err != nil {
		http.Error(responseWriter, err.Error(), http.StatusBadRequest)
		return
	}

	dbLock.Lock()
	defer dbLock.Unlock()
	db[entry.Key] = entry.Value

	sendResponse(entry, responseWriter)
}

func kvGetHandler(responseWriter http.ResponseWriter, request *http.Request) {
	key := request.URL.Path[4:] // Trim leading /db/ prefix

	dbLock.Lock()
	defer dbLock.Unlock()

	value, ok := db[key]

	if !ok {
		http.Error(responseWriter, fmt.Sprintf("Key %q not found", key), http.StatusNotFound)
		return
	}

	entry := &Entry{Key: key, Value: value}
	sendResponse(entry, responseWriter)
}

func main() {
	http.HandleFunc("/db", kvPostHandler)
	http.HandleFunc("/db/", kvGetHandler)

	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal(err)
	}
}
