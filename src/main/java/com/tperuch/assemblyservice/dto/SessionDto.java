package com.tperuch.assemblyservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.util.Objects;

@Valid
public class SessionDto {

    @NotNull(message = "Id da pauta não pode ser nulo")
    private Long topicId;

    @Positive(message = "Duração da votação de ser um numero positivo e maior que zero")
    private Integer sessionTimeInMinutes;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Integer getSessionTimeInMinutes() {
        return sessionTimeInMinutes;
    }

    public void setSessionTimeInMinutes(Integer sessionTimeInMinutes) {
        this.sessionTimeInMinutes = sessionTimeInMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionDto that = (SessionDto) o;
        return Objects.equals(topicId, that.topicId) && Objects.equals(sessionTimeInMinutes, that.sessionTimeInMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicId, sessionTimeInMinutes);
    }
}
