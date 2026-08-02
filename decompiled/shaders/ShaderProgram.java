/*
 * Decompiled with CFR 0.152.
 */
package shaders;

import errors.ErrorManager;
import java.io.BufferedReader;
import org.lwjgl.opengl.GL20;
import shaders.Uniform;
import utils.MyFile;

public class ShaderProgram {
    private int programID;

    public ShaderProgram(MyFile vertexFile, MyFile fragmentFile, String ... attributes) {
        int vertexShaderID = this.loadShader(vertexFile, 35633);
        int fragmentShaderID = this.loadShader(fragmentFile, 35632);
        this.programID = GL20.glCreateProgram();
        GL20.glAttachShader(this.programID, vertexShaderID);
        GL20.glAttachShader(this.programID, fragmentShaderID);
        this.bindAttributes(attributes);
        GL20.glLinkProgram(this.programID);
        GL20.glDetachShader(this.programID, vertexShaderID);
        GL20.glDetachShader(this.programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
    }

    private void bindAttributes(String[] attributes) {
        int i = 0;
        while (i < attributes.length) {
            GL20.glBindAttribLocation(this.programID, i, attributes[i]);
            ++i;
        }
    }

    protected void storeAllUniformLocations(Uniform ... uniforms) {
        Uniform[] uniformArray = uniforms;
        int n = uniforms.length;
        int n2 = 0;
        while (n2 < n) {
            Uniform uniform = uniformArray[n2];
            uniform.storeUniformLocation(this.programID);
            ++n2;
        }
        GL20.glValidateProgram(this.programID);
    }

    public void start() {
        GL20.glUseProgram(this.programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        GL20.glUseProgram(0);
        GL20.glDeleteProgram(this.programID);
    }

    private int loadShader(MyFile file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            String line;
            BufferedReader reader = file.getReader();
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append(System.lineSeparator());
            }
            reader.close();
        }
        catch (Exception e) {
            System.err.println("Could not read file.");
            ErrorManager.crashWithUserAlert("Shader Error", "Could not read shader file: " + file, e);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, 35713) == 0) {
            System.err.println("Could not compile shader " + file + ": ");
            System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
            ErrorManager.crashWithUserAlert("Shader Error", "Could not compile shader " + file, new Exception("Shader failed to compile"));
        }
        return shaderID;
    }
}

