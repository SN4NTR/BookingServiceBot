package com.amiron.booking.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
@Table(name = "masters")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Master {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "photo")
    private byte[] photo;
}
