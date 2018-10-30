package org.personalized.dashboard.auth;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.bootstrap.OAuthBootstrap;
import org.personalized.dashboard.dao.api.SessionDao;
import org.personalized.dashboard.model.User;
import org.personalized.dashboard.service.api.UserService;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.oauth2.AccessGrant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sudan on 26/7/15.
 */
public class GoogleLoginCallbackServlet extends HttpServlet {

    private final UserService userService;
    private final UserCookieGenerator userCookieGenerator;
    private final IdGenerator idGenerator;
    private final SessionDao sessionDao;

    @Inject
    public GoogleLoginCallbackServlet(UserService userService, UserCookieGenerator userCookieGenerator,
                                      IdGenerator idGenerator, SessionDao sessionDao) {
        this.userService = userService;
        this.userCookieGenerator = userCookieGenerator;
        this.idGenerator = idGenerator;
        this.sessionDao = sessionDao;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {


            AccessGrant accessGrant = OAuthBootstrap.getGooglePlusFactory().getOAuthOperations().exchangeForAccess(
                    request.getParameter("code"), OAuthBootstrap.getoAuth2Parameters().getRedirectUri(), null);
            Google google = new GoogleTemplate(accessGrant.getAccessToken());

            User user = new User();
            user.setEmail(google.plusOperations().getGoogleProfile().getAccountEmail());
            user.setUsername(google.plusOperations().getGoogleProfile().getDisplayName());
            user.setProfilePicURL(google.plusOperations().getGoogleProfile().getImageUrl());

            String userId = userService.upsert(user);
            String cookieValue = idGenerator.generateId(Constants.COOKIE_PREFIX, Constants.COOKIE_LENGTH, false);

            String oldCookieValue = userCookieGenerator.readCookieValue(request);
            if (StringUtils.isNotEmpty(oldCookieValue)) {
                sessionDao.delete(oldCookieValue);
            }
            sessionDao.create(cookieValue, userId);

            userCookieGenerator.removeCookie(response);
            userCookieGenerator.addCookie(cookieValue, response);
            response.sendRedirect(ConfigKeys.DASHBOARD);
        } catch (Exception e) {
            response.sendRedirect(ConfigKeys.GOOGLE_LOGIN);
        }

    }
}
