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
package eu.verdelhan.ta4j.analysis;

import eu.verdelhan.ta4j.*;
import org.apfloat.Apfloat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The cash flow.
 * <p>
 * This class allows to follow the money cash flow involved by a list of trades over a time series.
 */
public class CashFlowFloat implements Indicator<Apfloat> {

    /** The time series */
    private final TimeSeriesFloat timeSeries;

    /** The list of trades */
    private final List<Trade> trades;

    private List<Apfloat> values;

    /**
     * Constructor.
     * @param timeSeries the time series
     * @param trades the list of trades
     */
    public CashFlowFloat(TimeSeriesFloat timeSeries, List<Trade> trades) {
        this.timeSeries = timeSeries;
        this.trades = trades;
        values = new ArrayList<Apfloat>();
        values.add(Apfloat.ONE);
        calculate();
    }

    /**
     * @param index the index
     * @return the cash flow value at the index-th position
     */
    @Override
    public Apfloat getValue(int index) {
        return values.get(index);
    }

    /**
     * @return the size of the time series
     */
    public int getSize() {
        return timeSeries.getSize();
    }

    /**
     * Calculates the cash flow.
     */
    private void calculate() {

        for (Trade trade : trades) {
            // For each trade...
            int begin = trade.getEntry().getIndex() + 1;
            if (begin > values.size()) {
                values.addAll(Collections.nCopies(begin - values.size(), values.get(values.size() - 1)));
            }
            int end = trade.getExit().getIndex();
            for (int i = Math.max(begin, 1); i <= end; i++) {
                Apfloat ratio;
                if (trade.getEntry().getType().equals(OperationType.BUY)) {
                    ratio = timeSeries.getTickFloat(i).getClosePrice().divide(timeSeries.getTickFloat(trade.getEntry().getIndex()).getClosePrice());
                } else {
                    ratio = timeSeries.getTickFloat(trade.getEntry().getIndex()).getClosePrice().divide(timeSeries.getTickFloat(i).getClosePrice());
                }
                values.add(values.get(trade.getEntry().getIndex()).multiply(ratio));
            }
        }
        if ((timeSeries.getEnd() - values.size()) >= 0) {
            values.addAll(Collections.nCopies((timeSeries.getEnd() - values.size()) + 1, values.get(values.size() - 1)));
        }
    }
}