package states

import (
	"database/sql"
	"log/slog"
	"os"
	"sync/atomic"
	"time"

	turso "github.com/AbiXnash/four-market-api/internal/db"
)

var AppEnabled atomic.Bool

func StartStatusMonitor() {
	ticker := time.NewTicker(30 * time.Second)

	go func() {
		defer ticker.Stop()
		for range ticker.C {
			if err := RefreshStatus(); err != nil {
				slog.Error("failed to refresh application status", "error", err)
			}
		}
	}()
}

func RefreshStatus() error {
	status, err := getApplicationStatus()
	if err != nil {
		return err
	}

	AppEnabled.Store(status)

	return nil
}

func getApplicationStatus() (bool, error) {
	var status bool
	var lastModified string

	err := turso.DB.QueryRow(`
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
			"action", "server_startup_stopped",
		)
	}

	return status, nil
}
