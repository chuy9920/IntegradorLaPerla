package modelo;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

@SuppressWarnings("serial")
public class NotificationEvent extends Event {
	public static final EventType<NotificationEvent> NOTIFICATION_PRESSED = new EventType<NotificationEvent>(ANY, "NOTIFICATION_PRESSED");
    public static final EventType<NotificationEvent> SHOW_NOTIFICATION    = new EventType<NotificationEvent>(ANY, "SHOW_NOTIFICATION");
    public static final EventType<NotificationEvent> HIDE_NOTIFICATION    = new EventType<NotificationEvent>(ANY, "HIDE_NOTIFICATION");

    public final Notification NOTIFICATION;

    public NotificationEvent(final Notification NOTIFICATION, final Object SOURCE, final EventTarget TARGET, EventType<NotificationEvent> TYPE) {
        super(SOURCE, TARGET, TYPE);
        this.NOTIFICATION = NOTIFICATION;
    }
}