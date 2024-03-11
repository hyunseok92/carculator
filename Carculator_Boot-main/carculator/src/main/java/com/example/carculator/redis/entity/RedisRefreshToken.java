package com.example.carculator.redis.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Data
@RedisHash("refresh")
@NoArgsConstructor
public class RedisRefreshToken {
    @Id
    private String id;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;

    public RedisRefreshToken(String email, String refreshToken, Long expiration) {
        this.id = email;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    public RedisRefreshToken updateToken(String refreshToken, Long expiration){
        this.refreshToken = refreshToken;
        this.expiration = expiration;
        return this;
    }
}
