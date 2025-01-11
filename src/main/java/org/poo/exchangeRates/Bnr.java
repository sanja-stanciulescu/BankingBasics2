package org.poo.exchangeRates;

import org.poo.fileio.ObjectInput;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Bnr {
    private ArrayList<ExchangeRate> exchangeRates;

    /**
     * Constructs a new {@code Bnr} instance and initializes the list of exchange rates.
     */
    public Bnr() {
        exchangeRates = new ArrayList<>();
    }

    /**
     * Sets up the exchange rates by parsing the provided input data and populating
     * the exchange rates list.
     *
     * @param inputData the input data containing exchange rates to be set up.
     */
    public void setUp(final ObjectInput inputData) {
        int length;

        if (inputData.getExchangeRates() != null) {
            length = inputData.getExchangeRates().length;
        } else {
            length = 0;
        }
        for (int i = 0; i < length; i++) {
            exchangeRates.add(new ExchangeRate(inputData.getExchangeRates()[i]));
        }

        coverAllExchangeRates();
    }

    /**
     * Creates reciprocal exchange rates for all existing exchange rates.
     * For example, if there is an exchange rate from "USD" to "EUR", this method will
     * add a reverse rate from "EUR" to "USD".
     */
    private void coverAllExchangeRates() {
        int size = exchangeRates.size();
        for (int i = 0; i < size; i++) {
            ExchangeRate excR = exchangeRates.get(i);
            exchangeRates.add(new ExchangeRate(excR.getTo(), excR.getFrom(), 1 / excR.getRate()));
        }
    }

    /**
     * Retrieves the exchange rate between two currencies.
     * If no direct exchange rate exists, it tries to find a path through other currencies.
     *
     * @param from the currency code of the source currency.
     * @param to the currency code of the target currency.
     * @return the exchange rate between the two currencies, or -1 if no conversion is possible.
     */
    public double getExchangeRate(final String from, final String to) {
        Queue<ExchangeRateNode> queue = new LinkedList<>();
        ArrayList<String> visited = new ArrayList<>();

        queue.add(new ExchangeRateNode(from, 1.0));
        visited.add(from);

        while (!queue.isEmpty()) {
            ExchangeRateNode currentNode = queue.poll();
            String currentCurrency = currentNode.getCurrency();
            double currentRate = currentNode.getRate();

            if (currentCurrency.equals(to)) {
                return currentRate;
            }

            for (ExchangeRate rate : exchangeRates) {
                if (rate.getFrom().equals(currentCurrency) && !visited.contains(rate.getTo())) {
                    visited.add(rate.getTo());
                    queue.add(new ExchangeRateNode(rate.getTo(), currentRate * rate.getRate()));
                }
            }
        }

        return -1;
    }

    /**
     * Gets the list of all exchange rates currently stored in the {@code Bnr}.
     *
     * @return the list of exchange rates.
     */
    public ArrayList<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    /**
     * Sets the list of exchange rates.
     *
     * @param exchangeRates the new list of exchange rates to be set.
     */
    public void setExchangeRates(final ArrayList<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

}
