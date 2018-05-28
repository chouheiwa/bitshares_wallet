package com.borderless.wallet.socket.fc.io;


public interface base_encoder {
    void write(byte[] data);
    void write(byte[] data, int off, int len);
    void write(byte data);

}
