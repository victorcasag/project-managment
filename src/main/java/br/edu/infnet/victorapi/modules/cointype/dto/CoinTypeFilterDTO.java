package br.edu.infnet.victorapi.modules.cointype.dto;

public record CoinTypeFilterDTO(
        String name,
        String code,
        String symbol,
        Boolean isActive
) {
}
