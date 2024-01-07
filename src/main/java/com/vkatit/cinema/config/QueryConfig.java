package com.vkatit.cinema.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Configuration
public class QueryConfig {

    private final Map<String, String> sqlQueries;

    public QueryConfig(@Value("${sql.file.path}") String sqlFilePath) {
        this.sqlQueries = loadQueries(sqlFilePath);
    }

    private Map<String, String> loadQueries(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            InputStream inputStream = resource.getInputStream();
            String fileContent = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            Yaml yaml = new Yaml();
            return yaml.load(fileContent);
        } catch (IOException e) {
            throw new RuntimeException("Error loading SQL queries from file: " + fileName, e);
        }
    }

}