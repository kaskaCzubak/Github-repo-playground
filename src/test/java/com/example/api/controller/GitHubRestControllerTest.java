package com.example.api.controller;

import com.example.api.model.Branch;
import com.example.api.model.Commit;
import com.example.api.model.GitHubRepository;
import com.example.api.model.Owner;
import com.example.api.service.GitHubService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GitHubRestController.class)
public class GitHubRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @Test
    public void testGetUserRepositories_Success() throws Exception {
        GitHubRepository repo = new GitHubRepository();
        repo.setName("test-repo");
        repo.setFork(false);
        repo.setOwner(new Owner("test-owner"));
        Branch branch = new Branch();
        branch.setName("main");
        branch.setCommit(new Commit("sha123"));
        repo.setBranches(Collections.singletonList(branch));

        List<GitHubRepository> repositories = List.of(repo);

        when(gitHubService.getNonForkRepositories("test-user")).thenReturn(repositories);

        mockMvc.perform(get("/api/github/repositories/test-user")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("test-repo"))
                .andExpect(jsonPath("$[0].owner.login").value("test-owner"))
                .andExpect(jsonPath("$[0].branches[0].name").value("main"))
                .andExpect(jsonPath("$[0].branches[0].commit.sha").value("sha123"));
    }

    @Test
    public void testGetUserRepositories_UserNotFound() throws Exception {
        when(gitHubService.getNonForkRepositories("non-existing-user"))
                .thenThrow(new RuntimeException("User not found or error fetching data"));

        mockMvc.perform(get("/api/github/repositories/non-existing-user")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found or error fetching data"));
    }

    @Test
    public void testGetUserRepositories_InvalidHeader() throws Exception {
        when(gitHubService.getNonForkRepositories("existing-user-wrong-header"))
                .thenThrow(new RuntimeException("Accept header must be application/json"));

        mockMvc.perform(get("/api/github/repositories/testUser")
                        .header("Accept", MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isNotAcceptable());
    }
}
