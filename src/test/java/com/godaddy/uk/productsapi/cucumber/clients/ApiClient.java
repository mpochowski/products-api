package com.godaddy.uk.productsapi.cucumber.clients;

import com.godaddy.uk.productsapi.utilities.Loggable;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.godaddy.uk.productsapi.models.LoggedInUser.USERNAME_BRAND_DELIMITER;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public abstract class ApiClient implements Loggable {

    private static final String SERVER_URL = "http://localhost";

    @LocalServerPort
    private int port;
    private RestTemplate restTemplate = new RestTemplate();
    private BasicAuthenticationInterceptor interceptor;

    public void login(int brandId, String username, String password) {
        this.interceptor = new BasicAuthenticationInterceptor(
                String.join(USERNAME_BRAND_DELIMITER, String.valueOf(brandId), username),
                password
        );
        this.restTemplate.getInterceptors().add(this.interceptor);
    }

    public void logout() {
        this.restTemplate.getInterceptors().remove(this.interceptor);
        this.interceptor = null;
    }

    String buildServerUrl(String contextPath) {
        return SERVER_URL + ":" + this.port + contextPath;
    }

    <REQUEST, RESPONSE> RESPONSE doHttpRequest(HttpMethod method, String url, REQUEST request, ParameterizedTypeReference<RESPONSE> responseType) {
        try {
            return restTemplate
                    .exchange(url, method, new HttpEntity<>(request), responseType)
                    .getBody();
        } catch (Exception e) {
            logger().error("Http error occurred: {}", e.getMessage());
            return null;
        }
    }

}