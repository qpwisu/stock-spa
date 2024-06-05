package com.hany.stock.domain.dto;

import lombok.*;

@Data
@Builder
@ToString
@Getter
@AllArgsConstructor
public class SearchDto {
    private String category;
    private String keyword;
}
