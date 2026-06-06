package routes

import (
	"net/http"

	"github.com/AbiXnash/four-market-api/internal/middleware"
	"github.com/gin-gonic/gin"
)

func RouterSetup(r *gin.Engine) {
	r.SetTrustedProxies(nil)

	r.Use(middleware.ApplicationStatusTracker())
	r.GET("/", root)
}

func root(c *gin.Context) {
	c.String(http.StatusOK, "Hello there!")
}
