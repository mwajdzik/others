package datastructures

import "math"

// nodes are added from left to right in order to generate a complete tree (all levels filled)
// priority queue - elements removed in the order of their priority
// min or max heap
// implemented in an array: children L=n*2+1, R=n*2+2, parent: (n-1)/2

type heap struct {
	array  []int
	length int
}

func parent(i int) int {
	return (i - 1) / 2
}

func left(i int) int {
	return i*2 + 1
}

func right(i int) int {
	return i*2 + 2
}

func (h *heap) swap(i1, i2 int) {
	h.array[i1], h.array[i2] = h.array[i2], h.array[i1]
}

func (h *heap) heapifyUp(i int) {
	for h.array[parent(i)] > h.array[i] {
		h.swap(i, parent(i))
		i = parent(i)
	}
}

func (h *heap) isValidIndex(i int) bool {
	return i < len(h.array)
}

func (h *heap) value(index, defaultIndex int) int {
	if h.isValidIndex(index) {
		return h.array[index]
	} else {
		return h.array[defaultIndex]
	}
}

func (h *heap) heapifyDown(index int) {
	for {
		leftChildIndex := left(index)
		rightChildIndex := right(index)

		leftValue := h.value(leftChildIndex, index)
		rightValue := h.value(rightChildIndex, index)

		var smallerValueIndex int
		if leftValue < rightValue {
			smallerValueIndex = leftChildIndex
		} else {
			smallerValueIndex = rightChildIndex
		}

		if h.isValidIndex(smallerValueIndex) && h.array[smallerValueIndex] < h.array[index] {
			h.swap(index, smallerValueIndex)
			index = smallerValueIndex
		} else {
			break
		}
	}
}

func (h *heap) Insert(item int) {
	h.array = append(h.array, item)
	h.heapifyUp(len(h.array) - 1)
}

func (h *heap) Remove() int {
	if len(h.array) == 0 {
		return math.MinInt
	}

	removedItem := h.array[0]
	h.array[0] = h.array[len(h.array)-1]
	h.array = h.array[:len(h.array)-1]

	if len(h.array) > 0 {
		h.heapifyDown(0)
	}

	return removedItem
}

func NewHeap() *heap {
	h := heap{}
	h.array = []int{}
	return &h
}
