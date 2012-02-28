/**
 * Video
 * 28.02.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import play.db.jpa.Model;

@Entity
public class Video extends Model {

    public String filename;
    public long duration;
    public int width;
    public int height;
    @ManyToMany
    Set<Tag> tags;

    public Video() {
        this.tags = new HashSet<Tag>();
    }

    public static Video findOrCreateByFilename(String filename) {
        Video video = Video.find(" filename = ?", filename).first();
        if (video == null) {
            video = new Video();
            video.filename = filename;
            return video.save();
        }
        return video;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(filename);
        sb.append(" {");
        for (Tag t : tags) {
            sb.append(t);
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }
}
