package org.projectapp.Model;

public class Vote {
    Boolean positive,negative;

    public Vote(Boolean positive, Boolean negative) {
        this.positive = positive;
        this.negative = negative;
    }

    public Vote() {
    }

    public Boolean getPositive() {
        return positive;
    }

    public void setPositive(Boolean positive) {
        this.positive = positive;
    }

    public Boolean getNegative() {
        return negative;
    }

    public void setNegative(Boolean negative) {
        this.negative = negative;
    }
}
