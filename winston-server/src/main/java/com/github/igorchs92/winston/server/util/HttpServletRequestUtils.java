package com.github.igorchs92.winston.server.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Igor on 1-6-2017.
 */
public abstract class HttpServletRequestUtils {

    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

}
