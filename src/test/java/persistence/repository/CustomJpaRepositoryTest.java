package persistence.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import domain.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.DatabaseTest;

class CustomJpaRepositoryTest extends DatabaseTest {

    @Test
    @DisplayName("기존 객체가 없을 경우 save 테스트")
    void save() {
        // given
        final Person person = Person.of("test", 11, "test12@gmail.com");

        // when
        Person savedPerson = customJpaRepository.save(person);

        // then
        assertAll(
            () -> assertThat(savedPerson.getId()).isEqualTo(1L),
            () -> assertThat(savedPerson.getName()).isEqualTo(person.getName()),
            () -> assertThat(savedPerson.getAge()).isEqualTo(person.getAge()),
            () -> assertThat(savedPerson.getEmail()).isEqualTo(person.getEmail())
        );
    }

    @Test
    @DisplayName("기존 객체가 있을 경우 Dirty Checking 테스트 - merge")
    void merge() {
        // given
        final Person person = Person.of("test", 11, "test12@gmail.com");
        Person savedPerson = customJpaRepository.save(person);
        final String newName = "newName";
        savedPerson.setName(newName);

        // when
        Person resultPerson = customJpaRepository.save(savedPerson);

        // then
        assertAll(
            () -> assertThat(resultPerson.getId()).isEqualTo(savedPerson.getId()),
            () -> assertThat(resultPerson.getName()).isEqualTo(newName),
            () -> assertThat(resultPerson.getAge()).isEqualTo(savedPerson.getAge()),
            () -> assertThat(resultPerson.getEmail()).isEqualTo(savedPerson.getEmail())
        );
    }
}
