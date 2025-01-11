package org.poo.exchangeRates;

import org.poo.fileio.ExchangeInput;

public class ExchangeRate {
    private String from;
    private String to;
    private double rate;
    private int timestamp;

    /**
     * Constructs a new {@code ExchangeRate} object with the specified source currency,
     * target currency, and exchange rate.
     *
     * @param from the currency code of the source currency.
     * @param to the currency code of the target currency.
     * @param rate the exchange rate from the source currency to the target currency.
     */
    public ExchangeRate(final String from, final String to, final double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    /**
     * Constructs a new {@code ExchangeRate} object by copying data from an
     * {@code ExchangeInput} object.
     *
     * @param other the {@code ExchangeInput} object containing exchange rate information.
     */
    public ExchangeRate(final ExchangeInput other) {
        this.from = other.getFrom();
        this.to = other.getTo();
        this.rate = other.getRate();
        this.timestamp = other.getTimestamp();
    }

    /**
     * Gets the source currency code.
     *
     * @return the source currency code.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the source currency code.
     *
     * @param from the source currency code to set.
     */
    public void setFrom(final String from) {
        this.from = from;
    }

    /**
     * Gets the target currency code.
     *
     * @return the target currency code.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the target currency code.
     *
     * @param to the target currency code to set.
     */
    public void setTo(final String to) {
        this.to = to;
    }

    /**
     * Gets the exchange rate from the source currency to the target currency.
     *
     * @return the exchange rate.
     */
    public double getRate() {
        return rate;
    }

    /**
     * Sets the exchange rate from the source currency to the target currency.
     *
     * @param rate the exchange rate.
     */
    public void setRate(final double rate) {
        this.rate = rate;
    }

    /**
     * Gets the timestamp at which the exchange rate was requested.
     *
     * @return the timestamp.
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp at which the exchange rate was requested.
     *
     * @param timestamp the timestamp.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }
}
