package org.personalized.dashboard.auth;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.dao.impl.SessionDaoImpl;
import org.personalized.dashboard.service.api.SessionService;
import org.personalized.dashboard.service.impl.SessionServiceImpl;
import org.personalized.dashboard.utils.Constants;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * Created by sudan on 26/7/15.
 */
public class RequestInterceptor implements MethodInterceptor {

    // TODO another ugly initialization  plz find way :(
    private SessionService sessionService = new SessionServiceImpl(new SessionDaoImpl());
    private SessionManager sessionManager = new SessionManager();

    @Context
    private HttpHeaders httpHeaders;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Object[] params = methodInvocation.getArguments();
        for (Object param : params) {
            if (param instanceof HttpHeaders) {
                HttpHeaders httpHeaders = (HttpHeaders) param;
                String sessionId = StringUtils.EMPTY;
                if (!CollectionUtils.isEmpty(httpHeaders.getRequestHeader(Constants.AUTH_HEADER))) {
                    sessionId = httpHeaders.getRequestHeader(Constants.AUTH_HEADER).get(0);
                }
                if (StringUtils.isEmpty(sessionId)) {
                    Cookie cookie = httpHeaders.getCookies().get(Constants.COOKIE_NAME);
                    if (cookie != null) {
                        sessionId = httpHeaders.getCookies().get(Constants.COOKIE_NAME).getValue();
                    }
                }
                String userId = sessionService.getUserId(sessionId);
                if (StringUtils.isEmpty(userId)) {
                    break;
                }
                sessionManager.setUserId(userId);
                return methodInvocation.proceed();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
