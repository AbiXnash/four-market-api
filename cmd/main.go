package main

import (
	"log/slog"
	"os"

	db "github.com/AbiXnash/four-market-api/internal/db"
	"github.com/AbiXnash/four-market-api/internal/logger"
	"github.com/AbiXnash/four-market-api/internal/server"
	"github.com/joho/godotenv"
)

func init() {
	godotenv.Overload()                    // loaded .env
	slog.SetDefault(logger.CustomLogger()) // customize logger
}

func main() {
	err := db.ConnectTurso()
	if err != nil {
		panic(err)
	}
	defer db.DB.Close()

	server.Serve(os.Getenv("PORT"))
}
