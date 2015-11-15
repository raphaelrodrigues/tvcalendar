package io.tvcalendar.client;

import static org.junit.Assert.assertEquals;
import io.tvcalendar.web.rest.dto.SerieDTO;

import org.junit.Test;

public class OMDBClientTest {

    @Test
    public void testClient() throws Exception{
        OMDBClient client = new OMDBClient();
        SerieDTO theWireInfo = client.getSerie("The Wire");
        assertEquals(theWireInfo.getTitle(),"The Wire");
    }

    @Test
    public void testClientWithWhiteSpaces() throws Exception{
        OMDBClient client = new OMDBClient();
        SerieDTO theWireInfo = client.getSerie("Game of Thrones");
        assertEquals(theWireInfo.getTitle(),"Game of Thrones");
    }
}
