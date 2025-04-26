import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;
    private double balance;

    // Getters and Setters
}
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

    @PersistenceContext
    private EntityManager em;

    public Account findById(Long id) {
        return em.find(Account.class, id);
    }

    public void update(Account account) {
        em.merge(account);
    }
}
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void transfer(Long fromId, Long toId, double amount) {
        Account from = repository.findById(fromId);
        Account to = repository.findById(toId);

        if (from.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        repository.update(from);
        repository.update(to);
    }
}
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var repo = context.getBean(AccountRepository.class);
        var service = context.getBean(AccountService.class);

        // Manually create accounts (in a real app, use repository)
        Account acc1 = new Account();
        acc1.setOwner("Alice");
        acc1.setBalance(1000);

        Account acc2 = new Account();
        acc2.setOwner("Bob");
        acc2.setBalance(500);

        var em = context.getBean(javax.persistence.EntityManager.class);
        em.getTransaction().begin();
        em.persist(acc1);
        em.persist(acc2);
        em.getTransaction().commit();

        // Transfer money
        service.transfer(acc1.getId(), acc2.getId(), 200);

        System.out.println("Transfer complete.");
    }
}
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.3.34</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-orm</artifactId>
        <version>5.3.34</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.6.15.Final</version>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>2.2.224</version>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-dbcp2</artifactId>
        <version>2.9.0</version>
    </dependency>
</dependencies>

