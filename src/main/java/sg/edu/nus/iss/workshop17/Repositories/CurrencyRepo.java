package sg.edu.nus.iss.workshop17.Repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;



@Repository
public class CurrencyRepo {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
}
