package controllers;

import helper.EventTimeHelper;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        EventTimeHelper.getEventTimeFromString("[29/Sep/2011:01:21:45 +0200]");
        EventTimeHelper.getEventTimeFromString("Sep 29 01:52:41");
        EventTimeHelper.getEventTimeFromString("Sep 28 06:29:38");
        EventTimeHelper.getEventTimeFromString("[31/May/2010:17:05:18 +0200]");
        EventTimeHelper.getEventTimeFromString("[12/Apr/2009:23:21:45 +0200]");
        EventTimeHelper.getEventTimeFromString("Jan 30 01:52:41");
        EventTimeHelper.getEventTimeFromString("Feb 01 06:29:38");
        EventTimeHelper.getEventTimeFromString("[31/May/2010:17:05:18 +0200]");
        render();
    }

}