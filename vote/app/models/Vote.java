/**
 * Vote
 * 29.07.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class Vote extends Model {
    @ManyToOne
    public Session session;
    @ManyToOne
    public Voter voter;
    @OneToMany(mappedBy="vote")
    public List <VoteEntry> voteEntries;

    public String comment;

    public static Vote defaultVoteFactory(){
        return new Vote();
    }
}
