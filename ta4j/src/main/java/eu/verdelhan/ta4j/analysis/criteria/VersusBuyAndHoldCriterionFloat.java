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

import java.util.ArrayList;
import java.util.List;

/**
 * Versus "buy and hold" criterion.
 * <p>
 * Compares the value of a provided {@link eu.verdelhan.ta4j.AnalysisCriterion criterion} with the value of a {@link eu.verdelhan.ta4j.analysis.criteria.BuyAndHoldCriterion "buy and hold" criterion}.
 */
public class VersusBuyAndHoldCriterionFloat extends AbstractAnalysisCriterionFloat {

    private AnalysisCriterionFloat criterion;

    /**
     * Constructor.
     * @param criterion an analysis criterion to be compared
     */
    public VersusBuyAndHoldCriterionFloat(AnalysisCriterionFloat criterion) {
        this.criterion = criterion;
    }

    @Override
    public double calculate(TimeSeriesFloat series, List<Trade> trades) {
        List<Trade> fakeTrades = new ArrayList<Trade>();
        fakeTrades.add(new Trade(new Operation(series.getBegin(), OperationType.BUY), new Operation(series.getEnd(),
                OperationType.SELL)));

        return criterion.calculate(series, trades) / criterion.calculate(series, fakeTrades);
    }

    @Override
    public double calculate(TimeSeriesFloat series, Trade trade) {
        List<Trade> trades = new ArrayList<Trade>();
        trades.add(trade);
        return calculate(series, trades);
    }

    @Override
    public boolean betterThan(double criterionValue1, double criterionValue2) {
        return criterionValue1 > criterionValue2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (").append(criterion).append(')');
        return sb.toString();
    }
}
