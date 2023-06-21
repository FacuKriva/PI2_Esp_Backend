package com.digital.money.msvc.api.account.model.projections;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface GetCVUOnly {

    @JsonProperty("cvu")
    String getTo_Cvu();
}
