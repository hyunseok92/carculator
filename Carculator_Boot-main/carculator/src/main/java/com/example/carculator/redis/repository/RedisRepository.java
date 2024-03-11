package com.example.carculator.redis.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.carculator.redis.entity.RedisRefreshToken;

public interface RedisRepository extends  CrudRepository<RedisRefreshToken, String> {

}