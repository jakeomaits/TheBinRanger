package com.omaits.thebinranger.service;

import com.omaits.thebinranger.adyen.BinRange;
import com.omaits.thebinranger.adyen.CardTypeCache;
import com.omaits.thebinranger.model.FormattedBinRange;
import com.omaits.thebinranger.utility.Logger;
import com.omaits.thebinranger.utility.StringUtility;
import org.springframework.boot.logging.LogLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static org.apache.logging.log4j.util.Strings.isEmpty;

public class BinServiceOriginal implements CardTypeCache {

    TreeMap<Integer, FormattedBinRange> formattedBinRanges;
    List<BinRange> binRanges;
    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public BinServiceOriginal(List<BinRange> binRanges)
    {
        this.binRanges = binRanges;
    }

    private void log(LogLevel logLevel, String message)
    {
        //this is here for illustrative purposes. don't do anything at this time.
    }

    @Override
    public String get(String cardNumber) {
        //loop through each bin range - this is far from ideal but will work OK for the purposes of this test. to optimize, the ranges should be stored in a format which is most conducive to this type of evaluation.
        //logger.log(LoggingLevel.Debug, "Evaluating card number *just kidding* against X bin ranges");
        for(int i = 0; i< binRanges.size(); i++)
        {
            //pad the bin range start to be 16 digits
            Long start = Long.parseLong(rightPad(binRanges.get(i).start, cardNumber.length(), "0")); //use cardnumber.length instead of 16 to account for bin ranges <> 16

            //pad the bin range end to be 16 digits
            Long end = Long.parseLong(rightPad(binRanges.get(i).end, cardNumber.length(), "9"));

            //make card num numeric so we can evaluate it against the start and end
            Long cardNum = Long.parseLong(cardNumber);

            //check to see if this card falls in this range
            if (cardNum <= end && cardNum >= start)
            {
                //logger.log(LoggingLevel.Debug, "card number *just kidding* matches bin X");
                //we have a match. return the card type that matches
                return binRanges.get(i).cardType;
            }
        }
        //a card number which is not supported was provided.
        //logger.log(LoggingLevel.Error, "An invalid card type was encountered");
        //throw new Exception("throw some not found exception here");
        return null;
    }

    public static String rightPad(String input, int howMany, String paddingChar) {
        return String.format("%1$-" + howMany + "s", input).replace(' ', paddingChar.charAt(0));
    }

}
