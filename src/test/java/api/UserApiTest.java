package api;

import com.nagarro.driven.base.ApiBase;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class UserApiTest extends ApiBase {

    @Test
    public void testGetUser() {
        Response response = get("/users/2");

        response.then()
                .statusCode(200)
                .body("data.id", equalTo(2));
    }
}
