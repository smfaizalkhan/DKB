package com.example.demo.commons;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoneyValue {

    private String amount;
    private String currency;
}
