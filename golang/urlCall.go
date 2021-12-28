package main

import (
	"fmt"
	"net/http"
)

func contentType(url string) (string, error) {
	resp, err := http.Get(url)

	if err != nil {
		return "", err
	}

	defer resp.Body.Close()

	contentType := resp.Header.Get("content-type")

	if contentType == "" {
		return "", fmt.Errorf("Can't find the Content-Type Header")
	}

	return contentType, nil
}

func main() {
	contentType, err := contentType("https://golang.org")

	if err != nil {
		fmt.Printf("ERROR: %s\n", err)
	} else {
		fmt.Println(contentType)
	}
}
