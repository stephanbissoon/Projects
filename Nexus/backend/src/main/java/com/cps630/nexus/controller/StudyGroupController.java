package com.cps630.nexus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cps630.nexus.request.StudyGroupCreateRequest;
import com.cps630.nexus.request.StudyGroupGetRequest;
import com.cps630.nexus.request.StudyGroupJoinRequest;
import com.cps630.nexus.request.StudyGroupLeaveRequest;
import com.cps630.nexus.request.StudyGroupUpdateRequest;
import com.cps630.nexus.service.StudyGroupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class StudyGroupController {
	@Autowired
	private StudyGroupService service;
	
	/**
	 * Endpoint to create a study group
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/basic/studygroup/create")
	public ResponseEntity<Object> createStudyGroup(@RequestBody @Valid StudyGroupCreateRequest request) {
		return service.createStudyGroup(request);
	}
	
	/**
	 * Endpoint to update a study group
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/basic/studygroup/update")
	public ResponseEntity<Object> updateStudyGroup(@RequestBody @Valid StudyGroupUpdateRequest request) {
		return service.updateStudyGroup(request);
	}
	
	/**
	 * Endpoint to join a study group
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/basic/studygroup/join")
	public ResponseEntity<Object> joinStudyGroup(@RequestBody @Valid StudyGroupJoinRequest request) {
		return service.joinStudyGroup(request);
	}
	
	/**
	 * Endpoint to leave a study group
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/basic/studygroup/leave")
	public ResponseEntity<Object> leaveStudyGroup(@RequestBody @Valid StudyGroupLeaveRequest request) {
		return service.leaveStudyGroup(request);
	}
	
	/**
	 * Endpoint to get a list of active study groups
	 * @return
	 */
	@PostMapping("/internal/basic/studygroup/list/get")
	public ResponseEntity<Object> getStudyGroupList() {
		return service.getStudyGroupList();
	}
	
	/**
	 * Endpoint to get a specific study group
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/basic/studygroup/get")
	public ResponseEntity<Object> getStudyGroup(@RequestBody @Valid StudyGroupGetRequest request) {
		return service.getStudyGroup(request);
	}
}