/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Marc de Verdelhan & respective authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j;


import org.joda.time.DateTime;
import org.jscience.mathematics.number.Real;

/**
 * End tick of a period.
 * <p>
 */
public class TickReal {

    private DateTime beginTime;

    private DateTime endTime;

    private Real openPrice = null;

    private Real closePrice = null;

    private Real maxPrice = null;

    private Real minPrice = null;

    private Real amount = Real.valueOf(0);

    private Real volume = Real.valueOf(0);

    private int trades = 0;

    /**
     * @param beginTime the begin time of the tick period
     * @param endTime the end time of the tick period
     */
    public TickReal(DateTime beginTime, DateTime endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    /**
     * @param endTime the end time of the tick period
     * @param openPrice the open price of the tick period
     * @param highPrice the highest price of the tick period
     * @param lowPrice the lowest price of the tick period
     * @param closePrice the close price of the tick period
     * @param volume the volume of the tick period
     */
    public TickReal(DateTime endTime, double openPrice, double highPrice, double lowPrice, double closePrice, double volume) {
        this(endTime, Real.valueOf(openPrice),
                Real.valueOf(highPrice),
                Real.valueOf(lowPrice),
                Real.valueOf(closePrice),
                Real.valueOf(volume));
    }

    /**
     * @param endTime the end time of the tick period
     * @param openPrice the open price of the tick period
     * @param highPrice the highest price of the tick period
     * @param lowPrice the lowest price of the tick period
     * @param closePrice the close price of the tick period
     * @param volume the volume of the tick period
     */
    public TickReal(DateTime endTime, Real openPrice, Real highPrice, Real lowPrice, Real closePrice, Real volume) {
        this.endTime = endTime;
        this.openPrice = openPrice;
        this.maxPrice = highPrice;
        this.minPrice = lowPrice;
        this.closePrice = closePrice;
        this.volume = volume;
    }

    /**
     * @return the close price of the period
     */
    public Real getClosePrice() {
        return closePrice;
    }

    /**
     * @return the open price of the period
     */
    public Real getOpenPrice() {
        return openPrice;
    }

    /**
     * @return the number of trades in the period
     */
    public int getTrades() {
        return trades;
    }

    /**
     * @return the max price of the period
     */
    public Real getMaxPrice() {
        return maxPrice;
    }

    /**
     * @return the whole traded amount of the period
     */
    public Real getAmount() {
        return amount;
    }

    /**
     * @return the whole traded volume in the period
     */
    public Real getVolume() {
        return volume;
    }

    /**
     * Adds a trade at the end of tick period.
     * @param tradeAmount the tradable amount
     * @param tradePrice the price
     */
    public void addTrade(double tradeAmount, double tradePrice) {
        addTrade(Real.valueOf(tradeAmount), Real.valueOf(tradePrice));
    }

    /**
     * Adds a trade at the end of tick period.
     * @param tradeAmount the tradable amount
     * @param tradePrice the price
     */
    public void addTrade(String tradeAmount, String tradePrice) {
        addTrade(Real.valueOf(tradeAmount), Real.valueOf(tradePrice));
    }

    /**
     * Adds a trade at the end of tick period.
     * @param tradeAmount the tradable amount
     * @param tradePrice the price
     */
    public void addTrade(Real tradeAmount, Real tradePrice) {
        if (openPrice == null) {
            openPrice = tradePrice;
        }
        closePrice = tradePrice;

        if (maxPrice == null) {
            maxPrice = tradePrice;
        } else {
            maxPrice = maxPrice.isLessThan(tradePrice) ? tradePrice : maxPrice;
        }
        if (minPrice == null) {
            minPrice = tradePrice;
        } else {
            minPrice = minPrice.isGreaterThan(tradePrice) ? tradePrice : minPrice;
        }
        amount = amount.plus(tradeAmount);
        volume = volume.plus(tradeAmount.times(tradePrice));
        trades++;
    }

    /**
     * @return the min price of the period
     */
    public Real getMinPrice() {
        return minPrice;
    }

    /**
     * @return the begin timestamp of the tick period
     */
    public DateTime getBeginTime() {
        return beginTime;
    }

    /**
     * @return the end timestamp of the tick period
     */
    public DateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("[time: %1$td/%1$tm/%1$tY %1$tH:%1$tM:%1$tS, close price: %2$f]", endTime
                .toGregorianCalendar(), closePrice);
    }

    /**
     * @param timestamp a timestamp
     * @return true if the provided timestamp is between the begin time and the end time of the current period, false otherwise
     */
    public boolean inPeriod(DateTime timestamp) {
        return timestamp == null ? false : (!timestamp.isBefore(beginTime) && timestamp.isBefore(endTime));
    }

    /**
     * @return a human-friendly string of the end timestamp
     */
    public String getDateName() {
        return endTime.toString("hh:mm dd/MM/yyyy");
    }

    /**
     * @return a even more human-friendly string of the end timestamp
     */
    public String getSimpleDateName() {
        return endTime.toString("dd/MM/yyyy");
    }
}