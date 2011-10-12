package controllers;

import java.util.List;
import models.Event;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Events extends Controller {
    public static void index() {
        List<Event> entities = models.Event.all().fetch();
        render(entities);
    }

    public static void create(Event entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Event entity = Event.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Event entity = Event.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Event entity = Event.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Event entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Event"));
        index();
    }

    public static void update(@Valid Event entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Event"));
        index();
    }
}
