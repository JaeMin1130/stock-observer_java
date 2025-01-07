package com.observer.stock;

import java.time.LocalDate;

import com.observer.discord.Field;

public class StockDto {
    private final String companyName;
    private final Integer close;
    private final Double sma;
    private final Double closeSmaRatio;
    private final Double dy;
    private final Integer capRank;
    private final Integer tradingVolume;
    private final LocalDate date;

    public StockDto(Builder builder) {
        this.companyName = builder.companyName;
        this.close = builder.close;
        this.sma = builder.sma;
        this.closeSmaRatio = builder.closeSmaRatio;
        this.dy = builder.dy;
        this.capRank = builder.capRank;
        this.tradingVolume = builder.tradingVolume;
        this.date = builder.date;
    }

    public static class Builder {
        private String companyName;
        private Integer close;
        private Double sma;
        private Double closeSmaRatio;
        private Double dy;
        private Integer capRank;
        private Integer tradingVolume;
        private LocalDate date;

        public Builder companyName(String value) {
            this.companyName = value;
            return this;
        }

        public Builder close(Integer value) {
            this.close = value;
            return this;
        }

        public Builder sma(Double value) {
            this.sma = value;
            return this;
        }

        public Builder closeSmaRatio(Double value) {
            this.closeSmaRatio = value;
            return this;
        }

        public Builder dy(Double value) {
            this.dy = value;
            return this;
        }

        public Builder capRank(Integer value) {
            this.capRank = value;
            return this;
        }

        public Builder tradingVolume(Integer value) {
            this.tradingVolume = value;
            return this;
        }

        public Builder date(LocalDate value) {
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

    public Integer getClose() {
        return close;
    }

    public Double getSma() {
        return sma;
    }

    public Double getCloseSmaRatio() {
        return closeSmaRatio;
    }

    public Double getDy() {
        return dy;
    }

    public Integer getCapRank() {
        return capRank;
    }

    public Integer getTradingVolume() {
        return tradingVolume;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format(
                "StockDto(companyName: %s, close: %s, sma: %s, closeSmaRatio: %s, dy: %s, capRank: %s, tradingVolume: %s, date: %s)",
                companyName, sma, close, closeSmaRatio, dy, capRank, tradingVolume, date);
    }

    public Field toField() {
        return new Field(companyName,
                String.format(
                        "close: %,d\\n sma: %,.1f\\n closeSmaRatio: %,.1f%%\\n dy: %,.1f%%\\n capRank: %s\\n tradingVolume: %,d\\n date: %s",
                        close, sma, closeSmaRatio, dy, capRank, tradingVolume, date));
    }
}
