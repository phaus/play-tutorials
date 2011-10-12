package controllers;

import java.util.List;
import models.Voter;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Voters extends Controller {
    public static void index() {
        List<Voter> entities = models.Voter.all().fetch();
        render(entities);
    }

    public static void create(Voter entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Voter entity = Voter.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Voter entity = Voter.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Voter entity = Voter.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Voter entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Voter"));
        index();
    }

    public static void update(@Valid Voter entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Voter"));
        index();
    }
}
