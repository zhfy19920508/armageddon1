package com.zhfy.game.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * @author davedes
 */
public class GrayscaleTest implements ApplicationListener {
	
	/*public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Gray Test";
		cfg.useGL20 = true;
		cfg.width = 640;
		cfg.height = 480;
		cfg.resizable = false;

		new LwjglApplication(new GrayscaleTest(), cfg);
	}*/
	
	final String VERT =  
			"attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
			"attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
			"attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			
			"uniform mat4 u_projTrans;\n" + 
			" \n" + 
			"varying vec4 vColor;\n" +
			"varying vec2 vTexCoord;\n" +
			
			"void main() {\n" +  
			"	vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
			"	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			"	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
			"}";
	
	final String FRAG = 
			//GL ES specific stuff
			  "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" + //
			"varying LOWP vec4 vColor;\n" +
			"varying vec2 vTexCoord;\n" + 
			"uniform sampler2D u_texture;\n" +			
			"uniform float grayscale;\n" +
			"void main() {\n" +  
			"	vec4 texColor = texture2D(u_texture, vTexCoord);\n" + 
			"	\n" + 
			"	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n" + 
			"	texColor.rgb = mix(vec3(gray), texColor.rgb, grayscale);\n" + 
			"	\n" + 
			"	gl_FragColor = texColor * vColor;\n" + 
			"}";
	
	SpriteBatch batch;
	Stage stage;
	ShaderProgram shader;
	Texture tex;
	float grayscale = 0f;
	float time;
		
	@Override
	public void create() {
		//important since we aren't using some uniforms and attributes that SpriteBatch expects
		ShaderProgram.pedantic = false;
		
		shader = new ShaderProgram(VERT, FRAG);
		
		//shader didn't compile.. handle it somehow
		if (!shader.isCompiled()) {
			System.err.println(shader.getLog());
			System.exit(0);
		}
		
		//incase there were any warnings/info
		if (shader.getLog().length()!=0)
			System.out.println(shader.getLog());
		
		//create our own sprite batcher which ALL sprites will use
		batch = new SpriteBatch(1000, shader);
		batch.setShader(shader);
		
		//pass the custom batcher to our stage
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);

		tex = new Texture(Gdx.files.internal("data/libgdx.png"));
		
		//add entities/UI to stage...
		Image img1 = new Image(tex);
		img1.setPosition(250, 50);
		stage.addActor(img1);
		
		Image img2 = new Image(tex);
		img2.setScale(0.5f);
		stage.addActor(img2);
	}

	@Override
	public void resize(int width, int height) {
		//stage.setViewport(width, height, true);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		time += Gdx.graphics.getDeltaTime();		
		grayscale = (float)Math.sin(time)/2f+0.5f;
		
		shader.begin();
		shader.setUniformf("grayscale", grayscale);
		shader.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		shader.dispose();
		tex.dispose();
		stage.dispose();
	}	
}