package org.pyatsotok.bot.formatter;

public interface MessageFormatter<T> {

    String getFormattedMessage(T obj);

}
