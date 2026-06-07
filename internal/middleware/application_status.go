package middleware

import (
	"net/http"

	"github.com/AbiXnash/four-market-api/internal/status"
	"github.com/gin-gonic/gin"
)

func ApplicationStatusTracker() gin.HandlerFunc {
	return func(c *gin.Context) {
		enabled := status.Enabled()

		// Expose the current status via a custom header
		// Frontend can read this on every response (success or error)
		if enabled {
			c.Header("X-Application-Status", "online")
		} else {
			c.Header("X-Application-Status", "offline")
		}

		// Store in context so other middlewares or handlers can access it if needed
		c.Set("appEnabled", enabled)
		c.Set("appStatus", map[bool]string{true: "online", false: "offline"}[enabled])

		if !enabled {
			c.AbortWithStatusJSON(http.StatusServiceUnavailable, gin.H{
				"error":  "Application is temporarily not available. Please try again later.",
				"status": "offline",
			})
			return
		}

		c.Next()
	}
}
