tasks.register("updateOpenApiUrl") {
    doLast {
        val file = file("src/main/resources/openapi/documentation.yaml")
        var content = file.readText()
        content = content.replace(
            "https://pianoman",
            "http://127.0.0.1:8080"
        )
        file.writeText(content)
    }
}

// Hook the task into the build process
tasks.named("processResources") {
    dependsOn("updateOpenApiUrl")
}