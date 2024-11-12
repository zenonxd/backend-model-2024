package com.eventostec.api.domain.event;

import com.eventostec.api.domain.address.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 100)
    private String title;

    @Column(length = 250)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") // Formato desejado para a data
    @Temporal(TemporalType.DATE)
    private Date date;
    private Boolean remote;

    @Column(length = 100)
    private String img_url;

    @Column(length = 100)
    private String event_url;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL)
    private Address address;

    public long getDateAsLong() {
        return this.date.getTime();
    }

    public void setDateFromLong(long timestamp) {
        this.date = new Date(timestamp);
    }

}
