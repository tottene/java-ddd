package br.com.ctottene.catalog.domain.exceptions;

import br.com.ctottene.catalog.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
