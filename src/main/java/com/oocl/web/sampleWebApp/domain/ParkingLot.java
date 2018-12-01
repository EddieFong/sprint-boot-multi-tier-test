package com.oocl.web.sampleWebApp.domain;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "parkingLotId", unique = true, nullable = false)
    private String parkingLotId;

    @Column(name = "capacity", unique = false, nullable = false)
    private int capacity;

    public Long getId() {
        return id;
    }

    public ParkingLot() {}

    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

