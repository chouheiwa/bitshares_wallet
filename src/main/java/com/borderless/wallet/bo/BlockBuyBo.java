package com.borderless.wallet.bo;

public class BlockBuyBo   {

    private String base ;

    private String quote;

    private double amountToSell ;

    private double amount ;

    private String amount_to_fee;

    private String symbol_to_fee;

    private int index;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public double getAmountToSell() {
        return amountToSell;
    }

    public void setAmountToSell(double amountToSell) {
        this.amountToSell = amountToSell;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmount_to_fee() {
        return amount_to_fee;
    }

    public void setAmount_to_fee(String amount_to_fee) {
        this.amount_to_fee = amount_to_fee;
    }

    public String getSymbol_to_fee() {
        return symbol_to_fee;
    }

    public void setSymbol_to_fee(String symbol_to_fee) {
        this.symbol_to_fee = symbol_to_fee;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
