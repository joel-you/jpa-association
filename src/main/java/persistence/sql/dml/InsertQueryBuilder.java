package persistence.sql.dml;

import java.util.List;
import java.util.stream.Collectors;
import persistence.sql.metadata.ColumnMetadata;
import persistence.sql.metadata.EntityMetadata;

public class InsertQueryBuilder {

    public static final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";
    public static final String DELIMITER = ", ";
    public static final String DEFAULT = "default";
    private final EntityMetadata entity;

    private InsertQueryBuilder(EntityMetadata entity) {
        this.entity = entity;
    }

    public static Builder builder() {
        return new Builder();
    }

    private String valueClause() {
        return entity.getColumns().stream()
            .map(column -> entity.getPrimaryKey().getName().equals(column.getName()) ? DEFAULT
                : generateColumnValue(column.getValue()))
            .collect(Collectors.joining(DELIMITER));
    }

    private String columnsClause(List<ColumnMetadata> columns) {
        return columns.stream()
            .map(ColumnMetadata::getName)
            .collect(Collectors.joining(DELIMITER));
    }

    private String generateColumnValue(Object object) {
        if (object instanceof String) {
            return String.format("'%s'", object);
        } else {
            return String.valueOf(object);
        }
    }

    public String generateQuery() {
        return String.format(INSERT_TEMPLATE, entity.getName(), columnsClause(entity.getColumns()),
            valueClause());
    }

    public static class Builder {

        private EntityMetadata entity;

        private Builder() {
        }

        public Builder entity(Object object) {
            this.entity = EntityMetadata.of(object.getClass(), object);
            return this;
        }

        public InsertQueryBuilder build() {
            return new InsertQueryBuilder(entity);
        }
    }
}
