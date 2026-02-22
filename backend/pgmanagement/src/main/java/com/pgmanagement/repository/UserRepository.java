package com.pgmanagement.repository;

import com.pgmanagement.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailOrPhone(String email, String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    Optional<User> findByEmail(String email);
    
 // ✅ Count by role
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countUsersByRole();

    // ✅ Count by status
    @Query("SELECT u.status, COUNT(u) FROM User u GROUP BY u.status")
    List<Object[]> countUsersByStatus();

    // ✅ Custom order: ACTIVE first, then others
    @Query("SELECT u FROM User u ORDER BY " +
           "CASE " +
           "WHEN u.status = 'ACTIVE' THEN 1 " +
           "WHEN u.status = 'PENDING_APPROVAL' THEN 2 " +
           "WHEN u.status = 'SUSPENDED' THEN 3 " +
           "WHEN u.status = 'DELETED' THEN 4 " +
           "END, u.createdAt DESC")
    Page<User> findAllOrderByStatusPriority(Pageable pageable);
}