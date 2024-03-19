package persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import domain.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.DatabaseTest;

public class EntityPersisterTest extends DatabaseTest {

    @Test
    @DisplayName("Update query builder 동작 테스트")
    void update() {
        // given
        final String name = "joel";
        final Integer age = 30;
        final Person person = Person.of(1L, "crong", 35, "test@gmail.com");
        entityPersister.insert(person);

        // when
        Person findPerson = Person.of(1L, name, age, "test@gmail.com");
        entityPersister.update(findPerson);

        // then
        assertThat(entityManager.find(Person.class, 1L)).isEqualTo(findPerson);
    }

    @Test
    @DisplayName("Insert query builder 동작 테스트")
    void insert() {
        // given
        final String name = "crong";
        final Integer age = 35;
        final String email = "test@gmail.com";
        final Person person = Person.of(1L, name, age, email);

        // when
        entityPersister.insert(person);

        // then
        assertThat(entityManager.find(Person.class, 1L)).isEqualTo(person);
    }

    @Test
    @DisplayName("Delete query builder 동작 테스트")
    void delete() {
        // given
        final Person person = Person.of("crong", 35, "test@gmail.com");
        entityManager.persist(person);

        // when
        entityManager.remove(person);

        // then
        assertThatThrownBy(() -> entityManager.find(Person.class, 1L)).isInstanceOf(
            RuntimeException.class);
    }
}
