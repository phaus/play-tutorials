package controllers;

import java.util.List;
import models.VoteEntry;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class VoteEntrys extends Controller {
    public static void index() {
        List<VoteEntry> entities = models.VoteEntry.all().fetch();
        render(entities);
    }

    public static void create(VoteEntry entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        VoteEntry entity = VoteEntry.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        VoteEntry entity = VoteEntry.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        VoteEntry entity = VoteEntry.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid VoteEntry entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "VoteEntry"));
        index();
    }

    public static void update(@Valid VoteEntry entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "VoteEntry"));
        index();
    }
}
