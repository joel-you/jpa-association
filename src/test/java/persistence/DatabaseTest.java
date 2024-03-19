package persistence;

import database.DatabaseServer;
import database.H2;
import domain.Person;
import java.sql.SQLException;
import jdbc.JdbcTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import persistence.context.SimplePersistenceContext;
import persistence.entity.EntityManager;
import persistence.entity.EntityPersister;
import persistence.entity.SimpleEntityManager;
import persistence.repository.CustomJpaRepository;
import persistence.sql.ddl.CreateQueryBuilder;
import persistence.sql.ddl.DropQueryBuilder;
import persistence.sql.dialect.Dialect;
import persistence.sql.dialect.H2Dialect;

public class DatabaseTest {

    protected final Dialect DIALECT = new H2Dialect();
    protected JdbcTemplate jdbcTemplate;
    protected EntityManager entityManager;
    protected EntityPersister entityPersister;
    protected DatabaseServer databaseServer;
    protected SimplePersistenceContext persistenceContext;
    protected CustomJpaRepository<Person, Long> customJpaRepository;

    @BeforeEach
    void setUp() {
        CreateQueryBuilder createQueryBuilder = CreateQueryBuilder.builder()
            .dialect(DIALECT)
            .entity(Person.class)
            .build();

        try {
            databaseServer = new H2();
            databaseServer.start();
            jdbcTemplate = new JdbcTemplate(databaseServer.getConnection());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        persistenceContext = new SimplePersistenceContext();
        entityManager = new SimpleEntityManager(jdbcTemplate, persistenceContext);
        customJpaRepository = new CustomJpaRepository<>(entityManager);
        entityPersister = new EntityPersister(jdbcTemplate);

        jdbcTemplate.execute(createQueryBuilder.generateQuery());
    }

    @AfterEach
    void tearDown() {
        DropQueryBuilder dropQueryBuilder = DropQueryBuilder.builder()
            .entity(Person.class)
            .build();

        jdbcTemplate.execute(dropQueryBuilder.generateQuery());
        databaseServer.stop();
    }
}
