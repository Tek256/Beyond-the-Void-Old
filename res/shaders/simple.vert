#version 330

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_texcoord;
layout(location = 2) in vec3 in_normal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform int flip_x = 0;
uniform int flip_y = 0;

uniform int sub_texture = 0;
uniform vec2 sub_size;
uniform vec2 sub_offset;

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
	
	if(sub_texture == 1){
		out_texcoord = (sub_size) * mod_texcoord;
		out_texcoord += sub_offset;
	}else{
		out_texcoord = mod_texcoord;	
	}
}

