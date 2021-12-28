package main

import (
	"fmt"
	"log"
)

type Point struct {
	X int
	Y int
}

func (p *Point) Move(dx int, dy int) {
	p.X += dx
	p.Y += dy
}

// ---

type Square struct {
	Center Point
	Length int
}

func (square *Square) Move(dx int, dy int) {
	square.Center.Move(dx, dy)
}

func (square *Square) Area() int {
	return square.Length * square.Length
}

func NewSquare(x int, y int, length int) (*Square, error) {
	if length <= 0 {
		return nil, fmt.Errorf("length of a square must be possitive")
	}

	return &Square{Center: Point{x, y}, Length: length}, nil
}

// ---

func main() {
	square, err := NewSquare(5, 5, 15)

	if err != nil {
		log.Fatalf(err.Error())
	}

	fmt.Printf("%+v\n", square)
	fmt.Printf("Area: %v\n", square.Area())

	square.Move(5, 5)
	fmt.Printf("%+v\n", square)
	fmt.Printf("Area: %v\n", square.Area())
}
