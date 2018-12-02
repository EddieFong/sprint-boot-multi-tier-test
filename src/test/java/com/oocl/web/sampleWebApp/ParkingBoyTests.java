package com.oocl.web.sampleWebApp;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.AssociateParkingBoyParkingLotRequest;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import com.oocl.web.sampleWebApp.models.ParkingBoyWithParkingLotResponse;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
import static com.oocl.web.sampleWebApp.WebTestUtil.toJsonString;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ParkingBoyTests {
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

	@Test
	public void should_get_parking_boys() throws Exception {
	    // Given
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));

        // When
        final MvcResult result = mvc.perform(get("/parkingboys"))
            .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyResponse[] parkingBoys = getContentAsObject(result, ParkingBoyResponse[].class);

        assertEquals(1, parkingBoys.length);
        assertEquals("boy", parkingBoys[0].getEmployeeId());
    }

    @Test
    public void should_post_parking_boys() throws Exception {


        String newParkingBoyInJson = "{\"employeeId\":\"parkingBoy1\"}";

        mvc.perform(post("/parkingboys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newParkingBoyInJson)
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/parkingboys/")));

    }


    @Test
    public void should_get_parking_boy_with_parking_lots() throws Exception {
        // Given
        final ParkingBoy parkingBoy1 = new ParkingBoy("parkingBoy1");
        final ParkingLot parkingLot1 = new ParkingLot();
        parkingLot1.setParkingLotId("parkingLot1");
        parkingLot1.setCapacity(2);
        parkingLot1.setParkingBoy(parkingBoy1);

        final ParkingLot parkingLot2 = new ParkingLot();
        parkingLot2.setParkingLotId("parkingLot2");
        parkingLot2.setCapacity(3);
        parkingLot2.setParkingBoy(parkingBoy1);

        entityManager.persist(parkingLot1);
        entityManager.persist(parkingLot2);


        // When
        final MvcResult result = mvc.perform(get("/parkingboys/parkingBoy1"))
                .andExpect(status().isOk())
                .andReturn();
        final ParkingBoyWithParkingLotResponse response = getContentAsObject(
                result, ParkingBoyWithParkingLotResponse.class);

        assertEquals("parkingBoy1", response.getEmployeeId());
        List<ParkingLotResponse> parkingLots = response.getParkingLots();
        assertEquals(2, parkingLots.size());
        assertTrue(parkingLots.stream().anyMatch(pl -> pl.getParkingLotId().equals("parkingLot1")));
        assertTrue(parkingLots.stream().anyMatch(pl -> pl.getParkingLotId().equals("parkingLot2")));
    }

    @Test
    public void should_associate_parking_boy_with_parking_lot() throws Exception {
        // Given
        final ParkingBoy employee = new ParkingBoy("parkingBoy1");
        final ParkingLot parkingLot1 = new ParkingLot("parkingLot1", 2);
        entityManager.persist(employee);
        entityManager.persist(parkingLot1);

        AssociateParkingBoyParkingLotRequest request = AssociateParkingBoyParkingLotRequest.create("parkingLot1");

        // When
        mvc.perform(post("/parkingboys/parkingBoy1/parkinglots")
                .content(toJsonString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // Then
        final ParkingBoyWithParkingLotResponse parkingBoyWithParkingLots = getContentAsObject(
                mvc.perform(get("/parkingboys/parkingBoy1")).andReturn(), ParkingBoyWithParkingLotResponse.class);
        assertEquals("parkingLot1", parkingBoyWithParkingLots.getParkingLots().get(0).getParkingLotId());
    }


    @Test
    public void should_not_add_parking_lot_in_parking_boy() throws Exception {
        // Given
        final ParkingLot parkingLot1 = new ParkingLot("parkingLot1", 2);
        entityManager.persist(parkingLot1);
        AssociateParkingBoyParkingLotRequest request = AssociateParkingBoyParkingLotRequest.create("parkingLot1");

        // When
        mvc.perform(post("/parkingboys/parkingBoy2/parkinglots")
                .content(toJsonString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
