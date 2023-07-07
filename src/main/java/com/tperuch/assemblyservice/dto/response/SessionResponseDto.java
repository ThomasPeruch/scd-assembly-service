package com.tperuch.assemblyservice.dto.response;

public class SessionResponseDto {

    private Long id;
    private Long topicId;
    private String votingStart;
    private String votingEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getVotingStart() {
        return votingStart;
    }

    public void setVotingStart(String votingStart) {
        this.votingStart = votingStart;
    }

    public String getVotingEnd() {
        return votingEnd;
    }

    public void setVotingEnd(String votingEnd) {
        this.votingEnd = votingEnd;
    }
}
