#version 330 core

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_texcoord;
layout(location = 2) in vec3 in_normal;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;

uniform int flip_x = 0;
uniform int flip_y = 0;

uniform int subTexture;

uniform vec2 texSize;
uniform vec2 subOffset;
uniform vec2 subSize;

out vec2 out_texcoord;

void main(){
	gl_Position = projection * view * model * vec4(in_position, 1.0);
	
	vec2 mod_texcoord = in_texcoord;
	
	if(flip_x == 1){
		mod_texcoord.x = 1 - mod_texcoord.x;
	}
	
	if(flip_y == 1){
		mod_texcoord.y = 1 - mod_texcoord.y;
	}
	
	if(subTexture == 1){
		out_texcoord = (subOffset + subSize) * mod_texcoord;
	}else{
		out_texcoord = mod_texcoord;
	}
}