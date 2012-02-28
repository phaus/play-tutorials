package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import jobs.VideoScanner;

import models.*;

public class Application extends Controller {

    private static VideoScanner scanner = new VideoScanner();

    public static void index() {
        List<Video> videos = Video.all().fetch();
        render(videos);
    }

    public static void scan() {
        scanner.now();
        index();
    }
}
