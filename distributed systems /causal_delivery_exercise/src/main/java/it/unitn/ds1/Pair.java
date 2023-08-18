package it.unitn.ds1;

public class Pair {

    private final Character key;
    private Integer value;

    public Pair(Character key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public Character getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Pair)) return false;
        Pair other = (Pair) obj;
        return this.key.equals(other.key);
    }

}
