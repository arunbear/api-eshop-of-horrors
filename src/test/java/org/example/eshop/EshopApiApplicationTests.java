package org.example.eshop;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.eshop.dto.ProductDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

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
        var labels = Set.of("food", "limited");

        var productToCreate = new JSONObject()
            .put("name", "Special Smelly Cheese")
            .put("price", 20.99)
            .put("labels", new JSONArray(labels));

        ProductDto product = createProduct(productToCreate)
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

        assertThat(product.addedAt())
            .isEqualTo(todayAsString());
        assertThat(product.labels())
            .containsExactlyInAnyOrderElementsOf(labels);
    }

    @Test
    void product_labels_are_restricted() throws JSONException {
        var labels = Set.of("food", "drink", "clothes", "limited", "books");

        var productToCreate = new JSONObject()
            .put("labels", new JSONArray(labels))
        ;

        createProduct(productToCreate)
            .then()
            .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST))
            .body("message", equalTo("Invalid product label <books>"))
        ;
    }

    String todayAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.now().format(formatter);
    }

    @Test
    void duplicate_product_names_are_not_allowed() throws JSONException {
        var productToCreate = new JSONObject()
            .put("name", "Highlander")
            .put("labels", new JSONArray(Set.of()))
        ;

        createProduct(productToCreate);
        createProduct(productToCreate)
            .then()
            .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST))
            .body("message", equalTo("A product with name <Highlander> already exists"))
        ;
    }

    @Test
    void a_product_name_cannot_exceed_200_characters() throws JSONException {
        var productToCreate = new JSONObject()
            .put("name", "A".repeat(201))
            .put("labels", new JSONArray(Set.of()))
            ;

        createProduct(productToCreate)
            .then()
            .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST))
            .body("message", equalTo("A product name cannot exceed 200 characters"))
        ;
    }

    Response createProduct(JSONObject jsonObject) {
        return RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(jsonObject.toString())
            .post("/products")
        ;
    }

}
