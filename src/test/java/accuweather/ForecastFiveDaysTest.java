package accuweather;

import io.restassured.http.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seminar.accuweather.location.Location;
import seminar.accuweather.weather.Weather;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;


public class ForecastFiveDaysTest extends AccuweatherAbstractTest{


    @Test
    void testGetResponce() {
        Weather weather = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when().get(getBaseUrl() + "/forecasts/v1/daily/5day/{locationKey}")
                .then().statusCode(200).time(lessThan(2000L))
                .extract().response().body().as(Weather.class);
        Assertions.assertEquals(5, weather.getDailyForecasts().size());
        System.out.println(weather);
    }

    @Test
    void testGetLocations() {
        Map<String, String> mapQuery = new HashMap<>();
        mapQuery.put("apikey", getApiKey());
        mapQuery.put("q", "Samara");
        List<Location> listLocations = given().queryParams(mapQuery)
                .when().get(getBaseUrl() + "/locations/v1/cities/autocomplete")
                .then().statusCode(200)
                .extract().body().jsonPath().getList(".", Location.class);
        Assertions.assertAll(() -> Assertions.assertEquals(10, listLocations.size()),
                () -> Assertions.assertEquals("Samarai", listLocations.get(2).getLocalizedName()));


    }

    @Test
    void testGetLocations2() {
        Map<String, String> mapQuery = new HashMap<>();
        mapQuery.put("apikey", getApiKey());
        mapQuery.put("q", "Samara");
        List<Location> listLocations = given().queryParams(mapQuery)
                .when().request(Method.GET,getBaseUrl() + "/locations/v1/cities/autocomplete")
                .then().statusCode(200)
                .extract().body().jsonPath().getList(".", Location.class);
        Assertions.assertAll(() -> Assertions.assertEquals(10, listLocations.size()),
                () -> Assertions.assertEquals("Samarai", listLocations.get(2).getLocalizedName()));


    }
}
