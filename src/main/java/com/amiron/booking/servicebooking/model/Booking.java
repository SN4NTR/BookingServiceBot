package com.amiron.booking.servicebooking.model;

import com.amiron.booking.client.model.Client;
import com.amiron.booking.master.model.Master;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "master_id", referencedColumnName = "id")
    private Master master;
}
