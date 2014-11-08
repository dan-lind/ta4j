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
package eu.verdelhan.ta4j.analysis.criteria;

import eu.verdelhan.ta4j.*;
import org.jscience.mathematics.number.Real;

import java.util.List;

/**
 * Average profitable trades criterion.
 * <p>
 * The number of profitable trades.
 */
public class AverageProfitableTradesCriterionReal extends AbstractAnalysisCriterionReal {

    @Override
    public double calculate(TimeSeriesReal series, Trade trade) {
        int entryIndex = trade.getEntry().getIndex();
        int exitIndex = trade.getExit().getIndex();

        Real result;
        if (trade.getEntry().getType() == OperationType.BUY) {
            // buy-then-sell trade
            result = series.getTick(exitIndex).getClosePrice().divide(series.getTick(entryIndex).getClosePrice());
        } else {
            // sell-then-buy trade
            result = series.getTick(entryIndex).getClosePrice().divide(series.getTick(exitIndex).getClosePrice());
        }

        return (result.isGreaterThan(Real.ONE)) ? 1d : 0d;
    }

    @Override
    public double calculate(TimeSeriesReal series, List<Trade> trades) {
        int numberOfProfitable = 0;
        for (Trade trade : trades) {
            int entryIndex = trade.getEntry().getIndex();
            int exitIndex = trade.getExit().getIndex();

            Real result;
            if (trade.getEntry().getType() == OperationType.BUY) {
                // buy-then-sell trade
                result = series.getTick(exitIndex).getClosePrice().divide(series.getTick(entryIndex).getClosePrice());
            } else {
                // sell-then-buy trade
                result = series.getTick(entryIndex).getClosePrice().divide(series.getTick(exitIndex).getClosePrice());
            }
            if (result.isGreaterThan(Real.ONE)) {
                numberOfProfitable++;
            }
        }
        return ((double) numberOfProfitable) / trades.size();
    }

    @Override
    public boolean betterThan(double criterionValue1, double criterionValue2) {
        return criterionValue1 > criterionValue2;
    }
}
