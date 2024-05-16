package com.cps630.nexus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cps630.nexus.request.DiscussionCreateRequest;
import com.cps630.nexus.request.DiscussionReplyCreateRequest;
import com.cps630.nexus.request.DiscussionReplyUpdateRequest;
import com.cps630.nexus.request.DiscussionUpdateRequest;
import com.cps630.nexus.service.DiscussionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class DiscussionController {
	@Autowired
	private DiscussionService service;
	
	/**
	 * Endpoint to create a new discussion (Q&A) post
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/basic/discussion/create")
	public ResponseEntity<Object> createDiscussion(@RequestBody @Valid DiscussionCreateRequest request) {
		return service.createDiscussion(request);
	}
	
	/**
	 * Endpoint to update a discussion post
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/basic/discussion/update")
	public ResponseEntity<Object> updateDiscussionBasic(@RequestBody @Valid DiscussionUpdateRequest request) {
		return service.updateDiscussionBasic(request);
	}
	
	/**
	 * Endpoint to update a discussion post. Only accessible by administrators
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/admin/discussion/update")
	public ResponseEntity<Object> updateDiscussionAdmin(@RequestBody @Valid DiscussionUpdateRequest request) {
		return service.updateDiscussionAdmin(request);
	}
	
	/**
	 * Endpoint to reply to a discussion post
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/basic/discussion/reply/create")
	public ResponseEntity<Object> createDiscussionReply(@RequestBody @Valid DiscussionReplyCreateRequest request) {
		return service.createDiscussionReply(request);
	}
	
	/**
	 * Endpoint to update a reply to a discussion post
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/basic/discussion/reply/update")
	public ResponseEntity<Object> updateDiscussionReplyBasic(@RequestBody @Valid DiscussionReplyUpdateRequest request) {
		return service.updateDiscussionReplyBasic(request);
	}
	
	/**
	 * Endpoint to update a reply to a discussion post. Only accessible by administrators
	 * @param request
	 * @return
	 */
	@PostMapping("/internal/admin/discussion/reply/update")
	public ResponseEntity<Object> updateDiscussionReplyAdmin(@RequestBody @Valid DiscussionReplyUpdateRequest request) {
		return service.updateDiscussionReplyAdmin(request);
	}
	
	/**
	 * Endpoint to get a list of all discussion posts
	 * @return
	 */
	@PostMapping("/internal/basic/discussion/list/get")
	public ResponseEntity<Object> getDiscussionList() {
		return service.getDiscussionList();
	}
	
	/*@PostMapping("/internal/basic/discussion/get")
	public ResponseEntity<Object> getDiscussion(@RequestBody @Valid DiscussionGetRequest request) {
		return service.getDiscussion(request);
	}*/
}