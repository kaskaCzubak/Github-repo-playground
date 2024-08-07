package com.example.api.service;

import com.example.api.model.Branch;
import com.example.api.model.GitHubRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GitHubService {

    private final WebClient webClient;

    public List<GitHubRepository> getNonForkRepositories(String username) {
        try {
            List<GitHubRepository> repositories = webClient.get()
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .bodyToFlux(GitHubRepository.class)
                    .collectList()
                    .block();

            if (repositories == null) {
                throw new RuntimeException("Failed to fetch repositories.");
            }

            return repositories.stream()
                    .filter(repo -> !repo.isFork())
                    .peek(repo -> {
                        List<Branch> branches = webClient.get()
                                .uri("/repos/{owner}/{repo}/branches", repo.getOwner().getLogin(), repo.getName())
                                .retrieve()
                                .bodyToFlux(Branch.class)
                                .collectList()
                                .block();

                        repo.setBranches(branches);
                    })
                    .collect(Collectors.toList());

        } catch (WebClientResponseException e) {
            log.error("Error fetching data from GitHub: {}", e.getStatusText());
            throw new RuntimeException("Error fetching data from GitHub: " + e.getStatusText(), e);
        }
    }
}
