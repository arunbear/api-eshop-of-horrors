package org.example.eshop;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.example.eshop.dto.ProductDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EshopApiApplicationTests {
    @LocalServerPort
    private int localServerPort;

    @BeforeEach
    public void setUp() {
        RestAssured.port = localServerPort;
    }

    @Test
    void returns_details_of_a_new_product_on_POST_to_products() throws JSONException {
        var productToCreate = new JSONObject()
            .put("name", "Special Smelly Cheese")
            .put("price", 20.99);

        ProductDto product = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(productToCreate.toString())
            .post("/products")
            .then()
            .statusCode(equalTo(HttpStatus.SC_CREATED))
            .header("Location", matchesRegex(".+/products/[1-9][0-9]*"))
            .extract()
            .as(ProductDto.class);

        assertThat(product.name())
            .isEqualTo(productToCreate.getString("name"));
        assertThat(product.price())
            .isEqualTo(productToCreate.getDouble("price"));
        assertThat(product.id())
            .isGreaterThan(0);
    }

    @Test
    void duplicate_product_names_are_not_allowed() throws JSONException {
        var productToCreate = new JSONObject()
            .put("name", "Highlander");

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(productToCreate.toString())
            .post("/products")
            ;
        // Create it again
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(productToCreate.toString())
            .post("/products")
            .then()
            .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST))
            .body("message", equalTo("A product with name <Highlander> already exists"))
        ;
    }

}
