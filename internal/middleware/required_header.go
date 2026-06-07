package middleware

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

func RequireHeaders(requireHeaders ...string) gin.HandlerFunc {
	return func(c *gin.Context) {
		if headerValue := c.GetHeader("Client-App"); headerValue != "4M" {
			c.AbortWithStatusJSON(http.StatusUnauthorized, gin.H{
				"error": "Client not authorized",
			})
		}
	}
}
