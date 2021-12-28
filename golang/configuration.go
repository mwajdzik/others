package main

// go get github.com/pkg/errors

import (
	"fmt"
	"log"
	"os"
)

type Config struct {
	// some config fields
}

func readConfig(path string) (*Config, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}

	defer file.Close()
	cfg := &Config{}

	// parse the config file

	return cfg, nil
}

func setupLogging() {
	out, err := os.OpenFile("app.log", os.O_APPEND|os.O_CREATE|os.O_WRONLY, 0644)

	if err != nil {
		return
	}

	log.SetOutput(out)
}

func main() {
	setupLogging()

	cfg, err := readConfig("/path/to/config.yaml")

	if err != nil {
		log.Printf("ERROR: %+v", err)
		os.Exit(1)
	}

	fmt.Println(cfg)
}
