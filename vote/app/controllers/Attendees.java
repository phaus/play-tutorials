package controllers;

import java.util.List;
import models.Attendee;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Attendees extends Controller {
    public static void index() {
        List<Attendee> entities = models.Attendee.all().fetch();
        render(entities);
    }

    public static void create(Attendee entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Attendee entity = Attendee.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Attendee entity = Attendee.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Attendee entity = Attendee.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Attendee entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Attendee"));
        index();
    }

    public static void update(@Valid Attendee entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Attendee"));
        index();
    }
}
