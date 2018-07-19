package com.github.chouheiwa.wallet.net.model;

import java.util.ArrayList;

/**
 * Created by 18301 on 2018/1/7.
 */

public class ReallyCoinList {

    private String baseSymbol;
    private ArrayList<String> qouteSymbols;


    public String getBaseSymbol() {
        return baseSymbol;
    }

    public void setBaseSymbol(String baseSymbol) {
        this.baseSymbol = baseSymbol;
    }

    public ArrayList<String> getQouteSymbols() {
        return qouteSymbols;
    }

    public void setQouteSymbols(ArrayList<String> qouteSymbols) {
        this.qouteSymbols = qouteSymbols;
    }


}
