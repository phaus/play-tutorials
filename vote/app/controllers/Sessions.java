package controllers;

import java.util.List;
import models.Session;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Sessions extends Controller {
    public static void index() {
        List<Session> entities = models.Session.all().fetch();
        render(entities);
    }

    public static void create(Session entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Session entity = Session.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Session entity = Session.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Session entity = Session.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Session entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Session"));
        index();
    }

    public static void update(@Valid Session entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Session"));
        index();
    }
}
