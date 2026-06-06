package db

import (
	"database/sql"
	"errors"
	"log/slog"
	"os"

	_ "github.com/tursodatabase/libsql-client-go/libsql"
)

var DB *sql.DB

func ConnectTurso() error {
	tursoUrl := os.Getenv("TURSO_DATABASE_URL")
	turoToken := os.Getenv("TURSO_AUTH_TOKEN")
	slog.Debug("db connection", "url", tursoUrl, "token", turoToken)

	if tursoUrl == "" || turoToken == "" {
		slog.Warn("db is not configured")
		return errors.New("db not connected")
	}

	url := tursoUrl + "?authToken=" + turoToken

	db, err := sql.Open("libsql", url)
	if err != nil {
		return err
	}

	if err := db.Ping(); err != nil {
		return err
	}

	DB = db

	slog.Info("Turso Connected.")

	return nil
}
