// time object

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time implements Comparable<Time> {
    private int minutes;
    private int seconds;
    private int milliseconds;
    
    /**
     * Constructs a Time object given a string.
     * @param timeStr a string representing a Time object, in the format minutes:seconds:milliseconds
     * @throws IllegalArgumentException if the provided string is not in the expected format
     */
    public Time(String timeStr) throws IllegalArgumentException {
        Pattern timePattern = Pattern.compile("[0-9]{2,}:[0-5][0-9]\\.[0-9]{3}");
        Matcher timeMatch = timePattern.matcher(timeStr);

        if (timeMatch.matches()) {
            int colon = timeStr.indexOf(':');
            int period = timeStr.indexOf('.');
    
            int minutes = Integer.parseInt(timeStr.substring(0, colon));
            int seconds = Integer.parseInt(timeStr.substring(colon + 1, period));
            int milliseconds = Integer.parseInt(timeStr.substring(period + 1));
    
            this.minutes = minutes;
            this.seconds = seconds;
            this.milliseconds = milliseconds;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Constructs a new Time object with the value given in milliseconds. Does not allow time values exceeding one hour, nor negative time values.
     * @param milliseconds length of time, in milliseconds
     * @throws ParseException if the provided millisecond value exceeds one hour or is less than zero.
     */
    public Time(long milliseconds) throws IllegalArgumentException {
        if (!(milliseconds >= 3600000 || milliseconds < 0)) {
            int newMilliseconds = Math.toIntExact(milliseconds % 1000);
            long newSeconds = milliseconds / 1000;
    
            this.milliseconds = newMilliseconds;
    
            int newMinutes = Math.toIntExact(newSeconds / 60);
            int newSecondsInt = Math.toIntExact(newSeconds % 60);

            this.milliseconds = newMilliseconds;
            this.seconds = newSecondsInt;
            this.minutes = newMinutes;
        } else {
            throw new IllegalArgumentException();
        } 
    }
    
    /**
     * Constructs a Time object given the values for minutes, seconds, and milliseconds
     */
    public Time(int minutes, int seconds, int milliseconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
    }

    /**
     * Recalculates the correct time if the current numbers are invalid (i.e. seconds/minutes >= 60)
     */
    public void recalculateTime() {
        int extraSeconds = 0;
        int newMS = 0;

        if (this.milliseconds >= 1000) {
            newMS = this.milliseconds % 1000;
            extraSeconds = this.seconds / 1000;
            this.milliseconds = newMS;
            this.seconds += extraSeconds;
        }
        
        int extraMinutes = 0;
        int newSeconds = 0;

        if (this.minutes >= 60) {
            newSeconds = this.minutes % 60;
            extraMinutes = this.minutes / 60;
            this.seconds = newSeconds;
            this.minutes += extraMinutes;
        }
    }

    /**
     * Adds the given time object to the current time object
     * @param t2 the time object to add
     * @return the two times added together
     */
    public void add(Time t2) {
        this.milliseconds = this.milliseconds + t2.getMilliseconds();
        this.seconds = this.seconds + t2.getSeconds();
        this.minutes = this.minutes + t2.getMinutes();

        this.recalculateTime();
        return;
    }

    /**
     * Adds two provided time objects
     * @param t1 the first time object to add
     * @param t2 the second time object to add
     * @return the two times added together
     */
    public Time add(Time t1, Time t2) {
        int newMilliseconds = t1.getMilliseconds() + t2.getMilliseconds();
        int newSeconds = t1.getSeconds() + t2.getSeconds();
        int newMinutes = t1.getMinutes() + t2.getMinutes();
        
        Time result = new Time(newMinutes, newSeconds, newMilliseconds);
        result.recalculateTime();

        return result;
    }

    @Override
    public int compareTo(Time t2) {
        // Kinda unreadable, not gonna lie
        int minutesCompare = this.getMinutes() - t2.getMinutes();
        if (minutesCompare == 0) {
            int secondsCompare = this.getSeconds() - t2.getSeconds();
            if (secondsCompare == 0) {
                int millisecondsCompare = this.getMilliseconds() - t2.getMilliseconds();
                return millisecondsCompare;
            } else {
                return secondsCompare;
            }
        } else {
            return minutesCompare;
        }
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }
}
