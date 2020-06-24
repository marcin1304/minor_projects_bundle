package com.example.lab12;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScriptManager implements ScriptListener {
    private Context mainContext;
    private Context scriptContext;
    private BufferedImage originalImage;
    private BufferedImage transformedImage;

    private Source scriptSource;
    private Value loadedFunction;

    private ScriptManagerGui scriptManagerGui;

    public static void main(String[] args) throws Exception {
        ScriptManager scriptManager = new ScriptManager();
    }

    public ScriptManager() throws IOException {
        loadImages();
        init();
    }

    private void loadImages() throws IOException {
        originalImage = ImageIO.read(new File("test.jpg"));
        transformedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
    }

    private void init() {
        mainContext = Context
                .newBuilder()
                .allowAllAccess(true)
                .build();

        initScriptContext();

        scriptManagerGui = new ScriptManagerGui();
        scriptManagerGui.registerScriptListener(this);

        mainContext.getBindings("js").putMember("originalImage", originalImage);
        mainContext.getBindings("js").putMember("transformedImage", transformedImage);
        mainContext.getBindings("js").putMember("window", scriptManagerGui);
        mainContext.eval("js", "window.setOriginalImage(originalImage);");
        mainContext.eval("js", "window.setTransformedImage(transformedImage);");
    }

    private void initScriptContext() {
        scriptContext = Context
                .newBuilder()
                .allowAllAccess(true)
                .build();

        scriptContext.getBindings("js").putMember("originalImage", originalImage);
        scriptContext.getBindings("js").putMember("originalImage", transformedImage);
    }

    @Override
    public void loadScript(File scriptFile) throws IOException {
        if(scriptFile.isFile()) {
            scriptSource = Source.newBuilder("js", scriptFile).build();
            scriptContext.eval(scriptSource);
            loadedFunction = scriptContext.getBindings("js").getMember("fun");
        }
    }

    @Override
    public void executeLoadedScript() {
        if(loadedFunction != null) {
            loadedFunction.execute();
            mainContext.eval("js", "window.setTransformedImage(transformedImage);");
        }
    }

    @Override
    public void unloadScript() {
        scriptContext.close();
        initScriptContext();
    }
}
