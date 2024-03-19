package persistence.entity;

import jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.sql.dml.DeleteQueryBuilder;
import persistence.sql.dml.InsertQueryBuilder;
import persistence.sql.dml.UpdateQueryBuilder;

public class EntityPersister {
    private static final Logger logger = LoggerFactory.getLogger(EntityPersister.class);
    private final JdbcTemplate jdbcTemplate;

    public EntityPersister(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void update(Object entity) {
        UpdateQueryBuilder updateQueryBuilder = UpdateQueryBuilder.builder()
                .entity(entity)
                .build();

        String updateQuery = updateQueryBuilder.generateQuery();
        logger.debug("update query: {}", updateQuery);

        jdbcTemplate.execute(updateQuery);
    }

    public Object insert(Object entity) {
        InsertQueryBuilder insertQueryBuilder = InsertQueryBuilder.builder()
                .entity(entity)
                .build();

        String insertQuery = insertQueryBuilder.generateQuery();
        logger.debug("insert query: {}", insertQuery);

        return jdbcTemplate.executeAndReturnObject(insertQuery);
    }

    public void delete(Object entity) {
        DeleteQueryBuilder deleteQueryBuilder = DeleteQueryBuilder.builder()
                .entity(entity)
                .build();

        String deleteQuery = deleteQueryBuilder.generateQuery();
        logger.debug("delete query: {}", deleteQuery);

        jdbcTemplate.execute(deleteQuery);
    }
}
