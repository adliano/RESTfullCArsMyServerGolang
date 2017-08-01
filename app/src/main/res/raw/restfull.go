package main

// Go program to rum a REST full server

import (
	"net/http"
	"fmt"
	"log"

	// Router
	"github.com/gorilla/mux"
	"encoding/json"
)

const FRUIT = "fruit_name"


type Cars struct {
	Model      string    `json:"model"`
	Year int `json:"year"`
	Make string `json:"make"`
	Doors int `json:"doors"`
	Sold bool `json:"sold"`
}

type garage []Cars

func main() {
	// Add a router
	router := mux.NewRouter().StrictSlash(true)
	router.HandleFunc("/", getName)
	router.HandleFunc("/car", cars)
	router.HandleFunc("/fruit/{" + FRUIT + "}", showFruit)

	log.Fatal(http.ListenAndServe(":8080", router))
}

func getName(writer http.ResponseWriter, response *http.Request) {
	fmt.Fprint(writer, "Hello from go !")
}



func cars(writer http.ResponseWriter, response *http.Request) {

	tds := garage {
		Cars{Model:"Corola",Year:2012,Make:"Toyota",Doors:4,Sold:true},
		Cars{Model:"Civic",Year:2017,Make:"Honda",Doors:2},
		Cars{Model:"Civic",Year:2016,Make:"Honda",Doors:4,Sold:true},
		Cars{Model:"Ford",Year:2000,Make:"Econoline",Doors:2},
		Cars{Model:"Chevy",Year:2017,Make:"Bolt",Doors:2,Sold:true},
	}

	json.NewEncoder(writer).Encode(tds)
}

func showFruit(writer http.ResponseWriter, response *http.Request) {

	vars := mux.Vars(response)
	theFruit := vars[FRUIT]
	fmt.Fprint(writer, "Hello you typed ", theFruit)

}