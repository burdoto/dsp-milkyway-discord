package org.comroid.dcb.dspmw.milkyway.endpoint;

import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;

//http://8.140.162.132/login/header?user_id=<steam_id>
//http://8.140.162.132/download/<login_header[1]>
public enum MwEndpoint implements AccessibleEndpoint {
    LOGIN_HEADER("login/header?user_id=%s", "\\d+"),
    DOWNLOAD("download/%s", "\\d+");
    @SuppressWarnings("HttpUrlsUsage")
    public static final String BASE_URL = "http://8.140.162.132/";
    private final String extension;
    private final String[] regExp;
    private final Pattern pattern;

    @Override
    public String getUrlBase() {
        return BASE_URL;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String getUrlExtension() {
        return extension;
    }

    @Override
    public String[] getRegExpGroups() {
        return regExp;
    }

    MwEndpoint(String extension, @Language("RegExp") String... regExp) {
        this.extension = extension;
        this.regExp = regExp;
        this.pattern = buildUrlPattern();
    }
}
