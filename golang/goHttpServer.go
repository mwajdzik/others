package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

func helloHandler(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hello Gophers!")
}

type MathRequest struct {
	Op    string  `json:"op"`
	Left  float64 `json:"left"`
	Right float64 `json:"right"`
}

type MathResponse struct {
	Error  string  `json:"error"`
	Result float64 `json:"result"`
}

// curl --header "Content-Type: application/json" --request POST --data '{"op": "+", "left": 2, "right": 3}' http://localhost:8080/math
func mathHandler(responseWriter http.ResponseWriter, request *http.Request) {
	defer request.Body.Close()

	dec := json.NewDecoder(request.Body)
	req := &MathRequest{}

	if err := dec.Decode(req); err != nil {
		http.Error(responseWriter, err.Error(), http.StatusBadRequest)
		return
	}

	resp := &MathResponse{}

	switch req.Op {
	case "+":
		resp.Result = req.Left + req.Right
	case "-":
		resp.Result = req.Left - req.Right
	case "*":
		resp.Result = req.Left * req.Right
	case "/":
		if req.Right == 0.0 {
			resp.Error = "division by 0"
		} else {
			resp.Result = req.Left / req.Right
		}
	default:
		resp.Error = fmt.Sprintf("unknown operation: %s", req.Op)
	}

	responseWriter.Header().Set("Content-Type", "application/json")

	if resp.Error != "" {
		responseWriter.WriteHeader(http.StatusBadRequest)
	}

	enc := json.NewEncoder(responseWriter)

	if err := enc.Encode(resp); err != nil {
		log.Printf("can't encode %v - %s", resp, err)
	}
}

// check: https://golang.cafe/blog/golang-httptest-example.html
func main() {
	http.HandleFunc("/hello", helloHandler)
	http.HandleFunc("/math", mathHandler)

	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal(err)
	}
}
