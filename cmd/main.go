package main

import (
	"log/slog"
	"os"

	turso "github.com/AbiXnash/four-market-api/internal/db"
	"github.com/AbiXnash/four-market-api/internal/logger"
	"github.com/AbiXnash/four-market-api/internal/server"
	"github.com/joho/godotenv"
)

func init() {
	godotenv.Load()                        // loaded .env
	slog.SetDefault(logger.CustomLogger()) // customize logger
}

func main() {
	server.Serve(os.Getenv("PORT")) // start server

	err := turso.ConnectDB() // connects to DB, Turso Sqllite
	if err != nil {
		panic(err)
	}
}
