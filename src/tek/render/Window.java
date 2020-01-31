package tek.render;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import tek.ResourceLoader;
import tek.input.Keyboard;
import tek.input.Mouse;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
	public static final int DEFAULT_WIDTH, DEFAULT_HEIGHT;
	
	public static String title = "Beyond the Void";
	public static Window instance = null;
	
	private boolean exists = false;
	private boolean closeRequested = false;
	
	private long handle;
	private int x, y;
	private int lastWidth, lastHeight;
	private int width, height, refreshRate;
	private boolean fullscreen, vsync, visible, resizable;
	private boolean allowRender = false;

	//clear color
	private float cr, cg, cb;
	
	public ArrayList<VideoMode> videoModes;
	public ArrayList<Monitor> monitors;
	
	// CURRENT USAGE
	private VideoMode lastVideoMode;
	public VideoMode videoMode;
	public Monitor   monitor;
	
	static{
		//TODO change defaults to load
		DEFAULT_WIDTH = 1280;
		DEFAULT_HEIGHT = 720;
	}
	
	public Window(){
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public Window(int width, int height){
		this(width, height, false);
	}
	
	public Window(int width, int height, boolean fullscreen){
		this(width, height, fullscreen, true);
	}
	
	public Window(int width, int height, boolean fullscreen, boolean vsync){
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.vsync = vsync;
		this.refreshRate = -1;
		
		if(!init())
			throw new RuntimeException("Unable to initialize window");
	}
	
	public Window(int width, int height, boolean fullscreen, int refreshRate){
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.vsync = false;
		this.refreshRate = refreshRate;
		
		if(!init())
			throw new RuntimeException("Unable to initialize window");
	}
	
	private boolean init(){
		if(exists)
			return false;
		
		if(!glfwInit())
			return false;
		
		
		monitors = new ArrayList<Monitor>();
		PointerBuffer buffer = GLFW.glfwGetMonitors();
		
		for(int i=0;i<buffer.capacity();i++){
			buffer.position(i);
			long h = buffer.get();
			String name = GLFW.glfwGetMonitorName(h);
			
			GLFWVidMode t = glfwGetVideoMode(h);
			monitors.add(new Monitor(name, h, t.width(), t.height()));
		}
		
		if(monitors.size() > 1){
			long h = glfwGetPrimaryMonitor();
			for(Monitor m : monitors)
				if(m.handle == h)
					monitor = m;
		}else{
			monitor = monitors.get(0);
		}
		
		videoModes = new ArrayList<VideoMode>();
		GLFWVidMode.Buffer modes = glfwGetVideoModes(monitor.handle);
		
		for(int i=0;i<modes.capacity();i++){
			modes.position(i);
			videoModes.add(new VideoMode(
					modes.width(), modes.height(),
					modes.refreshRate(),
					modes.redBits(), modes.greenBits(),
					modes.blueBits()
					));
		}
		
		glfwDefaultWindowHints();
		
		setHint(GLFW_VISIBLE, false);
		setHint(GLFW_RESIZABLE, resizable);

		
		if(fullscreen){
			GLFWVidMode t = glfwGetVideoMode(monitor.handle);
			VideoMode temp = new VideoMode(t.width(), t.height(),
					t.refreshRate(), t.redBits(), t.greenBits(),
					t.blueBits());
			for(VideoMode mode : videoModes)
				if(mode.equals(temp))
					videoMode = mode;
			
			resizable = false;
			
			if(refreshRate == -1)
				refreshRate = videoMode.refreshRate;
			
			setHint(GLFW_REFRESH_RATE, refreshRate);
			
			//0L = context sharing
			handle = glfwCreateWindow(width, height, title, monitor.handle, 0L);
		}else{
			//not fullscreen
			videoMode = null;
			
			//1st 0L = monitor, 2nd = context sharing
			handle = glfwCreateWindow(width, height, title, 0L, 0L);
		}
		
		//if the window wasn't made 
		if(handle == 0L)
			return false;
		
		glfwSetKeyCallback(handle, (window, key, scancode, action, mods)->{
			Keyboard.setKey(key, (action == GLFW_PRESS) ? true : (action == GLFW_REPEAT) ? true : false);
		});
		
		glfwSetCharCallback(handle, (window, key) -> {
			Keyboard.addChar((char)key);
		});
		
		glfwSetWindowPosCallback(handle, (handle, x, y) -> {
			this.x = x;
			this.y = y;
		});
		
		glfwSetWindowSizeCallback(handle, (handle, w, h) -> {
			lastWidth = width;
			lastHeight = height;
			
			this.width = w;
			this.height = h;
		});
		
		glfwSetCursorPosCallback(handle, (handle, x, y) -> { 
			Mouse.setPos(x, y);
		});
		
		glfwSetScrollCallback(handle, (handle, x, y) -> {
			Mouse.setScroll(x, y);
		});
		
		glfwSetMouseButtonCallback(handle, (handle, button, action, mods) -> {
			Mouse.setButton(button, (action == GLFW_PRESS) ? true : (action == GLFW_REPEAT) ? true : false);
		});
		
		glfwSetCursorEnterCallback(handle, (handle, enter) -> {
			Mouse.setEntered(enter);
		});
		
		glfwSetWindowCloseCallback(handle, (handle) -> {
			closeRequested = true;
		});
		
		//no multi sampling
		setHint(GLFW_SAMPLES, 0);
		
		glfwMakeContextCurrent(handle);
		
		GL.createCapabilities();
		
		if(vsync){
			glfwSwapInterval(1);
		}else{
			glfwSwapInterval(0);
		}
		
		//set viewport
		glViewport(0, 0, width, height);
		
		glEnable(GL_TEXTURE_2D);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		
		glEnable(GL_POINT_SMOOTH);
		glHint(GL_POINT_SMOOTH, GL_NICEST);
		
		glEnable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH, GL_NICEST);
		
		glEnable(GL_PERSPECTIVE_CORRECTION_HINT);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		GL11.glFrontFace(GL11.GL_CW);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		center();
		show();
		
		allowRender = true;
		
		instance = this;
		
		return true;
	}
	
	public void pollEvents(){
		glfwPollEvents();
	}
	
	public void swapBuffers(){
		glfwSwapBuffers(handle);
	}
	
	public void clear(int buffers){
		glClear(buffers);
	}
	
	public void clear(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void setClearColor(float r, float g, float b){
		r = (r > 1) ? r / 255 : r;
		g = (g > 1) ? g / 255 : g;
		b = (b > 1) ? b / 255 : b;
		if(cr == r && cg == g && cb == b)
			return;
		glClearColor(r, g, b, 1);
	}
	
	public void setClearColor(Vector3f color){
		setClearColor(color.x, color.y, color.z);
	}
	
	public void center(){
		if(fullscreen)
			return;
		setPosition((monitor.width - width) / 2, 
				(monitor.height - height) / 2);
	}
	
	public void show(){
		if(visible)
			return;
		
		visible = true;
		setHint(GLFW_VISIBLE, true);
		glfwShowWindow(handle);
	}
	
	
	public void hide(){
		if(!visible)
			return;
		
		visible = false;
		setHint(GLFW_VISIBLE, false);
		glfwHideWindow(handle);
	}
	
	public boolean allowRender(){
		return allowRender;
	}
	
	public void setRendering(boolean render){
		if(allowRender == render)
			return;
		allowRender = render;
	}
	
	public void setFullscreen(boolean fullscreen){
		setFullscreen(fullscreen, null);
	}
	
	public void setFullscreen(boolean fullscreen, VideoMode videoMode){
		if(this.fullscreen == fullscreen)
			return;
		
		if(fullscreen){ //wasn't fullscreen < -- entering
			setRendering(false);
			destroy();
			
			fullscreen = false;
			
			if(videoMode == null)
				videoMode = getDefaultVideoMode();
			
			recreate(videoMode);
			
			setRendering(true);
		}else{ //is fullscreen < -- exiting
			setRendering(false);
			destroy();
			
			recreate(videoMode);
			setRendering(true);
		}
	}
	
	public VideoMode getDefaultVideoMode(){
		return getClosestVideoMode(monitor.width, monitor.height);
	}
	
	public VideoMode getBestVideoMode(){
		return getClosestVideoMode(10000000, 10000000);
	}
	
	public VideoMode getClosestVideoMode(int width, int height){
		int closeness = Integer.MAX_VALUE;
		VideoMode d = null;
		
		for(VideoMode mode : videoModes){
			int t = 
					Math.max(width, monitor.width) - Math.min(width, monitor.width) +
					Math.max(height, monitor.height) - Math.min(height, monitor.height);
			t += 1000 - mode.refreshRate;
			
			if(t < closeness)
				d = mode;
		}
		
		return d;
	}
	
	private void recreate(VideoMode mode){
		hide();
		
		if(fullscreen){
			lastVideoMode = videoMode;
			videoMode = mode;
			
			handle = glfwCreateWindow(mode.width, mode.height, title, monitor.handle, 0L);
			
			setHint(GLFW_REFRESH_RATE, mode.refreshRate);
		}else{
			handle = glfwCreateWindow(width, height, title, 0L, 0L);
			setPosition(x, y);
		}
		
		show();
	}
	
	public void revert(){
		if(!fullscreen){
			set(lastWidth, lastHeight, width, height);
			glfwSetWindowSize(handle, width, height);
		}else{
			setRendering(false);
			destroy();
			
			recreate(lastVideoMode);
			
			setRendering(false);
		}
	}
	
	private void set(int width, int height, int lastWidth, int lastHeight){
		this.width = width;
		this.height = height;
		this.lastWidth = lastWidth;
		this.lastHeight = lastHeight;
	}
	
	public void setVideoMode(VideoMode mode){
		if(videoMode == mode)
			return;
		lastVideoMode = mode;
		videoMode = mode;
	}
	
	public void setPosition(int x, int y){
		if((this.x == x && this.y == y) || fullscreen)
			return;
		glfwSetWindowPos(handle, x, y);
	}
	
	public void setSize(int w, int h){
		if((width == w && height == h) || fullscreen)
			return;
		glfwSetWindowSize(handle, w, h);
	}
	
	public void setHint(int src, boolean val){
		setHint(src, (val) ? 1 : 0);
	}
	
	public void setHint(int src, int val){
		glfwWindowHint(src, val);
	}
	
	public boolean isCloseRequested(){
		return closeRequested;
	}
	
	public int getWidth(){
		if(fullscreen)
			return videoMode.width;
		return width;
	}
	
	public int  getHeight(){
		if(fullscreen)
			return videoMode.height;
		return height;
	}
	
	public static int getWindowHeight(){
		return (instance == null) ? 0 : instance.getHeight();
	}
	
	public static int getWindowWidth(){
		return (instance == null) ? 0 : instance.getWidth();
	}
	
	public static void setInputMode(int mode, int value){
		glfwSetInputMode(instance.handle, mode, value);
	}
	
		public void setIcon(String icon16Path, String icon32Path){
			//setup buffers to work with stb
			IntBuffer w = BufferUtils.createIntBuffer(1);
			IntBuffer h = BufferUtils.createIntBuffer(1);
			IntBuffer comp = BufferUtils.createIntBuffer(1);
			
			//these will be the data buffers for the textures
			ByteBuffer icon16,icon32;
			try{
				//populate the buffers with the raw image data
				icon16 = ResourceLoader.getBytes(icon16Path);
				icon32 = ResourceLoader.getBytes(icon32Path);
				
				//setup image buffers for the images to be processed
				try(GLFWImage.Buffer icons = GLFWImage.malloc(2)){
					//process both images with stb
					//16x16 icon
					ByteBuffer p16 = STBImage.stbi_load_from_memory(icon16, w, h, comp, 4);
					icons.position(0).width(w.get(0)).height(h.get(0)).pixels(p16);
					
					//32x32 icon
					ByteBuffer p32 = STBImage.stbi_load_from_memory(icon32, w, h, comp, 4);
					icons.position(1).width(w.get(0)).height(h.get(0)).pixels(p32);
					
					//reset the icons buffer position
					icons.position(0);
					glfwSetWindowIcon(handle, icons);
					
					//free the stb resources
					STBImage.stbi_image_free(p16);
					STBImage.stbi_image_free(p32);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	
	/*
	 * allows for reconstruction of window
	 */
	public void destroy(){
		glfwDestroyWindow(handle);
	}
	
	/*
	 * Difference is ordering
	 */
	public void terminate(){
		glfwDestroyWindow(handle);
		glfwTerminate();
	}
	
	public static class VideoMode {
		/* IMPLIED FULLSCREEN */
		public final int width, height, refreshRate, r, g, b;
		
		public VideoMode(int width, int height, int refreshRate, int r, int g, int b){
			this.width = width;
			this.height = height;
			this.refreshRate = refreshRate;
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(!o.getClass().equals(getClass())) return false;
			
			VideoMode m = (VideoMode)o;
			return m.width == width &&
					m.height == height &&
					m.refreshRate == refreshRate &&
					m.r == r && m.g == g &&
					m.b == b;
		}
	}
	
	public static class Monitor {
		public final long handle;
		public final String name;
		public final int width, height;
		
		public Monitor(String name, long handle, int width, int height){
			this.handle = handle;
			this.name = name;
			this.width = width;
			this.height = height;	
		}
		
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(!o.getClass().equals(getClass())) return false;
			Monitor m = (Monitor)o;
			
			return m.handle == handle;
		}
	}
}
