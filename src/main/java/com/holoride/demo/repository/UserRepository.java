package com.holoride.demo.repository;

import com.holoride.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value="SELECT user_Id FROM users u where u.username =:username", nativeQuery = true)
    Long findIdByUsername(@Param("username") String username);

}
