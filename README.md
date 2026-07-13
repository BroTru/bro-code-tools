# bro-code-tools
## misc code tools - for java mostly.

Right now the only tool there is an @Expose annotation and it's processing that generate getter-like method that has the same name as the field.  The method is placed in the code, in the bottom of the file.  It does similar things to Lombok but it does not manipulate bytecode so there are no issues with debugging, java versions compatilitity etc.  It's a simple code generation.  You can see the code, you can generate it each build or on-demand.  Generated code is surrounded by code collapse tags recognized by major IDEs.

Sample usage:
```java
      /**
     * Contains Json parsing related tools.
     */
    @Expose
    private final JsonTool json;

    @Expose("Chat completion request related client classes")
    private final ChatCompletion chatCompletion;

    public Model() {
        super();
        json = new JsonTool();
        chatCompletion = new ChatCompletion(this);
    }


// <editor-fold defaultstate="collapsed" desc="Generated Code DO NOT EDIT">
// region Generated Code

    /**
     * Returns instance of {@link JsonTool}.
     * See: {@link #json}
     * @return {@link JsonTool} instance from field {@link #json}
     */
    public JsonTool json() { return json; }

    /**
     * Chat completion request related client classes
     * Returns instance of {@link ChatCompletion}.
     * See: {@link #chatCompletion}
     * @return {@link ChatCompletion} instance from field {@link #chatCompletion}
     */
    public ChatCompletion chatCompletion() { return chatCompletion; }

// endregion
// </editor-fold>
```
To use the tool - you can use the 'generate-code' maven goal, you can do it on demand or during build
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>com.brotru.code.tools</groupId>
                <artifactId>bro-code-generator</artifactId>
                <version>2026.01-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>generate-code</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
