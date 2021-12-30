package datastructures

import "fmt"

type linkedList struct {
	head   *node
	tail   *node
	length int
}

type node struct {
	data interface{}
	next *node
}

func (l *linkedList) prepend(data interface{}) {
	newNodePtr := &node{data: data}

	if l.head == nil {
		l.head = newNodePtr
		l.tail = newNodePtr
	} else {
		newNodePtr.next = l.head
		l.head = newNodePtr
	}

	l.length++
}

func (l *linkedList) append(data interface{}) {
	newNodePtr := &node{data: data}

	if l.head == nil {
		l.head = newNodePtr
		l.tail = newNodePtr
	} else {
		l.tail.next = newNodePtr
		l.tail = newNodePtr
	}

	l.length += 1
}

func (l *linkedList) getNode(position int) (*node, error) {
	counter := 0
	ptr := l.head

	if position < 0 || position > l.length-1 {
		return nil, fmt.Errorf("invalid position: %v", position)
	}

	for counter != position && ptr != nil {
		ptr = ptr.next
		counter++
	}

	return ptr, nil
}

func (l *linkedList) get(position int) (interface{}, error) {
	node, err := l.getNode(position)

	if err != nil {
		return nil, err
	}

	return node.data, nil
}

func (l *linkedList) delete(position int) {
	if position == 0 {
		l.head = l.head.next
	} else {
		previousNode, err := l.getNode(position - 1)

		if err != nil {
			return
		}

		previousNode.next = previousNode.next.next
	}

	l.length--
}

func (l *linkedList) toString() string {
	ptr := l.head
	result := "["

	for ptr != nil {
		result += fmt.Sprintf("%v", ptr.data)
		ptr = ptr.next

		if ptr != nil {
			result += ", "
		}
	}

	result += "]"
	return result
}

func NewLinkedList() *linkedList {
	return &linkedList{}
}
