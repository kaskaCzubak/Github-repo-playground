# GitHub Repositories Viewer🤖

## Description
GitHub Repositories Viewer is a Spring Boot application that retrieves a list of GitHub repositories for a specified user, excluding forks. Additionally, it provides details on the branches and the latest commits for each repository. If the user does not exist, the application returns an appropriate error message.

## Requirements
- Java 21
- Spring Boot 3.2
- Gradle 8.5

## Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/github-repositories-viewer.git
    cd github-repositories-viewer
    ```

2. Build the project using Gradle:
    ```bash
    ./gradlew build
    ```

## Configuration
1. Create a file named `application.yaml` in the `src/main/resources` directory with the following content:
    ```yaml
    api:
      github:
        url: https://api.github.com
    ```

## Running the Application
1. Start the application:
    ```bash
    ./gradlew bootRun
    ```

2. The application will be available at `http://localhost:8080`.

## Usage
### Endpoints
- **GET /api/github/repositories/{username}**

  Returns a list of repositories for the specified user, excluding forks, along with information about branches and the latest commits.

  **Example Request:**
  ```bash
  curl -H "Accept: application/json" http://localhost:8080/api/github/repositories/octocat
  ```
  
  **Example Response:**
  ```bash
  {
    "name": "test-repo",
    "owner": {
      "login": "test-owner"
    },
    "branches": [
      {
        "name": "main",
        "commit": {
          "sha": "sha123"
        }
      }
    ]
  }
  ```
  **Example Error Response:**
```bash
{
  "status": 404,
  "message": "User not found or error fetching data"
}
```
