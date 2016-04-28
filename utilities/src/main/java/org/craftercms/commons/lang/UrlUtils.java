package org.craftercms.commons.lang;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility methods for paths and URLs.
 *
 * @author avasquez
 */
public class UrlUtils {

    private UrlUtils() {
    }

    /**
     * Concats two urls, adding any "/" needed between them.
     *
     * @param mainUrl      the main url
     * @param relativeUrl  the relative url
     *
     * @return mainPath + relativeUrl
     */
    public static final String concat(String mainUrl, String relativeUrl) {
        if (StringUtils.isEmpty(mainUrl)) {
            return relativeUrl;
        } else if (StringUtils.isEmpty(relativeUrl)) {
            return mainUrl;
        } else {
            StringBuilder joinedUrl = new StringBuilder(mainUrl);

            if (mainUrl.endsWith("/") && relativeUrl.startsWith("/")) {
                relativeUrl = StringUtils.stripStart(relativeUrl, "/");
            } else if (!mainUrl.endsWith("/") && !relativeUrl.startsWith("/")) {
                joinedUrl.append("/");
            }

            joinedUrl.append(relativeUrl);

            return joinedUrl.toString();
        }
    }

    /**
     * Adds a query string param to the URL, adding a '?' if there's no query string yet.
     *
     * @param url       the URL
     * @param name      the name of the param
     * @param value     the value of the param
     * @param charset   the charset to encode the param key/value with
     *
     * @return the URL with the query string param appended
     */
    public static String addParam(String url, String name, String value,
                                  String charset) throws UnsupportedEncodingException {
        StringBuilder newUrl = new StringBuilder(url);

        if (!url.endsWith("?") && !url.endsWith("&")) {
            if (url.contains("?")) {
                newUrl.append('&');
            } else {
                newUrl.append('?');
            }
        }

        newUrl.append(URLEncoder.encode(name, charset));
        newUrl.append('=');
        newUrl.append(URLEncoder.encode(value, charset));

        return newUrl.toString();
    }

    /**
     * Adds a query string fragment to the URL, adding a '?' if there's no query string yet.
     *
     * @param url       the URL
     * @param fragment  the query string fragment
     *
     * @return the URL with the query string fragment appended
     */
    public static String addQueryStringFragment(String url, String fragment) {
        StringBuilder newUrl = new StringBuilder(url);

        if (fragment.startsWith("?") || fragment.startsWith("&")) {
            fragment = fragment.substring(1);
        }

        if (!url.endsWith("?") && !url.endsWith("&")) {
            if (url.contains("?")) {
                newUrl.append('&');
            } else {
                newUrl.append('?');
            }
        }

        return newUrl.append(fragment).toString();
    }


}
