package org.personalized.dashboard.auth;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.dao.api.SessionDao;
import org.personalized.dashboard.utils.ConfigKeys;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sudan on 26/7/15.
 */
public class LogoutServlet extends HttpServlet {

    private final UserCookieGenerator userCookieGenerator;
    private final SessionDao sessionDao;

    @Inject
    public LogoutServlet(UserCookieGenerator userCookieGenerator, SessionDao sessionDao) {
        this.userCookieGenerator = userCookieGenerator;
        this.sessionDao = sessionDao;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cookieValue = userCookieGenerator.readCookieValue(request);

        if (StringUtils.isNotEmpty(cookieValue)) {
            sessionDao.delete(cookieValue);
            userCookieGenerator.removeCookie(response);
        }

        response.sendRedirect(ConfigKeys.GOOGLE_LOGIN);
    }
}
