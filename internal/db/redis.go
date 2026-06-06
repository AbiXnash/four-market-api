package db

import (
	"context"
	"log/slog"
	"os"
	"strconv"

	"github.com/redis/go-redis/v9"
)

var RedisClient *redis.Client

func ConnectRedis() *redis.Client {
	addr := os.Getenv("REDIS_ADDR")
	if addr == "" {
		addr = "localhost:6379"
	}

	password := os.Getenv("REDIS_PASSWORD")

	db := 0
	if dbStr := os.Getenv("REDIS_DB"); dbStr != "" {
		if parsed, err := strconv.Atoi(dbStr); err == nil {
			db = parsed
		}
	}

	rdb := redis.NewClient(&redis.Options{
		Addr:     addr,
		Password: password,
		DB:       db,
	})

	if err := rdb.Ping(context.Background()).Err(); err != nil {
		slog.Warn("redis not reachable, continuing without cache", "error", err)
		return rdb
	}

	RedisClient = rdb
	slog.Info("Redis connected", "addr", addr, "db", db)

	return rdb
}
