package datastructures

import (
	"testing"
)

func TestLinkedListCreation(t *testing.T) {
	list := NewLinkedList()

	if list.head != nil {
		t.Fatalf("head should be nil for an empty list")
	}

	if list.tail != nil {
		t.Fatalf("tail should be nil for an empty list")
	}

	if list.length != 0 {
		t.Fatalf("length should be 0 for an empty list")
	}

	if list.toString() != "[]" {
		t.Fatalf("invalid empty list representation")
	}
}

func TestLinkedListAppend(t *testing.T) {
	list := NewLinkedList()
	data := "some data"

	list.append(data)

	if list.head == nil {
		t.Fatalf("head should not be nil for a not empty list")
	}

	if list.tail != list.head {
		t.Fatalf("tail should be equal to head for a one element list")
	}

	if list.length != 1 {
		t.Fatalf("length should be 1 for a list with one element")
	}

	if list.toString() != "[some data]" {
		t.Fatalf("invalid list representation")
	}

	moreData := "some more data"
	list.append(moreData)

	if list.tail == list.head {
		t.Fatalf("tail should not be equal to head for a two elements list")
	}

	if list.length != 2 {
		t.Fatalf("length should be 2 for a list with two elements")
	}

	if list.toString() != "[some data, some more data]" {
		t.Fatalf("invalid list representation")
	}

	list.append(23)
	if list.toString() != "[some data, some more data, 23]" {
		t.Fatalf("invalid list representation (is: %s)", list.toString())
	}

	list.prepend(32)
	if list.toString() != "[32, some data, some more data, 23]" {
		t.Fatalf("invalid list representation")
	}
}

func TestLinkedListPrepend(t *testing.T) {
	list := NewLinkedList()
	data := "some data"

	list.prepend(data)

	if list.head == nil {
		t.Fatalf("head should not be nil for a not empty list")
	}

	if list.tail != list.head {
		t.Fatalf("tail should be equal to head for a one element list")
	}

	if list.length != 1 {
		t.Fatalf("length should be 1 for a list with one element")
	}

	if list.toString() != "[some data]" {
		t.Fatalf("invalid list representation")
	}

	moreData := "some more data"
	list.prepend(moreData)

	if list.tail == list.head {
		t.Fatalf("tail should not be equal to head for a two elements list")
	}

	if list.length != 2 {
		t.Fatalf("length should be 2 for a list with two elements")
	}

	if list.toString() != "[some more data, some data]" {
		t.Fatalf("invalid list representation")
	}

	list.prepend(23)
	if list.toString() != "[23, some more data, some data]" {
		t.Fatalf("invalid list representation")
	}

	list.append(32)
	if list.toString() != "[23, some more data, some data, 32]" {
		t.Fatalf("invalid list representation")
	}
}

func TestLinkedListGet(t *testing.T) {
	list := NewLinkedList()

	list.append(0)
	list.append(1)
	list.append(2)
	list.append(3)

	if list.toString() != "[0, 1, 2, 3]" {
		t.Fatalf("invalid list representation")
	}

	for i := 3; i <= 3; i++ {
		item, _ := list.get(i)
		if item != i {
			t.Fatalf("invalid value at the position %d", i)
		}
	}

	_, err := list.get(4)
	if err.Error() != "invalid position: 4" {
		t.Fatalf("should report invalid position")
	}
}

func TestLinkedListDelete(t *testing.T) {
	list := NewLinkedList()

	list.append(0)
	list.append(1)
	list.append(2)
	list.append(3)
	list.append(4)

	if list.toString() != "[0, 1, 2, 3, 4]" {
		t.Fatalf("invalid list representation")
	}

	if list.length != 5 {
		t.Fatalf("invalid length")
	}

	// remove the first element
	list.delete(0)

	if list.toString() != "[1, 2, 3, 4]" {
		t.Fatalf("invalid list representation")
	}

	if list.length != 4 {
		t.Fatalf("invalid length")
	}

	// remove the element in a middle
	list.delete(2)
	if list.toString() != "[1, 2, 4]" {
		t.Fatalf("invalid list representation")
	}

	if list.length != 3 {
		t.Fatalf("invalid length")
	}

	// remove the last element
	list.delete(2)

	if list.toString() != "[1, 2]" {
		t.Fatalf("invalid list representation")
	}

	if list.length != 2 {
		t.Fatalf("invalid length")
	}

	// remove the rest
	list.delete(1)
	list.delete(0)

	if list.toString() != "[]" {
		t.Fatalf("invalid list representation")
	}

	if list.length != 0 {
		t.Fatalf("invalid length")
	}
}
