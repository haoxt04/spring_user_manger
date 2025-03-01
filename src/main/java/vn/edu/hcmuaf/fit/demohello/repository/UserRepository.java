package vn.edu.hcmuaf.fit.demohello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.demohello.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query(value = "select r from Role r inner join UserHasRole ur on r.id = ur.id where ur.id=: userId")
    List<String> findAllRolesByUserId(Long userId);
}
