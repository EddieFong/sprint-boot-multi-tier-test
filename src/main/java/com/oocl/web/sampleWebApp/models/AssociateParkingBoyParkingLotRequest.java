package com.oocl.web.sampleWebApp.models;

import java.util.Objects;

public class AssociateParkingBoyParkingLotRequest {
    private String parkingLotId;

    public String getParkingLotId() {
        return parkingLotId;
    }

    public static AssociateParkingBoyParkingLotRequest create(String parkingLotId) {
        final AssociateParkingBoyParkingLotRequest request = new AssociateParkingBoyParkingLotRequest();
        request.parkingLotId = parkingLotId;
        return request;
    }
}