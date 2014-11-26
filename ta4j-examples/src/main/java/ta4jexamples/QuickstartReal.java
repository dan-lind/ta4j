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
package ta4jexamples;

import eu.verdelhan.ta4j.AnalysisCriterionReal;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeriesReal;
import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.analysis.CashFlowReal;
import eu.verdelhan.ta4j.analysis.criteria.AverageProfitableTradesCriterionReal;
import eu.verdelhan.ta4j.analysis.criteria.RewardRiskRatioCriterionReal;
import eu.verdelhan.ta4j.analysis.criteria.TotalProfitCriterionReal;
import eu.verdelhan.ta4j.analysis.criteria.VersusBuyAndHoldCriterionReal;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicatorReal;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicatorReal;
import eu.verdelhan.ta4j.indicators.trackers.TripleEMAIndicatorReal;
import eu.verdelhan.ta4j.strategies.IndicatorOverIndicatorStrategyReal;
import org.jscience.mathematics.number.Real;
import ta4jexamples.loaders.CsvTradesLoaderReal;

import java.util.List;

/**
 * Quickstart for ta4j.
 * <p>
 * Global example.
 */
public class QuickstartReal {

    public static void main(String[] args) {
        System.out.println("Running Real");
        Real.setExactPrecision(1000);
        long time = System.currentTimeMillis();

        // Getting a time series (from any provider: CSV, web service, etc.)
        TimeSeriesReal series = CsvTradesLoaderReal.loadBitstampSeries();

        // Getting the close price of the ticks
        Real firstClosePrice = series.getTick(0).getClosePrice();
        System.out.println("First close price: " + firstClosePrice);
        // Or within an indicator:
        ClosePriceIndicatorReal closePrice = new ClosePriceIndicatorReal(series);
        // Here is the same close price:
        System.out.println(firstClosePrice.equals(closePrice.getValue(0))); // equal to firstClosePrice

        // Getting the simple moving average (SMA) of the close price over the last 5 ticks
        TripleEMAIndicatorReal shortSma = new TripleEMAIndicatorReal(closePrice, 5);
        // Here is the 5-ticks-SMA value at the 42nd index
//        System.out.println("5-ticks-SMA value at the 10th index: " + shortSma.getValue(10));
//        System.out.println("5-ticks-SMA value at the 20th index: " + shortSma.getValue(20));
//        System.out.println("5-ticks-SMA value at the 30th index: " + shortSma.getValue(30));
//        System.out.println("5-ticks-SMA value at the 40th index: " + shortSma.getValue(40));
//        System.out.println("5-ticks-SMA value at the 50th index: " + shortSma.getValue(50));
//        System.out.println("5-ticks-SMA value at the 60th index: " + shortSma.getValue(60));
//        System.out.println("5-ticks-SMA value at the 500th index: " + shortSma.getValue(500));


        // Getting a longer SMA (e.g. over the 30 last ticks)
        SMAIndicatorReal longSma = new SMAIndicatorReal(closePrice, 30);


        // Ok, now let's building our trading strategy!

        // Initial strategy:
        //  - Buy when 5-ticks SMA crosses over 30-ticks SMA
        //  - Sell when 5-ticks SMA crosses under 30-ticks SMA
        Strategy ourStrategy = new IndicatorOverIndicatorStrategyReal(shortSma, longSma);


        // Running our juicy trading strategy...
        List<Trade> trades = series.run(ourStrategy);
        System.out.println("Number of trades for our strategy: " + trades.size());



        System.out.println("5-ticks-SMA value at the 500th index: " + shortSma.getValue(series.getEnd()));

        // Analysis

        // Getting the cash flow of the resulting trades
        CashFlowReal cashFlow = new CashFlowReal(series, trades);

        // Getting the profitable trades ratio
        AnalysisCriterionReal profitTradesRatio = new AverageProfitableTradesCriterionReal();
        System.out.println("Profitable trades ratio: " + profitTradesRatio.calculate(series, trades));
        // Getting the reward-risk ratio
        AnalysisCriterionReal rewardRiskRatio = new RewardRiskRatioCriterionReal();
        System.out.println("Reward-risk ratio: " + rewardRiskRatio.calculate(series, trades));

        // Total profit of our strategy
        // vs total profit of a buy-and-hold strategy
        AnalysisCriterionReal vsBuyAndHold = new VersusBuyAndHoldCriterionReal(new TotalProfitCriterionReal());
        System.out.println("Our profit vs buy-and-hold profit: " + vsBuyAndHold.calculate(series, trades));

        // Your turn!
        System.out.println("Time: " + (System.currentTimeMillis() - time));

    }
}
