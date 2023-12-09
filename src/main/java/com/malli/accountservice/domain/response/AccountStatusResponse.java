package com.malli.accountservice.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountStatusResponse(String status, @JsonProperty("service_name") String serviceName, @JsonProperty("created_by") String createdBy) {
}
