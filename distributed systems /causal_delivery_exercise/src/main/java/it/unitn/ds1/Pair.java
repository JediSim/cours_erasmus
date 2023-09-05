package it.unitn.ds1;

public class Pair {

    private final Character key;
    private Integer value;

    private Integer version;

    public Pair(Character key, Integer value, Integer version) {
        this.version = version;
        this.value = value;
        this.key = key;
    }

    public Pair(Character key, Integer value) {
        this(key, value, 1);
    }

    public Character getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getVersion() { return this.version; }

    public void setValue(Integer value) {
        this.value = value;
        this.version++;
    }

//    public void setVersion(Integer version) { this.version = version; }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ", V" + this.version + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Pair)) return false;
        Pair other = (Pair) obj;
        return this.key.equals(other.key);
    }

}
