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
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

/**
 * Set of {@link eu.verdelhan.ta4j.TickMoney TickMoneys} separated by a predefined period (e.g. 15 minutes, 1 day, etc.)
 * <p>
 */
public class TimeSeriesMoney {
    /** List of TickMoneys */
    private final List<? extends TickMoney> TickMoneys;
    /** Begin index of the time series */
    private int beginIndex;
    /** End index of the time series */
    private int endIndex;
    /** Name of the series */
    private final String name;

    /**
     * Constructor.
     * @param name the name of the series
     * @param TickMoneys the list of TickMoneys of the series
     * @param beginIndex the begin index (inclusive) of the time series
     * @param endIndex the end index (inclusive) of the time series
     */
    public TimeSeriesMoney(String name, List<? extends TickMoney> TickMoneys, int beginIndex, int endIndex) {
        // TODO: add null checks and out of bounds checks
        if (endIndex < beginIndex - 1) {
            throw new IllegalArgumentException("end cannot be < than begin - 1");
        }
        this.name = name;
        this.TickMoneys = TickMoneys;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    /**
     * Constructor.
     * @param name the name of the series
     * @param TickMoneys the list of TickMoneys of the series
     */
    public TimeSeriesMoney(String name, List<? extends TickMoney> TickMoneys) {
        this(name, TickMoneys, 0, TickMoneys.size() - 1);
    }

    /**
     * Constructor of an unnamed series.
     * @param TickMoneys the list of TickMoneys of the series
     */
    public TimeSeriesMoney(List<? extends TickMoney> TickMoneys) {
        this("unnamed", TickMoneys);
    }

    /**
     * @return the name of the series
     */
    public String getName() {
        return name;
    }

    /**
     * @param i an index
     * @return the TickMoney at the i position
     */
    public TickMoney getTick(int i) {
        return TickMoneys.get(i);
    }

    /**
     * @return the number of TickMoneys in the series
     */
    public int getSize() {
        return (endIndex - beginIndex) + 1;
    }

    /**
     * @return the begin index of the series
     */
    public int getBegin() {
        return beginIndex;
    }

    /**
     * @return the end index of the series
     */
    public int getEnd() {
        return endIndex;
    }

    /**
     * Returns a new time series which is a view of a subset of the current series.
     * <p>
     * The new series has begin and end indexes which correspond to the bounds of the sub-set into the full series.<br>
     * The TickMoney of the series are shared between the original time series and the returned one (i.e. no copy).
     * @param beginIndex the begin index (inclusive) of the time series
     * @param endIndex the end index (inclusive) of the time series
     * @return a constrained {@link eu.verdelhan.ta4j.TimeSeriesMoney time series} which is a sub-set of the current series
     */
    public TimeSeriesMoney subseries(int beginIndex, int endIndex) {
        return new TimeSeriesMoney(name, TickMoneys, beginIndex, endIndex);
    }

    /**
     * Returns a new time series which is a view of a subset of the current series.
     * <p>
     * The new series has begin and end indexes which correspond to the bounds of the sub-set into the full series.<br>
     * The TickMoney of the series are shared between the original time series and the returned one (i.e. no copy).
     * @param beginIndex the begin index (inclusive) of the time series
     * @param duration the duration of the time series
     * @return a constrained {@link eu.verdelhan.ta4j.TimeSeriesMoney time series} which is a sub-set of the current series
     */
    public TimeSeriesMoney subseries(int beginIndex, Period duration) {

        // Calculating the sub-series interval
        DateTime beginInterval = TickMoneys.get(beginIndex).getEndTime();
        DateTime endInterval = beginInterval.plus(duration);
        Interval subseriesInterval = new Interval(beginInterval, endInterval);

        // Checking TickMoneys belonging to the sub-series (starting at the provided index)
        int subseriesNbTickMoneys = 0;
        for (int i = beginIndex; i <= endIndex; i++) {
            // For each TickMoney...
            DateTime TickMoneyTime = TickMoneys.get(i).getEndTime();
            if (!subseriesInterval.contains(TickMoneyTime)) {
                // TickMoney out of the interval
                break;
            }
            // TickMoney in the interval
            // --> Incrementing the number of TickMoneys in the subseries
            subseriesNbTickMoneys++;
        }

        return subseries(beginIndex, beginIndex + subseriesNbTickMoneys - 1);
    }

    /**
     * Splits the time series into sub-series containing nbTickMoneys TickMoneys each.<br>
     * The current time series is splitted every nbTickMoneys TickMoneys.<br>
     * The last sub-series may have less TickMoneys than nbTickMoneys.
     * @param nbTickMoneys the number of TickMoneys of each sub-series
     * @return a list of sub-series
     */
    public List<TimeSeriesMoney> split(int nbTickMoneys) {
        ArrayList<TimeSeriesMoney> subseries = new ArrayList<TimeSeriesMoney>();
        for (int i = beginIndex; i <= endIndex; i += nbTickMoneys) {
            // For each nbTickMoneys TickMoneys
            int subseriesBegin = i;
            int subseriesEnd = Math.min(subseriesBegin + nbTickMoneys - 1, endIndex);
            subseries.add(subseries(subseriesBegin, subseriesEnd));
        }
        return subseries;
    }

    /**
     * Splits the time series into sub-series lasting sliceDuration.<br>
     * The current time series is splitted every splitDuration.<br>
     * The last sub-series may last less than sliceDuration.
     * @param splitDuration the duration between 2 splits
     * @param sliceDuration the duration of each sub-series
     * @return a list of sub-series
     */
    public List<TimeSeriesMoney> split(Period splitDuration, Period sliceDuration) {
        ArrayList<TimeSeriesMoney> subseries = new ArrayList<TimeSeriesMoney>();
        if (splitDuration != null && !splitDuration.equals(Period.ZERO)
                && sliceDuration != null && !sliceDuration.equals(Period.ZERO)) {

            List<Integer> beginIndexes = getSplitBeginIndexes(splitDuration);
            for (Integer subseriesBegin : beginIndexes) {
                subseries.add(subseries(subseriesBegin, sliceDuration));
            }
        }
        return subseries;
    }

    /**
     * Splits the time series into sub-series lasting duration.<br>
     * The current time series is splitted every duration.<br>
     * The last sub-series may last less than duration.
     * @param duration the duration between 2 splits (and of each sub-series)
     * @return a list of sub-series
     */
    public List<TimeSeriesMoney> split(Period duration) {
        return split(duration, duration);
    }

    /**
     * Runs the strategy over the series.
     * Opens the trades with {@link eu.verdelhan.ta4j.OperationType.BUY} operations.
     * @param strategy the trading strategy
     * @return a list of trades
     */
    public List<Trade> run(Strategy strategy) {
        return run(strategy, OperationType.BUY);
    }

    /**
     * Runs the strategy over the series.
     * @param strategy the trading strategy
     * @param operationType the {@link eu.verdelhan.ta4j.OperationType} used to open the trades
     * @return a list of trades
     */
    public List<Trade> run(Strategy strategy, OperationType operationType) {

        List<Trade> trades = new ArrayList<Trade>();

        Trade lastTrade = new Trade(operationType);
        for (int i = beginIndex; i <= endIndex; i++) {
            // For each TickMoney in the sub-series...
            if (strategy.shouldOperate(lastTrade, i)) {
                lastTrade.operate(i);
                if (lastTrade.isClosed()) {
                    // Adding the trade when closed
                    trades.add(lastTrade);
                    lastTrade = new Trade(operationType);
                }
            }
        }

        if (lastTrade.isOpened()) {
            // If the last trade is still opened, we search out of the end index.
            // May works if the current series is a sub-series (but not the last sub-series).
            for (int i = endIndex + 1; i < TickMoneys.size(); i++) {
                // For each TickMoney out of sub-series bound...
                // --> Trying to close the last trade
                if (strategy.shouldOperate(lastTrade, i)) {
                    lastTrade.operate(i);
                    break;
                }
            }
            if (lastTrade.isClosed()) {
                // Last trade added only if it has been closed finally
                trades.add(lastTrade);
            }
        }
        return trades;
    }

    /**
     * @return the period name of the series (e.g. "from 12:00 21/01/2014 to 12:15 21/01/2014")
     */
    public String getPeriodName() {
        return TickMoneys.get(beginIndex).getEndTime().toString("hh:mm dd/MM/yyyy - ")
                + TickMoneys.get(endIndex).getEndTime().toString("hh:mm dd/MM/yyyy");
    }

    /**
     * @return the period of the series
     */
    public Period getPeriod() {
        final long firstTickMoneyPeriod = TickMoneys.get(beginIndex + 1).getEndTime().getMillis() - TickMoneys.get(beginIndex).getEndTime().getMillis();
        final long secondTickMoneyPeriod = TickMoneys.get(beginIndex + 2).getEndTime().getMillis() - TickMoneys.get(beginIndex + 1).getEndTime().getMillis();
        Period period = new Period(Math.min(firstTickMoneyPeriod, secondTickMoneyPeriod));
        assert !Period.ZERO.equals(period) : "Period should not be zero";
        return period;
    }

    /**
     * Builds a list of split indexes from splitDuration.
     * @param splitDuration the duration between 2 splits
     * @return a list of begin indexes after split
     */
    private List<Integer> getSplitBeginIndexes(Period splitDuration) {
        ArrayList<Integer> beginIndexes = new ArrayList<Integer>();

        // Adding the first begin index
        beginIndexes.add(beginIndex);

        // Building the first interval before next split
        DateTime beginInterval = TickMoneys.get(beginIndex).getEndTime();
        DateTime endInterval = beginInterval.plus(splitDuration);
        Interval splitInterval = new Interval(beginInterval, endInterval);

        for (int i = beginIndex; i <= endIndex; i++) {
            // For each TickMoney...
            DateTime TickMoneyTime = TickMoneys.get(i).getEndTime();
            if (!splitInterval.contains(TickMoneyTime)) {
                // TickMoney out of the interval
                if (!endInterval.isAfter(TickMoneyTime)) {
                    // TickMoney after the interval
                    // --> Adding a new begin index
                    beginIndexes.add(i);
                }

                // Building the new interval before next split
                beginInterval = endInterval.isBefore(TickMoneyTime) ? TickMoneyTime : endInterval;
                endInterval = beginInterval.plus(splitDuration);
                splitInterval = new Interval(beginInterval, endInterval);
            }
        }
        return beginIndexes;
    }
}