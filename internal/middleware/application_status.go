package middleware

import (
	"net/http"

	"github.com/AbiXnash/four-market-api/internal/states"
	"github.com/gin-gonic/gin"
)

func ApplicationStatusTracker() gin.HandlerFunc {
	return func(c *gin.Context) {
		if !states.AppEnabled.Load() {
			c.AbortWithStatusJSON(http.StatusServiceUnavailable, gin.H{
				"error": "Application is temporarily not available. Please try again later.",
			})
			return
		}
		c.Next()
	}
}
