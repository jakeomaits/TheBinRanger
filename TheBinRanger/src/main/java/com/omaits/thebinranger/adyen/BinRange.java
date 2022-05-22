package com.omaits.thebinranger.adyen;

public class BinRange {
    public final String start;
    public final String end;
    public final String cardType;

    public BinRange(String start, String end, String cardType) {
        this.start = start;
        this.end = end;
        this.cardType = cardType;
    }
}
