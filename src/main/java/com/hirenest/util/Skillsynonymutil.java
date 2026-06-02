package com.hirenest.util;
 
import org.springframework.stereotype.Component;
 
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
/**
 * AI-Powered Semantic Skill Matching Utility
 *
 * Maps skills to their synonyms and related
 * technologies so that "Java" matches
 * "Spring Boot", "J2EE", "JavaFX" etc.
 *
 * This is the core of HireNest's
 * AI recommendation engine.
 */
@Component
public class Skillsynonymutil {
 
    // Skill synonym map
    // Each key maps to related skills
    private static final Map<String,
        List<String>> SKILL_SYNONYMS =
        new HashMap<>();
 
    static {
 
        // Java ecosystem
        SKILL_SYNONYMS.put("java",
            Arrays.asList("java", "j2ee",
                "javafx", "java8", "java11",
                "java17", "core java",
                "advanced java"));
 
        SKILL_SYNONYMS.put("spring",
            Arrays.asList("spring",
                "spring boot", "springboot",
                "spring mvc", "spring security",
                "spring data", "spring cloud",
                "spring framework"));
 
        SKILL_SYNONYMS.put("spring boot",
            Arrays.asList("spring boot",
                "springboot", "spring",
                "spring mvc", "spring security",
                "spring data"));
 
        // Web technologies
        SKILL_SYNONYMS.put("javascript",
            Arrays.asList("javascript", "js",
                "es6", "es2015", "nodejs",
                "node.js", "node"));
 
        SKILL_SYNONYMS.put("react",
            Arrays.asList("react", "reactjs",
                "react.js", "react native",
                "redux", "react hooks"));
 
        SKILL_SYNONYMS.put("angular",
            Arrays.asList("angular",
                "angularjs", "angular2",
                "angular4", "angular8",
                "angular12", "typescript"));
 
        SKILL_SYNONYMS.put("vue",
            Arrays.asList("vue", "vuejs",
                "vue.js", "vuex",
                "nuxt", "nuxtjs"));
 
        // Python ecosystem
        SKILL_SYNONYMS.put("python",
            Arrays.asList("python", "python3",
                "django", "flask", "fastapi",
                "numpy", "pandas", "scipy",
                "matplotlib", "pytorch",
                "tensorflow", "sklearn",
                "scikit-learn"));
 
        SKILL_SYNONYMS.put("django",
            Arrays.asList("django", "python",
                "flask", "fastapi",
                "python web"));
 
        // Database
        SKILL_SYNONYMS.put("mysql",
            Arrays.asList("mysql", "sql",
                "database", "rdbms",
                "mariadb", "aurora"));
 
        SKILL_SYNONYMS.put("sql",
            Arrays.asList("sql", "mysql",
                "postgresql", "postgres",
                "oracle", "mssql",
                "sqlite", "database",
                "rdbms", "plsql"));
 
        SKILL_SYNONYMS.put("mongodb",
            Arrays.asList("mongodb", "mongo",
                "nosql", "document db",
                "atlas", "mongoose"));
 
        SKILL_SYNONYMS.put("nosql",
            Arrays.asList("nosql", "mongodb",
                "cassandra", "redis",
                "dynamodb", "firebase",
                "couchdb"));
 
        // Cloud and DevOps
        SKILL_SYNONYMS.put("aws",
            Arrays.asList("aws",
                "amazon web services",
                "ec2", "s3", "lambda",
                "cloud", "cloud computing"));
 
        SKILL_SYNONYMS.put("docker",
            Arrays.asList("docker",
                "kubernetes", "k8s",
                "containerization",
                "devops", "ci/cd"));
 
        SKILL_SYNONYMS.put("devops",
            Arrays.asList("devops", "docker",
                "kubernetes", "jenkins",
                "ci/cd", "git", "github",
                "gitlab", "ansible",
                "terraform"));
 
        // Mobile
        SKILL_SYNONYMS.put("android",
            Arrays.asList("android",
                "android development",
                "java android", "kotlin",
                "android studio",
                "mobile development"));
 
        SKILL_SYNONYMS.put("flutter",
            Arrays.asList("flutter", "dart",
                "cross platform",
                "mobile development",
                "react native"));
 
        SKILL_SYNONYMS.put("kotlin",
            Arrays.asList("kotlin", "android",
                "java", "jvm",
                "android development"));
 
        // Data Science and AI
        SKILL_SYNONYMS.put("machine learning",
            Arrays.asList("machine learning",
                "ml", "deep learning",
                "ai", "artificial intelligence",
                "neural networks",
                "data science",
                "scikit-learn", "tensorflow",
                "pytorch", "keras"));
 
        SKILL_SYNONYMS.put("data science",
            Arrays.asList("data science",
                "machine learning", "ml",
                "python", "statistics",
                "data analysis",
                "data analytics",
                "tableau", "powerbi",
                "excel", "r language"));
 
        // Frontend
        SKILL_SYNONYMS.put("html",
            Arrays.asList("html", "html5",
                "css", "css3", "bootstrap",
                "tailwind", "frontend",
                "web development",
                "responsive design"));
 
        SKILL_SYNONYMS.put("css",
            Arrays.asList("css", "css3",
                "html", "sass", "scss",
                "less", "bootstrap",
                "tailwind", "frontend"));
 
        // Testing
        SKILL_SYNONYMS.put("testing",
            Arrays.asList("testing",
                "junit", "selenium",
                "testng", "jest",
                "unit testing",
                "integration testing",
                "qa", "quality assurance",
                "automation testing"));
 
        // Version Control
        SKILL_SYNONYMS.put("git",
            Arrays.asList("git", "github",
                "gitlab", "bitbucket",
                "version control",
                "source control"));
 
        // Other common skills
        SKILL_SYNONYMS.put("rest api",
            Arrays.asList("rest api",
                "restful", "rest",
                "api", "web services",
                "microservices",
                "api development"));
 
        SKILL_SYNONYMS.put("microservices",
            Arrays.asList("microservices",
                "rest api", "spring boot",
                "docker", "kubernetes",
                "distributed systems",
                "api gateway"));
 
        SKILL_SYNONYMS.put("c++",
            Arrays.asList("c++", "cpp",
                "c", "c language",
                "object oriented",
                "oop", "data structures"));
 
        SKILL_SYNONYMS.put("c#",
            Arrays.asList("c#", "csharp",
                ".net", "dotnet",
                "asp.net", "unity",
                "microsoft"));
 
        SKILL_SYNONYMS.put("php",
            Arrays.asList("php", "laravel",
                "symfony", "wordpress",
                "codeigniter",
                "web development"));
 
        SKILL_SYNONYMS.put("linux",
            Arrays.asList("linux", "unix",
                "ubuntu", "centos",
                "shell scripting",
                "bash", "command line",
                "server administration"));
    }
 
    /**
     * Expand a skill to include
     * all its synonyms and related skills
     */
    public List<String> expandSkill(
        String skill) {
 
        String normalizedSkill =
            skill.trim().toLowerCase();
 
        // Check direct match first
        if (SKILL_SYNONYMS.containsKey(
            normalizedSkill)) {
            return SKILL_SYNONYMS.get(
                normalizedSkill);
        }
 
        // Check if skill is a value
        // in any synonym list
        for (Map.Entry<String,
            List<String>> entry :
            SKILL_SYNONYMS.entrySet()) {
            if (entry.getValue().stream()
                .anyMatch(s ->
                    s.equals(normalizedSkill)
                    || normalizedSkill.contains(s)
                    || s.contains(
                        normalizedSkill))) {
                return entry.getValue();
            }
        }
 
        // No synonym found
        // return just the skill itself
        return Arrays.asList(normalizedSkill);
    }
 
    /**
     * Check if two skills are semantically
     * related using synonym mapping
     */
    public boolean areSkillsRelated(
        String skill1, String skill2) {
 
        String s1 = skill1.trim().toLowerCase();
        String s2 = skill2.trim().toLowerCase();
 
        // Direct match
        if (s1.equals(s2)) return true;
        
     // Word boundary match
        // "spring boot" matches "spring"
        // but "strong" does NOT match "spring"
        if (s1.contains(" ") || s2.contains(" ")) {
            if (s1.contains(s2) || 
                s2.contains(s1)) return true;
        }

        
 
        // Contains match
        //if (s1.contains(s2) ||
          //  s2.contains(s1)) return true;
 
        // Synonym match
        List<String> synonyms1 =
            expandSkill(s1);
        List<String> synonyms2 =
            expandSkill(s2);
 
        return synonyms1.stream()
            .anyMatch(syn1 ->
                synonyms2.stream()
                    .anyMatch(syn2 ->
                        syn1.equals(syn2)));
                      
    }
}
 



























