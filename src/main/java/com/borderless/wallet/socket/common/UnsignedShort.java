package com.borderless.wallet.socket.common;


public class UnsignedShort extends Number {
    public  static final UnsignedShort ZERO = new UnsignedShort((short)0);

    private short value;
    public UnsignedShort(short value) {
        this.value = value;
    }
    public static UnsignedShort valueOf(String string) {
        return new UnsignedShort(Integer.valueOf(string).shortValue());
    }

    @Override
    public int intValue() {
        return toInt();
    }

    @Override
    public short shortValue() {
        return value;
    }

    @Override
    public long longValue() {
        return toInt();
    }

    @Override
    public float floatValue() {
        return toInt();
    }

    @Override
    public double doubleValue() {
        return toInt();
    }


    private int toInt() {
        return value & 0xffff;
    }
}
