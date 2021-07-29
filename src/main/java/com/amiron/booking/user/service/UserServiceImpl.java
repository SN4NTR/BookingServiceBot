package com.amiron.booking.user.service;

import com.amiron.booking.user.model.User;
import com.amiron.booking.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(@NotNull final User user) {
        return userRepository.save(user);
    }

    @Override
    public User getById(@NotNull final Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public User update(@NotNull final User user) {
        final Long id = user.getId();
        final User existingUser = getById(id);

        existingUser.setUsername(user.getUsername());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setChatId(user.getChatId());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        return save(user);
    }

    @Override
    public boolean existsById(@NotNull final Long id) {
        return userRepository.findById(id).isPresent();
    }
}
