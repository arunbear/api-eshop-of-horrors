## Tests

End to end [tests](https://github.com/arunbear/api-eshop-of-horrors/blob/master/src/test/java/org/example/eshop/EshopApiApplicationTests.java)

## Test Output

```
(base) pop-os% ./mvnw clean test
[INFO] 
[INFO] --- surefire:3.3.1:test (default-test) @ eshop-api ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
Java HotSpot(TM) 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
[INFO] +--org.example.eshop.service.ProductServiceTest - 2.164 s
[INFO] |  +-- [OK] a saved DTO is saved as an entity - 2.076 s
[INFO] |  '-- [OK] a product repository is used to find products - 0.011 s
[INFO] +--org.example.eshop.EshopApiApplicationTests - 15.68 s
[INFO] |  +-- [??] non existent products cannot be added to a cart (void org.example.eshop.EshopApiApplicationTests.non_existent_products_cannot_be_added_to_a_cart() is @Disabled) - 0 s
[INFO] |  +-- [OK] a new cart is empty and not checked out - 2.403 s
[INFO] |  +-- [OK] returns details of a new product on POST to products - 0.474 s
[INFO] |  +-- [OK] a cart can be checked out - 0.299 s
[INFO] |  +-- [??] products can be removed from a cart (void org.example.eshop.EshopApiApplicationTests.products_can_be_removed_from_a_cart() is @Disabled) - 0 s
[INFO] |  +-- [OK] duplicate product names are not allowed - 1.229 s
[INFO] |  +-- [??] a checked out cart cannot be modified (void org.example.eshop.EshopApiApplicationTests.a_checked_out_cart_cannot_be_modified() is @Disabled) - 0 s
[INFO] |  +-- [OK] a deleted product cannot be retrieved by id - 0.133 s
[INFO] |  +-- [OK] a list of all products can be retrieved - 0.151 s
[INFO] |  +-- [OK] product labels are restricted - 0.080 s
[INFO] |  +-- [OK] products can be added to a cart - 0.123 s
[INFO] |  +-- [OK] a list of all carts can be retrieved - 0.288 s
[INFO] |  +-- [OK] an existing product can be retrieved by id - 0.136 s
[INFO] |  '-- [OK] a product name cannot exceed 200 characters - 0.059 s
[INFO] 
[INFO] Results:
[INFO] 
[WARNING] Tests run: 16, Failures: 0, Errors: 0, Skipped: 3
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  28.077 s
[INFO] Finished at: 2024-09-02T23:33:41+01:00
[INFO] ------------------------------------------------------------------------
```
