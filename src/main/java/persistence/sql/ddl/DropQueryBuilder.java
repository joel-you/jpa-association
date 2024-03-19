package persistence.sql.ddl;

import persistence.sql.metadata.EntityMetadata;

public class DropQueryBuilder {

    public static final String DROP_TABLE_TEMPLATE = "DROP TABLE %s";
    private final EntityMetadata entity;

    private DropQueryBuilder(EntityMetadata entity) {
        this.entity = entity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String generateQuery() {
        return String.format(DROP_TABLE_TEMPLATE, entity.getName());
    }

    public static class Builder {

        private EntityMetadata entity;

        private Builder() {
        }

        public Builder entity(Class<?> clazz) {
            this.entity = EntityMetadata.from(clazz);
            return this;
        }

        public DropQueryBuilder build() {
            return new DropQueryBuilder(entity);
        }
    }
}
