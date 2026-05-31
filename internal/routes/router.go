package routes

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

func RouterSetup(r *gin.Engine) {
	r.SetTrustedProxies(nil)

	r.GET("/", root)
}

func root(c *gin.Context) {
	c.String(http.StatusOK, "Hello there!")
}
