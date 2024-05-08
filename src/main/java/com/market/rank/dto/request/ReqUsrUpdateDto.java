package com.market.rank.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ReqUsrUpdateDto {
    private List<String> updateColumn;
    private List<String> updateValue;
}
