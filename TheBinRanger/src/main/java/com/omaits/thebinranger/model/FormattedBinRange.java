package com.omaits.thebinranger.model;

public class FormattedBinRange {
    public final int start;
    public final int end;
    public final String cardType;

    public FormattedBinRange(int start, int end, String cardType) {
        this.start = start;
        this.end = end;
        this.cardType = cardType;
    }
}
