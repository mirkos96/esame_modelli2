package org.personalized.dashboard.utils.htmltidy;


import org.jsoup.Jsoup;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

/**
 * Created by sudan on 18/7/15.
 */
public class DOMParser {

    public static final PolicyFactory FONT_TAGS = new HtmlPolicyBuilder().allowElements("font")
            .allowAttributes("face", "size")
            .onElements("font")
            .toFactory();

    public String removeMalformedTags(String html) {

        return (Sanitizers.FORMATTING.and(Sanitizers.BLOCKS).and(Sanitizers.IMAGES)
                .and(Sanitizers.STYLES).and(Sanitizers.IMAGES).and(FONT_TAGS)
                .sanitize(html));
    }

    public String extractSummary(String html) {
        String content = this.removeHtmlTags(html);
        return content.substring(0, Math.min(content.length(), 150));
    }

    public String removeHtmlTags(String html) {
        return Jsoup.parse(html).text();
    }

}
