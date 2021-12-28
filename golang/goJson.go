package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"log"
	"os"
)

var data = `
{
  "user": "Scrooge McDuck",
  "type": "deposit",
  "amount": 1000000.3
}
`

type Request struct {
	Login  string  `json:"user"`
	Type   string  `json:"type"`
	Amount float64 `json:"amount"`
}

func main() {
	rdr := bytes.NewBufferString(data)
	dec := json.NewDecoder(rdr)
	req := &Request{}

	if err := dec.Decode(req); err != nil {
		log.Fatalf("error: can't decode - %s", err)
	}

	fmt.Printf("got: %+v\n", req)

	// ---

	// use map instead of Request struct
	// interface{} == any
	resp := map[string]interface{}{
		"ok":      true,
		"balance": 8500000.0 + req.Amount,
	}

	writer := os.Stdout
	enc := json.NewEncoder(writer)

	if err := enc.Encode(resp); err != nil {
		log.Fatalf("error: can't encode - %s", err)
	}
}
