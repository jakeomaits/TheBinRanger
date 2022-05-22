package com.omaits.thebinranger.service;

import com.omaits.thebinranger.adyen.BinRange;
import com.omaits.thebinranger.adyen.CardTypeCache;
import com.omaits.thebinranger.model.FormattedBinRange;
import com.omaits.thebinranger.utility.Logger;
import com.omaits.thebinranger.utility.StringUtility;
import org.springframework.boot.logging.LogLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static org.apache.logging.log4j.util.Strings.isEmpty;

public class BinServiceStream implements CardTypeCache {

    //use a treemap so it is in natural order by key
    TreeMap<Integer, FormattedBinRange> formattedBinRangesTreeMap;
    ArrayList<FormattedBinRange> formattedBinRanges;

    private final String UNKNOWN_BIN = "unknown";

    /**
     * Constructor will accept a list of bin ranges. It will validate and load into a formatted TreeMap which will be re-used for lookups. This would be cached on the app server but for purpose of our test, we will simply reuse this instance of the class.
     * @param binRanges: A list of bin ranges which should be
     */
    public BinServiceStream(List<BinRange> binRanges) {

        //declare an array and load bin numbers.
        //i am using a TreeMap to load the array in sorted order.
        formattedBinRangesTreeMap = new TreeMap<Integer, FormattedBinRange>();

        for(int i = 0; i< binRanges.size(); i++) {
            if (!StringUtility.isNumeric(binRanges.get(i).start) || !StringUtility.isNumeric(binRanges.get(i).end)) {
                Logger.log(LogLevel.ERROR, String.format("The following bin range is invalid and will be skipped. The start and/or end are not numeric: % - %", binRanges.get(i).start, binRanges.get(i).end));
            }

            //validate bin Range
            //effective 4/22, bins will become 8 digits. we will assume provided bin ranges are given in a standard 8 digit form.
            if (binRanges.get(i).start.length() >= 8 || binRanges.get(i).end.length() >= 8)
            {
                //add a 0 on the start bin and 9 on the end bin. This will result in a 10 digit number.
                FormattedBinRange formattedBinRange = new FormattedBinRange(Integer.parseInt(binRanges.get(i).start.substring(0, 8) + "0"), Integer.parseInt(binRanges.get(i).end.substring(0, 8) + "9"), binRanges.get(i).cardType);

                formattedBinRangesTreeMap.put(formattedBinRange.start, formattedBinRange);
            }
            else
            {
                //an invalid range was provided. Start and end of the range should be 8 digits
                Logger.log(LogLevel.ERROR, String.format("The following bin range is invalid and will be skipped. The start and/or end are not 8 digits: % - %", binRanges.get(i).start, binRanges.get(i).end));
            }
        }

        //copy to a hashmap which can be accessed via index.
        //todo: one simplification could be to use an arraylist originally and sort rather than the treemap/copy approach
        formattedBinRanges = new ArrayList<FormattedBinRange>(formattedBinRangesTreeMap.values());
    }

    @Override
    public String get(String cardNumber) {

        //do some initial validation. return card type that indicates the bin is unknown.
        if (isEmpty(cardNumber) || cardNumber.length() < 8 || !StringUtility.isNumeric(cardNumber))
        {
            return UNKNOWN_BIN;
        }

        //just get the first 8. we dont need the whole card number.
        //no sense in keeping it in case someone accidentally logs it.
        //add a 0 to the end to ensure our truncated card number is comporable against a bin which is 10 digits.
        Integer cardPrefix = Integer.parseInt(cardNumber.substring(0, 8) + "0");

       Optional<FormattedBinRange> binRange = formattedBinRanges.stream().filter(range -> cardPrefix >= range.start && cardPrefix <= range.end).findAny();

       if (binRange.isPresent())
       {
            return binRange.get().cardType;
       }
       else
       {
           return UNKNOWN_BIN;
       }

    }

}
