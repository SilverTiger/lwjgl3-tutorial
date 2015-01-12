#version 150 core

in vec2 position;
in vec4 color;
in vec2 texcoord;

out vec4 vertexColor;
out vec2 textureCoord;

void main() {
    vertexColor = color;
    textureCoord = texcoord;
    gl_Position = vec4(position, 0.0, 1.0);
}