package org.personalized.dashboard.auth;

import org.personalized.dashboard.bootstrap.OAuthBootstrap;
import org.springframework.social.oauth2.GrantType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sudan on 26/7/15.
 */
public class GoogleLoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = OAuthBootstrap.getGooglePlusFactory().getOAuthOperations().
                buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, OAuthBootstrap.getoAuth2Parameters());
        response.sendRedirect(url);
    }
}
