package vn.edu.hcmuaf.fit.demohello.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.demohello.model.User;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query(value = "select r from Role r inner join UserHasRole ur on r.id = ur.id where ur.id=: userId")
    List<String> findAllRolesByUserId(Long userId);

    // distinct
    //@Query(value = "select distinct u from User u where u.firstName=:firstName and u.lastName=:lastName")
    List<User> findDistinctByFirstNameAndLastName(String firstName, String lastName);

    // single field
    //@Query(value = "select * from User u where u.email=?")
    //List<User> findByEmail(String email);

    // Or
    @Query(value = "select u from User u where u.firstName=:firstName or u.lastName=:lastName")
    List<User> findByFirstNameOrLastName(String name);

    // Is, equal
    @Query(value = "select u from User u where u.firstName=:name")
    List<User> findByFirstNameIs(String name);
    List<User> findByFirstNameEquals(String name);
    List<User> findByFirstName(String name);

    // Between
    @Query(value = "select u from User u where u.createAt between ?1 and ?2")
    List<User> findByCreatedAtBetween(Date startDate, Date endDate);

    // LessThan
   // @Query(value = "select u from User u where u.age < :age")
    List<User> findByAgeLessThan(int age);

    // Before And After
    @Query(value = "select u from User u where u.createAt < :date")
    List<User> findByCreatedAtBefore(Date date);
    List<User> findByCreatedAtAfter(Date date);

    // IsNull, Null
    //@Query(value = "select u from User u where u.age is null")
    List<User> findByAgeIsNull();

    //@Query(value = "select u from User u where u.age is not null")
    List<User> findByAgeNotNull();

    // Like
    //@Query(value = "select u from User u where u.firstName like %:firstName%")
    //List<User> findByFirstName(String firstName);

    //@Query(value = "select u from User u where u.firstName not like %:firstName%")
    //List<User> findByFirstName(String firstName);

    // startingWith
    @Query(value = "select u from User u where u.lastName like :lastName%")
    List<User> findByLastNameStartingWith(String lastName);

    // endingWith
    @Query(value = "select u from User u where u.lastName like %:lastName")
    List<User> findByLastNameEndingWith(String lastName);

    // containing
    @Query(value = "select u from User u where u.firstName like %:name%")
    List<User> findByLastNameContaining(String name);

    // Not
    @Query(value = "select u from User u where u.lastName <> :name")
    List<User> findByLastNameNot(String name);

    // In
    @Query(value = "select u from User u where u.age in (18, 25, 30)")
    List<User> findByAgeIn(Collection<Integer> ages);
    // Not In

    //True / False
    @Query(value = "select u from User u where u.activated=true")
    List<User> findByActivatedTrue();
    List<User> findByActivatedFalse();

    // IgnoreCase
    @Query(value = "select u from User u where lower(u.lastName) <> lower(:name)")
    List<User> findByFirstNameIgnoreCase(String firstName);

    // orderBy
    List<User> findByFirstNameOrderByCreateAtDesc(String name);

    List<User> findByFirstNameAndLastNameAllIgnoreCase(String firstName, String lastName);

    @Query(value = "select u from User u inner join Address a on u.id = a.id where a.city =: city")
    List<User> getAllUser(String city);
}
