package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"
)

type Payment struct {
	Time   time.Time
	User   string
	To     string
	Amount float64
}

func (p *Payment) Validate() error {
	if p.Amount <= 0 {
		return fmt.Errorf("bad Amount in %#v", p)
	}

	return nil
}

func paymentHandler(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	var payment Payment
	dec := json.NewDecoder(r.Body)

	if err := dec.Decode(&payment); err != nil {
		http.Error(w, "bad JSON", http.StatusBadRequest)
		return
	}

	if err := payment.Validate(); err != nil {
		log.Printf("error: paymenetHandler - %s", err)
		http.Error(w, "bad data", http.StatusBadRequest)
		return
	}

	fmt.Fprintf(w, "OK\n")
}

// check with:
// 	curl -d@invalidPayment.json localhost:8080/payment

func main() {
	http.HandleFunc("/payment", paymentHandler)

	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal(err)
	}
}
