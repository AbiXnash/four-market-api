package server

import (
	"log/slog"

	"github.com/gin-gonic/gin"
)

func Serve(port string) {
	slog.Info("Running Server", "port", port)
	r := gin.Default()

	r.Run(":" + port)
}
