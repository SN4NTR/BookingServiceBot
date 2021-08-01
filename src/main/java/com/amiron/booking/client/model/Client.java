package com.amiron.booking.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
@Entity
@Data
public class Client {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @Nullable
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "chat_id")
    private Long chatId;

    @Nullable
    @Column(name = "email")
    private String email;

    @Nullable
    @Column(name = "phone_number")
    private String phoneNumber;
}
