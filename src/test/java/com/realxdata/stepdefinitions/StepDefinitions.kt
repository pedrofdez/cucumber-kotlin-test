package com.realxdata.stepdefinitions

import com.realxdata.RestAssuredSupport
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import gherkin.deps.com.google.gson.Gson
import gherkin.deps.com.google.gson.reflect.TypeToken
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification

import io.restassured.RestAssured.given
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple

class StepDefinitions : RestAssuredSupport {

    data class PropertiesDTO(val name: String, val code: String, val level: String)
    data class FeatureDTO(val type: String, val properties: PropertiesDTO)
    data class MapsDTO (val type: String, val features: List<FeatureDTO>)
    data class AssetsDTO(val id: Int, val address: String)

    private var response: Response? = null
    private var request: RequestSpecification? = null
    private val MAPS_ENDPOINT = "http://test.realxdata.com/api/v1/maps"

    @Given("^there is a valid user$")
    fun thereIsAValidUser(authMap: Map<String, String>) {
        request = given()
            .contentType(ContentType.JSON)
            .header("X-User-Email", authMap["X-User-Email"])
            .header("X-User-Token", authMap["X-User-Token"])
    }

    @When("^the maps endpoint is called with params$")
    fun whenMapsAreCalledWithParams(params: Map<String, String>) {
        request!!.params(params)
        response = request!!.When().get(MAPS_ENDPOINT)
    }

    @When("^city view options is called for (?:.+) \\[city_id:(\\d+)\\]$")
    fun cityViewOptionsIsCalled(city_id: Int) {
        request!!.param("city_id", city_id)
        response = request!!.When().get(MAPS_ENDPOINT + "/city-view-options")
    }

    @When("^assets is called for (?:.+) \\[city_id:(\\d+)\\]$")
    fun assetsIsCalled(city_id: Int) {
        request!!.param("city_id", city_id)
        response = request!!.When().get(MAPS_ENDPOINT + "/assets")
    }

    @Then("^the status code is (\\d+)$")
    fun verifyStatusCode(statusCode: Int) {
        assertThat(response!!.statusCode())
            .isEqualTo(statusCode)
    }

    @Then("^the response has (\\d+) areas$")
    fun theResponseHasANumberOfAreas(areasSize: Int) {
        val gson = Gson()
        val subareas : MapsDTO = gson.fromJson(response!!.body.asString(), MapsDTO::class.java);

        assertThat(subareas.features)
            .hasSize(areasSize)
    }

    @Then("^the areas returned are$")
    fun theAreasReturnedAre(areasNameAndCode: Map<String, String>) {
        val gson = Gson()
        val subareas : MapsDTO = gson.fromJson(response!!.body.asString(), MapsDTO::class.java);

        areasNameAndCode.forEach {
            assertThat(subareas.features)
                    .extracting("properties.name", "properties.code")
                    .contains(Tuple(it.key, it.value));
        }
    }

    @Then("^(\\d+) assets are returned$")
    fun aNumberOfAssetsAreReturned(numberOfAssets: Int) {
        val gson = Gson()
        val listType = object : TypeToken<List<AssetsDTO>>() {}.type
        val assets : List<AssetsDTO> = gson.fromJson(response!!.body.asString(), listType);

        assertThat(assets)
                .hasSize(numberOfAssets)
    }
}
