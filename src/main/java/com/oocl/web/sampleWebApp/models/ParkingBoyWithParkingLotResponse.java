package com.oocl.web.sampleWebApp.models;
import java.util.List;
import java.util.Objects;
public class ParkingBoyWithParkingLotResponse {
    private String employeeId;
    private List<ParkingLotResponse> parkingLots;
    public String getEmployeeId() {
        return employeeId;
    }
    public List<ParkingLotResponse> getParkingLots() {
        return parkingLots;
    }
    public static ParkingBoyWithParkingLotResponse create(String employeeId, List<ParkingLotResponse> parkingLots) {
        final ParkingBoyWithParkingLotResponse response = new ParkingBoyWithParkingLotResponse();
        response.employeeId = employeeId;
        response.parkingLots = parkingLots;
        return response;
    }
}