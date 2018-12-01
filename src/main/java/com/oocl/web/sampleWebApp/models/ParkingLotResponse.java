package com.oocl.web.sampleWebApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oocl.web.sampleWebApp.domain.ParkingLot;

import java.util.Objects;

public class ParkingLotResponse {

    private String parkingLotId;
    private int capacity;

    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public static ParkingLotResponse create(String parkingLotId, int capacity) {
        Objects.requireNonNull(parkingLotId);

        final ParkingLotResponse response = new ParkingLotResponse();
        response.setParkingLotId(parkingLotId);
        response.setCapacity(capacity);
        return response;
    }

    public static ParkingLotResponse create(ParkingLot entity) {
        return create(entity.getParkingLotId(),entity.getCapacity() );
    }

    @JsonIgnore
    public boolean isValid() {
        return parkingLotId != null;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
