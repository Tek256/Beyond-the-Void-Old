##Basic Shader Usage
In OpenGL there are some quality default shaders available.
However when expanding into your own style it's usually in your best interest to expand and use custom shaders.  

In this example, I won't be going over how to write shaders. For quality information on that here are a few quality resources.
**Online Available**
 * [LearnOpenGL](http://www.learnopengl.com/)
 * [LightHouse3D](http://www.lighthouse3d.com/tutorials/)  

**Books**

 * [OpenGL SuperBible](http://www.openglsuperbible.com/)
 * [OpenGL Programming Guide](https://www.amazon.com/OpenGL-Programming-Guide-Official-Learning/dp/0321773039)

Here is how shader handling is done in Beyond the Void.

`  // Shader Creation`  

  `Shader shaderProgram = new ShaderProgram("Test", "shaders/test.vert", "shaders/test.frag");`

  `// Shader binding`  
  
  `shaderProgram.bind();`

  `// Passing Uniforms`  
  
  `shaderProgram.set("uniform", uniform value);`

  `// Unbinding all shaders`  
  
  `Shader.unbind();`

  `// Destroying a shader`  
  
  `shaderProgram.destroy();`

  `// Finding an existing Shader`  
  
  `Shader.getShader("Test");`
