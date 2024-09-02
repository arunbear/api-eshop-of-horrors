package org.example.eshop;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.eshop.dto.CartDto;
import org.example.eshop.dto.ProductDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        assertThat(product.productId())
            .isGreaterThan(0);

        assertThat(product.addedAt())
            .isEqualTo(todayAsString());
        assertThat(product.labels())
            .containsExactlyInAnyOrderElementsOf(labels);
    }

    @Test
    void a_deleted_product_cannot_be_retrieved_by_id() throws JSONException {
        // given

        var productToCreate = new JSONObject()
            .put("name", "Special Smelly Cheese")
            .put("price", 20.99)
            .put("labels", new JSONArray(Set.of("food", "limited")));

        var createdProduct = createProduct(productToCreate)
            .then()
            .extract()
            .as(ProductDto.class);

        final String pathWithProductId = "/products/%d".formatted(createdProduct.productId());

        // when
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .delete(pathWithProductId)
            .then()
            .statusCode(equalTo(HttpStatus.SC_NO_CONTENT))
            ;
        // then
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .get(pathWithProductId)
            .then()
            .statusCode(equalTo(HttpStatus.SC_NOT_FOUND))
        ;
    }

    @Test
    void an_existing_product_can_be_retrieved_by_id() throws JSONException {
        var labels = Set.of("food", "limited");

        var productToCreate = new JSONObject()
            .put("name", "Special Smelly Cheese")
            .put("price", 20.99)
            .put("labels", new JSONArray(labels));

        var creationResponse = createProduct(productToCreate);

        ProductDto retrievedProduct = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .get(creationResponse.header("Location"))
            .then()
            .extract()
            .as(ProductDto.class)
        ;
        assertThat(retrievedProduct.name())
            .isEqualTo(productToCreate.getString("name"));
        assertThat(retrievedProduct.price())
            .isEqualTo(productToCreate.getDouble("price"));

        assertThat(retrievedProduct.addedAt())
            .isEqualTo(todayAsString());
        assertThat(retrievedProduct.labels())
            .containsExactlyInAnyOrderElementsOf(labels);
    }

    @Test
    void a_list_of_all_products_can_be_retrieved() throws JSONException {
        // given
        var labelSets = List.of(
            Set.of("drink", "food"),
            Set.of("food")
        );
        final int first  = 0;
        final int second = 1;

        var productsToCreate = List.of(
            new JSONObject()
                .put("name", "Fancy IPA Beer")
                .put("price", 5.99)
                .put("labels", new JSONArray(labelSets.getFirst())),
            new JSONObject()
                .put("name", "Delicious Cake")
                .put("price", 10.11)
                .put("labels", new JSONArray(labelSets.get(second)))
        );
        productsToCreate.forEach(this::createProduct);

        // when
        ProductDto[] retrievedProducts = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .get("/products")
            .then()
            .statusCode(equalTo(HttpStatus.SC_OK))
            .extract()
            .as(ProductDto[].class)
        ;
        // then
        assertThat(retrievedProducts.length).isEqualTo(2);

        assertThat(retrievedProducts[first].name())
            .isEqualTo(productsToCreate.getFirst().getString("name"));
        assertThat(retrievedProducts[first].price())
            .isEqualTo(productsToCreate.getFirst().getDouble("price"));

        assertThat(retrievedProducts[first].addedAt())
            .isEqualTo(todayAsString());
        assertThat(retrievedProducts[first].labels())
            .containsExactlyInAnyOrderElementsOf(labelSets.getFirst());

        // and
        assertThat(retrievedProducts[second].name())
            .isEqualTo(productsToCreate.get(second).getString("name"));
        assertThat(retrievedProducts[second].price())
            .isEqualTo(productsToCreate.get(second).getDouble("price"));

        assertThat(retrievedProducts[second].addedAt())
            .isEqualTo(todayAsString());
        assertThat(retrievedProducts[second].labels())
            .containsExactlyInAnyOrderElementsOf(labelSets.get(second));
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

    @Test
    void a_new_cart_is_empty_and_not_checked_out() {
        CartDto cartDto = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .post("/carts")
            .then()
            .statusCode(equalTo(HttpStatus.SC_CREATED))
            .header("Location", matchesRegex(".+/carts/[1-9][0-9]*"))
            .extract()
            .as(CartDto.class);
        ;
        assertThat(cartDto.cartId()).isGreaterThan(0);
        assertThat(cartDto.checkedOut()).isFalse();
        assertThat(cartDto.products()).isEmpty();
    }

    @BeforeEach
    void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,
            "product_labels",
            "label",
            "product"
        );
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
