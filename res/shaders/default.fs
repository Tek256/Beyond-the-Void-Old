#version 330 core

in vec2 out_texcoord;

uniform sampler2D sample_tex;
uniform vec4 color = vec4(1); //color 
uniform float alphaThreshhold = 0.0001;

uniform int colorOnly = 0; //false 

out vec4 out_color;

void main(){
	vec4 sampleColor = color;
	
	sampleColor = texture(sample_tex, out_texcoord);
	
	if(sampleColor.a < alphaThreshhold){ //alpha check
		discard;
	}
	
	out_color = sampleColor;
}