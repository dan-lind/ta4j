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

import eu.verdelhan.ta4j.TimeSeriesFloat;
import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.analysis.CashFlowFloat;
import org.apfloat.Apfloat;

import java.util.ArrayList;
import java.util.List;

/**
 * Maximum drawdown criterion.
 * <p>
 * @see <a href="http://en.wikipedia.org/wiki/Drawdown_%28economics%29">http://en.wikipedia.org/wiki/Drawdown_%28economics%29</a>
 */
public class MaximumDrawdownCriterionFloat extends AbstractAnalysisCriterionFloat {

    @Override
    public double calculate(TimeSeriesFloat series, List<Trade> trades) {
        Apfloat maximumDrawdown = Apfloat.ZERO;
        Apfloat maxPeak = Apfloat.ZERO;
        CashFlowFloat cashFlow = new CashFlowFloat(series, trades);

        for (int i = series.getBegin(); i <= series.getEnd(); i++) {
            Apfloat value = cashFlow.getValue(i);
            if (value.compareTo(maxPeak) == 1) {
                maxPeak = value;
            }

            Apfloat drawdown = maxPeak.subtract(value).divide(maxPeak);
            if (drawdown.compareTo(maximumDrawdown) == 1) {
                maximumDrawdown = drawdown;
                // absolute maximumDrawdown.
                // should it be maximumDrawdown = drawDown/maxPeak ?
            }
        }
        return maximumDrawdown.doubleValue();
    }

    @Override
    public double calculate(TimeSeriesFloat series, Trade trade) {
        List<Trade> trades = new ArrayList<Trade>();
        trades.add(trade);
        return calculate(series, trades);
    }

    @Override
    public boolean betterThan(double criterionValue1, double criterionValue2) {
        return criterionValue1 < criterionValue2;
    }
}
