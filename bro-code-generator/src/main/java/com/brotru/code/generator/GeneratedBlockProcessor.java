package com.brotru.code.generator;

import com.brotru.code.generator.logging.Log;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author bronek
 */
public class GeneratedBlockProcessor {

    private final Log log;

    public GeneratedBlockProcessor(final Log log) {
        this.log = Objects.requireNonNull(log);
    }

    private interface AppendGeneratedBlockStrategy {
        void append(StringBuilder blockBuilder);
    }

    public boolean writeGeneratedBlock(final List<String> fragments, String wholeFileContent, Path path, String beginBlockString,
            String endBlockString) throws IOException {
        return writeGeneratedBlock(blockBuilder -> {
            for (int i = 0; i < fragments.size(); i++) {
                String line = fragments.get(i);
                blockBuilder.append(line);
                if (i < fragments.size() - 1) {
                    blockBuilder.append("\n");
                }
            }
        }, wholeFileContent, path, beginBlockString, endBlockString);
    }

    public boolean writeGeneratedBlock(final String generatedCode, String wholeFileContent, Path path, String beginBlockString,
            String endBlockString) throws IOException {
        return writeGeneratedBlock(blockBuilder -> blockBuilder.append(generatedCode), wholeFileContent, path, beginBlockString,
                endBlockString);
    }

    public boolean writeGeneratedBlock(AppendGeneratedBlockStrategy generateStrategy, String wholeFileContent, Path path,
            String beginBlockString,
            String endBlockString)
            throws IOException {
        // Build the block content
        StringBuilder blockBuilder = new StringBuilder();
        blockBuilder.append(beginBlockString).append("\n");

        generateStrategy.append(blockBuilder);

        blockBuilder.append("\n").append(endBlockString);
        String targetReplacementBlock = blockBuilder.toString();
        String updatedContent;

        final int beginBlockIndex = wholeFileContent.indexOf(beginBlockString);
        final int endBlockIndex = wholeFileContent.indexOf(endBlockString);
        if (beginBlockIndex != -1 && endBlockIndex != -1 && beginBlockIndex < endBlockIndex) {
            int endPos = endBlockIndex + endBlockString.length();

            StringBuilder sb = new StringBuilder(wholeFileContent);
            sb.replace(beginBlockIndex, endPos, targetReplacementBlock);
            updatedContent = sb.toString();
        } else {
            // Scenario B: Smart Insert if anchors do not exist yet
            // Find the last closing brace '}' of the class definition and insert before it
            int lastBraceIdx = wholeFileContent.lastIndexOf("}");
            if (lastBraceIdx == -1) {
                return true;
            }
            updatedContent = wholeFileContent.substring(0, lastBraceIdx)
                    + "\n" + targetReplacementBlock + "\n"
                    + wholeFileContent.substring(lastBraceIdx);
        }
        // Only update disk if content has structurally changed
        if (!wholeFileContent.equals(updatedContent)) {
            Files.writeString(path, updatedContent);
            if (log.isInfoEnabled()) {
                log.info("Regenerated code injection block inside: " + path.getFileName());
            }
        }
        return false;
    }
}
