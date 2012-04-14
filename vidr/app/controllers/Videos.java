package controllers;

import java.util.List;
import models.Video;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Videos extends Controller {
    public static void index() {
        List<Video> entities = models.Video.all().fetch();
        render(entities);
    }

    public static void create(Video entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Video entity = Video.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Video entity = Video.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Video entity = Video.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Video entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Video"));
        index();
    }

    public static void update(@Valid Video entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Video"));
        index();
    }
}
