package persistence.association;

import persistence.sql.metadata.ColumnMetadata;
import persistence.sql.metadata.EntityMetadata;

import java.lang.reflect.Field;

public class OneToManyAssociation {

    private final EntityMetadata entityMetadata;
    private final ColumnMetadata columnMetadata;
    private final Field oneField;

    private OneToManyAssociation(EntityMetadata entityMetadata, ColumnMetadata columnMetadata, Field oneField) {
        this.entityMetadata = entityMetadata;
        this.columnMetadata = columnMetadata;
        this.oneField = oneField;
    }

    public static OneToManyAssociation from(Class<?> clazz) {
        EntityMetadata entity = EntityMetadata.from(clazz);
        return new OneToManyAssociation(entity, null, null);
    }
}
