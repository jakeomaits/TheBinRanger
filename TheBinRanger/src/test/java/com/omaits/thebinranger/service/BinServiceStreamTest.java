package com.omaits.thebinranger.service;

import com.omaits.thebinranger.adyen.BinRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinServiceStreamTest {

    List<BinRange> smallBinRangeList;
    List<BinRange> largeBinRangeList;
    BinRange binRange;

    BinServiceStream binServiceStream;

    @BeforeEach
    void setUp() {
        smallBinRangeList = new ArrayList<BinRange>();

        //create a simple bin range list useful for debugging
        binRange = new BinRange("3000000033", "3999999999", "three");
        smallBinRangeList.add(binRange);
        binRange = new BinRange("40000000", "49999999", "four");
        smallBinRangeList.add(binRange);
        binRange = new BinRange("10000000", "19999999", "one");
        smallBinRangeList.add(binRange);
        binRange = new BinRange("50000000", "59999999", "five");
        smallBinRangeList.add(binRange);
        binRange = new BinRange("20000000", "29999999", "two");
        smallBinRangeList.add(binRange);
        binRange = new BinRange("30000000", "39999999", "three");
        smallBinRangeList.add(binRange);

        //create a second list that contains a bunch of bin ranges.
        //this will be used to test for performance
        int increment = 12345;
        largeBinRangeList = new ArrayList<BinRange>();
        for(int i = 10000000; i < 90999999; i = i + increment + 1)
        {
            binRange = new BinRange(String.valueOf(i), String.valueOf(i + increment), String.valueOf(i));
            largeBinRangeList.add(binRange);
        }

        System.out.println("done");
    }

    @Test
    void get_57124101_fiveReturned() {
        binServiceStream = new BinServiceStream(smallBinRangeList);
        assertEquals("five", binServiceStream.get("57124101000000"));
    }

    @Test
    void get_27124101_twoReturned() {
        binServiceStream = new BinServiceStream(smallBinRangeList);
        assertEquals("two", binServiceStream.get("27124101000000"));
    }

    @Test
    void get_37124101_37124101Returned() {
        binServiceStream = new BinServiceStream(smallBinRangeList);
        assertEquals("three", binServiceStream.get("37124101000000"));
    }

    @Test
    void get_bigBinRangeList_lastItemReturned() {
        binServiceStream = new BinServiceStream(largeBinRangeList);
        assertEquals(largeBinRangeList.get(largeBinRangeList.size() - 1).cardType, binServiceStream.get(largeBinRangeList.get(largeBinRangeList.size() -1).start + 1));
    }

}