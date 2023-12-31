package com.example.leave_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.example.leave_app.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // @Query(value = "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM
    // User u WHERE u.email = :email", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);
}