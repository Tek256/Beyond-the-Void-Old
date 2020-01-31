#version 330

uniform sampler2D tex;
uniform vec4 color;
uniform int color_only = 0;

in vec2 out_texcoord;

out vec4 out_color;

void main(){
	if(color.a == 0){
		discard;
	}else if(color_only == 1){
		out_color = color;
	}else{
		vec4 sample_color = texture(tex, out_texcoord);
		
		if(sample_color.a == 0){
			discard;
		}else{
			out_color = sample_color * color;
		}
	}
}