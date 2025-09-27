package com.pomostudy.config.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "My Api", version = "1.0", description = "Documentation API for my project"),
        tags = {
                @Tag(name = "authentication", description = "Controller for register and login user."),
                @Tag(name = "user", description = "Controller for saving, edit, search and delete user."),
                @Tag(name = "task", description = "Controller for saving, edit, search and delete task."),
                @Tag(name = "category", description = "Controller for saving, edit, search and delete category."),
                @Tag(name = "goal", description = "Controller for saving, edit, search and delete goal.")
        }
)
public class OpenApiConfig {
    // Esta classe não precisa de conteúdo, apenas das anotações.
    // This class don't need content, only for annotations.
}