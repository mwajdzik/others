package main

import (
	"bufio"
	"crypto/md5"
	"fmt"
	"io"
	"log"
	"os"
	"strings"
)

func parseSignaturesFile(path string) (map[string]string, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}

	defer file.Close()

	signatures := make(map[string]string)
	scanner := bufio.NewScanner(file)

	for lineNumber := 1; scanner.Scan(); lineNumber++ {
		// ae5252a205000e972b9747b0b125cf96  nasa-05.log
		fields := strings.Fields(scanner.Text())

		if len(fields) != 2 {
			return nil, fmt.Errorf("%s:%d bad line", path, lineNumber)
		}

		fileSignature := fields[0]
		fileName := fields[1]

		signatures[fileName] = fileSignature
	}

	if err := scanner.Err(); err != nil {
		return nil, err
	}

	return signatures, nil
}

func fileMD5(path string) (string, error) {
	file, err := os.Open(path)
	if err != nil {
		return "", err
	}
	defer file.Close()

	hash := md5.New()
	if _, err := io.Copy(hash, file); err != nil {
		return "", err
	}

	return fmt.Sprintf("%x", hash.Sum(nil)), nil
}

type WorkerResult struct {
	path  string
	match bool
	err   error
}

func md5Worker(path string, expectedSignature string, out chan *WorkerResult) {
	result := &WorkerResult{path: path}
	fileSignature, err := fileMD5(path)

	if err != nil {
		result.err = err
		out <- result
		return
	}

	result.match = fileSignature == expectedSignature
	out <- result
}

func main() {
	signatures, err := parseSignaturesFile("md5sum.txt")

	if err != nil {
		log.Fatalf("error: can't read signaure file - %s", err)
	}

	out := make(chan *WorkerResult)
	for path, signature := range signatures {
		go md5Worker(path, signature, out)
	}

	ok := true
	for range signatures {
		r := <-out

		switch {
		case r.err != nil:
			fmt.Printf("%s: error - %s\n", r.path, r.err)
			ok = false

		case !r.match:
			fmt.Printf("%s: signature mismatch\n", r.path)
			ok = false
		}
	}

	if !ok {
		os.Exit(1)
	}
}
