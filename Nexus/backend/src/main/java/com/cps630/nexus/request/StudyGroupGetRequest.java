package com.cps630.nexus.request;

import jakarta.validation.constraints.NotNull;

public class StudyGroupGetRequest {
	@NotNull
	private Integer studyGroupId;

	public Integer getStudyGroupId() {
		return studyGroupId;
	}

	public void setStudyGroupId(Integer studyGroupId) {
		this.studyGroupId = studyGroupId;
	}
}