package com.borderless.wallet.socket;

import com.borderless.wallet.constants.OwnerInfoConstant;

import java.util.Arrays;
import java.util.List;

public class FullNodeServerSelect {
    private List<String> mListNode = Arrays.asList(
//           "ws://118.190.159.23:22227"
            "ws://118.190.159.23:44447"
    );

    public String getServer() {

        return OwnerInfoConstant.BORDERLESS_BLOCK_SERVER;   //139.199.124.245:8056

    }
}
