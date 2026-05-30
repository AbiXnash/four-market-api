package main

import (
	"fmt"
	"os"

	turso "github.com/AbiXnash/four-market-api/internal/db"
	"github.com/joho/godotenv"
)

func init() {
	godotenv.Load()
}

func main() {
	err := turso.ConnectDB()
	if err != nil {
		panic(err)
	}

	fmt.Println(os.Getenv("PORT"))
}
