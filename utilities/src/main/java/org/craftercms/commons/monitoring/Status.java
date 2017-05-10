package org.craftercms.commons.monitoring;


import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Get's current basic JVM statusLabel with a custom statusLabel label (default's to `healthy`)
 * @since 3.0
 * @author Carlos Ortiz.
 */
public final class Status {

    /**
     * Default Status Label name.
     */
    private final static String DEFAULT_STATUS_LABEL ="healthy";
    private String statusLabel;
    private String uptime;
    private String datetime;
    private final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ssZ");
    /**
     * Creates the Status with current information and a custom statusLabel label.
     * @param statusLabel Custom statusLabel label.
     */
    private Status(String statusLabel){
        long uptimeInMS = ManagementFactory.getRuntimeMXBean().getUptime();
        this.statusLabel = statusLabel;
        uptime=String.format("%sh %sm %ss", TimeUnit.MILLISECONDS.toHours(uptimeInMS),
                TimeUnit.MILLISECONDS.toMinutes(uptimeInMS),
                TimeUnit.MILLISECONDS.toSeconds(uptimeInMS));
        datetime=DATETIME_FORMATTER.format(new Date());
    }


    /**
     * Creates the Status with current information and a custom statusLabel label.
     * @param statusLabel Custom statusLabel label.
     */
    public static Status getCurrentStatus(String statusLabel){
        return new Status(statusLabel);
    }
    /**
     * Creates the Status with current information and a custom statusLabel label.
     * @param statusLabel Custom statusLabel label.
     */
    public static Status getCurrentStatus(){
        return new Status(DEFAULT_STATUS_LABEL);
    }
    public String getStatus() {
        return statusLabel;
    }

    public String getUptime() {
        return uptime;
    }

    public String getDatetime() {
        return datetime;
    }

    @Override
    public String toString() {
        return "Status{" +
                "statusLabel='" + statusLabel + '\'' +
                ", uptime='" + uptime + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
