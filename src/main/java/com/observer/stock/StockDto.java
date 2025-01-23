package com.observer.stock;

import java.time.LocalDate;
import java.util.Map;

import com.observer.discord.Field;

public class StockDto {
    private final String companyName;
    private final Integer close;
    private final Integer capRank;
    private final Integer tradingVolume;
    private final LocalDate date;
    private final Map<String, Double> indicatorMap;

    public StockDto(Builder builder) {
        this.companyName = builder.companyName;
        this.close = builder.close;
        this.capRank = builder.capRank;
        this.tradingVolume = builder.tradingVolume;
        this.date = builder.date;
        this.indicatorMap = builder.indicatorMap;
    }

    public static class Builder {
        private String companyName;
        private Integer close;
        private Integer capRank;
        private Integer tradingVolume;
        private LocalDate date;
        private Map<String, Double> indicatorMap;

        public Builder companyName(String value) {
            this.companyName = value;
            return this;
        }

        public Builder close(Integer value) {
            this.close = value;
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

        public Builder indicatorMap(Map<String, Double> indicatorMap) {
            this.indicatorMap = indicatorMap;
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

    public Integer getCapRank() {
        return capRank;
    }

    public Integer getTradingVolume() {
        return tradingVolume;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<String, Double> getIndicatorMap() {
        return indicatorMap;
    }

    @Override
    public String toString() {
        return String.format(
                "StockDto(companyName: %s, close: %s, capRank: %s, tradingVolume: %s, date: %s, indicatorMap: %s)",
                companyName, close, capRank, tradingVolume, date, indicatorMap.toString());
    }

    public Field toField() {
        return new Field(companyName,
                String.format(
                        "close: %,d\\n capRank: %,d\\n tradingVolume: %,d\\n date: %s\\n indicators: %s\\n",
                        close, capRank, tradingVolume, date, indicatorMap.toString()));
    }
}
