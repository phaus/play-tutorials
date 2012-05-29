package controllers;

import java.util.Map;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Header;

public class Application extends Controller {

    @Before
    private static void checkSSL() {
        if (request.headers.get("x_forwarded_proto") != null
                && "https".equals(request.headers.get("x_forwarded_proto").value())) {
            request.secure = true;
            request.port = 443;
        }
        if (request.headers.get("x-forwarded-server") != null) {
            request.domain = request.headers.get("x-forwarded-server").value();
        }
    }

    public static void index() {
        String headers = headersToString(request.headers);
        render(headers);
    }

    public static void redirect() {
        index();
    }

    private static String headersToString(Map<String, Header> headers) {
        StringBuilder sb = new StringBuilder();
        for (String key : headers.keySet()) {
            sb.append("<li>").append(key).append("=").append(headers.get(key)).append("</li>");
        }
        return sb.toString();
    }
}
