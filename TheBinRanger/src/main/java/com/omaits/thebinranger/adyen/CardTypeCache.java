package com.omaits.thebinranger.adyen;

public interface CardTypeCache {
    /**
     * @param cardNumber 12 to 23 digit card number.
     *
     * @return the card type for this cardNumber or null if the card number does not
     *      fall into any valid bin ranges.
     */
    String get(String cardNumber);
}
