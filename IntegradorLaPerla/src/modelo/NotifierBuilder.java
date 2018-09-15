package modelo;

import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashMap;

public class NotifierBuilder<B extends NotifierBuilder<B>> {
    @SuppressWarnings("rawtypes")
	private HashMap<String, Property> properties = new HashMap<>();

    protected NotifierBuilder() {}

    @SuppressWarnings("rawtypes")
	public final static NotifierBuilder create() {
        return new NotifierBuilder();
    }

    @SuppressWarnings("unchecked")
	public final B owner(final Stage OWNER) {
        properties.put("stage", new SimpleObjectProperty<>(OWNER));
        return (B)this;
    }

    @SuppressWarnings("unchecked")
	public final B popupLocation(Pos LOCATION) {
        properties.put("popupLocation", new SimpleObjectProperty<>(LOCATION));
        return (B)this;
    }

    @SuppressWarnings("unchecked")
	public final B width(final double WIDTH) {
        properties.put("width", new SimpleDoubleProperty(WIDTH));
        return (B) this;
    }

    @SuppressWarnings("unchecked")
	public final B height(final double HEIGHT) {
        properties.put("height", new SimpleDoubleProperty(HEIGHT));
        return (B) this;
    }

    @SuppressWarnings("unchecked")
	public final B spacingY(final double SPACING_Y) {
        properties.put("spacingY", new SimpleDoubleProperty(SPACING_Y));
        return (B) this;
    }

    @SuppressWarnings("unchecked")
	public final B popupLifeTime(final Duration POPUP_LIFETIME) {
        properties.put("popupLifeTime", new SimpleObjectProperty<>(POPUP_LIFETIME));
        return (B) this;
    }

    @SuppressWarnings("unchecked")
	public final B popupAnimationTime(final Duration POPUP_ANIMATION_TIME) {
        properties.put("popupAnimationTime", new SimpleObjectProperty<>(POPUP_ANIMATION_TIME));
        return (B) this;
    }

    @SuppressWarnings("unchecked")
	public final B styleSheet(final String STYLE_SHEET) {
        properties.put("styleSheet", new SimpleStringProperty(STYLE_SHEET));
        return (B) this;
    }

    @SuppressWarnings({ "static-access", "unchecked" })
	public final Notification.Notifier build() {
        final Notification.Notifier NOTIFIER = Notification.Notifier.INSTANCE;
        for (String key : properties.keySet()) {
            if ("owner".equals(key)) {
                NOTIFIER.setNotificationOwner(((ObjectProperty<Stage>) properties.get(key)).get());
            } else if ("popupLocation".equals(key)) {
                NOTIFIER.setPopupLocation(null, ((ObjectProperty<Pos>) properties.get(key)).get());
            } else if ("width".equals(key)) {
                NOTIFIER.setWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("height".equals(key)) {
                NOTIFIER.setHeight(((DoubleProperty) properties.get(key)).get());
            } else if ("spacingY".equals(key)) {
                NOTIFIER.setSpacingY(((DoubleProperty) properties.get(key)).get());
            } else if ("popupLifeTime".equals(key)) {
                NOTIFIER.setPopupLifetime(((ObjectProperty<Duration>) properties.get(key)).get());
            } else if ("popupAnimationTime".equals(key)) {
                NOTIFIER.setPopupAnimationTime(((ObjectProperty<Duration>) properties.get(key)).get());
            } else if ("styleSheet".equals(key)) {
                NOTIFIER.setStyleSheet(((StringProperty) properties.get(key)).get());
            }
        }
        return NOTIFIER;
    }
}
