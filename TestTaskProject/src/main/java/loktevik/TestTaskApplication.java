package loktevik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class TestTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestTaskApplication.class, args);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("balances");
    }
}
