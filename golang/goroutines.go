package main

import (
	"fmt"
	"net/http"
	"sync"
)

var urls = []string{
	"https://golang.org",
	"https://api.github.com",
	"https://httpbin.org/xml",
}

// ---

func getContentTypeWithWaitGroups(url string) {
	fmt.Printf("Staring for %s\n", url)
	resp, err := http.Get(url)

	if err != nil {
		return
	}

	defer resp.Body.Close()

	contentType := resp.Header.Get("content-type")
	fmt.Printf("%s -> %s\n", url, contentType)
}

func goRoutinesWithWaitGroups() {
	waitGroup := sync.WaitGroup{}

	for _, url := range urls {
		waitGroup.Add(1)
		go func(u string) {
			getContentTypeWithWaitGroups(u)
			waitGroup.Done()
		}(url)
	}

	waitGroup.Wait()
}

// ---

func getContentTypeWithChannels(url string, channel chan string) {
	fmt.Printf("Staring for %s\n", url)
	resp, err := http.Get(url)

	if err != nil {
		channel <- fmt.Sprintf("%s -> %s\n", url, err)
		return
	}

	defer resp.Body.Close()

	contentType := resp.Header.Get("content-type")
	channel <- fmt.Sprintf("%s -> %s\n", url, contentType)
}

func goRoutinesWithChannels() {
	channel := make(chan string)

	for _, url := range urls {
		go getContentTypeWithChannels(url, channel)
	}

	// run len(urls) times
	for range urls {
		out := <-channel
		fmt.Print(out)
	}
}

func main() {
	goRoutinesWithWaitGroups()
	fmt.Println()
	goRoutinesWithChannels()
}
