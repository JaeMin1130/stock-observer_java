package com.observer;

import com.observer.discordWebhookMessage.Field;

public class StockDto {
    private final String companyName;
    private final String close;
    private final String sma;
    private final String closeSmaRatio;
    private final String dy;
    private final String capRank;
    private final String date;

    public StockDto(Builder builder) {
        this.companyName = builder.companyName;
        this.close = builder.close;
        this.sma = builder.sma;
        this.closeSmaRatio = builder.closeSmaRatio;
        this.dy = builder.dy;
        this.capRank = builder.capRank;
        this.date = builder.date;
    }

    public static class Builder {
        private String companyName;
        private String close;
        private String sma;
        private String closeSmaRatio;
        private String dy;
        private String capRank;
        private String date;

        public Builder companyName(String value) {
            this.companyName = value;
            return this;
        }

        public Builder close(String value) {
            this.sma = value;
            return this;
        }

        public Builder sma(String value) {
            this.close = value;
            return this;
        }

        public Builder closeSmaRatio(String value) {
            this.closeSmaRatio = value;
            return this;
        }

        public Builder dy(String value) {
            this.dy = value;
            return this;
        }

        public Builder capRank(String value) {
            this.capRank = value;
            return this;
        }

        public Builder date(String value) {
            this.date = value;
            return this;
        }

        public StockDto build() {
            return new StockDto(this);
        }
    }

    // Getter
    public String getCompanyName() {
        return companyName;
    }

    public String getClose() {
        return close;
    }

    public String getSma() {
        return sma;
    }

    public String getFigure() {
        return closeSmaRatio;
    }

    public String getDy() {
        return dy;
    }

    public String getCapRank() {
        return capRank;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format(
                "StockDto(companyName: %s, close: %s, sma: %s, closeSmaRatio: %s, dy: %s, capRank: %s, date: %s)",
                companyName, sma, close, closeSmaRatio, dy, capRank, date);
    }

    public Field toField() {
        return new Field(companyName,
                String.format("close: %s\\n sma: %s\\n closeSmaRatio: %s%%\\n dy: %s\\n capRank: %s\\n date: %s",
                        sma, close, closeSmaRatio, dy, capRank, date));
    }
}
