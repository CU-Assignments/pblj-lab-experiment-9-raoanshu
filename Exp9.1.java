
public interface GreetingService {
    void greet(String name);
}


// 2nd step
public class GreetingServiceImpl implements GreetingService {
    public void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }
}
// third step 
import org.springframework.context.annotation.*;

@Configuration
public class AppConfig {
    @Bean
    public GreetingService greetingService() {
        return new GreetingServiceImpl();
    }
}
