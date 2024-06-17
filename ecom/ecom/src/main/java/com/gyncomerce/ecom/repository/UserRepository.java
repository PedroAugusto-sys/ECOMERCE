package com.gyncomerce.ecom.repository;

import com.gyncomerce.ecom.entity.User;
import com.gyncomerce.ecom.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByRole(UserRole role);
    Optional<User> findFirstByEmail(String email);
}
