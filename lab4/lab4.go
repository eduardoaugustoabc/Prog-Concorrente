package main

import (
    "fmt"
    "math/rand"
    "time"
)


func request_stream() chan string {
    ch := make(chan string)
    go func() {
        for {
            time.Sleep(time.Duration(rand.Intn(2000)) * time.Millisecond)
            ch <- randomString(5)
        }
    }()
    return ch
}

func randomString(n int) string {
    var letters = []rune("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
    rng := rand.New(rand.NewSource(time.Now().UnixNano()))
    b := make([]rune, n)
    for i := range b {
        b[i] = letters[rng.Intn(len(letters))]
    }
    return string(b)
}

func ingest(ch1, ch2 chan string, in chan string) {
    defer close(in)
    for {
        select {
        case item := <-ch1:
            in <- item
        case item := <-ch2:
            in <- item
        }
    }
}

func main() {
    ch1 := request_stream()
    ch2 := request_stream()
    in := make(chan string)

    go ingest(ch1, ch2, in)

    for item := range in {
        fmt.Println("Received:", item)
    }
}
