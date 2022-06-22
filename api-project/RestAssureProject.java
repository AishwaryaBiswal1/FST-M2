import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class RestAssureProject {
    RequestSpecification requestSpec1;
    ResponseSpecification responseSpec1;
    String sshKey;
    int sshKeyId;
    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpec1 = new RequestSpecBuilder()
                // Set content type
                .setContentType(ContentType.JSON)
                // Set base URL
                .addHeader("Authorization","token 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")
                .setBaseUri("https://api.github.com")
                // Build request specification
                .build();
        responseSpec1 = new ResponseSpecBuilder()
                // Check status code in response
                .expectStatusCode(201)
                // Check response content type
                .expectContentType("application/json")
                // Check if response contains name property
                .build();
        sshKey="ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIDg40P/O5OTi17LUu5GX/nDI/av5KWRvsOBz6Gk2QU6D";
    }
    @Test
    public void post()
    {
        String reqBody = "{\"title\": \"RestAssuredAPIKey\",  \"key\": \""+sshKey+"\" }";

        Response response=given().spec(requestSpec1).body(reqBody).when().post("/user/keys");
        String resBody= response.getBody().asPrettyString();
        System.out.println(resBody);
        System.out.println(response.getStatusCode());
        sshKeyId=response.then().extract().path("id");
        System.out.println(sshKeyId);

        Assert.assertEquals(response.getStatusCode(), 201, "status code");


    }

    @Test(priority=2)
    public void getKey() {
        Response response =
                given().spec(requestSpec1).when()
                        .get("/user/keys");
        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code");
    }

    @Test(priority=3)
    public void deleteKey() {
        Response response =
                given().spec(requestSpec1)
                        .when()
                        .delete("/user/keys/"+sshKeyId); // Send DELETE request

        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 204, "Incorrect code");

    }
}