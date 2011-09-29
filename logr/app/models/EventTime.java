/**
 * EventTime
 * 29.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.cache.Cache;
import play.db.jpa.Model;

@Entity
public class EventTime extends Model {

    @OneToMany(mappedBy = "time")
    public List<Event> events;

    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;
    long milliseond;

    public EventTime(){}
    public EventTime(int year, int month, int day, int hour, int minute, int second, long milliseond){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.milliseond = milliseond;
    }
    public static EventTime findOrCreateWith(int year, int month, int day, int hour, int minute, int second, long milliseond){
        String key = "EventTime-"+year+"-"+month+"-"+day+"-"+minute+"-"+second+"-"+milliseond;
        EventTime eventTime = Cache.get(key, EventTime.class);
        if(eventTime != null){
            return eventTime;
        }
        eventTime = EventTime.find(" year = ? and month = ? and day = ? and hour = ? and minute = ? and second = ? and milliseond = ? ",
                year,
                month,
                day,
                hour,
                minute,
                second,
                milliseond).first();
        if(eventTime == null){
            eventTime = new EventTime(year, month, day, hour, minute, second, milliseond);
            eventTime.save();
            Cache.set(key, eventTime, "1d");
        }
        return eventTime;
    }
}
