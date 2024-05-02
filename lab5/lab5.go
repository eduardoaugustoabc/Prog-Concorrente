package main

import(
	"fmt"
	"time"
)

const maxCapacity = 10
var counter = 0

type Request struct{
        ID int
}

func create_req() Request{
	counter++
	return Request{ID: counter}
}

func exec_req(req Request){
	time.Sleep(time.Second)
	fmt.Printf("Requisicao %d processada\n", req.ID)
}

func main(){
	var reqQueue = make(chan Request)

	processRequests := func(){
		for req := range reqQueue{
			exec_req(req)
		}
	}

	for i := 0; i < maxCapacity; i++{
		go processRequests()
	}

	for{
		var req = create_req()
		reqQueue <- req
	}
}
