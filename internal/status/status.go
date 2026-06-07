package status

import (
	"database/sql"
	"log/slog"
	"os"
	"sync/atomic"
	"time"

	"github.com/AbiXnash/four-market-api/internal/db"
)

var enabled atomic.Bool

// Enabled reports whether the application is currently enabled (online).
func Enabled() bool {
	return enabled.Load()
}

// Refresh fetches the current status from the database and updates the in-memory flag.
func Refresh() error {
	status, err := get()
	if err != nil {
		return err
	}

	enabled.Store(status)
	return nil
}

// StartMonitor launches a background goroutine that periodically refreshes
// the application status from the database.
func StartMonitor() {
	ticker := time.NewTicker(30 * time.Minute)

	go func() {
		defer ticker.Stop()
		for range ticker.C {
			if err := Refresh(); err != nil {
				slog.Error("failed to refresh application status", "error", err)
			}
		}
	}()
}

func get() (bool, error) {
	var status bool
	var lastModified string

	err := db.DB.QueryRow(`
		SELECT status, last_mdy
		FROM application_status
		WHERE app_id = ?
		`, os.Getenv("APP_ID")).Scan(&status, &lastModified)
	if err != nil {
		if err == sql.ErrNoRows {
			status = false
			lastModified = "UNKNOWN"
		} else {
			return false, err
		}
	}

	if status {
		slog.Debug("Application Run Status: Operational", "lastModified", lastModified)
	} else {
		slog.Debug(
			"Application is offline",
			"status", "not_operational",
			"lastModified", lastModified,
			"action", "server_not_available",
		)
	}

	return status, nil
}
