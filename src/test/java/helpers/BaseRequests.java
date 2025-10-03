package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseRequests {

    public static RequestSpecification requestSpec(String authToken) {
        return new RequestSpecBuilder()
                .setBaseUri(ParametersProvider.getProperty("apiUrl"))
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "OAuth " + authToken)
                .build();
    }

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ParametersProvider.getProperty("apiUrl"))
                .setContentType(ContentType.JSON)
                .build();
    }
}