package com.example.lab12;

import java.io.File;
import java.io.IOException;

public interface ScriptListener {
    void loadScript(File scriptFile) throws IOException;

    void executeLoadedScript();

    void unloadScript();
}
