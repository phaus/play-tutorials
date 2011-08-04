package controllers;

import play.mvc.*;

import java.util.*;

import models.*;
import play.data.validation.Valid;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void list() {
        List<Bookmark> bookmarks = Bookmark.find("order by createdAt, title").fetch();
        render(bookmarks);
    }

    public static void form(Long id) {
        if(id == null) {
            render();
        }
        Bookmark bookmark = Bookmark.findById(id);
        render(bookmark);
    }

    public static void save(@Valid Bookmark bookmark) {
        if(validation.hasErrors()) {
            if(request.isAjax()) error("Invalid value");
            render("@form", bookmark);
        }
        bookmark.save();
        list();
    }
}