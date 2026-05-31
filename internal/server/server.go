package server

import (
	"log/slog"

	"github.com/AbiXnash/four-market-api/internal/routes"
	"github.com/gin-gonic/gin"
)

func Serve(port string) {
	slog.Info("Running Server", "port", port)

	r := gin.Default()
	routes.RouterSetup(r)

	r.Run(":" + port)
}
