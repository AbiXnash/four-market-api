package logger

import (
	"log/slog"
	"os"
	"time"

	"github.com/lmittmann/tint"
)

func CustomLogger() *slog.Logger {
	return slog.New(tint.NewHandler(os.Stderr, &tint.Options{
		AddSource:  true,
		Level:      slog.LevelDebug,
		TimeFormat: time.StampMilli,
		NoColor:    false,
		ReplaceAttr: func(_ []string, a slog.Attr) slog.Attr {
			if a.Key == slog.LevelKey {
				level := a.Value.Any().(slog.Level)
				switch {
				case level < slog.LevelInfo:
					return slog.String(slog.LevelKey, "DEBUG")
				case level < slog.LevelWarn:
					return slog.String(slog.LevelKey, "INFO")
				case level < slog.LevelError:
					return slog.String(slog.LevelKey, "WARN")
				default:
					return slog.String(slog.LevelKey, "ERROR")
				}
			}
			return a
		},
	}))
}
