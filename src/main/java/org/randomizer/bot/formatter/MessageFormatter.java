package org.randomizer.bot.formatter;

public interface MessageFormatter<T> {

    String getFormattedMessage(T obj);

}
