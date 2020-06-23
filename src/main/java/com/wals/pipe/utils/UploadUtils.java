package com.wals.pipe.utils;

import java.net.URI;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-05-06 21:39:00
 */
public class UploadUtils {

    public static URI getHost(URI uri) {
        URI effectiveURI = null;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (Throwable var4) {
            effectiveURI = null;
        }
        return effectiveURI;
    }
}