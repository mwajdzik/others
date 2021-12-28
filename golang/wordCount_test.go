package main

import (
	"fmt"
	"testing"
)

func TestSimple(t *testing.T) {
	result := countWords(`
		Needles and pins
		Needles and pins
		Sew me a sail
		To catch me the wind
	`)

	if result["pins"] != 2 {
		t.Fatalf("bad count of 'pin'")
	}
}

type testCase struct {
	value    string
	expected int
}

func TestMany(t *testing.T) {
	testCases := []testCase{
		{"Needles", 2},
		{"and", 2},
		{"pins", 2},
		{"Sew", 1},
		{"me", 1},
		{"a", 1},
		{"sail", 1},
	}

	for _, tc := range testCases {
		t.Run(fmt.Sprintf("%f", tc.value), func(t *testing.T) {
			result := countWords(`
				Needles and pins
				Needles and pins
				Sew me a sail
				To catch me the wind
			`)

			if int(result[tc.value]) != tc.expected {
				t.Fatalf("bad count of '%s'", tc.value)
			}
		})
	}
}
