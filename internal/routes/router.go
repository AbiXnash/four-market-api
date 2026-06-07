package routes

import (
	"log/slog"
	"net/http"

	"github.com/AbiXnash/four-market-api/internal/middleware"
	"github.com/AbiXnash/four-market-api/internal/status"
	"github.com/gin-gonic/gin"
)

func RouterSetup(r *gin.Engine) {
	r.SetTrustedProxies(nil)

	r.Use(middleware.ApplicationStatusTracker())
	r.Use(middleware.RequireHeaders())

	r.GET("/", root)

	api := r.Group("/api")
	{
		// NOTE: need to Group with parent group object
		statusCheck := api.Group("/check")
		{
			statusCheck.GET("/app", applicationStatus)
		}
	}
}

func applicationStatus(c *gin.Context) {
	status.Refresh()
	enabled := status.Enabled()

	slog.Debug("Application status is checked",
		"ip", c.ClientIP(), "appStatus", enabled)

	c.JSON(http.StatusOK, gin.H{
		"status": enabled,
	})
}

func root(c *gin.Context) {
	c.String(http.StatusOK, "Hello there!")
}
