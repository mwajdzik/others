package datastructures

import (
	"fmt"
	"math"
	"testing"
)

func TestHeapCreation(t *testing.T) {
	heap := NewHeap()

	if heap.array == nil {
		t.Fatalf("array should not be nil")
	}
}

func TestInsert(t *testing.T) {
	heap := NewHeap()

	for _, item := range []int{15, 68, 62, 75, 17, 87, 54, 39, 25, 8, 15, 88} {
		heap.Insert(item)
	}

	if len(heap.array) != 12 {
		t.Fatalf("array should have 12 elements")
	}

	if heap.array[0] != 8 {
		t.Fatalf("the min element should be on the first possition")
	}
}

func assertEquals(t *testing.T, actual, expected int) {
	if actual != expected {
		t.Fatalf("values are not equal (expected: %v, actual: %v)", expected, actual)
	}
}

type testCase struct {
	value    []int
	expected []int
}

func TestMany(t *testing.T) {
	testCases := []testCase{
		{
			[]int{15, 68, 62, 75, 17, 87, 54, 39, 25, 8, 15, 88},
			[]int{8, 15, 15, 17, 25, 39, 54, 62, 68, 75, 87, 88, math.MinInt},
		},
		{
			[]int{23, 23, 23, 23, 23},
			[]int{23, 23, 23, 23, 23, math.MinInt},
		},
		{
			[]int{23},
			[]int{23, math.MinInt},
		},
		{
			[]int{3, 2, 1},
			[]int{1, 2, 3, math.MinInt},
		},
		{
			[]int{1, 2, 3},
			[]int{1, 2, 3, math.MinInt},
		},
		{
			[]int{},
			[]int{math.MinInt},
		},
	}

	for _, tc := range testCases {
		t.Run(fmt.Sprintf("check %v", tc.value), func(t *testing.T) {
			heap := NewHeap()

			for _, item := range tc.value {
				heap.Insert(item)
			}

			for _, expected := range tc.expected {
				assertEquals(t, heap.Remove(), expected)
			}
		})
	}
}
