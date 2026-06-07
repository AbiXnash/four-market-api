package main

import (
	"log/slog"
	"os"

	db "github.com/AbiXnash/four-market-api/internal/db"
	"github.com/AbiXnash/four-market-api/internal/logger"
	"github.com/AbiXnash/four-market-api/internal/server"
	"github.com/AbiXnash/four-market-api/internal/states"
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

	// Initialize application status (loads current status and starts background monitor)
	if err := states.RefreshStatus(); err != nil {
		slog.Warn("initial application status refresh failed", "error", err)
	}
	states.StartStatusMonitor()

	server.Serve(os.Getenv("PORT"))
}
