package org.personalized.dashboard.bootstrap;

import com.googlecode.googleplus.GooglePlusFactory;
import org.personalized.dashboard.utils.ConfigKeys;
import org.springframework.social.oauth2.OAuth2Parameters;

/**
 * Created by sudan on 26/7/15.
 */
public class OAuthBootstrap {

    private static GooglePlusFactory googlePlusFactory;
    private static OAuth2Parameters oAuth2Parameters;
    private static boolean isInitialized = false;

    public static void init() {

        if (!isInitialized) {

            googlePlusFactory = new GooglePlusFactory(ConfigKeys.GOOGLE_APP_ID, ConfigKeys.GOOGLE_APP_SECRET);
            oAuth2Parameters = new OAuth2Parameters();
            oAuth2Parameters.setRedirectUri(ConfigKeys.GOOGLE_REDIRECT_URI);
            oAuth2Parameters.setScope(ConfigKeys.GOOGLE_SCOPE);
            isInitialized = true;
        }
    }

    public static GooglePlusFactory getGooglePlusFactory() {
        return googlePlusFactory;
    }

    public static OAuth2Parameters getoAuth2Parameters() {
        return oAuth2Parameters;
    }
}
