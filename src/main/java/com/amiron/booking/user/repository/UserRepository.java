package com.amiron.booking.user.repository;

import com.amiron.booking.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Aliaksandr Miron
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
