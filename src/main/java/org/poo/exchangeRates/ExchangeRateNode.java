package org.poo.exchangeRates;

public class ExchangeRateNode {
    private String currency;
    private double rate;

    /**
     * Constructs a new {@code ExchangeRateNode} with the specified currency and exchange rate.
     *
     * @param currency the currency code.
     * @param rate the exchange rate of the currency.
     */
    public ExchangeRateNode(final String currency, final double rate) {
        this.currency = currency;
        this.rate = rate;
    }

    /**
     * Gets the currency code associated with this node.
     *
     * @return the currency code.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency code for this node.
     *
     * @param currency the currency code to set.
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Gets the exchange rate of the currency relative to a base currency.
     *
     * @return the exchange rate.
     */
    public double getRate() {
        return rate;
    }

    /**
     * Sets the exchange rate of the currency relative to a base currency.
     *
     * @param rate the exchange rate to set.
     */
    public void setRate(final double rate) {
        this.rate = rate;
    }
}
