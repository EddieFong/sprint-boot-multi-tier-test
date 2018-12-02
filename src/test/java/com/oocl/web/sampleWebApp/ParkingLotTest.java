package com.oocl.web.sampleWebApp;

import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
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

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
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
public class ParkingLotTest {
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;


    @Test
    public void should_get_parking_lots() throws Exception {
        // Given
        ParkingLot parkingLot1 = new ParkingLot();
        parkingLot1.setParkingLotId("parkingLot1");
        parkingLot1.setCapacity(10);
        final ParkingLot parkingLot = parkingLotRepository.save(parkingLot1);

        // When
        final MvcResult result = mvc.perform(get("/parkinglots"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingLotResponse[] parkingLots = getContentAsObject(result, ParkingLotResponse[].class);

        assertEquals(1, parkingLots.length);
        assertEquals("parkingLot1", parkingLots[0].getParkingLotId());
        assertEquals(10, parkingLots[0].getCapacity());
    }


    @Test
    public void should_add_new_parking_lot() throws Exception {
        //given
        String newParkingLotInJson = "{\"parkingLotId\":\"parkingLot1\", \"capacity\":10}";
        //when
        mvc.perform(post("/parkinglots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newParkingLotInJson)
        )//then
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/parkinglots/parkingLot1")));
    }

    @Test
    public void should_not_add_new_parking_lot() throws Exception {
        //given
        String newParkingLotInJson = "{\"parkingLotId\":\"parkingLot1\", \"capacity\":1000}";
        //when
        mvc.perform(post("/parkinglots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newParkingLotInJson)
        )//then
                .andExpect(status().is4xxClientError());
    }
}
