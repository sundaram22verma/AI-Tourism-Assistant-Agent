# Project Guidelines

## LangChain4j Versioning
This project uses LangChain4j 1.x.
Ensure that all `langchain4j-*` dependencies use compatible versions.
Specifically, `langchain4j` and `langchain4j-open-ai` should use `1.0.1`, while newer modules like `langchain4j-mcp` use `1.0.1-beta6`.

**CRITICAL:** Avoid including duplicate dependencies with older versions (e.g., `1.0.0-beta3`), as this causes compilation errors due to API changes (e.g., `ChatLanguageModel` vs `ChatModel`).
