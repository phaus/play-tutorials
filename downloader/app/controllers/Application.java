package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import play.libs.F;
import play.libs.WS;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void startdownload(String url) {
        F.Promise<WS.HttpResponse> r1 = WS.url(url).getAsync();
        F.Promise<List<WS.HttpResponse>> promises = F.Promise.waitAll(r1);
        // Suspend processing here, until all three remote calls are complete.
        List<WS.HttpResponse> httpResponses = await(promises);

        render(httpResponses);
    }
}
