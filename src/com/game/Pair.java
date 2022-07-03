package com.game;

public class Pair {
    private Integer key;
    private Integer val;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public Pair(Integer key, Integer val) {
        this.key = key;
        this.val = val;
    }
}
