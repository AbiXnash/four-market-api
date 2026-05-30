package turso

import (
	"database/sql"
	"errors"
	"log/slog"
	"os"

	_ "github.com/tursodatabase/libsql-client-go/libsql"
)

func ConnectDB() error {
	tursoUrl := os.Getenv("TURSO_DATABASE_URL")
	turoToken := os.Getenv("TURSO_AUTH_TOKEN")

	if tursoUrl == "" || turoToken == "" {
		slog.Warn("db is not configured")
		return errors.New("db not connected")
	}

	url := tursoUrl + "?authToken=" + turoToken

	db, _ := sql.Open("libsql", url)

	slog.Info("Turso Connected.")

	defer db.Close()

	return nil
}
