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

import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.analysis.CashFlow;
import eu.verdelhan.ta4j.analysis.CashFlowMoney;
import eu.verdelhan.ta4j.analysis.criteria.*;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicatorMoney;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicatorMoney;
import eu.verdelhan.ta4j.indicators.trackers.TripleEMAIndicatorMoney;
import eu.verdelhan.ta4j.money.Money;
import eu.verdelhan.ta4j.strategies.IndicatorOverIndicatorStrategy;
import eu.verdelhan.ta4j.strategies.IndicatorOverIndicatorStrategyMoney;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.loaders.CsvTradesLoaderMoney;

import java.util.ArrayList;
import java.util.List;

/**
 * Quickstart for ta4j.
 * <p>
 * Global example.
 */
public class QuickstartMoney {

    public static void main(String[] args) {
        System.out.println("Running Money");
        long time = System.currentTimeMillis();


        // Getting a time series (from any provider: CSV, web service, etc.)
        TimeSeriesMoney series = CsvTradesLoaderMoney.loadBitstampSeries();


        // Getting the close price of the ticks
        Money firstClosePrice = series.getTick(0).getClosePrice();
        System.out.println("First close price: " + firstClosePrice);
        // Or within an indicator:
        ClosePriceIndicatorMoney closePrice = new ClosePriceIndicatorMoney(series);
        // Here is the same close price:
        System.out.println(firstClosePrice.equals(closePrice.getValue(0))); // equal to firstClosePrice



//        Getting the simple moving average (SMA) of the close price over the last 5 ticks
        TripleEMAIndicatorMoney shortSma = new TripleEMAIndicatorMoney(closePrice, 5);
        // Here is the 5-ticks-SMA value at the 42nd index

//
        // Getting a longer SMA (e.g. over the 30 last ticks)
        TripleEMAIndicatorMoney longSma = new TripleEMAIndicatorMoney(closePrice, 30);

        System.out.println("5-ticks-SMA value at the 42nd index: " + shortSma.getValue(10));

//        List<Strategy> strats = new ArrayList<Strategy>();
//
//
//        for (int i = 5; i < 25; i++) {
//            for (int j = 5; j < 100; j++) {
//                SMAIndicatorMoney indicatorFloatShort = new SMAIndicatorMoney(closePrice,i);
//                SMAIndicatorMoney indicatorFloatLong = new SMAIndicatorMoney(closePrice,j);
//                Strategy ourStrategy = new IndicatorOverIndicatorStrategyMoney(indicatorFloatShort, indicatorFloatLong);
//                strats.add(ourStrategy);
//            }
//        }



//
//        // Ok, now let's building our trading strategy!
//        List<Trade> trades = null;
//        // Initial strategy:
//        //  - Buy when 5-ticks SMA crosses over 30-ticks SMA
//        //  - Sell when 5-ticks SMA crosses under 30-ticks SMA
//        System.out.println(strats.size());
//
//        for (Strategy strat : strats) {
//            trades = series.run(strat);
//        }

        // Ok, now let's building our trading strategy!

        // Initial strategy:
        //  - Buy when 5-ticks SMA crosses over 30-ticks SMA
        //  - Sell when 5-ticks SMA crosses under 30-ticks SMA
        Strategy ourStrategy = new IndicatorOverIndicatorStrategyMoney(shortSma, longSma);

//        System.out.println(shortSma.getValue(series.getSize()-1));
//        System.out.println(longSma.getValue(series.getSize()-1));

        // Running our juicy trading strategy...
        List<Trade> trades = series.run(ourStrategy);
        System.out.println("Number of trades for our strategy: " + trades.size());

        System.out.println(trades);

        // Analysis

        // Getting the cash flow of the resulting trades
        CashFlowMoney cashFlow = new CashFlowMoney(series, trades);

        // Getting the profitable trades ratio
        AnalysisCriterionMoney profitTradesRatio = new AverageProfitableTradesCriterionMoney();
        System.out.println("Profitable trades ratio: " + profitTradesRatio.calculate(series, trades));
        // Getting the reward-risk ratio
        AnalysisCriterionMoney rewardRiskRatio = new RewardRiskRatioCriterionMoney();
        System.out.println("Reward-risk ratio: " + rewardRiskRatio.calculate(series, trades));

        // Total profit of our strategy
        // vs total profit of a buy-and-hold strategy
        AnalysisCriterionMoney vsBuyAndHold = new VersusBuyAndHoldCriterionMoney(new TotalProfitCriterionMoney());
        System.out.println("Our profit vs buy-and-hold profit: " + vsBuyAndHold.calculate(series, trades));

        // Your turn!
        System.out.println("Time: " + (System.currentTimeMillis() - time));
    }
}