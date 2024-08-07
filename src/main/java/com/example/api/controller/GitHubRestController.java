package com.example.api.controller;

import com.example.api.model.ErrorResponse;
import com.example.api.model.GitHubRepository;
import com.example.api.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GitHubRestController {

    private static final String REPOS = "/repositories/{username}";

    private final GitHubService gitHubService;

    @GetMapping(value = REPOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserRepositories(
            @PathVariable String username,
            @RequestHeader("Accept") String acceptHeader) {

        if (!MediaType.APPLICATION_JSON_VALUE.equals(acceptHeader)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Accept header must be application/json"));
        }

        try {
            List<GitHubRepository> repositories = gitHubService.getNonForkRepositories(username);
            return ResponseEntity.ok(repositories);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found or error fetching data"));
        }
    }


}
