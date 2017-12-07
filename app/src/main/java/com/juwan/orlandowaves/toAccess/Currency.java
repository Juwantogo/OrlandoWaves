package com.juwan.orlandowaves.toAccess;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static android.content.ContentValues.TAG;

/**
 * Created by Juwan on 12/4/2017.
 */

public class Currency {

    public Currency() {
    }

    public BigDecimal getBigDecimal(String money){
        money = money.replace("$","");
        BigDecimal priceNew = new BigDecimal(money);
        BigDecimal newNew = priceNew.setScale(2, RoundingMode.HALF_EVEN);
        return newNew;
        //priceDBL = priceDBL * .9;
    }

    public String getMoneyString(BigDecimal price){
        BigDecimal newNew = price.setScale(2, RoundingMode.HALF_EVEN);
        String money = newNew.toString();
        return money;
    }

    public String stringPercent(String money, int percent){
        money = money.replace("$","");
        BigDecimal percentNEW = new BigDecimal(percent);
        BigDecimal oneHundred = new BigDecimal(100);
        Log.e(TAG, "check percentNEW " +  percentNEW);
        percentNEW = (oneHundred.subtract(percentNEW)).divide(oneHundred);
        Log.e(TAG, "check percentNEW " +  percentNEW);


        BigDecimal priceNew = new BigDecimal(money);
        Log.e(TAG, "check priceNew" +  priceNew);

        BigDecimal newNew = priceNew.setScale(2, RoundingMode.HALF_EVEN);
        Log.e(TAG, "check newNew" +  newNew);

        newNew = newNew.multiply(percentNEW);
        Log.e(TAG, "check newNew" +  newNew);

        BigDecimal finalNew = newNew.setScale(2, RoundingMode.HALF_EVEN);
        Log.e(TAG, "check finalNew" +  finalNew);
        String returnSTR = "$" + finalNew;
        return returnSTR;
    }

    public String stringMultiply(String money, int quantity){
        money = money.replace("$","");
        //percent = 100 - percent;
        BigDecimal priceNew = new BigDecimal(money);
        BigDecimal newNew = priceNew.setScale(2, RoundingMode.HALF_EVEN);
        newNew = newNew.multiply(new BigDecimal(quantity));
        BigDecimal finalNew = newNew.setScale(2, RoundingMode.HALF_EVEN);
        String returnSTR = "$" + finalNew;
        return returnSTR;
    }

    public String stringPercentAndQuantity(String money, int percent,int quantity){
        money = money.replace("$","");
        BigDecimal percentNEW = new BigDecimal(percent);
        BigDecimal oneHundred = new BigDecimal(100);
        percentNEW = (oneHundred.subtract(percentNEW)).divide(oneHundred);
        BigDecimal priceNew = new BigDecimal(money);
        BigDecimal newNew = priceNew.setScale(2, RoundingMode.HALF_EVEN);
        newNew = newNew.multiply(percentNEW);
        newNew = newNew.multiply(new BigDecimal(quantity));
        BigDecimal finalNew = newNew.setScale(2, RoundingMode.HALF_EVEN);
        String returnSTR = "$" + finalNew;
        return returnSTR;
    }
}
