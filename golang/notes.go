package main

import (
	"fmt"
	"log"
	"math"
)

func main() {
	testStrings()
	log.Println()

	testSlices()
	log.Println()

	testMaps()
	log.Println()

	testPointers()
	log.Println()

	testStructs()
	log.Println()

	testInterfaces()
	log.Println()
}

// ---

type Shape interface {
	Area() float64
}

type Rectangle struct {
	Length float64
	Height float64
}

func (r *Rectangle) Area() float64 {
	return r.Height * r.Length
}

type Circle struct {
	Radius float64
}

func (c *Circle) Area() float64 {
	return math.Pi * c.Radius * c.Radius
}

func sumAreas(shapes []Shape) float64 {
	total := 0.0

	for _, shape := range shapes {
		total += shape.Area()
	}

	return total
}

func testInterfaces() {
	shapes := []Shape{
		&Circle{5},
		&Rectangle{10, 5},
		&Circle{8},
	}

	totalArea := sumAreas(shapes)
	log.Printf("Total area of %d is %v\n", len(shapes), totalArea)
}

// ---

type Trade struct {
	Symbol string
	Volume int
	Price  float64
	Buy    bool
}

// NewTrade is like a constructor
func NewTrade(symbol string, volume int, price float64, buy bool) (*Trade, error) {
	// validate params

	return &Trade{Symbol: symbol, Volume: volume, Price: price, Buy: buy}, nil
}

func Value(trade Trade) float64 {
	return trade.Price * float64(trade.Volume)
}

func (trade *Trade) ValueWithPointer() float64 {
	return trade.Price * float64(trade.Volume)
}

func testStructs() {
	t1 := Trade{"MSFT", 10, 99.98, true}
	log.Println(t1)
	log.Printf("%+v: %f\n", t1, Value(t1))
	log.Printf("%+v: %f\n", t1, t1.ValueWithPointer())

	t2 := Trade{}
	log.Printf("%+v: %f\n", t2, Value(t2))
	log.Printf("%+v: %f\n", t2, t2.ValueWithPointer())

	t3, err := NewTrade("MSFT", 5, 89.98, false)
	if err == nil {
		log.Printf("%+v: %f\n", t3, Value(*t3))
		log.Printf("%+v: %f\n", t3, t3.ValueWithPointer())
	}
}

// ---

func testStrings() {
	book := "The color of magic"

	log.Println(book, len(book))
	log.Println(book[4:9])
	log.Println(book[4:])

	log.Println(`
		The 
		multiline
		string
	`)
}

// ---

func testSlices() {
	loons := []string{"bugs", "daffy", "taz"}
	log.Println(len(loons))
	log.Println(loons[0])
	log.Println(loons[1:])

	for i := range loons {
		log.Println(i)
	}

	for i, name := range loons {
		log.Printf("%s at %d\n", name, i)
	}

	loons = append(loons, "elmer")

	// Find max value in a slice

	maxValue := math.MinInt
	nums := []int{16, 8, 42, 4, 23, 15}
	for _, num := range nums {
		if num > maxValue {
			maxValue = num
		}
	}

	log.Printf("Maximum number of %v is %d\n", nums, maxValue)
}

// ---

func testMaps() {
	stocks := map[string]float64{
		"AMZN": 1699.8,
		"GOOG": 1129.19,
		"MSFT": 98.61,
	}

	log.Println(len(stocks))
	log.Println(stocks["MSFT"])
	log.Println(stocks["TSLA"])

	value, ok := stocks["TSLA"]
	if !ok {
		log.Println("TSLA not found")
	} else {
		log.Println(value)
	}

	stocks["TSLA"] = 322.12
	delete(stocks, "AMZN")

	for key := range stocks {
		log.Println(key)
	}

	for key, value := range stocks {
		log.Printf("%s -> %.2f\n", key, value)
	}
}

// ---

func testPointers() {
	// 1. pointers

	myNumber := 23
	// & get pointer to a variable
	doubleValue(&myNumber)
	log.Println(myNumber)

	// 2. error handling - don't use panic (anti pattern)
	for _, number := range []float64{-2, 2} {
		value, err := sqrt(number)
		if err != nil {
			log.Printf("ERROR: %s\n", err)
		} else {
			log.Printf("VALUE: %f\n", value)
		}
	}

	// 3. defer
	testDefer()
}

// the parameter is a pointer to int
func doubleValue(n *int) {
	// * deference value
	*n *= 2
}

// return error
func sqrt(n float64) (float64, error) {
	if n < 0 {
		return 0.0, fmt.Errorf("sqrt of netgative value (%f)", n)
	}

	return math.Sqrt(n), nil
}

func cleanup(name string) {
	log.Printf("Cleaning up %s\n", name)
}

func testDefer() {
	defer cleanup("a mess")
	log.Println("Some work to do")
}
