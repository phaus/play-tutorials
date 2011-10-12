package controllers;

import java.util.List;
import models.RangeVoteEntry;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class RangeVoteEntrys extends Controller {
    public static void index() {
        List<RangeVoteEntry> entities = models.RangeVoteEntry.all().fetch();
        render(entities);
    }

    public static void create(RangeVoteEntry entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        RangeVoteEntry entity = RangeVoteEntry.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        RangeVoteEntry entity = RangeVoteEntry.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        RangeVoteEntry entity = RangeVoteEntry.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid RangeVoteEntry entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "RangeVoteEntry"));
        index();
    }

    public static void update(@Valid RangeVoteEntry entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "RangeVoteEntry"));
        index();
    }
}
