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

public class ForecastOtherDaysTest extends AccuweatherAbstractTest{

    @Test
    void testResponseForecast1Day() {
        Weather weather = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 60)
                .when().request(Method.GET, getBaseUrl() + "/forecasts/v1/daily/1day/{locationKey}")
                .then().statusCode(200)
                .extract().response().body().as(Weather.class);
        Assertions.assertEquals(1, weather.getDailyForecasts().size());
    }

    @Test
    void testResponseForecast10Days() {
        String code = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 20)
                .when().request(Method.GET, getBaseUrl() + "/forecasts/v1/daily/10day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Code");
        Assertions.assertEquals("Unauthorized", code);
    }

    @Test
    void  testResponseForecast15Days(){
        String message = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 30)
                .when().request(Method.GET, getBaseUrl() + "/forecasts/v1/daily/15day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Message");
        Assertions.assertEquals("Api Authorization failed", message);
    }

    @Test
    void testGetLocalizationMoscow(){
        Map<String, String> mapQueries = new HashMap<>();
        mapQueries.put("apikey", getApiKey());
        mapQueries.put("q", "Moscow");
        List<Location> locationList = given().queryParams(mapQueries)
                .when().get(getBaseUrl() + "/locations/v1/cities/autocomplete")
                .then().statusCode(200)
                .extract().body().jsonPath().getList(".", Location.class);
        Assertions.assertAll(() -> Assertions.assertEquals(10, locationList.size()),
                () -> Assertions.assertEquals("Moscow Mills", locationList.get(6).getLocalizedName()));
    }
}
