package main

import (
	"fmt"
	"strings"
)

func countWords(text string) map[string]uint {
	counts := map[string]uint{}
	words := strings.Fields(text)

	for _, word := range words {
		transformedWord := strings.ToLower(strings.Trim(word, " "))
		counts[transformedWord] += 1
	}

	return counts
}

func main() {
	words := countWords(`
		Needles and pins
		Needles and pins
		Sew me a sail
		To catch me the wind
	`)

	fmt.Printf("Word counts: %v\n", words)
}
