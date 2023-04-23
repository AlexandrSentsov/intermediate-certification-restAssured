import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static io.restassured.RestAssured.*;


public class BaseTest {

    String URL = "https://api.openweathermap.org/data/2.5/weather?";
    String appid = "appid=2cee583454702db67405731b5ee8901f";


    @ParameterizedTest(name = "Проверка работы фильтра по городам")
    @ValueSource(strings = {"Moscow", "London", "Paris", "Prague", "Sydney", "Tokyo", "New York"})
    @Step("Проверка работы фильтра по городам")
    public void checkWeatherWithCity (String city) {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        SuccessfulResponseDto successfulResponseDto = given()
                .when()
                .get(URL + "q=" + city + "&units=standard&lang=en&mode=json&" + appid)
                .then().log().all()
                .extract().body().as(SuccessfulResponseDto.class);

        Assertions.assertEquals(city, successfulResponseDto.getName());
    }

    @ParameterizedTest(name = "Проверка работы фильтра по id")
    @ValueSource(ints = {524901, 2643743, 2988507, 3067696, 2147714, 1850144, 5128581})
    @Step("Проверка работы фильтра по id")
    public void checkWeatherWithId (int id) {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        SuccessfulResponseDto successfulResponseDto = given()
                .when()
                .get(URL + "id=" + id + "&units=standard&lang=en&mode=json&" + appid)
                .then().log().all()
                .extract().body().as(SuccessfulResponseDto.class);

        Assertions.assertEquals(id, successfulResponseDto.getId());
    }

    @Test()
    @DisplayName("Проверка работы фильтров по координатам")
    @Step("Проверка работы фильтров по координатам")
    public void checkWeatherWithCoordinates () {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        SuccessfulResponseDto successfulResponseDto = given()
                .when()
                .get(URL + "lat=55.7522200&lon=37.6155600&units=standard&lang=en&mode=json&" + appid)
                .then().log().all()
                .extract().body().as(SuccessfulResponseDto.class);

        Assertions.assertEquals("Moscow", successfulResponseDto.getName());
    }

    @Test
    @DisplayName("Проверка работы фильтра по городам")
    @Step("Проверка работы фильтра по ZIP коду")
    public void checkWeatherWithZIP () {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        SuccessfulResponseDto successfulResponseDto = given()
                .when()
                .get(URL + "zip=95453&units=standard&lang=en&mode=json&" + appid)
                .then().log().all()
                .extract().body().as(SuccessfulResponseDto.class);

        Assertions.assertEquals("Lakeport", successfulResponseDto.getName());
    }

    @Test
    @DisplayName("Проверка работы фильтра по системе измерения")
    @Step("Проверка работы фильтра по системе измерения")
    public void checkWeatherWithUnits () {

        //вызываем метод с units=metric
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        SuccessfulResponseDto successfulResponseMetric = given()
                .when()
                .get(URL + "q=Moscow&units=metric&lang=en&mode=json&" + appid)
                .then().log().all()
                .extract().body().as(SuccessfulResponseDto.class);


        //вызываем метод с units=standard
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        SuccessfulResponseDto successfulResponseStandard = given()
                .when()
                .get(URL + "q=Moscow&units=standard&lang=en&mode=json&" + appid)
                .then().log().all()
                .extract().body().as(SuccessfulResponseDto.class);


        //вызываем метод с units=imperial
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        SuccessfulResponseDto successfulResponseImperial = given()
                .when()
                .get(URL + "q=Moscow&units=imperial&lang=en&mode=json&" + appid)
                .then().log().all()
                .extract().body().as(SuccessfulResponseDto.class);


        //сравнение округлённой температуры в цельсиях и кельвинах
        Assertions.assertEquals(Math.floor(successfulResponseMetric.getMain().getTemp() + 273.15),
                Math.floor(successfulResponseStandard.getMain().getTemp()));
        //сравнение округлённой температуры в цельсиях и фаренгейтах
        Assertions.assertEquals(Math.floor((successfulResponseMetric.getMain().getTemp() * 9/5) + 32),
                Math.floor(successfulResponseImperial.getMain().getTemp()));


        //сравнение округлённой ощущаемой температуры в цельсиях и кельвинах
        Assertions.assertEquals(Math.floor(successfulResponseMetric.getMain().getFeels_like() + 273.15),
                Math.floor(successfulResponseStandard.getMain().getFeels_like()));
        //сравнение округлённой ощущаемой температуры в цельсиях и фаренгейтах
        Assertions.assertEquals(Math.floor((successfulResponseMetric.getMain().getFeels_like() * 9/5) + 32),
                Math.floor(successfulResponseImperial.getMain().getFeels_like()));


        //сравнение округлённой максимальной температуры в цельсиях и кельвинах
        Assertions.assertEquals(Math.floor(successfulResponseMetric.getMain().getTemp_max() + 273.15),
                Math.floor(successfulResponseStandard.getMain().getTemp_max()));
        //сравнение округлённой минимальной температуры в цельсиях и кельвинах
        Assertions.assertEquals(Math.floor((successfulResponseMetric.getMain().getTemp_max() * 9/5) + 32),
                Math.floor(successfulResponseImperial.getMain().getTemp_max()));


        //сравнение округлённой максимальной температуры в цельсиях и кельвинах
        Assertions.assertEquals(Math.floor(successfulResponseMetric.getMain().getTemp_min() + 273.15),
                Math.floor(successfulResponseStandard.getMain().getTemp_min()));
        //сравнение округлённой минимальной температуры в цельсиях и кельвинах
        Assertions.assertEquals(Math.floor((successfulResponseMetric.getMain().getTemp_min() * 9/5) + 32),
                Math.floor(successfulResponseImperial.getMain().getTemp_min()));

        //сравнение скоростей ветра в м/c в метрической и международной системах
        Assertions.assertEquals(successfulResponseMetric.getWind().getSpeed(),
                successfulResponseStandard.getWind().getSpeed());
        //сравнение округлённых скоростей ветра в метры/секунды и мили/час
        Assertions.assertEquals(String.format("%.1f", successfulResponseMetric.getWind().getSpeed() * 2.237) ,
                String.format("%.1f", successfulResponseImperial.getWind().getSpeed()));
    }

    @ParameterizedTest(name = "Проверка работы фильтра по формату ответа")
    @ValueSource(strings = {"json", "xml", "html"})
    @Step("Проверка работы фильтра по формату ответа")
    public void checkWeatherWithMode (String mode) {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        Response response = given()
                .when()
                .get(URL + "q=Moscow&units=standard&lang=en&mode=" + mode + "&" + appid)
                .then().log().all()
                .extract().response();

        Assertions.assertEquals(mode, response.getContentType().split("/")[1].split(";")[0]);
    }


    @Test
    @DisplayName("Проверка получения 400й ошибки при вызове без фильтров")
    @Step("Проверка получения 400й ошибки при вызове без фильтров")
    public void checkWeatherWithOutFilters () {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400());
        Response response = given()
                .when()
                .get(URL + "units=standard&lang=en&mode=json&" + appid)
                .then().log().all()
                .extract().response();

        Assertions.assertEquals("400", response.jsonPath().get("cod"));
        Assertions.assertEquals("Nothing to geocode", response.jsonPath().get("message"));
    }

    @ParameterizedTest(name = "Проверка получения 404й ошибки при вызове c несуществующим названием города")
    @ValueSource(strings = {"q=TestCity", "id=1337"})
    @Step("Проверка получения 404й ошибки при вызове c несуществующим названием города")
    public void checkWeatherWithWrongCity(String sliceOfURL) {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError404());
        Response response = given()
                .when()
                .get(URL + sliceOfURL + "&units=standard&lang=en&mode=json&" + appid)
                .then().log().all()
                .extract().response();

        Assertions.assertEquals("404", response.jsonPath().get("cod"));
        Assertions.assertEquals("city not found", response.jsonPath().get("message"));
    }
}
