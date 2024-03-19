package persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import domain.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.DatabaseTest;

class SimpleEntityManagerTest extends DatabaseTest {

    private final Person person = Person.of(1L, "test", 11, "test!@gmail.com");

    @Test
    @DisplayName("요구사항1 - find")
    void find() {
        // given
        entityManager.persist(person);

        // when
        Person findPerson = entityManager.find(Person.class, 1L);

        // then
        assertThat(findPerson).isEqualTo(person);
    }

    @Test
    @DisplayName("요구사항2 - persist (insert)")
    void insert() {
        // when
        Person savedPerson = entityManager.persist(person);

        // then
        assertThat(savedPerson).isEqualTo(person);
    }

    @Test
    @DisplayName("persist - update")
    void update() {
        // given
        entityManager.persist(person);
        Person newPerson = Person.of(1L, "test12", 12, "test12@gmail.com");

        // when
        Person updatePerson = entityManager.persist(newPerson);

        // then
        assertAll(
            () -> assertThat(updatePerson).isNotEqualTo(person),
            () -> assertThat(updatePerson).isEqualTo(newPerson)
        );
    }

    @Test
    @DisplayName("요구사항3 - remove (delete)")
    void remove() {
        // given
        entityManager.persist(person);

        // when
        entityManager.remove(person);

        // then
        assertThatThrownBy(() -> entityManager.find(Person.class, 1L))
            .isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("요구사항1 - First Level Cache")
    void find_WithFirstLevelCache() {
        // given
        entityManager.persist(person);

        // when
        Person nonCachingPerson = entityManager.find(Person.class, 1L);
        Person cachingPerson = entityManager.find(Person.class, 1L);

        // then
        assertThat(nonCachingPerson).isEqualTo(cachingPerson);
    }

    @Test
    @DisplayName("merge 시 수정 사항이 없는 케이스 테스트")
    void merge_WithNoChanges_entityEntryIsManagedStatus_WhenPersisted() {
        // given
        entityManager.persist(person);
        Person person = entityManager.find(Person.class, 1L);

        // when
        Person mergedPerson = entityManager.merge(person);

        // then
        assertThat(mergedPerson).isEqualTo(person);
        assertThat(persistenceContext.getEntry(person).getStatus()).isEqualTo(EntityStatus.MANAGED);
    }

    @Test
    @DisplayName("remove 시 EntityEntry 가 Gone 상태인지 확인한다.")
    void remove_ThenEntityEntryIsGoneStatus() {
        // given
        entityManager.persist(person);

        // when
        entityManager.remove(person);

        // then
        assertThat(persistenceContext.getEntry(person).getStatus()).isEqualTo(EntityStatus.GONE);
    }

    @Test
    @DisplayName("")
    void find_WhenReadOnlyStatus_ThenException() {
        // given
        entityManager.persist(person);
        Person findPerson = entityManager.find(Person.class, 1L);
        findPerson.setName("test2");
        persistenceContext.getEntry(findPerson).readOnly();

        // when
        // then
        assertThatThrownBy(() -> entityManager.merge(findPerson))
            .isExactlyInstanceOf(IllegalStateException.class);

        assertThatThrownBy(() -> entityManager.remove(findPerson))
            .isExactlyInstanceOf(IllegalStateException.class);
    }
}
