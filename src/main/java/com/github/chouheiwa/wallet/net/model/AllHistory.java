package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by 18301 on 2018/1/6.
 */

public class AllHistory {


    /**
     * id : 186
     * jsonrpc : 2.0
     * result : [{"id":"0.0.97","key":{"base":"1.3.0","quote":"1.3.3","sequence":-93},"time":"2018-01-06T08:53:21","op":{"fee":{"amount":0,"asset_id":"1.3.0"},"order_id":"1.7.97","account_id":"1.2.14","pays":{"amount":53600,"asset_id":"1.3.3"},"receives":{"amount":100000,"asset_id":"1.3.0"}}}]
     */

    private int id;
    private String jsonrpc;
    private List<ResultBean> result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 0.0.97
         * key : {"base":"1.3.0","quote":"1.3.3","sequence":-93}
         * time : 2018-01-06T08:53:21
         * op : {"fee":{"amount":0,"asset_id":"1.3.0"},"order_id":"1.7.97","account_id":"1.2.14","pays":{"amount":53600,"asset_id":"1.3.3"},"receives":{"amount":100000,"asset_id":"1.3.0"}}
         */

        private String id;
        private KeyBean key;
        private String time;
        private OpBean op;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public KeyBean getKey() {
            return key;
        }

        public void setKey(KeyBean key) {
            this.key = key;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public OpBean getOp() {
            return op;
        }

        public void setOp(OpBean op) {
            this.op = op;
        }

        public static class KeyBean {
            /**
             * base : 1.3.0
             * quote : 1.3.3
             * sequence : -93
             */

            private String base;
            private String quote;
            private int sequence;

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

            public int getSequence() {
                return sequence;
            }

            public void setSequence(int sequence) {
                this.sequence = sequence;
            }
        }

        public static class OpBean {
            /**
             * fee : {"amount":0,"asset_id":"1.3.0"}
             * order_id : 1.7.97
             * account_id : 1.2.14
             * pays : {"amount":53600,"asset_id":"1.3.3"}
             * receives : {"amount":100000,"asset_id":"1.3.0"}
             */

            private FeeBean fee;
            private String order_id;
            private String account_id;
            private PaysBean pays;
            private ReceivesBean receives;

            public FeeBean getFee() {
                return fee;
            }

            public void setFee(FeeBean fee) {
                this.fee = fee;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getAccount_id() {
                return account_id;
            }

            public void setAccount_id(String account_id) {
                this.account_id = account_id;
            }

            public PaysBean getPays() {
                return pays;
            }

            public void setPays(PaysBean pays) {
                this.pays = pays;
            }

            public ReceivesBean getReceives() {
                return receives;
            }

            public void setReceives(ReceivesBean receives) {
                this.receives = receives;
            }

            public static class FeeBean {
                /**
                 * amount : 0
                 * asset_id : 1.3.0
                 */

                private long amount;
                private String asset_id;

                public long getAmount() {
                    return amount;
                }

                public void setAmount(long amount) {
                    this.amount = amount;
                }

                public String getAsset_id() {
                    return asset_id;
                }

                public void setAsset_id(String asset_id) {
                    this.asset_id = asset_id;
                }
            }

            public static class PaysBean {
                /**
                 * amount : 53600
                 * asset_id : 1.3.3
                 */

                private long amount;
                private String asset_id;

                public long getAmount() {
                    return amount;
                }

                public void setAmount(long amount) {
                    this.amount = amount;
                }

                public String getAsset_id() {
                    return asset_id;
                }

                public void setAsset_id(String asset_id) {
                    this.asset_id = asset_id;
                }
            }

            public static class ReceivesBean {
                /**
                 * amount : 100000
                 * asset_id : 1.3.0
                 */

                private long amount;
                private String asset_id;

                public long getAmount() {
                    return amount;
                }

                public void setAmount(long amount) {
                    this.amount = amount;
                }

                public String getAsset_id() {
                    return asset_id;
                }

                public void setAsset_id(String asset_id) {
                    this.asset_id = asset_id;
                }
            }
        }
    }
}
