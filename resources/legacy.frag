#version 120

varying vec3 vertexColor;
varying vec2 textureCoord;

uniform sampler2D texImage;

void main() {
    vec4 textureColor = texture2D(texImage, textureCoord);
    gl_FragColor = vec4(vertexColor, 1.0) * textureColor;
}
