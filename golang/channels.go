package main

import (
	"fmt"
	"time"
)

// ---

func main() {
	ch := make(chan int)

	go func() {
		for i := 0; i < 3; i++ {
			fmt.Printf("Sending: %v\n", i)
			ch <- i
			time.Sleep(time.Second)
		}

		close(ch)
	}()

	// receive from the channel
	val := <-ch
	fmt.Printf("Got: %v\n", val)

	// receive from the channel until it is closed
	for i := range ch {
		fmt.Printf("Got: %v\n", i)
	}
}
