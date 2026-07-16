# bro-code-tools
## misc code tools for Java.

I'm putting here some code tools that I create and use myself.  If they're useful for others - that's great, feel free to use them.
There will be some simple annotations that'll cause code generation but there will also be code templates using Pebble that'll allow extensive generated code creation without programming.

### @Expose annotation

There is an @Expose annotation and it's processing that generate getter-like method that has the same name as the field.  The method is placed in the code, in the bottom of the file.  It does similar things to Lombok but it does not manipulate bytecode so there are no issues with debugging, java versions compatilitity etc.  It's a simple code generation.  You can see the code, you can generate it each build or on-demand.  Generated code is surrounded by code collapse tags recognized by major IDEs.  You can benerate multiple times - it recognizes the tags surrounding generated code and just replaces old generated code with new version.

Generated code contains javadocs, There's some default javadocs but you can add any text in the annotation value; see the example.  Javadoc generation without any delombok playing is a major benefit of using that.  At least for me ;)

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
