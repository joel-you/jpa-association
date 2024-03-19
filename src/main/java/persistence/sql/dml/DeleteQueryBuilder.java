package persistence.sql.dml;

import java.util.List;
import java.util.Objects;
import persistence.sql.dml.conditions.WhereRecord;
import persistence.sql.metadata.EntityMetadata;


public class DeleteQueryBuilder {

    public static final String DELETE_TEMPLATE = "DELETE FROM %s";
    public static final String WHERE_DELIMITER = " ";
    private final EntityMetadata entity;
    private final WhereQueryBuilder whereQueryBuilder;

    private DeleteQueryBuilder(EntityMetadata entity, WhereQueryBuilder whereQueryBuilder) {
        this.entity = entity;
        this.whereQueryBuilder = whereQueryBuilder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String generateQuery() {
        return String.format(DELETE_TEMPLATE, Objects.isNull(whereQueryBuilder) ? entity.getName()
            : String.join(WHERE_DELIMITER, entity.getName(),
                whereQueryBuilder.generateWhereClausesQuery()));
    }

    public static class Builder {

        private EntityMetadata entity;
        private WhereQueryBuilder whereQueryBuilder;

        private Builder() {
        }

        public Builder entity(Object entity) {
            this.entity = EntityMetadata.of(entity.getClass(), entity);
            if (Objects.nonNull(this.entity.getPrimaryKey().getValue())) {
                this.whereQueryBuilder = WhereQueryBuilder.builder()
                    .whereConditions(this.entity)
                    .build();
            }
            return this;
        }

        public Builder where(List<WhereRecord> whereRecords) {
            if (Objects.isNull(entity)) {
                throw new IllegalStateException("Entity must be set before setting where clause");
            }

            this.whereQueryBuilder = WhereQueryBuilder.builder()
                .whereConditions(entity.getColumnsMetadata(), whereRecords)
                .build();
            return this;
        }

        public DeleteQueryBuilder build() {
            return new DeleteQueryBuilder(entity, whereQueryBuilder);
        }
    }
}
