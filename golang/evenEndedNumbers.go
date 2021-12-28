package main

import "fmt"

func evenEndedNumbers() {
	counter := 0

	for i := 1000; i <= 9999; i++ {
		for j := i + 1; j <= 9999; j++ {
			kStr := fmt.Sprintf("%v", i*j)
			if kStr[0] == kStr[len(kStr)-1] {
				counter++
			}
		}
	}

	fmt.Println("Number of even ended numbers: ", counter)
}

func main() {
	evenEndedNumbers()
}
